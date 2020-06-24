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

import androidx.annotation.NonNull;

import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.business.service.impl.RuleSetServiceImpl;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.ui.contract.ListRulesetsContract;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.List;

import lombok.Setter;

/**
 * Implementation of {@link ListRulesetsContract.ListRulesUserActionsListener}
 *
 * Mediator between a {@link ListRulesetsContract.ListRulesView} and the model
 *
 * Should be kept as a @EBean, as long as we do not remove @Background and @UiThread
 */
@EBean
public class ListRulesPresenter implements ListRulesetsContract.ListRulesUserActionsListener {

    @Setter
    @Bean(RuleSetServiceImpl.class)
    RuleSetService mRuleSetService;

    private ListRulesetsContract.ListRulesView mListRulesView;

    /**
     * setter
     *
     * @param listRulesView a view
     */
    public void setView(@NonNull ListRulesetsContract.ListRulesView listRulesView) {
        mListRulesView = listRulesView;
    }

    @Background
    public void loadRulesets() {
        List<RulesetEntity> rulesets = mRuleSetService.getDownloadedRulesetDetails();
        displayRulesets(rulesets);
    }

    @UiThread
    void displayRulesets(List<RulesetEntity> rulesets) {
        mListRulesView.showRulesets(rulesets);
    }
}
