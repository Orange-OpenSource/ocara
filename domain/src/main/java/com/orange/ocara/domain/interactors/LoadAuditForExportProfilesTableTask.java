/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */


package com.orange.ocara.domain.interactors;

import com.orange.ocara.domain.models.AuditEquipmentModel;
import com.orange.ocara.domain.models.AuditModel;
import com.orange.ocara.domain.models.DocReportProfileQuestionsRulesAnswersModel;
import com.orange.ocara.domain.repositories.AuditDocReportRepository;
import com.orange.ocara.domain.repositories.AuditRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class LoadAuditForExportProfilesTableTask {

    AuditDocReportRepository repository;

    @Inject
    public LoadAuditForExportProfilesTableTask(AuditDocReportRepository repository) {
        this.repository = repository;
    }

    public Single<ArrayList<DocReportProfileQuestionsRulesAnswersModel>> execute(Long auditId, String ruleSetRef, int ruleSetVer) {

        return repository.getProfilesQuestionsRulesAnswers(auditId, ruleSetRef, ruleSetVer);
    }

}
