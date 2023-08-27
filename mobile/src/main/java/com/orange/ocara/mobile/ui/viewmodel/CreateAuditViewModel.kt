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
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orange.ocara.domain.interactors.*
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.domain.models.AuditorModel
import com.orange.ocara.domain.models.RulesetModel
import com.orange.ocara.domain.models.SiteModel
import com.orange.ocara.mobile.ui.extensionFunctions.addTo
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateAuditViewModel @Inject constructor(
        private val retrieveRulesetsTaskWithRxJava: RetrieveRulesetsTaskWithRxJava,
        private val insertAuditTask: InsertAuditTask,
        private val deleteRuleset: DeleteRuleset,
        downloadRulesetAndInsertRx: DownloadRulesetAndInsertRx,
        savePreferredRulesetTask: NewSavePreferredRulesetTask,
        getNumberOfRelatedAuditsToRuleset: GetNumberOfRelatedAuditsToRuleset,
        retrieveAllSitesSortedDesc: RetrieveAllSitesSortedDesc,
        loadAllAuditors: LoadAllAuditorsSortedDesc,
        updateSiteTask: UpdateSiteTask,
        updateAuditorTask: UpdateAuditorTask,

        getNumberOfRelatedAuditsToSite: GetNumberOfRelatedAuditsToSite,
        deleteSite: DeleteSite,
        getNumberOfRelatedAuditsToAuditor: GetNumberOfRelatedAuditsToAuditor,
        deleteAuditor: DeleteAuditor,

        ) : AuditInfoParentViewModel(retrieveAllSitesSortedDesc,
        loadAllAuditors,
        getNumberOfRelatedAuditsToRuleset,
        savePreferredRulesetTask,
        downloadRulesetAndInsertRx,
        updateSiteTask, deleteRuleset, updateAuditorTask,
        getNumberOfRelatedAuditsToSite, deleteSite,
        getNumberOfRelatedAuditsToAuditor, deleteAuditor
) {


    override fun getRulesets() {
        retrieveRulesetsTaskWithRxJava.execute()
                .subscribeAndObserve {
                    rulesetsLiveData.postValue(it)
                }.addTo(disposables)
    }


    fun insertAudit(auditModel: AuditModel): Single<Long> {
        return insertAuditTask.execute(auditModel)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    companion object {
        val TAG = "CreateAuditViewModel"
    }
}