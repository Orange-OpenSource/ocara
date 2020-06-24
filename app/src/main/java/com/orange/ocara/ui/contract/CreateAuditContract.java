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

import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.ui.model.AuditFormUiModel;

import java.util.List;

/**
 * Contract between the view and the presenter, that deal with the creation of audits
 */
public interface CreateAuditContract {

    /**
     * Behaviour of the view
     */
    interface CreateAuditView {

        void showRulesetList(List<RulesetModel> rulesets);

        void showDownloadError();

        void disableRulesInfo();

        void enableRulesInfo();

        void showProgressDialog();

        void hideProgressDialog();

        void showRulesInfoLabel();

        void showRulesetIcons();

        void hideRulesetIcons();

        void enableSaveButton();

        void disableSaveButton();

        void acceptTerms();
    }

    /**
     * Behaviour of the presenter
     */
    interface CreateAuditUserActionsListener {

        void validateAudit(AuditFormUiModel audit);

        void loadRulesets();

        void savePreferredRuleset(RulesetModel ruleset);
    }
}
