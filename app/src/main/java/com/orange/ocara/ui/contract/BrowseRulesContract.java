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

import com.orange.ocara.business.interactor.LoadRulesTask;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.model.RuleGroupModel;
import com.orange.ocara.business.model.RuleModel;

import java.util.List;

public interface BrowseRulesContract {

    interface BrowseRulesView {

        void showRules(List<RuleGroupModel> groups, List<RuleModel> rules);

        void showNoRules();
    }

    interface BrowseRulesUserActionsListener {

        void loadAllRulesByEquipmentId(long rulesetId, long equipmentId, UseCase.UseCaseCallback<LoadRulesTask.LoadRulesResponse> callback);
    }
}
