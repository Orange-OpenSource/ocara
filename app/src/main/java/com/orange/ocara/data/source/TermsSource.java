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

package com.orange.ocara.data.source;

/** contract between a data store that exposes terms-of-use  and repositories that handle them locally and remotely */
public interface TermsSource {

    /**
     * description of the repository that manage TOU locally
     */
    interface TermsCache {
        /**
         * Retrieves a {@link String}
         *
         * @return the text of terms
         */
        String find();

        /**
         * Registers the Terms-Of-Use as accepted by the user.
         */
        void markAsAccepted();

        /**
         * Registers the Terms-Of-Use as refused by the user.
         */
        void markAsRefused();

        /**
         * Checks if the Terms-Of-Use have already been checked by the user.
         *
         * @return true if the Terms-Of-Use were checked by the user. Returns false, when never displayed yet.
         */
        boolean checkTermsAreDefined();

        /**
         * Checks if the Terms-Of-Use have already been accepted by the user.
         *
         * @return true if the Terms-Of-Use were accepted by the user. Returns false, when
         * refused or not displayed yet.
         */
        boolean checkTermsAreAccepted();

    }

    /**
     * description of the repository that manage TOU through webservices
     */
    interface TermsRemote {
        /**
         * Retrieves a {@link String}
         *
         * @return the text of terms
         */
        String find();

    }

    /**
     * description of a repository that manages TOU between both local and remote repositories
     */
    interface TermsDataStore {

        /**
         * Retrieves a {@link String}
         *
         * @return the text of terms
         */
        String find();


        /**
         * Registers the Terms-Of-Use as accepted by the user.
         */
        void markAsAccepted();

        /**
         * Registers the Terms-Of-Use as refused by the user.
         */
        void markAsRefused();

        /**
         * Checks if the Terms-Of-Use have already been checked by the user.
         *
         * @return true if the Terms-Of-Use were checked by the user. Returns false, when never displayed yet.
         */
        boolean checkTermsAreDefined();

        /**
         * Checks if the Terms-Of-Use have already been accepted by the user.
         *
         * @return true if the Terms-Of-Use were accepted by the user. Returns false, when
         * refused or not displayed yet.
         */
        boolean checkTermsAreAccepted();

    }
}
