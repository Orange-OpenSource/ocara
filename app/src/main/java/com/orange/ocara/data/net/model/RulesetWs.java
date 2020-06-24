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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/** a model for remote rulesets */
public class RulesetWs implements Serializable {

    private static final String REFERENCE = "reference";
    private static final String VERSION = "version";
    private static final String TYPE = "type";
    private static final String COMMENT = "comment";
    private static final String LANGUAGE = "language";
    private static final String USER_CREDENTIALS = "userCredentials";
    private static final String QUESTIONNAIRES = "questionnaires";
    private static final String DATE = "date";
    private static final String ILLUSTRATIONS = "illustrations";
    private static final String RULES = "rules";
    private static final String QUESTIONS = "questions";
    private static final String OBJECT_DESCRIPTIONS = "objectDescriptions";
    private static final String QUESTIONNAIRE_GROUPS = "questionnaireGroups";
    private static final String IMPACT_VALUES = "impactValues";
    private static final String PROFILE_TYPES = "profileTypes";

    @SerializedName(VERSION)
    @Expose
    private Integer version;

    @SerializedName(LANGUAGE)
    @Expose
    private String language;

    @SerializedName(REFERENCE)
    @Expose
    private String reference;

    @SerializedName(COMMENT)
    @Expose
    private String comment;

    @SerializedName(TYPE)
    @Expose
    private String type;

    @SerializedName(USER_CREDENTIALS)
    @Expose
    private AuthorWs userCredentials;

    @SerializedName(OBJECT_DESCRIPTIONS)
    @Expose
    private List<EquipmentWs> objectDescriptions = null;

    @SerializedName(QUESTIONNAIRE_GROUPS)
    @Expose
    private List<QuestionnaireGroupWs> questionnaireGroups = null;

    @SerializedName(QUESTIONS)
    @Expose
    private List<QuestionWs> questions = null;

    @SerializedName(QUESTIONNAIRES)
    @Expose
    private List<QuestionnaireWs> questionnaires = null;

    @SerializedName(RULES)
    @Expose
    private List<RuleWs> rules = null;

    @SerializedName(ILLUSTRATIONS)
    @Expose
    private List<IllustrationWs> illustrations = null;

    @SerializedName(IMPACT_VALUES)
    @Expose
    private List<ImpactValueWs> impactValues = null;

    @SerializedName(PROFILE_TYPES)
    @Expose
    private List<ProfileTypeWs> profileTypes = null;

    @SerializedName(DATE)
    @Expose
    private String date;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AuthorWs getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(AuthorWs userCredentials) {
        this.userCredentials = userCredentials;
    }

    public List<EquipmentWs> getEquipments() {
        return objectDescriptions != null ? objectDescriptions : Collections.emptyList();
    }

    public void setEquipments(List<EquipmentWs> objectDescriptions) {
        this.objectDescriptions = objectDescriptions;
    }

    public List<QuestionnaireGroupWs> getQuestionnaireGroups() {
        return questionnaireGroups;
    }

    public void setQuestionnaireGroups(List<QuestionnaireGroupWs> questionnaireGroups) {
        this.questionnaireGroups = questionnaireGroups;
    }

    public List<QuestionWs> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionWs> questions) {
        this.questions = questions;
    }

    public List<QuestionnaireWs> getQuestionnaires() {
        return questionnaires;
    }

    public void setQuestionnaires(List<QuestionnaireWs> questionnaires) {
        this.questionnaires = questionnaires;
    }

    public List<RuleWs> getRules() {
        return rules;
    }

    public void setRules(List<RuleWs> rules) {
        this.rules = rules;
    }

    public List<IllustrationWs> getIllustrations() {
        return illustrations != null ? illustrations : Collections.emptyList();
    }

    public void setIllustrations(List<IllustrationWs> illustrations) {
        this.illustrations = illustrations;
    }

    public List<ImpactValueWs> getImpactValues() {
        return impactValues;
    }

    public void setImpactValues(List<ImpactValueWs> impactValues) {
        this.impactValues = impactValues;
    }

    public List<ProfileTypeWs> getProfileTypes() {
        return profileTypes != null ? profileTypes : Collections.emptyList();
    }

    public void setProfileTypes(List<ProfileTypeWs> profileTypes) {
        this.profileTypes = profileTypes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
