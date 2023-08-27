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
import com.orange.ocara.data.network.models.QuestionWs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** a model for cached questions (aka groups of rules) */
@Table(name = QuestionEntity.TABLE_NAME, id = QuestionEntity.ID)
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionEntity extends Model implements Serializable {

    public static final String TABLE_NAME = "question";
    public static final String ID = "_id";
    public static final String REFERENCE = "reference";
    public static final String LABEL = "label";
    public static final String STATE = "state";
    public static final String ORIGIN = "origin";
    public static final String USER_CREDENTIALS = "userCredentials";
    public static final String SUBJECT = "subject";
    public static final String RULES = "rulesRef";
    public static final String DATE = "date";
    public static final String CHAIN = "chain";
    public static final String RULESET_DETAILS = "details";

    @SerializedName(REFERENCE)
    @Expose
    @Column(name = REFERENCE)
    public String reference;

    @SerializedName(LABEL)
    @Expose
    @Column(name = LABEL)
    public String label;

    @SerializedName(STATE)
    @Expose
    @Column(name = STATE)
    public String state;

    @SerializedName(SUBJECT)
    @Expose
    @Column(name = SUBJECT)
    public SubjectEntity subject;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public SubjectEntity getSubject() {
        return subject;
    }

    public void setSubject(SubjectEntity subject) {
        this.subject = subject;
    }

    public List<String> getRulesRef() {
        return rulesRef;
    }

    public void setRulesRef(List<String> rulesRef) {
        this.rulesRef = rulesRef;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RulesetEntity getRuleSetDetail() {
        return ruleSetDetail;
    }

    public void setRuleSetDetail(RulesetEntity ruleSetDetail) {
        this.ruleSetDetail = ruleSetDetail;
    }

    @SerializedName(RULES)
    @Expose
    @Column(name = RULES)
    public List<String> rulesRef = new ArrayList<>();

    @SerializedName(DATE)
    @Expose
    @Column(name = DATE)
    public String date;

    @Column(name = RULESET_DETAILS)
    private RulesetEntity ruleSetDetail;

    public static QuestionEntity toEntity(QuestionWs input, RulesetEntity ruleset) {

        QuestionEntity output = new QuestionEntity();

        output.setDate(input.getCreationDate());
        output.setLabel(input.getLabel());
        output.setReference(input.getReference());
        output.setRulesRef(input.getRulesReferences());
        output.setState(input.getState());
        output.setSubject(SubjectEntity.toEntity(input.getSubject()));
        output.setRuleSetDetail(ruleset);

        return output;
    }
}
