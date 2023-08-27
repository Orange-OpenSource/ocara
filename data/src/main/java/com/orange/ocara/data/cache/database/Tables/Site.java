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
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sites")
public class Site {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "siteId")
    private int id;
    private String name;
    private String addressStreet;
    private String addressCity;
    private Integer addressCode;
    private String noImmo;

    public String getNoImmo() {
        return noImmo;
    }

    public void setNoImmo(String noImmo) {
        this.noImmo = noImmo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public Integer getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(Integer addressCode) {
        this.addressCode = addressCode;
    }

    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        private final Site site;
        Builder(){
            site = new Site();
        }
        public Builder name(String name){
            site.name=name;
            return this;
        }
        public Builder addressStreet(String addressStreet){
            site.addressStreet=addressStreet;
            return this;
        }
        public Builder addressCity(String addressCity){
            site.addressCity=addressCity;
            return this;
        }
        public Builder addressCode(Integer addressCode){
            site.addressCode=addressCode;
            return this;
        }
        public Builder noImmo(String noImmo){
            site.noImmo=noImmo;
            return this;
        }
        public Site build(){
            return site;
        }
    }
}
