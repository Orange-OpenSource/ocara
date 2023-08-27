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

package com.orange.ocara.data.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** a model for remote groups of questionnaires */
public class QuestionnaireGroupWs implements Serializable {

    private static final String NAME = "name";
    private static final String OBJECT_REF = "objectRef";

    @SerializedName(NAME)
    @Expose
    private String name;

    @SerializedName(OBJECT_REF)
    @Expose
    private List<String> objectRef = new ArrayList<>();

    public QuestionnaireGroupWs() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getObjectRef() {
        return objectRef;
    }

    public void setObjectRef(List<String> objectRef) {
        this.objectRef = objectRef;
    }
}
