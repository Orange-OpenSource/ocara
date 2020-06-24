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

package com.orange.ocara.business.repository;

/**
 * a repository for querying terms of use
 */
public interface TermsRepository {

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