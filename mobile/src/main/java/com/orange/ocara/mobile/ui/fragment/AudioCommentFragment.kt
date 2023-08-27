/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara.mobile.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.orange.ocara.domain.models.CommentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentAudioCommentBinding
import com.orange.ocara.mobile.ui.dialogs.GenericDeleteConfirmationDialog
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.managers.PlayAudioForRecognitionManager
import com.orange.ocara.mobile.ui.managers.PlayAudioManager
import com.orange.ocara.mobile.ui.managers.SpeechRecognitionManager
import com.orange.ocara.mobile.ui.viewmodel.AudioCommentViewModel
import com.orange.ocara.utils.TimeUtils
import com.orange.ocara.utils.enums.CommentType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AudioCommentFragment : OcaraFragment(), SpeechRecognitionManager.SpeechToTextCallback {

    lateinit var binding: FragmentAudioCommentBinding
    val viewModel: AudioCommentViewModel by viewModels()
    private val args: AudioCommentFragmentArgs by navArgs()
    private var playAudioManager: PlayAudioManager? = null
    private var playAudioForRecognitionManager: PlayAudioForRecognitionManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_audio_comment, container, false
        )
        changeActionBarTitle()
        setHasOptionsMenu(true)
        loadData()
        addClickListeners()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        changeActionBarTitle()

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.default_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun changeActionBarTitle() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.audio_comment)
    }

    private fun addClickListeners() {
        binding.btnCancel.setOnClickListener {
//            showDeleteCommentDialog()
            requireActivity().onBackPressed()
        }
        binding.btnRetake.setOnClickListener {
//            showDeleteRecordDialog()
            deleteCommentAttachment()
        }
        binding.btnValidate.setOnClickListener {
//            requireActivity().onBackPressed()
            onValidate()
        }
        binding.btnSpeechToText.setOnClickListener {
            runSpeechToText()
        }
    }

    private fun runSpeechToText() {
        playAudioForRecognitionManager?.startRecognition()
    }


    private fun showDeleteRecordDialog() {
        val dialog = GenericDeleteConfirmationDialog(
            title = getString(R.string.delete_record),
            msg = getString(R.string.delete_record_confirmation_msg)
        ) {
            deleteCommentAttachment()
        }
        dialog.show(childFragmentManager, "DeleteCommentDialog")
    }

    private fun showDeleteCommentDialog() {
        val dialog = GenericDeleteConfirmationDialog(
            title = getString(R.string.delete_comment),
            msg = getString(R.string.delete_comment_confirmation_msg)
        ) {
            deleteComment()
        }
        dialog.show(childFragmentManager, "DeleteCommentDialog")
    }

    private fun deleteComment() {
        viewModel.deleteComment(args.commentId)
            .subscribeAndObserve {
                requireActivity().onBackPressed()
            }
    }

    private fun deleteCommentAttachment() {
        navigateToRecord()

    }

    private fun loadData() {
        if (args.commentId != -1) {
            // coming from either list comments or from retake
            loadDataForGivenComment()
        } else {
            // comment id = -1
            //no comment saved yet : coming from record with attachment
            if (args.recordPath != null) {
                setupFileNameForManagers(args.recordPath!!)
            }
        }
    }

    private fun loadDataForGivenComment() {
        if (args.recordPath == null) {
            // no record path given : not comming from retake
            // coming from the list : show the comment attachement
            viewModel.getComment(args.commentId)
                .subscribeAndObserve {
                    if (it.attachment.isNotEmpty())
                        setupFileNameForManagers(it.attachment)
                    binding.speechToTextTV.text = it.content
                }
        } else {
            //record path given : comming from retake
            setupFileNameForManagers(args.recordPath!!)
        }
    }

    private fun navigateToRecord() {
        NavHostFragment.findNavController(this)
            .navigate(
                AudioCommentFragmentDirections.actionAudioCommentFragmentToRecordCommentFragment(
                    commentId = args.commentId,
                    equipmentId = args.equipmentId
                )
            )
    }

    private fun onValidate() {
        if (args.commentId == -1) {
            viewModel.insertComment(buildCommentModel(args.recordPath!!)).subscribeAndObserve {
//                findNavController().navigateUp()
                requireActivity().onBackPressed()
            }

        } else {
            if (args.recordPath != null)
                viewModel.updateAttachment(args.commentId, args.recordPath!!).subscribeAndObserve {
                }
            findNavController().navigateUp()
        }
    }

    private fun buildCommentModel(attachment: String = ""): CommentModel {
        return CommentModel(
            0, CommentType.AUDIO, TimeUtils.getTimeInFormatForDatebase(),
            attachment, ""
        )
            .equipmentIdBuider(args.equipmentId.toLong())
    }

    private fun setupFileNameForManagers(fileName: String) {
        setupPlayAudioManager(fileName)
        setupRecognitionManager(fileName)
    }

    private fun setupRecognitionManager(fileName: String) {
        playAudioForRecognitionManager =
            PlayAudioForRecognitionManager(requireContext(), fileName, this)
    }

    private fun setupPlayAudioManager(fileName: String) {
        playAudioManager = PlayAudioManager.builder()
            .timer(binding.timer)
            .playButton(binding.audioStatImage)
            .seekBar(binding.seekBar)
            .fileName(fileName)
            .context(requireContext())
            .build()

    }

    override fun speechToTextResultReceived(result: String) {
        viewModel.updateContent(args.commentId, result)
            .subscribeAndObserve {
                binding.speechToTextTV.text = result
            }
    }

    override fun onPause() {
        super.onPause()
        playAudioManager?.stopAudio()
    }
}