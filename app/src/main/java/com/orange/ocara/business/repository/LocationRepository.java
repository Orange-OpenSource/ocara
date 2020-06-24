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
 * Behaviour of a repository dedicated to the retrieval of rulesets
 */
public interface LocationRepository extends InitializableRepository {

    /**
     * check if a location exists
     *
     * @param noImmo an identifier for locations
     * @return true if the repository contains at least one location that matches the argument
     */
    boolean exists(String noImmo);
}
