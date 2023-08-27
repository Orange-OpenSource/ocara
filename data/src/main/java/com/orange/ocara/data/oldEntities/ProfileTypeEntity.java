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

package com.orange.ocara.data.oldEntities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orange.ocara.data.network.models.ProfileTypeWs;
import com.orange.ocara.data.network.models.RulesetCategoryWs;
import com.orange.ocara.data.network.models.WithIcon;
import com.orange.ocara.utils.ListUtils;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** a model for cached profile types */
@Table(name = ProfileTypeEntity.TABLE_NAME, id = ProfileTypeEntity.ID)
@Data
@EqualsAndHashCode(callSuper = false)
public class ProfileTypeEntity extends Model implements Serializable, WithIcon {

    public static final String TABLE_NAME = "profil_type";
    public static final String NAME = "name";
    public static final String REFERENCE = "reference";
    public static final String ICON = "icon";
    public static final String RULES_CATEGORY = "rulesCategories";
    public static final String RULE_IMPACT = "rule_impact";
    public static final String ID = "_id";
    public static final String DETAILS = "details";

    @SerializedName(NAME)
    @Expose
    @Column(name = NAME)
    public String name;

    @SerializedName(REFERENCE)
    @Expose
    @Column(name = REFERENCE)
    public String reference;

    @SerializedName(ICON)
    @Expose
    @Column(name = ICON)
    public String icon;

    @SerializedName(RULES_CATEGORY)
    @Expose
    public List<RulesetCategoryEntity> rulesCategories;

    @Column(name = DETAILS)
    public RulesetEntity mRulsetDetails;

    @Override
    public boolean hasIcon() {
        return getIcon() != null && !getIcon().isEmpty();
    }

    public static ProfileTypeEntity toEntity(ProfileTypeWs input, RulesetEntity ruleset) {

        ProfileTypeEntity output = new ProfileTypeEntity();

        output.setIcon(input.getIcon());
        output.setName(input.getName());
        output.setReference(input.getReference());
        output.mRulsetDetails = ruleset;

        List<RulesetCategoryWs> inputRulesCategories = input.getRulesCategories();
        List<RulesetCategoryEntity> outputRulesCategories = ListUtils.newArrayList();
        for (RulesetCategoryWs item : inputRulesCategories) {
            outputRulesCategories.add(RulesetCategoryEntity.toEntity(item));
        }
        output.setRulesCategories(outputRulesCategories);

        return output;
    }
}
