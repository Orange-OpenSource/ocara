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

import com.orange.ocara.business.interactor.LoadRulesTask;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.interactor.UseCaseHandler;
import com.orange.ocara.ui.contract.BrowseRulesContract;

/**
 * default implementation of {@link BrowseRulesContract.BrowseRulesUserActionsListener}
 */
public class BrowseRulesPresenter implements BrowseRulesContract.BrowseRulesUserActionsListener {

    private final UseCaseHandler useCaseHandler;

    private final UseCase<LoadRulesTask.LoadRulesRequest, LoadRulesTask.LoadRulesResponse> task;

    public BrowseRulesPresenter(UseCaseHandler useCaseHandler, UseCase<LoadRulesTask.LoadRulesRequest, LoadRulesTask.LoadRulesResponse> task) {
        this.useCaseHandler = useCaseHandler;
        this.task = task;
    }

    @Override
    public void loadAllRulesByEquipmentId(long rulesetId, long equipmentId, UseCase.UseCaseCallback<LoadRulesTask.LoadRulesResponse> callback) {

        useCaseHandler.execute(task, new LoadRulesTask.LoadRulesRequest(rulesetId, equipmentId), callback);
    }
}
