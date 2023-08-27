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

import androidx.room.ColumnInfo;

import com.orange.ocara.data.cache.database.Tables.Auditor;

public class AuditorModel {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean subscribe;

    public AuditorModel(String name) {
        this.firstName = name;
    }

    public AuditorModel() {
    }

    public AuditorModel(int id, String name) {
        this.id = id;
        this.firstName = name;
    }

    public AuditorModel(Auditor auditor) {
        this.id = auditor.getId();
        this.firstName = auditor.getFirstName();
        this.lastName = auditor.getLastName();
        this.email = auditor.getEmail();
        this.subscribe = auditor.getSubscribe();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        AuditorModel auditorModel;

        public Builder() {
            auditorModel = new AuditorModel();
        }

        public Builder firstName(String firstName) {
            auditorModel.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            auditorModel.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            auditorModel.email = email;
            return this;
        }

        public Builder isSubscribed(Boolean b) {
            auditorModel.subscribe = b;
            return this;
        }


        public Builder id(int id) {
            auditorModel.id = id;
            return this;
        }

        public AuditorModel build() {
            return auditorModel;
        }
    }
}
