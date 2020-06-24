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

import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.db.ModelManagerImpl;
import com.orange.ocara.ui.presenter.ListAuditCommentsPresenter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.orange.ocara.ui.contract.ListAuditCommentsContract.ListAuditCommentView;
import static com.orange.ocara.ui.contract.ListAuditCommentsContract.ListAuditCommentsUserActionsListener;

/**
 * Configuration for the UI layer of the module that lists the comments about an audit
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EBean(scope = EBean.Scope.Singleton)
public class ListAuditCommentsUiConfig {

    @Bean(ModelManagerImpl.class)
    ModelManager modelManager;

    public ListAuditCommentsUserActionsListener actionsListener(ListAuditCommentView view) {

        return new ListAuditCommentsPresenter(view, modelManager);
    }
}
