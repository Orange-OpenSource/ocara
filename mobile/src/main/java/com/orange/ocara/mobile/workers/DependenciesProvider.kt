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

package com.orange.ocara.mobile.workers

import com.orange.ocara.domain.interactors.GetProfileTypeFromRuleSetAsMap
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.domain.models.DocReportProfileQuestionsRulesAnswersModel
import com.orange.ocara.domain.models.ProfileTypeModel
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserveOnIo
import com.orange.ocara.mobile.ui.viewmodel.ReportViewModel
import timber.log.Timber
import javax.inject.Inject


class DependenciesProvider @Inject constructor(
    private val getProfileTypeFromRuleSetAsMap: GetProfileTypeFromRuleSetAsMap
) : Mediator {

    private var profileTypeAsMap: Map<String, ProfileTypeModel>? = null
    private var auditModel: AuditModel? = null
    private var profiles: ArrayList<DocReportProfileQuestionsRulesAnswersModel>? = null

    init {
        Timber.d("init")
    }


    override fun provideProfileMap(): Map<String, ProfileTypeModel>? {
        return profileTypeAsMap
    }

    override fun getProfileMapFromViewModel(map: Map<String, ProfileTypeModel>?) {
        profileTypeAsMap = map
    }

    override fun provideAuditModel(): AuditModel? {
        return auditModel
    }

    override fun provideAuditProfiles(): ArrayList<DocReportProfileQuestionsRulesAnswersModel>? {
        return profiles
    }
    override fun getAuditModelFromViewModel(auditModel: AuditModel?) {
        this.auditModel = auditModel
    }

    override fun getAuditProfileModelsFromViewModel(profiles: ArrayList<DocReportProfileQuestionsRulesAnswersModel>?) {
        this.profiles = profiles
    }


}