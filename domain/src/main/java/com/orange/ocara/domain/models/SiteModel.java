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

import com.orange.ocara.data.cache.database.Tables.Site;
import com.orange.ocara.data.oldEntities.SiteEntity;

import java.io.Serializable;

public class SiteModel implements Serializable {


    private int id;
    private String name;
    private String noImmo;
    private String addressStreet;
    private String addressCity;
    private Integer addressCode;

    public SiteModel() {
    }

    public SiteModel(Site site) {
        id = site.getId();
        noImmo = site.getNoImmo();
        name = site.getName();
        addressCity = site.getAddressCity();
        addressStreet = site.getAddressStreet();
        addressCode = site.getAddressCode();
    }

    public SiteModel(SiteEntity site) {
        id = site.getId().intValue();
        noImmo = site.getNoImmo();
        name = site.getName();
        addressCity = site.getCity();
        addressStreet = site.getStreet();
        addressCode = site.getCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoImmo() {
        return noImmo;
    }

    public String getName() {
        return name;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public Integer getAddressCode() {
        return addressCode;
    }
    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        private final SiteModel site;

        Builder(){
            site = new SiteModel();
        }
        public Builder id(int id){
            site.id=id;
            return this;
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
        public SiteModel build(){
            return site;
        }
    }
}
