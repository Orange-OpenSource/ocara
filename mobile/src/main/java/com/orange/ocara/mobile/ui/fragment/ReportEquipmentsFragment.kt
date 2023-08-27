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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.ocara.data.cache.database.NonTables.RuleAnswerAndLabel
import com.orange.ocara.domain.models.CommentForCommentListModel
import com.orange.ocara.domain.models.CommentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentReportEquipmentsBinding
import com.orange.ocara.mobile.ui.adapters.AnomaliesAdapter
import com.orange.ocara.mobile.ui.adapters.CommentsAdapter
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.managers.PlayAudioSuperManager
import com.orange.ocara.mobile.ui.managers.ShowAndCollapseViewWithAnimationManager
import com.orange.ocara.mobile.ui.viewmodel.ReportEquipmentsViewModel
import com.orange.ocara.utils.enums.CommentType
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ReportEquipmentsFragment : OcaraFragment() {

    private val args: ReportEquipmentsFragmentArgs by navArgs()
    lateinit var binding: FragmentReportEquipmentsBinding
    lateinit var viewModel: ReportEquipmentsViewModel
    lateinit var commentsAdapter: CommentsAdapter
    lateinit var anomaliesAdapter: AnomaliesAdapter
    private val audioSuperManager: PlayAudioSuperManager = PlayAudioSuperManager()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_report_equipments, container, false)
        viewModel = ViewModelProvider(this).get(ReportEquipmentsViewModel::class.java)
        initShowAndCollapseView()
        loadEquipmentInfo()
        loadComments()
        loadAnomalies()
        addClickListener()
        return binding.root
    }

    private fun addClickListener() {
        if (args.isEditable) {
            binding.auditTV.visibility = View.VISIBLE
            binding.auditTV.setOnClickListener {
                navigateToQuestionsFragment()
            }
        }else{
            binding.auditTV.visibility = View.GONE
        }
    }

    private fun navigateToQuestionsFragment() {
        NavHostFragment.findNavController(this)
                .navigate(ReportEquipmentsFragmentDirections
                        .actionReportEquipmentsFragmentToQuestionsFragment(args.auditEquipmentId))
    }

    private fun initShowAndCollapseView() {
        ShowAndCollapseViewWithAnimationManager(
                binding.commentsListCard,
                binding.commentsView,
                binding.showComments,
                isShown = true,
        )
        ShowAndCollapseViewWithAnimationManager(
                binding.anomaliesListCard,
                binding.anomaliesView,
                binding.showAnomalies,
                isShown = true,
        )
    }

    private fun loadAnomalies() {
        viewModel.getAnomalies(args.auditEquipmentId)
                .subscribeAndObserve {
                    setAnomliesList(it)
                    setAnomaliesCount(it.size)
                }
    }

    private fun setAnomliesList(rules: ArrayList<RuleAnswerAndLabel>) {
        anomaliesAdapter = AnomaliesAdapter(rules)
        binding.anomaliesList.adapter = anomaliesAdapter
        binding.anomaliesList.layoutManager = LinearLayoutManager(requireContext())
        binding.anomaliesList.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
    }

    private fun setCommentsList(it: ArrayList<CommentForCommentListModel>) {
        commentsAdapter = CommentsAdapter(requireContext(), it, audioSuperManager, ::onCommentClick)
        binding.commentsList.adapter = commentsAdapter
        binding.commentsList.layoutManager = LinearLayoutManager(requireContext())
        binding.commentsList.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
    }

    private fun loadComments() {
        viewModel.loadComments(args.auditEquipmentId)
                .subscribeAndObserve {
                    setCommentsList(it)
                    setCommentCount(it.size)
                }
    }

    private fun setCommentCount(size: Int) {
        binding.commentsCount.text = getString(R.string.report_eq_comments) + " ($size)"
    }

    private fun setAnomaliesCount(size: Int) {
        binding.anomaliesCount.text = getString(R.string.report_eq_anomalies) + " ($size)"
    }

    private fun onCommentClick(comment: CommentModel) {
        when (comment.type) {
            CommentType.TEXT -> navigateToTextComment(comment.id)
            CommentType.AUDIO -> navigateToAudioComment(comment.id)
            CommentType.PHOTO, CommentType.FILE -> navigateToFileComment(comment.id)
        }
    }

    private fun navigateToFileComment(id: Int) {
        NavHostFragment.findNavController(this)
                .navigate(ReportEquipmentsFragmentDirections.actionReportEquipmentsFragmentToPhotoCommentFragment(id))
    }

    private fun navigateToAudioComment(id: Int) {
        NavHostFragment.findNavController(this)
                .navigate(ReportEquipmentsFragmentDirections.actionReportEquipmentsFragmentToAudioCommentNav(id))
    }

    private fun navigateToTextComment(id: Int) {
        NavHostFragment.findNavController(this)
                .navigate(ReportEquipmentsFragmentDirections.actionReportEquipmentsFragmentToTextCommentFragment(id, 0, false))
    }

    private fun loadEquipmentInfo() {
        viewModel.getAuditEquimentInfo(args.auditEquipmentId)
                .subscribeAndObserve {
                    binding.equipmentName.text = it.name
                    loadIconIntoImageView(it.icon)
                }
    }

    private fun loadIconIntoImageView(iconPath: String) {
        val path = requireContext().externalCacheDir.toString() + File.separator + iconPath
        val icon = File(path)
        Picasso
                .with(context)
                .load(icon)
                .error(android.R.color.black)
                .into(binding.equipmentIcon)
    }

    override fun onStop() {
        super.onStop()
        audioSuperManager.stopAll()
    }
}