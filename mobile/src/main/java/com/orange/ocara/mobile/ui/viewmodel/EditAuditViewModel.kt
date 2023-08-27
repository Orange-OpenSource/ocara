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
import com.orange.ocara.domain.interactors.*
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditAuditViewModel @Inject constructor(
        private val editAuditTask: EditAuditTask,
        private val loadAudit: LoadAuditWithSiteAndAuditorAndRulesetByIdTask,
        retrieveAllSitesSortedDesc: RetrieveAllSitesSortedDesc,
        loadAllAuditors: LoadAllAuditorsSortedDesc,
        getNumberOfRelatedAuditsToRuleset: GetNumberOfRelatedAuditsToRuleset,
        savePreferredRulesetTask: NewSavePreferredRulesetTask,
        downloadRulesetAndInsertRx: DownloadRulesetAndInsertRx,
        updateSiteTask: UpdateSiteTask,
        private val deleteRuleset: DeleteRuleset,
        updateAuditorTask: UpdateAuditorTask,

        getNumberOfRelatedAuditsToSite: GetNumberOfRelatedAuditsToSite,
        deleteSite: DeleteSite,
        getNumberOfRelatedAuditsToAuditor: GetNumberOfRelatedAuditsToAuditor,
        deleteAuditor: DeleteAuditor,
) :
        AuditInfoParentViewModel(retrieveAllSitesSortedDesc, loadAllAuditors,
                getNumberOfRelatedAuditsToRuleset, savePreferredRulesetTask,
                downloadRulesetAndInsertRx, updateSiteTask,
                deleteRuleset, updateAuditorTask,
                getNumberOfRelatedAuditsToSite, deleteSite,
                getNumberOfRelatedAuditsToAuditor, deleteAuditor) {

    private val _auditLiveData = MutableLiveData<AuditModel>()
    val auditLiveData = _auditLiveData

    fun getCurrentAudit(): AuditModel {
        return auditLiveData.value!!
    }

    fun updateAudit(audit: AuditModel): Completable {
        return editAuditTask.execute(audit)
    }

    fun loadAudit(id: Int) {
        loadAudit.execute(id)
                .subscribeAndObserve {
                    Timber.d("EditAuditViewModel: loadAudit")
                    _auditLiveData.postValue(it)
                    rulesetsLiveData.postValue(listOf(it.ruleset))
                }
    }

    override fun getRulesets() {
        if (auditLiveData.value != null) {
            rulesetsLiveData.postValue(listOf(auditLiveData.value!!.ruleset))
        }
    }
}