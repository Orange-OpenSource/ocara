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
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.orange.ocara.domain.models.CommentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentTextCommentBinding
import com.orange.ocara.mobile.ui.dialogs.DeleteCommentDialog
import com.orange.ocara.mobile.ui.extensionFunctions.afterTextChanged
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.helpers.DeleteCommentInterface
import com.orange.ocara.mobile.ui.setFocusBackgroundForTextField
import com.orange.ocara.mobile.ui.validation.textCommentValidator
import com.orange.ocara.mobile.ui.validation.validateAuditorName
import com.orange.ocara.mobile.ui.viewmodel.ListCommentsViewModel
import com.orange.ocara.mobile.ui.viewmodel.TextCommentViewModel
import com.orange.ocara.utils.TimeUtils
import com.orange.ocara.utils.enums.CommentType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.log

@AndroidEntryPoint
class TextCommentFragment : OcaraFragment(), DeleteCommentInterface {
    lateinit var binding: FragmentTextCommentBinding
    lateinit var viewModel: TextCommentViewModel
    val args: TextCommentFragmentArgs by navArgs()
    val commentsListViewModel: ListCommentsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_text_comment, container, false
        )
        viewModel = ViewModelProvider(this).get(TextCommentViewModel::class.java)
        setHasOptionsMenu(true)
        initClickListeners()
        loadCommentContent()

        setFocusBackgroundForTextField(binding.content, requireActivity())
        binding.content.afterTextChanged {
            if (textCommentValidator(binding.content)) {
                binding.fillCommText.visibility = View.GONE
                ViewCompat.setBackground(
                    binding.content,
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.edit_text_bg_focus,
                        null
                    )
                )
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.default_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.delete_comment)
//            showDeleteCommentDialog()
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteCommentDialog() {
        val dialog = DeleteCommentDialog(this, args.commentId)
        dialog.show(childFragmentManager, "DeleteCommentDialog")
    }

    private fun loadCommentContent() {
        viewModel.getCommentContent(args.commentId)
            .subscribeAndObserve {
                binding.content.setText(it!!)
            }
    }

    private fun initClickListeners() {
        binding.save.setOnClickListener {
            updateContent()
        }

        binding.cancelBtn.setOnClickListener {

            // binding.content.text.clear()
            requireActivity().onBackPressed()
        }
    }

    private fun updateContent() {

        if (args.isNewComment) {
            //insert new comment
            validateAndInsert()
        } else {
            //update existing comment
            updateComment()
        }

    }

    private fun updateComment() {
        if (textCommentValidator(binding.content)) {
            binding.fillCommText.visibility = View.GONE
            viewModel.changeContent(args.commentId, binding.content.text.toString())
                .subscribeAndObserve {
                    requireActivity().onBackPressed()
                }
        } else {
            binding.fillCommText.visibility = View.VISIBLE
            binding.content.requestFocus()
            ViewCompat.setBackground(
                binding.content,
                ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )
//            requireActivity().onBackPressed()
        }

    }

    private fun validateAndInsert() {
        if (textCommentValidator(binding.content)) {
            binding.fillCommText.visibility = View.GONE
            Timber.d("text comment is valid")
//           viewModel.changeContent(args.commentId, binding.content.text.toString())
//                   .subscribeAndObserve {
//                       requireActivity().onBackPressed()
//                   }

            commentsListViewModel.insertComment(buildCommentModel(binding.content.text.toString()))
                .subscribeAndObserve {
                    Timber.d("text comment inserted id = $it")
                    requireActivity().onBackPressed()
                }

        } else {
            binding.fillCommText.visibility = View.VISIBLE
//            requireActivity().onBackPressed()
            binding.content.requestFocus()
            ViewCompat.setBackground(
                binding.content,
                ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )
        }
    }

    override fun deleteComment(commentId: Int) {
        viewModel.deleteComment(commentId)
            .subscribeAndObserve {
                requireActivity().onBackPressed()
            }
    }

    private fun buildCommentModel(content: String): CommentModel {
        return CommentModel(
            0,
            CommentType.TEXT,
            TimeUtils.getTimeInFormatForDatebase(),
            "",
            content
        )
            .equipmentIdBuider(args.equipmentId.toLong())
    }
}