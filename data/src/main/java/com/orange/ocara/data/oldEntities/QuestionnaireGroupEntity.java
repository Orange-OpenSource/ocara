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
import com.orange.ocara.data.network.models.QuestionnaireGroupWs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** a model for cached groups of questionnaires */
@Table(name = QuestionnaireGroupEntity.TABLE_NAME, id = QuestionnaireGroupEntity.ID)
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionnaireGroupEntity extends Model implements Serializable {

    public static final String TABLE_NAME = "questionnaire_group";
    public static final String NAME = "name";
    public static final String OBJECT_REF = "objectRef";
    public static final String ID = "_id";
    public static final String RULESSET_DETAILS = "details";
    @SerializedName(NAME)
    @Expose
    @Column(name = NAME)
    public String name;
    @SerializedName(OBJECT_REF)
    @Expose
    @Column(name = OBJECT_REF)
    public List<String> objectRef = new ArrayList<>();
    @Column(name = RULESSET_DETAILS)
    private RulesetEntity ruleSetDetail;

    public QuestionnaireGroupEntity() {
    }

    public static QuestionnaireGroupEntity toEntity(QuestionnaireGroupWs input, RulesetEntity ruleset) {

        QuestionnaireGroupEntity output = new QuestionnaireGroupEntity();
        output.setName(input.getName());
        output.setObjectRef(input.getObjectRef());
        output.setRuleSetDetail(ruleset);

        return output;
    }

    public RulesetEntity getRuleSetDetail() {
        return ruleSetDetail;
    }
}
