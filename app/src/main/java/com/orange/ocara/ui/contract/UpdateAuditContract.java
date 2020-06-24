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

import com.orange.ocara.business.model.AuditModel;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.AuditorEntity;
import com.orange.ocara.data.cache.model.SiteEntity;
import com.orange.ocara.data.net.model.Ruleset;
import com.orange.ocara.ui.model.AuditFormUiModel;

/**
 * Behaviour of a view and a presenter, that follow the MVP pattern.
 *
 * Contract between the view and the presenter, that deal with the update of audits
 */
public interface UpdateAuditContract {

    /**
     * Behaviour of the view
     */
    interface UpdateAuditView {

        /**
         * displays the audit
         *
         * @param audit an {@link AuditEntity}
         */
        void showAudit(AuditEntity audit);

        /**
         * displays the site
         * @param site a {@link SiteEntity}
         */
        void showSite(SiteEntity site);

        /**
         * displays the use
         * @param auditor an {@link AuditorEntity}
         */
        void showAuthor(AuditorEntity auditor);

        /**
         * displays the level
         * @param level an {@link AuditEntity.Level}
         */
        void showLevel(AuditEntity.Level level);

        /**
         * displays the related ruleset
         * @param ruleset a {@link Ruleset}
         */
        void showRuleset(RulesetModel ruleset);

        /**
         * displays a loading information
         */
        void showProgressDialog();

        /**
         * hides a loading information
         */
        void hideProgressDialog();

        /**
         * displays some information
         */
        void showRulesInfoLabel();

        /**
         * displays some icons
         */
        void showRulesetIcons();

        /**
         * hides some information
         */
        void hideRulesetIcons();

        /**
         * activates a button for saving data
         */
        void enableSaveButton();

        /**
         * deactivates a button to avoid saving data
         */
        void disableSaveButton();

        /**
         * displays a message
         */
        void showValidationErrors();

        /**
         * displays a button for saving data
         */
        void showSaveAuditButton();

        /**
         * displays a button for continuing the process
         */
        void showContinueAuditButton();

        /**
         * displays a message
         */
        void showAuditUpdatedSuccessfully();
    }

    /**
     * Behaviour of the presenter
     */
    interface UpdateAuditUserActionsListener {

        /**
         * checks that the audit is valid
         * @param audit an {@link AuditEntity}
         */
        void validateAudit(AuditFormUiModel audit);

        /**
         * Saves the audit
         * @param auditId an identifier
         * @param newInfo the new content
         */
        void updateAudit(Long auditId, AuditModel newInfo);

        /**
         * load some data
         * @param audit an {@link AuditEntity}
         */
        void loadSite(AuditEntity audit);

        /**
         * load some data
         * @param audit an {@link AuditEntity}
         */
        void loadRuleset(AuditEntity audit);

        /**
         * load some data
         * @param audit an {@link AuditEntity}
         */
        void loadAuthor(AuditEntity audit);

        /**
         * load some data
         * @param audit an {@link AuditEntity}
         */
        void loadLevel(AuditEntity audit);

        /**
         * load some data
         * @param id an identifier
         */
        void loadAudit(Long id);
    }
}
