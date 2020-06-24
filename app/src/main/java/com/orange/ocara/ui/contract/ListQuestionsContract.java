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

package com.orange.ocara.ui.contract;

import com.orange.ocara.data.net.model.EquipmentWithReference;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.RulesetEntity;

import java.util.List;

/**
 * Contract between the view and the presenter, that deal with the listing of questions
 */
public interface ListQuestionsContract {

    interface ListQuestionsView {
        void showQuestions(List<QuestionEntity> questions);

        void showNoQuestions();
    }

    interface ListQuestionsUserActionsListener {

        void loadAllQuestionsByEquipmentId(RulesetEntity ruleset, EquipmentWithReference equipment);
    }
}
