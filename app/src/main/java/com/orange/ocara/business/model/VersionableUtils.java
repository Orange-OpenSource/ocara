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

package com.orange.ocara.business.model;

import com.orange.ocara.data.net.model.Ruleset;

/** Toolbox for {@link VersionableModel}s */
public class VersionableUtils {

    private VersionableUtils() {
    }

    /**
     * Compares the current {@link RulesetModel} with another one.
     *
     * @param initial the {@link RulesetModel} to compare with.
     * @return true if the current {@link RulesetModel} has same reference, and an newer version than the given one.
     */
    public static boolean isNewerThan(VersionableModel actual, VersionableModel initial) {

        boolean result = false;
        boolean isValid = initial != null && initial.getReference() != null && initial.getVersion() != null;
        if (isValid) {
            boolean hasSameReference = initial.getReference().compareTo(actual.getReference()) == 0;
            boolean hasNewerVersion = actual.getVersion().compareTo(initial.getVersion()) == 1;
            result = hasSameReference && hasNewerVersion;
        }
        return result;
    }

    /**
     * Compares the current {@link RulesetModel} with another one.
     *
     * @param compared the {@link RulesetModel} to compare with.
     * @return true if the current {@link RulesetModel} has same reference and same version than the compared one.
     */
    public static boolean isSameAs(VersionableModel actual, VersionableModel compared) {
        boolean result = false;
        boolean isValid = compared != null && compared.getReference() != null && compared.getVersion() != null;
        if (isValid) {
            boolean hasSameReference = compared.getReference().compareTo(actual.getReference()) == 0;
            boolean hasSameVersion = actual.getVersion().compareTo(compared.getVersion()) == 0;
            result = hasSameReference && hasSameVersion;
        }

        return result;
    }

    /**
     * Compares the current {@link RulesetModel} with another one.
     *
     * @param ruleset the {@link RulesetModel} to compare with.
     * @return true if the current {@link RulesetModel} has same reference, and an older version than the given one.
     */
    public static boolean isOlderThan(VersionableModel actual, Ruleset ruleset) {

        boolean result = false;
        boolean isValid = ruleset != null && ruleset.getReference() != null && ruleset.getVersion() != null;
        if (isValid) {
            boolean hasSameReference = ruleset.getReference().compareTo(actual.getReference()) == 0;
            boolean hasOlderVersion = actual.getVersion().compareTo(ruleset.getVersion()) == -1;
            result = hasSameReference && hasOlderVersion;
        }
        return result;
    }
}
