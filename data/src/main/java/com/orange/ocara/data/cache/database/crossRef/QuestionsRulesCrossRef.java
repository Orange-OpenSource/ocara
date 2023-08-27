package com.orange.ocara.data.cache.database.crossRef;
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
import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "questions_rules", primaryKeys = {"questionRef", "ruleRef", "rulesetRef", "rulesetVersion"})
public class QuestionsRulesCrossRef {
    @NonNull
    private String questionRef;
    @NonNull
    private String ruleRef;
    @NonNull
    private String rulesetRef;
    private int rulesetVersion;

    public QuestionsRulesCrossRef(@NonNull String questionRef, @NonNull String ruleRef, @NonNull String rulesetRef, int rulesetVersion) {
        this.questionRef = questionRef;
        this.ruleRef = ruleRef;
        this.rulesetRef = rulesetRef;
        this.rulesetVersion = rulesetVersion;
    }

    @NonNull
    public String getRulesetRef() {
        return rulesetRef;
    }

    public QuestionsRulesCrossRef setRulesetRef(@NonNull String rulesetRef) {
        this.rulesetRef = rulesetRef;
        return this;
    }

    public int getRulesetVersion() {
        return rulesetVersion;
    }

    public QuestionsRulesCrossRef setRulesetVersion(int rulesetVersion) {
        this.rulesetVersion = rulesetVersion;
        return this;
    }

    public String getQuestionRef() {
        return questionRef;
    }

    public void setQuestionRef(String questionRef) {
        this.questionRef = questionRef;
    }

    public String getRuleRef() {
        return ruleRef;
    }

    public void setRuleRef(String ruleRef) {
        this.ruleRef = ruleRef;
    }


    public static class QuestionsRulesCrossRefBuilder {
        private String questionRef;
        private String ruleRef;
        private String rulesetRef;
        private int rulesetVersion;

        private QuestionsRulesCrossRefBuilder() {
        }

        public static QuestionsRulesCrossRefBuilder aQuestionsRulesCrossRef() {
            return new QuestionsRulesCrossRefBuilder();
        }

        public QuestionsRulesCrossRefBuilder withQuestionRef(String questionRef) {
            this.questionRef = questionRef;
            return this;
        }

        public QuestionsRulesCrossRefBuilder withRuleRef(String ruleRef) {
            this.ruleRef = ruleRef;
            return this;
        }

        public QuestionsRulesCrossRefBuilder withRulesetRef(String rulesetRef) {
            this.rulesetRef = rulesetRef;
            return this;
        }

        public QuestionsRulesCrossRefBuilder withRulesetVersion(int rulesetVersion) {
            this.rulesetVersion = rulesetVersion;
            return this;
        }

        public QuestionsRulesCrossRef build() {
            return new QuestionsRulesCrossRef(questionRef, ruleRef, rulesetRef, rulesetVersion);
        }
    }

}
