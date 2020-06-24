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
import com.orange.ocara.business.service.EquipmentService;
import com.orange.ocara.business.service.impl.EquipmentServiceImpl;
import com.orange.ocara.ui.contract.BrowseRulesContract;
import com.orange.ocara.ui.contract.ListEquipmentsContract;
import com.orange.ocara.ui.presenter.BrowseRulesPresenter;
import com.orange.ocara.ui.presenter.ListEquipmentsPresenter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Configuration for the UI layer of the rules listing module
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EBean(scope = EBean.Scope.Singleton)
public class BrowseRulesUiConfig {

    @Bean(BizConfig.class)
    BizConfig bizConfig;

    @Bean(EquipmentServiceImpl.class)
    EquipmentService equipmentService;

    /**
     * Retrieves an Action Listener
     *
     * @return an implementation of {@link BrowseRulesContract.BrowseRulesUserActionsListener}
     */
    public BrowseRulesContract.BrowseRulesUserActionsListener browseRulesActionsListener() {

        return new BrowseRulesPresenter(BizConfig.USE_CASE_HANDLER, bizConfig.browseRulesTask());
    }

    public ListEquipmentsContract.ListEquipmentsUserActionsListener listEquipmentsActionsListener(ListEquipmentsContract.ListEquipmentsView view) {
        return new ListEquipmentsPresenter(equipmentService, view);
    }
}
