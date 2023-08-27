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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentRecordCommentBinding
import com.orange.ocara.mobile.ui.helpers.OnAudioRecordFinished
import com.orange.ocara.mobile.ui.managers.RecordAudioManager
import com.orange.ocara.mobile.ui.viewmodel.RecordCommentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordCommentFragment : OcaraFragment(), OnAudioRecordFinished/*, DeleteCommentInterface */{
    companion object {
        const val RECORD_REQ = "RECORD_REQ"
    }


    lateinit var binding: FragmentRecordCommentBinding
    lateinit var viewModel: RecordCommentViewModel

    private val args: RecordCommentFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_record_comment, container, false)
        changeActionBarTitle()
        viewModel = ViewModelProvider(this).get(RecordCommentViewModel::class.java)
        setHasOptionsMenu(true)
        setupRecordAudioManager()

        return binding.root
    }
    private fun changeActionBarTitle() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.record_comment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.default_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onResume() {
        super.onResume()
        changeActionBarTitle()
    }

    private fun navigateToAudioComment(fileName: String) {
            NavHostFragment.findNavController(this)
                .navigate(RecordCommentFragmentDirections.actionRecordCommentFragmentToAudioCommentFragment(recordPath = fileName, commentId = args.commentId,equipmentId = args.equipmentId))
    }


    private fun setupRecordAudioManager() {
        RecordAudioManager.builder()
                .labelTV(binding.recordingStateTV)
                .fragment(this)
                .onAudioRecordFinished(this)
                .recordButton(binding.recordButton)
                .recordTimer(binding.recordTimer)
                .build()
    }

    override fun onAudioRecordFinished(fileName: String) {
        navigateToAudioComment(fileName)
    }

}