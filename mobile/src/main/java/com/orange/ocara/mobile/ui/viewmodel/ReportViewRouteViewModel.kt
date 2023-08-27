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
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentWithAuditAndEquipment
import com.orange.ocara.domain.interactors.LoadAuditEquipments
import com.orange.ocara.domain.models.AuditEquipmentModel
import com.orange.ocara.domain.models.AuditEquipmentWithNumberOfCommentAndOrderModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import javax.inject.Inject

@HiltViewModel
class ReportViewRouteViewModel @Inject constructor(private val loadAuditEquipments: LoadAuditEquipments) :
    ViewModel() {
    fun loadAuditEquipments(auditId: Int): Single<ArrayList<AuditEquipmentModel>> {
        return loadAuditEquipments.execute(auditId.toLong())
            .map {
                mapListToArrayList(it)
            }
    }
//    fun sortEquipments(auditEquipments : ArrayList<AuditEquipmentModel>):ArrayList<AuditEquipmentModel> {
//        auditEquipments?.sortedBy { auditEquipment ->
//            (auditEquipment as AuditEquipmentWithAuditAndEquipment).order
//        }
//        return auditEquipments
//    }
    private fun mapListToArrayList(it: List<AuditEquipmentModel>): ArrayList<AuditEquipmentModel> {
        return ArrayList(it)
    }
}