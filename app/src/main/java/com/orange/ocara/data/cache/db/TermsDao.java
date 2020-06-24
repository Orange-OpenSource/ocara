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

package com.orange.ocara.data.cache.db;

/**
 * Behaviour of a service that retrieves the app's Terms-Of-Use locally
 */
public interface TermsDao {

    /**
     * retrieves the content of terms
     *
     * @return some text
     */
    String get();
}
