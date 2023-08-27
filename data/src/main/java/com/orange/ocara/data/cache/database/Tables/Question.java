package com.orange.ocara.data.cache.database.Tables;
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
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions")
public class Question {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "questionRef")
    private String reference;
    @ColumnInfo(name = "questionLabel")
    private String label;
    private String state;
    @ColumnInfo(name = "questionDate")
    private String date;
    @ColumnInfo(name = "questionOrigin")
    private String origin;
    private String subject;

    public Question(@NonNull String reference, String label, String state, String date, String subject) {
        this.reference = reference;
        this.label = label;
        this.state = state;
        this.date = date;
        this.subject = subject;
    }

    @NonNull
    public String getReference() {
        return reference;
    }

    public void setReference(@NonNull String reference) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public static class QuestionBuilder {
        private String origin;
        private String reference;
        private String label;
        private String state;
        private String date;
        private String subject;

        private QuestionBuilder() {
        }

        public static QuestionBuilder aQuestion() {
            return new QuestionBuilder();
        }

        public QuestionBuilder withOrigin(String origin) {
            this.origin = origin;
            return this;
        }

        public QuestionBuilder withReference(String reference) {
            this.reference = reference;
            return this;
        }

        public QuestionBuilder withLabel(String label) {
            this.label = label;
            return this;
        }

        public QuestionBuilder withState(String state) {
            this.state = state;
            return this;
        }

        public QuestionBuilder withDate(String date) {
            this.date = date;
            return this;
        }

        public QuestionBuilder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Question build() {
            Question question = new Question(reference, label, state, date, subject);
            question.setOrigin(origin);
            return question;
        }
    }

}
