package com.orange.ocara.data.cache.database.NonTables;
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
import androidx.room.Embedded;

import com.orange.ocara.data.cache.database.Tables.Rule;

public class RuleWithIllustrationsCount {
    @Embedded
    private Rule rule;
    private int illustrations;
    private String questionRef;

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public int getIllustrations() {
        return illustrations;
    }

    public void setIllustrations(int illustrations) {
        this.illustrations = illustrations;
    }

    public String getQuestionRef() {
        return questionRef;
    }

    public void setQuestionRef(String questionRef) {
        this.questionRef = questionRef;
    }
}
