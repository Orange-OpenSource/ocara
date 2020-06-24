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

import com.orange.ocara.business.interactor.LoadRuleExplanationsTask.LoadRuleExplanationsResponse;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.model.ExplanationModel;

import java.util.List;

/** Contract between a view and a presenter that deal with the browsing of the explanations / illustrations of a rule */
public interface BrowseExplanationsContract {

    interface ExplanationDisplayView {

        void displayExplanations(List<ExplanationModel> data);

        void displayNothing();

        boolean reachedMaxLeft();

        boolean reachedMaxRight();

        void showPreviousElement();

        void showNextElement();

        void showElement(int index);
    }

    interface ExplanationDisplayUserActionsListener {

        void loadExplanations(Long ruleId, UseCase.UseCaseCallback<LoadRuleExplanationsResponse> callback);

    }
}
