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
import java.util.ArrayList;
import java.util.List;

/** a model for remote equipments (aka objectDescriptions) */
public class EquipmentWs implements Serializable, WithIcon {

    private static final String REFERENCE = "reference";
    private static final String NAME = "name";
    private static final String ICON = "icon";
    private static final String DEFINITION = "definition";
    private static final String TYPE = "type";
    private static final String ILLUSTRATION = "illustrationRef";
    private static final String USER_CREDENTIALS = "userCredentials";
    private static final String CATEGORY = "objectDescriptions";
    private static final String SUB_OBJECTS = "subObject";
    private static final String DATE = "date";
    private static final String QO_REF = "questionnaireRef";

    @SerializedName(REFERENCE)
    @Expose
    private String reference;

    @SerializedName(NAME)
    @Expose
    private String name;

    @SerializedName(ICON)
    @Expose
    private String icon;

    @SerializedName(DEFINITION)
    @Expose
    private String definition;

    @SerializedName(TYPE)
    @Expose
    private String type;

    @SerializedName(ILLUSTRATION)
    @Expose
    private List<String> illustration = new ArrayList<>();

    @SerializedName(USER_CREDENTIALS)
    @Expose
    private AuthorWs userCredentials;

    @SerializedName(CATEGORY)
    @Expose
    private EquipmentCategoryWs category;

    @SerializedName(SUB_OBJECTS)
    @Expose
    private List<String> subObject = new ArrayList<>();


    @SerializedName(QO_REF)
    @Expose
    private String questionaireRef;

    @SerializedName(DATE)
    @Expose
    private String date;


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public boolean hasIcon() {
        return getIcon() != null && !getIcon().isEmpty();
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getIllustration() {
        return illustration;
    }

    public void setIllustration(List<String> illustration) {
        this.illustration = illustration;
    }

    public AuthorWs getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(AuthorWs userCredentials) {
        this.userCredentials = userCredentials;
    }

    public EquipmentCategoryWs getCategory() {
        return category;
    }

    public void setCategory(EquipmentCategoryWs category) {
        this.category = category;
    }

    public List<String> getSubObject() {
        return subObject;
    }

    public void setSubObject(List<String> subObject) {
        this.subObject = subObject;
    }

    public String getQuestionaireRef() {
        return questionaireRef;
    }

    public void setQuestionaireRef(String questionaireRef) {
        this.questionaireRef = questionaireRef;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
