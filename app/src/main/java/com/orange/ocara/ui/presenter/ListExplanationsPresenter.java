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

import com.orange.ocara.business.interactor.LoadRuleExplanationsTask.LoadRuleExplanationsResponse;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.interactor.UseCaseHandler;
import com.orange.ocara.ui.contract.BrowseExplanationsContract;

import static com.orange.ocara.business.interactor.LoadRuleExplanationsTask.LoadRuleExplanationsRequest;

/** default implementation of {@link BrowseExplanationsContract.ExplanationDisplayUserActionsListener} */
public class ListExplanationsPresenter implements BrowseExplanationsContract.ExplanationDisplayUserActionsListener {

    private final UseCaseHandler useCaseHandler;

    private final UseCase<LoadRuleExplanationsRequest, LoadRuleExplanationsResponse> task;

    public ListExplanationsPresenter(UseCaseHandler useCaseHandler, UseCase<LoadRuleExplanationsRequest, LoadRuleExplanationsResponse> task) {
        this.useCaseHandler = useCaseHandler;
        this.task = task;
    }

    @Override
    public void loadExplanations(Long ruleId, UseCase.UseCaseCallback<LoadRuleExplanationsResponse> callback) {

        useCaseHandler.execute(task, new LoadRuleExplanationsRequest(ruleId), callback);
    }
}
