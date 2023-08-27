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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orange.ocara.domain.interactors.DeleteAuditEquipmentWithItsData
import com.orange.ocara.domain.interactors.GetAuditEquipmentById
import com.orange.ocara.domain.interactors.LoadAuditObjectName
import com.orange.ocara.domain.interactors.UpdateAuditObjectName
import com.orange.ocara.domain.models.AuditEquipmentWithNumberOfCommentAndOrderModel
import com.orange.ocara.mobile.ui.extensionFunctions.addTo
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

abstract class AuditEquipmentDetailsParentViewModel(
        private val getAuditEquipmentById: GetAuditEquipmentById,
        private val deleteAuditEquipmentWithItsData: DeleteAuditEquipmentWithItsData,
        private val updateAuditObjectName: UpdateAuditObjectName,
        private val loadAuditObjectName: LoadAuditObjectName
) : ViewModel() {
    val disposables = CompositeDisposable()
    val auditEquipmentLiveData = MutableLiveData<AuditEquipmentWithNumberOfCommentAndOrderModel>()

    fun getAuditEquipment(auditEquipmentId: Int) {
        getAuditEquipmentById.execute(auditEquipmentId)
                .subscribeAndObserve {
                    auditEquipmentLiveData.postValue(it)
                }.addTo(disposables)
    }

    fun loadAuditObjectName(auditEquipmentId: Int): Single<String> {
        return loadAuditObjectName.execute(auditEquipmentId)
    }

    fun updateAuditEquipmentName(auditEquipmentId: Int, name: String): Completable {
        return updateAuditObjectName.execute(auditEquipmentId, name)
    }

    fun deleteAuditEquipment(auditEquipmentId: Int): Completable {
        return deleteAuditEquipmentWithItsData.execute(auditEquipmentId)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}