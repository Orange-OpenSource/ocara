/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.service.QuestionService;
import com.orange.ocara.data.net.model.EquipmentWithReference;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.ui.contract.ListQuestionsContract;

import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link ListQuestionsContract.ListQuestionsUserActionsListener}
 *
 * Presenter related to a view that implements {@link ListQuestionsContract.ListQuestionsView}
 */
@RequiredArgsConstructor
public class ListQuestionsPresenter implements ListQuestionsContract.ListQuestionsUserActionsListener {

    // TODO try to change this service for a repository
    private final QuestionService questionService;

    private final ListQuestionsContract.ListQuestionsView view;

    @Override
    public void loadAllQuestionsByEquipmentId(RulesetEntity ruleset, EquipmentWithReference equipment) {
        List<QuestionEntity> questions = questionService.retrieveAllQuestionsByRulesetAndEquipment(ruleset, equipment);
        if (questions.isEmpty()) {
            view.showNoQuestions();
        } else {
            view.showQuestions(questions);
        }
    }
}
