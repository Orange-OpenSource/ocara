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

/** a model for cached questionnaires (aka chapters) */
@Table(name = QuestionnaireEntity.TABLE_NAME, id = QuestionnaireEntity.ID)
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionnaireEntity extends Model implements Serializable {

    public static final String ID = "_id";
    public static final String REFERENCE = "reference";
    public static final String STATE = "state";
    public static final String USER_CREDENTIALS = "userCredentials";
    public static final String OBJECT_DESCRIPTION = "objectDescriptionRef";
    public static final String CHAINS = "chains";
    public static final String DATE = "date";
    public static final String TABLE_NAME = "questionnaire";
    public static final String RULSETDETAILS = "rulsetdetails";
    public static final String COMPLETE = "complete";
    private static final String PARENT_OBJECTS_REF = "parentObjectRefs";

    @SerializedName(REFERENCE)
    @Expose
    @Column(name = REFERENCE)
    public String reference;

    @SerializedName(STATE)
    @Expose
    @Column(name = STATE)
    public String state;

    @SerializedName(COMPLETE)
    @Expose
    @Column(name = COMPLETE)
    public Boolean complete;

    @SerializedName(OBJECT_DESCRIPTION)
    @Expose
    @Column(name = OBJECT_DESCRIPTION)
    public String objectDescriptionRef;

    @SerializedName(CHAINS)
    @Expose
    public List<ChainEntity> chains = null;

    @SerializedName(PARENT_OBJECTS_REF)
    @Expose
    @Column(name = PARENT_OBJECTS_REF)
    public List<String> parentObjectRefs = new ArrayList<>();

    @SerializedName(DATE)
    @Expose
    @Column(name = DATE)
    public String date;

    @Column(name = RULSETDETAILS)
    private RulesetEntity ruleSetDetail;

    public List<ChainEntity> getChaineDb() {
        return getMany(ChainEntity.class, ChainEntity.QUESTIONNAIRE);
    }

    public static QuestionnaireEntity toEntity(QuestionnaireWs input, RulesetEntity ruleset) {

        QuestionnaireEntity output = new QuestionnaireEntity();
        output.setDate(input.getDate());
        output.setComplete(input.getComplete());
        output.setObjectDescriptionRef(input.getObjectDescriptionRef());
        output.setParentObjectRefs(input.getParentObjectRefs());
        output.setState(input.getState());
        output.setReference(input.getReference());
        output.setRuleSetDetail(ruleset);

        return output;

    }
}
