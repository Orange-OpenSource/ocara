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
import com.orange.ocara.domain.interactors.DeepCopyAudit
import com.orange.ocara.domain.interactors.DeleteAudit
import com.orange.ocara.domain.interactors.LoadAuditsWithRuleset
import com.orange.ocara.domain.models.AuditForAuditListModel
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListAuditsViewModel @Inject constructor(
        private val loadAuditsWithRuleset: LoadAuditsWithRuleset,
        private val deleteAudit: DeleteAudit,
        private val deepCopyAudit: DeepCopyAudit
) : ViewModel() {

    private val auditsLiveData = MutableLiveData<ArrayList<AuditForAuditListModel>>()
    private val audits = ArrayList<AuditForAuditListModel>()

    fun addObserverForAudits(owner: LifecycleOwner, observer: Observer<ArrayList<AuditForAuditListModel>>) {
        auditsLiveData.observe(owner, observer)
    }

    fun loadAudits(filter: String = "") {
        loadAuditsWithRuleset.execute(filter)
                .map { convertAuditList(it) }
                .subscribeAndObserve {
                    audits.clear()
                    audits.addAll(it)
                    auditsLiveData.postValue(it)
                }
    }

    fun getAuditNameById(auditId: Int): String {
        return audits.filter {
            it.id == auditId
        }[0].name
    }

    private fun convertAuditList(audits: List<AuditModel>): ArrayList<AuditForAuditListModel> {
        val result = ArrayList<AuditForAuditListModel>()
        for (audit in audits) {
            result.add(AuditForAuditListModel(audit))
        }
        return result
    }

    fun deleteAudit(id: Int): Completable {
        return deleteAudit.execute(id.toLong())
    }

    fun copyAudit(id: Int, copyAnswers: Boolean) = deepCopyAudit.execute(id, copyAnswers)


    fun sortByStatus() {
        sortAudits {
            it.isInProgress
        }
    }

    fun sortBySite() {
        sortAudits {
            it.site?.name
        }
    }

    fun sortByDate() {
        sortAudits {
            it.date
        }
    }

    private fun <R : Comparable<R>> sortAudits(selector: (AuditForAuditListModel) -> R?) {
        // the result of sortedBy can't be casted to java.util.ArrayList so I have add the elements by hand to an ArrayList object
        val audits = auditsLiveData.value?.sortedBy(selector)
        postListToLiveData(audits)
    }

    private fun postListToLiveData(audits: List<AuditForAuditListModel>?) {
        val auditsList = ArrayList<AuditForAuditListModel>()
        for (audit in audits!!) {
            auditsList.add(audit)
        }
        auditsLiveData.postValue(auditsList)
    }

    fun reverse() {
        postListToLiveData(auditsLiveData.value?.reversed())
    }

    fun getAudits() = auditsLiveData.value
}