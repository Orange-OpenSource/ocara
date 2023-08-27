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
import com.orange.ocara.data.network.models.ImpactValueWs;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** a model for cached impact values */
@Table(name = ImpactValueEntity.TABLE_NAME, id = ImpactValueEntity.ID)
@Data
@EqualsAndHashCode(callSuper = false)
public class ImpactValueEntity extends Model implements Serializable {

    public static final String TABLE_NAME = "impact_value";
    public static final String NAME = "name";
    public static final String EDITABLE = "editable";
    public static final String REFERENCE = "reference";
    public static final String ID = "_id";
    public static final String RULE_DETAIL = "details";

    @SerializedName(REFERENCE)
    @Expose
    @Column(name = REFERENCE)
    public String reference;

    @SerializedName(NAME)
    @Expose
    @Column(name = NAME)
    public String name;
    @SerializedName(EDITABLE)
    @Expose
    @Column(name = EDITABLE)
    public Boolean editable;

    @Column(name = RULE_DETAIL)
    public RulesetEntity details;

    public static ImpactValueEntity toEntity(ImpactValueWs input, RulesetEntity ruleset) {

        ImpactValueEntity output = new ImpactValueEntity();

        output.setEditable(input.getEditable());
        output.setName(input.getName());
        output.setReference(input.getReference());
        output.setDetails(ruleset);

        return output;
    }
}
