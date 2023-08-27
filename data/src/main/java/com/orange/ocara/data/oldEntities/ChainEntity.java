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
import com.orange.ocara.data.network.models.ChainWs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** a model for cached groups of questions */
@Table(name = ChainEntity.TABLE_NAME, id = ChainEntity.ID)
@Data
@EqualsAndHashCode(callSuper = false)
public class ChainEntity extends Model implements Serializable {

    public static final String REFERENCE = "reference";
    public static final String NAME = "name";
    public static final String QUESTIONS = "questionsRef";
    public static final String DATE = "date";
    public static final String TABLE_NAME = "chain";
    public static final String QUESTIONNAIRE = "questionnaire";
    public static final String RULSETDETAILS = "rulset";
    public static final String ID = "_id";

    @SerializedName(REFERENCE)
    @Expose
    @Column(name = REFERENCE)
    public String reference;

    @SerializedName(NAME)
    @Expose
    @Column(name = NAME)
    public String name;

    @SerializedName(QUESTIONS)
    @Expose
    @Column(name = QUESTIONS)
    public List<String> questionsRef = new ArrayList<>();

    @SerializedName(DATE)
    @Expose
    @Column(name = DATE)
    public String date;

    @Column(name = QUESTIONNAIRE)
    private QuestionnaireEntity questionnaire;

    @Column(name = RULSETDETAILS)
    private RulesetEntity ruleset;

    /**
     * Converts a {@link ChainWs} into a {@link ChainEntity}
     * @param input a {@link ChainWs}
     * @return an instance of {@link ChainEntity}
     */
    public static ChainEntity toEntity(ChainWs input, RulesetEntity ruleset, QuestionnaireEntity questionnaire) {

        ChainEntity output = new ChainEntity();

        output.setDate(input.getDate());
        output.setName(input.getName());
        output.setReference(input.getReference());
        output.setQuestionsRef(input.getQuestionsRef());
        output.setRuleset(ruleset);
        output.setQuestionnaire(questionnaire);

        return output;
    }
}
