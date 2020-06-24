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

package com.orange.ocara.data.net.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** a model for cached RulesetCategories */
@Table(name = RulesetCategoryEntity.TABLE_NAME, id = EquipmentEntity.ID)
@Data
@EqualsAndHashCode(callSuper = false)
public class RulesetCategoryEntity extends Model implements Serializable {

    public static final String TABLE_NAME = "rules_category";
    public static final String NAME = "name";
    public static final String ACCEPTED_IMPACT_LIST = "acceptedImpactList";
    public static final String DEFAULT_IMPACT = "defaultImpact";
    public static final String PROFILTYPE = "profil_type";

    @SerializedName(NAME)
    @Expose
    @Column(name = NAME)
    public String name;

    @SerializedName(ACCEPTED_IMPACT_LIST)
    @Expose
    @Column(name = ACCEPTED_IMPACT_LIST)
    public List<String> acceptedImpactList = new ArrayList<>();

    @SerializedName(DEFAULT_IMPACT)
    @Expose
    @Column(name = DEFAULT_IMPACT)
    public String defaultImpactRef;

    @Column(name = PROFILTYPE)
    public ProfileTypeEntity profilType;

    static RulesetCategoryEntity toEntity(RulesetCategoryWs input) {

        RulesetCategoryEntity output = new RulesetCategoryEntity();

        output.setName(input.getName());
        output.setAcceptedImpactList(input.getAcceptedImpactList());
        output.setDefaultImpactRef(input.getDefaultImpactRef());

        return output;

    }
}
