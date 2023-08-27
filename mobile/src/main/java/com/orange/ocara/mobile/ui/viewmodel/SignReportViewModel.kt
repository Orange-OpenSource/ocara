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
class SignReportViewModel @Inject
constructor(
        private var lockAuditTask: LockAudit,
) : ViewModel() {

    fun lockAudit(auditId: Int) : Completable {
        return lockAuditTask.execute(auditId.toLong())
    }

}

