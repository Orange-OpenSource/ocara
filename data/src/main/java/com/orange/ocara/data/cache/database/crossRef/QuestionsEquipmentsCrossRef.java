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

@Entity(tableName = "questions_objects", primaryKeys = {"questionRef", "objectReference", "rulesetRef", "rulesetVer"})
public class QuestionsEquipmentsCrossRef {
    @NonNull
    String rulesetRef;
    @NonNull
    private String questionRef;
    @NonNull
    private String objectReference;
    private int rulesetVer;

    public QuestionsEquipmentsCrossRef(@NonNull String questionRef, @NonNull String objectReference, @NonNull String rulesetRef, int rulesetVer) {
        this.questionRef = questionRef;
        this.objectReference = objectReference;
        this.rulesetRef = rulesetRef;
        this.rulesetVer = rulesetVer;
    }

    @NonNull
    public String getObjectReference() {
        return objectReference;
    }

    public void setObjectReference(@NonNull String objectReference) {
        this.objectReference = objectReference;
    }

    @NonNull
    public String getRulesetRef() {
        return rulesetRef;
    }

    public QuestionsEquipmentsCrossRef setRulesetRef(@NonNull String rulesetRef) {
        this.rulesetRef = rulesetRef;
        return this;
    }

    public int getRulesetVer() {
        return rulesetVer;
    }

    public QuestionsEquipmentsCrossRef setRulesetVer(int rulesetVer) {
        this.rulesetVer = rulesetVer;
        return this;
    }

    @NonNull
    public String getQuestionRef() {
        return questionRef;
    }

    public void setQuestionRef(@NonNull String questionRef) {
        this.questionRef = questionRef;
    }

    public static class QuestionsEquipmentsBuilder{

        private String questionRef;
        private String objectReference;
        private String rulesetRef;
        private int rulesetVer;

        public QuestionsEquipmentsBuilder setQuestionRef(String questionRef) {
            this.questionRef = questionRef;
            return this;
        }

        public QuestionsEquipmentsBuilder setObjectReference(String objectReference) {
            this.objectReference = objectReference;
            return this;
        }

        public QuestionsEquipmentsBuilder setRulesetRef(String rulesetRef) {
            this.rulesetRef = rulesetRef;
            return this;
        }

        public QuestionsEquipmentsBuilder setRulesetVer(int rulesetVer) {
            this.rulesetVer = rulesetVer;
            return this;
        }

        public QuestionsEquipmentsCrossRef createQuestionsEquipmentsCrossRef() {
            return new QuestionsEquipmentsCrossRef(questionRef, objectReference, rulesetRef, rulesetVer);
        }
    }
}
