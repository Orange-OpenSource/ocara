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
import com.orange.ocara.domain.interactors.InsertAuditor
import com.orange.ocara.domain.models.AuditorModel
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddAuditorViewModel @Inject constructor(private val insertAuditor: InsertAuditor) : ViewModel() {

    fun insertAuditor(auditorModel: AuditorModel): Single<Long> {
        return insertAuditor.execute(auditorModel)
    }
}