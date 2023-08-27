/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.domain.tools;

import com.orange.ocara.domain.models.RulesetModel;
import com.orange.ocara.data.oldEntities.Ruleset;
import com.orange.ocara.data.oldEntities.RulesetByReferenceAndVersion;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Tri suivant :
 * - jeux présents, dernier utilisé en haut et présélectionné,
 * - puis les autres jeux présents par ordre alphabétique
 * - pour chacun de ces jeux, s'il existe une nouvelle version téléchargeable, l'afficher juste après la version présente
 * - jeux non présents, téléchargeables - par ordre alphabétique
 */
public class RulesetComparator implements Comparator<RulesetModel>, Serializable {

    private static final long serialVersionUID = 1;
    private final String defaultVersionName;

    /**
     * instantiate with a default {@link Ruleset}
     *
     * @param reference the default value, to compare with
     */
    public RulesetComparator(RulesetModel reference) {
        defaultVersionName = reference.getVersionName();
    }

    /**
     * default constructor, with no default value
     */
    public RulesetComparator() {
        this.defaultVersionName = "DEMO_1";
    }

    /**
     * Compares two {@link RulesetByReferenceAndVersion}s
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second.
     */
    @Override
    public int compare(RulesetModel o1, RulesetModel o2) {

        if (defaultVersionName.equals(o1.getVersionName())) {
            return -1;
        }

        if (defaultVersionName.equals(o2.getVersionName())) {
            return 1;
        }

        if (o1.isLocallyAvailable() && o2.isRemotelyAvailable()) {
            return -1;
        } else if (o1.isRemotelyAvailable() && o2.isLocallyAvailable()) {
            return 1;
        } else {
            return o1.getType().compareTo(o2.getType());
        }
    }
}
