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

import com.orange.ocara.domain.interactors.*
import com.orange.ocara.domain.models.AuditEquipmentModel
import com.orange.ocara.mobile.ui.extensionFunctions.addTo
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuditEquipmentWithCharacteristicsViewModel @Inject constructor(
        getAuditEquipmentWithCharacteristics: GetAuditEquipmentWithCharacteristics,
        deleteAuditEquipmentWithItsData: DeleteAuditEquipmentWithItsData,
        updateAuditObjectName: UpdateAuditObjectName,
        loadAuditObjectName: LoadAuditObjectName,
        private val updateAuditEquipmentSubEquipmentList: UpdateAuditEquipmentSubEquipmentList
) : AuditEquipmentDetailsParentViewModel(getAuditEquipmentWithCharacteristics, deleteAuditEquipmentWithItsData, updateAuditObjectName, loadAuditObjectName) {

    fun getCopyOfAuditEquipment(): AuditEquipmentModel {
        val originalAuditEquipment = auditEquipmentLiveData.value!!
        val copiedAuditEquipment = AuditEquipmentModel.Builder()
                .setEquipment(originalAuditEquipment.equipment)
                .setAudit(originalAuditEquipment.audit)
                .setAnswer(originalAuditEquipment.answer)
                .setId(originalAuditEquipment.id)
                .setName(originalAuditEquipment.name)
                .build()
        copyChildren(originalAuditEquipment, copiedAuditEquipment)
        return copiedAuditEquipment
    }

    private fun copyChildren(originalAuditEquipment:AuditEquipmentModel,copiedAuditEquipment:AuditEquipmentModel){
        for(child in originalAuditEquipment.children){
            copiedAuditEquipment.addChild(child)
        }
    }

    fun updateSubEquipments(auditEquipment:AuditEquipmentModel){
        updateAuditEquipmentSubEquipmentList.execute(auditEquipment)
                .subscribeAndObserve{
                    Timber.d("updated subEquipments successfully")
                    getAuditEquipment(auditEquipment.id)
                }.addTo(disposables)
    }

}