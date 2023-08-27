package com.orange.ocara.data.cache.database.NonTables;
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
import androidx.room.Embedded;

import com.orange.ocara.data.cache.database.Tables.ImpactValue;
import com.orange.ocara.data.cache.database.Tables.RulesetDetails;


public class ImpactValueWithRuleset {
    @Embedded
    private ImpactValue impactValue;
    @Embedded
    private RulesetDetails rulesetDetails;


    public ImpactValue getImpactValue() {
        return impactValue;
    }

    public void setImpactValue(ImpactValue impactValue) {
        this.impactValue = impactValue;
    }

    public RulesetDetails getRulesetDetails() {
        return rulesetDetails;
    }

    public void setRulesetDetails(RulesetDetails rulesetDetailst) {
        this.rulesetDetails = rulesetDetailst;
    }
}
