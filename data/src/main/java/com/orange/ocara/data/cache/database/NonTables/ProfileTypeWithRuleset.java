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

import com.orange.ocara.data.cache.database.Tables.ProfileType;
import com.orange.ocara.data.cache.database.Tables.RulesetDetails;

public class ProfileTypeWithRuleset {
    @Embedded
    private ProfileType profileType;
    @Embedded
    private RulesetDetails rulesetDetails;

    public ProfileType getProfileType() {
        return profileType;
    }

    public void setProfileType(ProfileType profileType) {
        this.profileType = profileType;
    }

    public RulesetDetails getRulesetDetails() {
        return rulesetDetails;
    }

    public void setRulesetDetails(RulesetDetails rulesetDetailst) {
        this.rulesetDetails = rulesetDetailst;
    }
}
