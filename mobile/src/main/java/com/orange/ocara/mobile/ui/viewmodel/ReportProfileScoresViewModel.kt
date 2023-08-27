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
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentForReport
import com.orange.ocara.domain.interactors.*
import com.orange.ocara.domain.models.AuditInfoForReportModel
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.domain.models.ProfileAnswersUIModel
import com.orange.ocara.domain.models.ProfileTypeModel
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserveOnIo

import com.orange.ocara.mobile.workers.Mediator
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReportProfileScoresViewModel @Inject
constructor(private var getAuditEquipmentForReport: GetAuditEquipmentForReport,
            private var getAuditScoresTask: GetAuditScoresTask,
            private var getAuditRulesetInfo: GetAuditRulesetInfo
) : ViewModel() {

    val allProfiles = ArrayList<ProfileAnswersUIModel>()
    val profilesLiveData = MutableLiveData<ArrayList<ProfileAnswersUIModel>>()
    val disposables: CompositeDisposable = CompositeDisposable()


    fun getEquipments(auditId: Int): Single<List<AuditEquipmentForReport>> {
        return getAuditEquipmentForReport.execute(auditId)
    }

    private fun getRulesetInfo(auditId: Int): Observable<List<ProfileAnswersUIModel>> {
        return getAuditRulesetInfo.execute(auditId)
                .toObservable()
                .concatMap {
                    getAuditScoresTask.execute(auditId.toLong(), it.reference, it.version)
                }
    }

    fun getAuditScores(auditId: Int) {
        getRulesetInfo(auditId)
                .single(emptyList())
                .subscribeAndObserve {
                    allProfiles.clear()
                    //allProfiles.addAll(removeAllDisabilitiesOption(it))
                    allProfiles.addAll(putAllDisabilitiesOptionAtBegin(it))
                    profilesLiveData.postValue(allProfiles)
                }
    }

    private fun putAllDisabilitiesOptionAtBegin(profileAnswersUIModels: List<ProfileAnswersUIModel>): List<ProfileAnswersUIModel> {
        val newList: MutableList<ProfileAnswersUIModel> = ArrayList(profileAnswersUIModels)
//        newList.addAll(profileAnswersUIModels)
        //last item is all disablities
        if (newList.get((newList.size - 1)).profileTypeModel.reference.equals(GetAuditScoresTask.ALL_DISABILITIES, ignoreCase = true)) {
            newList.add(0, newList.get(newList.size - 1))
            newList.removeAt(newList.size - 1)
        }
        return newList
    }

    private fun removeAllDisabilitiesOption(profileAnswersUIModels: List<ProfileAnswersUIModel>): List<ProfileAnswersUIModel> {
        val newList: MutableList<ProfileAnswersUIModel> = ArrayList(profileAnswersUIModels)
        for (profile in newList) {
            if (profile.profileTypeModel.reference.equals(GetAuditScoresTask.ALL_DISABILITIES, ignoreCase = true)) {
                newList.remove(profile)
                break
            }
        }
        return newList
    }


    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
