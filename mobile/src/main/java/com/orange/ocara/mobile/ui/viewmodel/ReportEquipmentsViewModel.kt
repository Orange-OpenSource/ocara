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

package com.orange.ocara.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentNameAndIcon
import com.orange.ocara.data.cache.database.NonTables.RuleAnswerAndLabel
import com.orange.ocara.domain.interactors.GetAuditEquipmentAnomalies
import com.orange.ocara.domain.interactors.GetAuditEquipmentNameAndIcon
import com.orange.ocara.domain.interactors.LoadAuditObjectComments
import com.orange.ocara.domain.models.CommentForCommentListModel
import com.orange.ocara.domain.models.CommentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReportEquipmentsViewModel @Inject constructor(
        private val getAuditEquipmentNameAndIcon: GetAuditEquipmentNameAndIcon,
        private val loadAuditObjectComments: LoadAuditObjectComments,
        private val getAuditEquipmentAnomalies: GetAuditEquipmentAnomalies) : ViewModel() {

    fun getAuditEquimentInfo(id: Int): Single<AuditEquipmentNameAndIcon> {
        return getAuditEquipmentNameAndIcon.execute(id)
    }

    fun loadComments(id: Int): Single<ArrayList<CommentForCommentListModel>> {
        return loadAuditObjectComments.execute(id.toLong())
                .map (::convertCommentToListMode)
    }

    private fun convertCommentToListMode(commentModels: List<CommentModel>): ArrayList<CommentForCommentListModel> {
        val commentForCommentListModels = ArrayList<CommentForCommentListModel>()
        commentModels.forEach {
            commentForCommentListModels.add(CommentForCommentListModel(it))
        }
        return commentForCommentListModels
    }

    fun getAnomalies(auditEqId: Int): Single<ArrayList<RuleAnswerAndLabel>>{
        Timber.d("nnnn getAnomalies id = $auditEqId")
        return getAuditEquipmentAnomalies.execute(auditEqId)
                .map(::convertListToArrayList)

    }
    private fun convertListToArrayList(rules:List<RuleAnswerAndLabel>):ArrayList<RuleAnswerAndLabel>{
        Timber.d("nnnn convertListToArrayList = ${rules.size}")
        val res = ArrayList<RuleAnswerAndLabel>()
        rules.forEach {
            res.add(it)
        }
        return res
    }
}