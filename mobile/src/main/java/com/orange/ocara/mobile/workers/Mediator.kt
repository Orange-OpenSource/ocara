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

import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.domain.models.DocReportProfileQuestionsRulesAnswersModel
import com.orange.ocara.domain.models.ProfileTypeModel


interface Mediator {

    fun provideProfileMap() : Map<String,ProfileTypeModel>?
    fun getProfileMapFromViewModel(map: Map<String,ProfileTypeModel>?)
    fun provideAuditModel() : AuditModel?
    fun provideAuditProfiles() : ArrayList<DocReportProfileQuestionsRulesAnswersModel>?
    fun getAuditModelFromViewModel(auditModel: AuditModel?)
    fun getAuditProfileModelsFromViewModel(profiles: ArrayList<DocReportProfileQuestionsRulesAnswersModel>?)

}