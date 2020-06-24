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
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.data.net.model.Ruleset;

/**
 * Contract between a view and an action listener that deal with Rulesets information presentation
 */
public interface RulesetInfoContract {

    /**
     * Behaviour of a view dedicated to display information about rulesets
     */
    interface RulesetInfoView {

        /**
         * display information about the ruleset
         *
         * @param ruleset item to display in the view
         */
        void showRuleset(RulesetModel ruleset);

        /**
         * display the rules related to a ruleset
         */
        void showRules();

        /**
         * helps on upgrading the version of a ruleset
         */
        void downloadRuleset();

        /**
         * displays a progression tool
         */
        void showProgressDialog();

        /**
         * hides a progression tool
         */
        void hideProgressDialog();

        /**
         * displays a message when an operation is successful
         */
        void showDownloadSucceeded();

        /**
         * displays a message when an operation has failed
         */
        void showDownloadFailed();

        /**
         * displays a message when the ruleset is not valid
         */
        void showInvalidRuleset();

        /**
         * activate access to the details of the ruleset
         */
        void enableRulesetDetails();

        /**
         * deactivate access to the details of the ruleset
         */
        void disableRulesetDetails();

        /**
         * activate the upgrade of a ruleset
         */
        void enableRulesetUpgrade();

        /**
         * deactivate the upgrade of a ruleset
         */
        void disableRulesetUpgrade();

        /**
         * displays extra information about the download of a ruleset
         */
        void displayDownloadRulesetLabel();

        /**
         * displays extra information about the detail of rules
         */
        void displayShowRulesLabel();
    }

    /**
     * Behaviour of an action listener dedicated to display information about rulesets
     */
    interface RulesetInfoUserActionsListener {

        /**
         * Changes the version of the given {@link Ruleset}
         *
         * @param ruleset the item to upgrade
         */
        void upgradeRuleset(VersionableModel ruleset);

        /**
         * Checks that the given {@link Ruleset} has been downloaded.
         *
         * @param ruleset the item to validate
         */
        void checkRulesetIsValid(VersionableModel ruleset);

        /**
         * Loads the content of a {@link Ruleset}
         *
         * @param ruleset attributes of the ruleset to retrieve
         */
        void loadRuleset(VersionableModel ruleset);
    }
}
