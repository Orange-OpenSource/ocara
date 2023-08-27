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
import com.orange.ocara.domain.interactors.LoadEquipmentsWithRulesetId
import com.orange.ocara.domain.models.EquipmentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class RulsetEquipmentsViewModel @Inject constructor(private val loadEquipmentsWithRulesetId
: LoadEquipmentsWithRulesetId) : ViewModel() {
    // this variable will be contain all the equipments to use it in filtering
    val allEquipments = ArrayList<EquipmentModel?>()
    val equipmentsLiveData = MutableLiveData<List<EquipmentModel?>?>()
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun getEquipments(rulesetId: Long) {
        loadEquipmentsWithRulesetId.execute(rulesetId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe{equipments->
                    equipmentsLiveData.postValue(equipments)
                    allEquipments.clear()
                    allEquipments.addAll(equipments)
//                    allEquipments.addAll(equipments)
                }.let {
                    disposables.add(it)
                }
    }
    fun filterEquipments(filter: String) {
        val result = ArrayList<EquipmentModel>()
        allEquipments.forEach { equipment ->
            if (equipment?.name?.toLowerCase()?.contains(filter) == true) {
                result.add(equipment)
            }
        }
        equipmentsLiveData.postValue(result)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}