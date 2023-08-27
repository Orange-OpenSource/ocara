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


package com.orange.ocara.domain.models;

import com.orange.ocara.data.cache.database.Tables.Rule;

import java.util.ArrayList;
import java.util.List;

public class RuleModel {
    private final String ref;
    private final String label;
    private String questionRef;
    private int index;
    private boolean illustrated;
    private final List<IllustrationModel> illustrations=new ArrayList<>();
    private final List<RuleImpactModel> ruleImpacts=new ArrayList<>();
    private QuestionModel question;

    public RuleModel(String ref, String label, String questionRef, int i
            , boolean illustrated) {
        this.ref = ref;
        this.label = label;
        this.illustrated = illustrated;
        this.questionRef = questionRef;
        index = i;
    }

    public RuleModel(Rule rule) {
        this.ref = rule.getReference();
        this.label = rule.getLabel();
    }

    public QuestionModel getQuestion() {
        return question;
    }

    public void setQuestion(QuestionModel question) {
        this.question = question;
    }

    public List<IllustrationModel> getIllustrations() {
        return illustrations;
    }

    public void addIllustration(IllustrationModel illustration) {
        illustrations.add(illustration);
    }

    public boolean isIllustrated() {
        return illustrated;
    }

    public void setIllustrated(boolean illustrated) {
        this.illustrated = illustrated;
    }

    public String getRef() {
        return ref;
    }

    public String getLabel() {
        return label;
    }

    public String getQuestionRef() {
        return questionRef;
    }

    public int getIndex() {
        return index;
    }
}
