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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** a model for cached equipments (aka objectDescriptions) */
@Table(name = EquipmentEntity.TABLE_NAME, id = EquipmentEntity.ID)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class EquipmentEntity extends Model implements Serializable, Equipment {

    public static final String TABLE_NAME = "objectdescription";
    public static final String ID = "_id";
    public static final String REFERENCE = "reference";
    public static final String NAME = "name";
    public static final String ICON = "icon";
    public static final String DEFINITION = "definition";
    public static final String TYPE = "type";
    public static final String ILLUSTRATION = "illustrationRef";
    public static final String USER_CREDENTIALS = "userCredentials";
    public static final String CATEGORY = "categories";
    public static final String SUB_OBJECTS = "subObject";
    public static final String DATE = "date";
    public static final String ID_ONLINE = "id_online";
    public static final String RULESSET_DETAILS = "details";
    private static final String QO_REF = "questionnaireRef";

    @SerializedName(REFERENCE)
    @Expose
    @Column(name = REFERENCE)
    public String reference;

    @SerializedName(NAME)
    @Expose
    @Column(name = NAME)
    public String name;

    @SerializedName(ICON)
    @Expose
    @Column(name = ICON)
    public String icon;

    @SerializedName(DEFINITION)
    @Expose
    @Column(name = DEFINITION)
    public String definition;

    @SerializedName(TYPE)
    @Expose
    @Column(name = TYPE)
    public String type;

    @SerializedName(ILLUSTRATION)
    @Expose
    @Column(name = ILLUSTRATION)
    public List<String> illustration = new ArrayList<>();

    @SerializedName(USER_CREDENTIALS)
    @Expose
    @Column(name = USER_CREDENTIALS)
    public AuthorEntity userCredentials;

//    @SerializedName(CATEGORY)
//    @Expose
//    @Column(name = CATEGORY)
//    public List<EquipmentCategoryEntity> categories;

    @SerializedName(SUB_OBJECTS)
    @Expose
    @Column(name = SUB_OBJECTS)
    public List<String> subObject = new ArrayList<>();


    @SerializedName(QO_REF)
    @Expose
    @Column(name = QO_REF)
    public String questionaireRef;

    @SerializedName(DATE)
    @Expose
    @Column(name = DATE)
    public String date;

    @Column(name = RULESSET_DETAILS)
    private RulesetEntity ruleSetDetail;

    /**
     * Check if the equipment is a sub-object (ie a characteristic) or not
     *
     * @return true if matches
     */
    public boolean isCharacteristic() {
        return "subobject".equals(getType());
    }

    public static EquipmentEntity toEntity(EquipmentWs input, RulesetEntity ruleset) {

        EquipmentEntity output = new EquipmentEntity();

        output.setDate(input.getDate());
        output.setDefinition(input.getDefinition());
        output.setIcon(input.getIcon());
        output.setIllustration(input.getIllustration());
        output.setQuestionaireRef(input.getQuestionaireRef());
        output.setName(input.getName());
        output.setReference(input.getReference());
        output.setSubObject(input.getSubObject());
        output.setType(input.getType());
        output.setRuleSetDetail(ruleset);

        return output;
    }

    @Override
    public boolean hasIcon() {
        return getIcon() != null && !getIcon().isEmpty();
    }
}
