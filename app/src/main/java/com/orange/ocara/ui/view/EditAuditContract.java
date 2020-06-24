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

package com.orange.ocara.ui.view;

import com.orange.ocara.data.cache.model.AuditEntity;

/**
 * Contract between the view and the presenter, that deal with the creation of audits
 */
public interface EditAuditContract {

    /**
     * Behaviour of the view
     */
    interface View {

        void showRulesets();

        void showRulesetInfo();

        void showRulesList();

        void showRulesLabel();

        void showRulesInfoLabel();

        void showRulesetIcons();

        void enableSaveButton();

        void disableSaveButton();
    }

    /**
     * Behaviour of the presenter
     */
    interface UserActionsListener {

        void saveAudit(AuditEntity audit);
    }
}
