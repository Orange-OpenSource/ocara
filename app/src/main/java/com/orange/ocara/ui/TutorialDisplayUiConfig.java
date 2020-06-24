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

package com.orange.ocara.ui;

import com.orange.ocara.business.BizConfig;
import com.orange.ocara.ui.presenter.TutorialDisplayPresenter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.orange.ocara.ui.contract.TutorialDisplayContract.TutorialDisplayUserActionsListener;

/** Configuration for the UI layer */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EBean(scope = EBean.Scope.Singleton)
public class TutorialDisplayUiConfig {

    @Bean(BizConfig.class)
    BizConfig bizConfig;

    public TutorialDisplayUserActionsListener actionsListener() {

        return new TutorialDisplayPresenter(
                BizConfig.USE_CASE_HANDLER,
                bizConfig.completeOnboardingTask(),
                bizConfig.loadOnboardingItemsTask(),
                bizConfig.skipOnboardingTask(),
                bizConfig.changeOnboardingItemTask());
    }
}
