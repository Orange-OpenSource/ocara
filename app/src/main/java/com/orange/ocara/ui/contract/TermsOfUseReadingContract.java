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

import java.net.URL;

/**
 * Contract between the view and the presenter that manage the reading of the Terms-Of-Use
 */
public interface TermsOfUseReadingContract {

    /**
     * Behaviour of the view
     */
    interface TermsOfUseReadingView {

        /**
         * displays the terms-of-use
         */
        void showTerms(String rawData);

        /**
         * displays the terms-of-use
         */
        void showTerms(URL url);

        /**
         * displays information about errors during the terms acceptance process
         *
         * @param message a reason
         */
        void showError(String message);

        /**
         * Closes the terms
         */
        void hideTerms();
    }

    /**
     * Behaviour of the presenter
     */
    interface TermsOfUseReadingUserActionsListener {

        /**
         * the user accepts the terms
         */
        void loadTerms();
    }
}
