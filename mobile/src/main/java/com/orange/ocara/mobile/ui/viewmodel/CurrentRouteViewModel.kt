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

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.orange.ocara.domain.interactors.*
import com.orange.ocara.domain.models.AuditEquipmentForCurrentRouteModel
import com.orange.ocara.domain.models.AuditEquipmentModel
import com.orange.ocara.domain.models.AuditEquipmentWithNumberOfCommentAndOrderModel
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CurrentRouteViewModel @Inject constructor(
    private val loadAuditWithAuditEquipmentsAndComments: LoadAuditWithAuditEquipmentsAndComments,
    private val updateAuditEquipmentOrder: UpdateAuditEquipmentOrder,
    private val deleteAuditEquipment: DeleteAuditEquipmentWithItsData,
    private val deleteAuditEquipments: DeleteAuditEquipmentsWithThereData,
    private val updateAuditEquipmentListOrder: UpdateListOfAuditEquipmentsOrder,


    ) : ViewModel() {
    private val disposables: CompositeDisposable = CompositeDisposable()
    var audit: AuditModel? = null
    private val equipmentsOrderChanged = MutableLiveData<Boolean>()

    fun addObserverForEquipmentsOrderChanged(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<Boolean>
    ) {
        equipmentsOrderChanged.observe(lifecycleOwner, observer)
    }

    fun getAudit(auditId: Int): Single<AuditModel> {
        return loadAuditWithAuditEquipmentsAndComments.execute(auditId).map {
            audit = it
            sortEquipments()
            Timber.d("getAudit number of objects ${audit!!.objects.size}")
            it
        }
    }

    fun deleteAuditEquipment(auditEquipmentId: Int): Completable {
        return deleteAuditEquipment.execute(auditEquipmentId)
    }


    fun deleteAuditEquipments(eqs: List<AuditEquipmentModel>): Completable {
        var auditEquipmentId = ArrayList<Int>()
        for (eq: AuditEquipmentModel in eqs) {
            if ((eq as AuditEquipmentForCurrentRouteModel).isSelected)
                auditEquipmentId.add(eq.id)
        }
        return deleteAuditEquipments.execute(auditEquipmentId)
    }

    fun sortEquipments() {
        val auditEquipments = audit?.objects
        audit?.objects = auditEquipments?.sortedBy { auditEquipment ->
            (auditEquipment as AuditEquipmentWithNumberOfCommentAndOrderModel).order
        }
    }

    fun onUpClick(position: Int) {
        if (checkIsFirstAuditEquipmentIndex(position)) return
//        updatePositionInDB(position, 1)
        updatePositionInMemory(position, 1)
    }

    fun onDownClick(position: Int) {
        if (checkIsLastAuditEquipmentIndex(position)) return
//        updatePositionInDB(position, -1)
        updatePositionInMemory(position, -1)
    }

    private fun updatePositionInDB(position: Int, add: Int) {
        val currentEquipment =
            audit!!.objects[position] as AuditEquipmentWithNumberOfCommentAndOrderModel
        val otherEquipment =
            audit!!.objects[position - add] as AuditEquipmentWithNumberOfCommentAndOrderModel
        updateAuditEquipmentOrder.execute(currentEquipment.id, currentEquipment.order - add)
            .subscribeAndObserve {
                Timber.d("updated ${currentEquipment.id} from ${currentEquipment.order} to ${currentEquipment.order + 1}")
            }
        updateAuditEquipmentOrder.execute(otherEquipment.id, otherEquipment.order + add)
            .subscribeAndObserve {
                Timber.d("updated ${otherEquipment.id} from ${otherEquipment.order} to ${otherEquipment.order + 1}")
            }
    }

    // add = 1 when upClick , add = -1 when downClick
    private fun updatePositionInMemory(position: Int, add: Int) {
        val otherEquipment =
            audit!!.objects[position - add] as AuditEquipmentWithNumberOfCommentAndOrderModel
        val currentEquipment =
            audit!!.objects[position] as AuditEquipmentWithNumberOfCommentAndOrderModel
        // updating order
        currentEquipment.order -= add
        otherEquipment.order += add

        audit!!.objects[position - add] = currentEquipment
        audit!!.objects[position] = otherEquipment

        equipmentsOrderChanged.postValue(true)
    }

    public fun performUpdateOrderInDB() : Completable {
        return updateAuditEquipmentListOrder.execute(audit!!.objects as List <AuditEquipmentWithNumberOfCommentAndOrderModel>)
    }

    private fun checkIsLastAuditEquipmentIndex(position: Int) = audit!!.objects.size - 1 <= position

    private fun checkIsFirstAuditEquipmentIndex(position: Int) = position == 0

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}