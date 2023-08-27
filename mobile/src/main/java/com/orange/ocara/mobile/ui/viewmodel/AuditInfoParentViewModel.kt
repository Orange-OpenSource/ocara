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

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orange.ocara.domain.interactors.*
import com.orange.ocara.domain.models.AuditorModel
import com.orange.ocara.domain.models.RulesetModel
import com.orange.ocara.domain.models.SiteModel
import com.orange.ocara.mobile.ui.extensionFunctions.addTo
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class AuditInfoParentViewModel(
        private val retrieveAllSitesSortedDesc: RetrieveAllSitesSortedDesc,
        private val loadAllAuditors: LoadAllAuditorsSortedDesc,
        private val getNumberOfRelatedAuditsToRuleset: GetNumberOfRelatedAuditsToRuleset,
        private val savePreferredRulesetTask: NewSavePreferredRulesetTask,
        private val downloadRulesetAndInsertRx: DownloadRulesetAndInsertRx,
        private val updateSiteTask: UpdateSiteTask,
        private val deleteRuleset: DeleteRuleset,
        private val updateAuditorTask: UpdateAuditorTask,
        private val getNumberOfRelatedAuditsToSite: GetNumberOfRelatedAuditsToSite,
        private val deleteSite: DeleteSite,
        private val getNumberOfRelatedAuditsToAuditor: GetNumberOfRelatedAuditsToAuditor,
        private val deleteAuditor: DeleteAuditor,


        ) : ViewModel() {
    val rulesetDownloadCompleteLiveData = MutableLiveData<RulesetModel>()
    val currentSite: MutableLiveData<SiteModel> = MutableLiveData()
    val currentAuditor: MutableLiveData<AuditorModel> = MutableLiveData()
    val currentAuditorFullName: MutableLiveData<String> = MutableLiveData()
    val allSitesLive = MutableLiveData<List<SiteModel>>()
    val allAuditorsLive = MutableLiveData<List<AuditorModel>>()
    val rulesetsLiveData: MutableLiveData<List<RulesetModel>> = MutableLiveData()
    val currentAuditName: MutableLiveData<String> = MutableLiveData()
    val expertModeLiveData: MutableLiveData<Boolean> = MutableLiveData()

    var expertMode = false
        get() = field
        set(mode) {
            field = mode
            expertModeLiveData.postValue(mode)
        }
    val rulesetDeleted = MutableLiveData<Boolean>()

    val siteDeleted = MutableLiveData<Boolean>()
    val auditorDeleted = MutableLiveData<Boolean>()

    protected val disposables: CompositeDisposable = CompositeDisposable()

    abstract fun getRulesets()

    fun downloadRuleset(ruleset: RulesetModel) {
        downloadRulesetAndInsertRx.execute(ruleset)
                .subscribeAndObserve {
                    rulesetDownloadCompleteLiveData.postValue(ruleset)
                }
    }

    fun deleteRuleset(rulesetId: Int) {
        deleteRuleset.execute(rulesetId)
                .subscribeAndObserve {
                    rulesetDeleted.postValue(true)
                    savePreferedRuleset(null)
                    Timber.d("ruleset deleted successfully")
                }.addTo(disposables)
    }

    fun savePreferedRuleset(ruleset: RulesetModel?) {
        savePreferredRulesetTask.execute(ruleset)
    }

    fun getNumberOfRelatedAuditsToRuleset(id: Int): Single<Int> {
        return getNumberOfRelatedAuditsToRuleset.execute(id)
    }

    fun loadAllSite(defaultSite: Int = -1, fromDelete: Boolean = false) {
        System.err.println("LOAD SITES " + defaultSite)
        retrieveAllSitesSortedDesc.execute()
                .subscribeAndObserve { sitesList ->
                    Log.d(CreateAuditViewModel.TAG, "loadAllSite success")
                    allSitesLive.postValue(sitesList)
                    postDefaultSite(defaultSite, sitesList,fromDelete)
                }

    }

    private fun postDefaultSite(defaultSite: Int, sitesList: List<SiteModel>, fromDelete: Boolean = false) {
        if (fromDelete){
            currentSite.postValue(null)
            return
        }
        if (defaultSite == -1) {
            if (currentSite.value != null) {
                val currId = currentSite!!.value!!.id
                val currentSiteFound = sitesList.firstOrNull { it.id == currId }
                if (currentSiteFound != null)
                    currentSite.postValue(currentSiteFound)
                else
                    currentSite.postValue(currentSite.value)
                return
            }
            sitesList.apply { if (!this.isNullOrEmpty()) currentSite.postValue(this.first()) }
        } else {
            currentSite.postValue(sitesList.first {
                it.id == defaultSite
            })
        }
    }

    fun updateSite(site: SiteModel) = updateSiteTask.execute(site)

    fun updateSelectedSite(site: SiteModel) {
        currentSite.postValue(site)
    }

    fun hasSelectedSite() = currentSite.value != null


    fun updateAuditor(auditor: AuditorModel) = updateAuditorTask.execute(auditor)

    fun hasSelectedAuditor() = currentAuditor.value != null


    fun updateSelectedAuditor(auditor: AuditorModel) {
        currentAuditor.postValue(auditor)
        currentAuditorFullName.postValue(auditor.firstName + " " + auditor.lastName)
    }

    fun updateAuditName(auditName: String) {
        currentAuditName.postValue(auditName)
    }

    fun loadAuditors(defaultAuditor: Int = -1, fromDelete: Boolean = false) {
        loadAllAuditors.execute()
                .subscribeAndObserve { auditors ->
                    allAuditorsLive.postValue(auditors)
//                    auditors.apply { if (!this.isNullOrEmpty()) currentAuditor.postValue(this.first()) }
                    postDefaultAuditor(defaultAuditor, auditors , fromDelete)
                }
    }

    private fun postDefaultAuditor(defaultAuditor: Int, auditorsList: List<AuditorModel>, fromDelete: Boolean = false) {
        if (fromDelete) {
            currentAuditor.postValue(null)
            currentAuditorFullName.postValue(null)
            return
        }
        if (defaultAuditor == -1) {
            if (currentAuditor.value != null) {
                val currId = currentAuditor!!.value!!.id
                val currAuditorFound = auditorsList.firstOrNull { it.id == currId }
                if (currAuditorFound != null) {
                    currentAuditor.postValue(currAuditorFound)
                    currentAuditorFullName.postValue(currAuditorFound!!.firstName + " " + currAuditorFound!!.lastName)

                } else {
                    currentAuditor.postValue(currentAuditor.value)
                    currentAuditorFullName.postValue(currentAuditor.value!!.firstName + " " + currentAuditor.value!!.lastName)
                }
                return
            }
            auditorsList.apply {
                if (!this.isNullOrEmpty()) {
                    currentAuditor.postValue(this.first())
                    currentAuditorFullName.postValue(this.first().firstName + " " + this.first().lastName)
                }
            }
        } else {
            var auditor = auditorsList.first {
                it.id == defaultAuditor
            }
            currentAuditor.postValue(auditor)
            currentAuditorFullName.postValue(auditor.firstName + " " + auditor.lastName)

        }
    }

    fun filterSites(filter: String): List<SiteModel> {
        val result = ArrayList<SiteModel>()
        allSitesLive.value?.forEach { site ->
            if (site.name.contains(filter)) {
                result.add(site)
            }
        }
        return result
    }

    fun filterAuditors(filter: String): List<AuditorModel> {
        val result = ArrayList<AuditorModel>()
        allAuditorsLive.value?.forEach { auditor ->
            if (auditor.firstName.toLowerCase().contains(filter.toLowerCase())) {
                System.err.println("FOUND !! " + filter + " : " + auditor.firstName)
                result.add(auditor)
            }
        }
        return result
    }


    fun getNumberOfRelatedAuditsToSite(id: Int): Single<Int> {
        return getNumberOfRelatedAuditsToSite.execute(id)
    }

    fun deleteSite(id: Int) {
        deleteSite.execute(id)
                .subscribeAndObserve {
                    siteDeleted.postValue(true)
                    loadAllSite(fromDelete = true)
                    Timber.d("site deleted successfully")
                }.addTo(disposables)
    }

    fun getNumberOfRelatedAuditsToAuditor(id: Int): Single<Int> {
        return getNumberOfRelatedAuditsToAuditor.execute(id)
    }

    fun deleteAuditor(id: Int) {
        deleteAuditor.execute(id)
                .subscribeAndObserve {
                    auditorDeleted.postValue(true)
                    loadAuditors(fromDelete = true)
                    Timber.d("auditor deleted successfully")
                }.addTo(disposables)
    }

    fun clearAudit() {
        currentAuditName.postValue("")
        expertMode = false
    }

    override fun onCleared() {
        clearAudit()
        super.onCleared()
    }
}