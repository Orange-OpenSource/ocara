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

import androidx.lifecycle.ViewModel
import com.orange.ocara.domain.interactors.InsertSiteTask
import com.orange.ocara.domain.models.SiteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@HiltViewModel
class AddSiteViewModel @Inject constructor(private val insertSiteTask: InsertSiteTask) : ViewModel() {
    fun insertSite(site:SiteModel): Single<Long> {
        return insertSiteTask.execute(site)
    }
}