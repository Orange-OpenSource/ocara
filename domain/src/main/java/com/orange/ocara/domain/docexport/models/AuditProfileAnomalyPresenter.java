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

package com.orange.ocara.domain.docexport.models;

import com.orange.ocara.domain.models.DocReportProfileQuestionsRulesAnswersModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class AuditProfileAnomalyPresenter extends Presenter<DocReportProfileQuestionsRulesAnswersModel> {

    private final List<AuditProfileAnomalyQuestionPresenter> questionsProfileInAnomaly = new ArrayList<>();
    private final List<Integer> noQuestionsProfileInAnomaly = new ArrayList<>();

    AuditProfileAnomalyPresenter(DocReportProfileQuestionsRulesAnswersModel data) {
        super(data);

        buildAnomaliesOrDoubts();

    }


    public String getName() {
        return notNull(value.getProfileName());
    }

    public String getId() {
        return value.getProfileRef();
    }

    private void buildAnomaliesOrDoubts() {
        questionsProfileInAnomaly.clear();
        for (Map.Entry<String, ArrayList<DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName>> entry : value.getQuestionRulesMap().entrySet()) {
            questionsProfileInAnomaly.add(new AuditProfileAnomalyQuestionPresenter(entry.getValue(), entry.getKey(), entry.getValue().get(0).getQuestionName()));
        }

        if (questionsProfileInAnomaly.isEmpty()) {
            noQuestionsProfileInAnomaly.add(1);
        } else {
            noQuestionsProfileInAnomaly.clear();
        }

    }

    public List<Integer> getNoQuestionsProfileInAnomaly() {
        return noQuestionsProfileInAnomaly;
    }

    public List<AuditProfileAnomalyQuestionPresenter> getQuestionsInAnomalyOrDoubt() {
        return questionsProfileInAnomaly;
    }
}
