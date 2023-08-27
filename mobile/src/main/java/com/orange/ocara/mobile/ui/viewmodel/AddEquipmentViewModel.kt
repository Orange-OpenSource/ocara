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
import com.orange.ocara.domain.interactors.InsertAuditEquipment
import com.orange.ocara.domain.interactors.LoadAuditById
import com.orange.ocara.domain.interactors.LoadEquipmentsWithRulesetId
import com.orange.ocara.domain.interactors.LoadRulesetIdByAuditId
import com.orange.ocara.domain.models.AuditEquipmentModel
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.domain.models.EquipmentForAddEquipmentModel
import com.orange.ocara.domain.models.EquipmentModel
import com.orange.ocara.mobile.exceptions.AddEmptyEquipmentListException
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@HiltViewModel
class AddEquipmentViewModel @Inject constructor(
        private val insertAuditEquipment: InsertAuditEquipment,
        private val loadEquipmentsWithRulesetId: LoadEquipmentsWithRulesetId,
        private val loadRulesetIdByAuditId: LoadRulesetIdByAuditId,
        private val loadAuditById: LoadAuditById
) : ViewModel() {

    var audit: AuditModel? = null

    fun getRulesetId(auditId: Int): Single<Int> {
        return loadRulesetIdByAuditId.execute(auditId)
    }

    fun loadAuditById(auditId: Int){
        loadAuditById.execute(auditId.toLong())
                .subscribeAndObserve {
                    audit = it
                }
    }

    fun addEquipmentsToAudit(equipments: List<EquipmentModel>): Completable {
        if (equipments.isEmpty()) {
            // this exception shouldn't be called as the caller of the this method should make sure that he isn't sending an empty list
            throw AddEmptyEquipmentListException("cannot add empty list of equipment to audit")
        }
        var result = Completable.fromSingle(addEquipmentToAudit(equipments[0]))
        for (i in 1 until equipments.size) {
            result = result.concatWith(Completable.fromSingle(addEquipmentToAudit(equipments[i])))
        }
        return result
    }

    private fun addEquipmentToAudit(equipment: EquipmentModel): Single<Long> {
        val auditEquipment = createAuditEquipmentModel(equipment, audit!!)
        return insertAuditEquipment.execute(auditEquipment)
    }

    fun loadEquipments(rulesetId: Int): Single<List<EquipmentForAddEquipmentModel>> {
        return loadEquipmentsWithRulesetId.execute(rulesetId.toLong())
                .map(this::convertEquipmentsToAddEquipmentModel)
    }

    private fun convertEquipmentsToAddEquipmentModel(equipments:List<EquipmentModel>): List<EquipmentForAddEquipmentModel> {
        val result = ArrayList<EquipmentForAddEquipmentModel>()
        for (equipment in equipments) {
            result.add(EquipmentForAddEquipmentModel(equipment))
        }
        return result
    }

    private fun createAuditEquipmentModel(equipment: EquipmentModel, audit: AuditModel): AuditEquipmentModel {
        return AuditEquipmentModel.Builder()
                .setAudit(audit)
                .setEquipment(equipment)
                .setName(equipment.name)
                .build()
    }

}