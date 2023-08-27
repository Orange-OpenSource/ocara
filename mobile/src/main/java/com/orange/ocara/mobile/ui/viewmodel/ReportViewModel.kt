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
class ReportViewModel @Inject
constructor(
    private var getAuditInfoForReport: GetAuditInfoForReport,
    private var getAuditEquipmentForReport: GetAuditEquipmentForReport,
    private var getAuditScoresTask: GetAuditScoresTask,
    private var getAuditRulesetInfo: GetAuditRulesetInfo,
    private val loadAuditForExportTask: LoadAuditForExportTask,
    private val loadAuditForExportProfilesTableTask: LoadAuditForExportProfilesTableTask,
    private val getProfileTypeFromRuleSetAsMap: GetProfileTypeFromRuleSetAsMap,
    private var lockAuditTask: LockAudit,
    private val mediator: Mediator
) : ViewModel() {

    val allProfiles = ArrayList<ProfileAnswersUIModel>()
    val selectedProfilesLiveData = MutableLiveData<ArrayList<ProfileAnswersUIModel>>()
    val auditModelLiveData = MutableLiveData<AuditModel>()
    val profileTypeAsMapLive = MutableLiveData<Map<String, ProfileTypeModel>>()
    val initWorkManagerForExportingFlag = MutableLiveData<Boolean>()
    val auditInfoLiveData = MutableLiveData<AuditInfoForReportModel>()
    val auditEquipmentsInfoLiveData = MutableLiveData<List<AuditEquipmentForReport>>()
    val disposables: CompositeDisposable = CompositeDisposable()

    fun loadAuditInfo(auditId: Int) {
        getAuditInfoForReport.execute(auditId)
            .subscribeAndObserve {
                auditInfoLiveData.postValue(it)
            }
    }

    fun lockAudit(auditId: Int): Completable {
        return lockAuditTask.execute(auditId.toLong())
    }

    fun getEquipments(auditId: Int) {
        getAuditEquipmentForReport.execute(auditId)
            .subscribeAndObserve {
                auditEquipmentsInfoLiveData.postValue(it)
            }
    }

    fun getAuditScores(auditId: Int) {
        getRulesetInfo(auditId)
            .single(emptyList())
            .subscribeAndObserve {
                allProfiles.clear()
                // allProfiles.addAll(removeAllDisabilitiesOption(it))
                selectedProfilesLiveData.postValue(it as ArrayList<ProfileAnswersUIModel>)
            }
    }

    private fun removeAllDisabilitiesOption(profileAnswersUIModels: List<ProfileAnswersUIModel>): List<ProfileAnswersUIModel> {
        val newList: MutableList<ProfileAnswersUIModel> = ArrayList(profileAnswersUIModels)
        for (profile in newList) {
            if (profile.profileTypeModel.reference.equals(
                    GetAuditScoresTask.ALL_DISABILITIES,
                    ignoreCase = true
                )
            ) {
                newList.remove(profile)
                break
            }
        }
        return newList
    }

    private fun getRulesetInfo(auditId: Int): Observable<List<ProfileAnswersUIModel>> {
        return getAuditRulesetInfo.execute(auditId)
            .toObservable()
            .concatMap {
                getAuditScoresTask.execute(auditId.toLong(), it.reference, it.version)
            }
    }

    fun onDocumentFormatSelected(format: DOCFormat) {
        Timber.d("document format : ${format.name}")

//        initWorkManagerForExportingFlag.postValue(true)

    }

    fun prepareAuditModelForExporting(auditId: Int) {
        loadAuditForExportTask.execute(auditId.toLong())
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe {
                prepareAuditProfilesTableData(auditId,it.rulesetRef, it.rulesetVer)
                mediator.getAuditModelFromViewModel(it)
                getProfileMap(it.rulesetRef, it.rulesetVer)
            }.let { disposable -> disposables.add(disposable) }

    }

    fun prepareAuditProfilesTableData(auditId: Int, ruleSetRef: String, ruleSetVer: Int) {
        loadAuditForExportProfilesTableTask.execute(auditId.toLong(),ruleSetRef , ruleSetVer)
            .subscribeAndObserve {
                mediator.getAuditProfileModelsFromViewModel(it)
                initWorkManagerForExportingFlag.postValue(true)

            }.let { disposable -> disposables.add(disposable) }
    }

    private fun getProfileMap(ruleSetRef: String, ruleSetVer: Int) {
        getProfileTypeFromRuleSetAsMap.execute(ruleSetRef, ruleSetVer)
            .subscribeAndObserveOnIo {
                profileTypeAsMapLive.postValue(it)
                mediator.getProfileMapFromViewModel(it)

            }
    }


    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}

enum class DOCFormat {
    PDF,
    WORD,
    OOF
}
