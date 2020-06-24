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

package com.orange.ocara.data.cache.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.orange.ocara.business.model.SiteModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import timber.log.Timber;

@Table(name = SiteEntity.TABLE_NAME)
public class SiteEntity extends Model implements SiteModel {

    static final String TABLE_NAME = "sites";

    public static final String COLUMN_NOIMMO = "NOIMMO";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_UGI = "UGI";
    public static final String COLUMN_LABEL_UGI = "labelUGI";
    public static final String COLUMN_ADDR_STREET = "addr_street";
    public static final String COLUMN_ADDR_CITY = "addr_city";
    public static final String COLUMN_ADDR_CODE = "addr_code";

    @Column(name = COLUMN_NOIMMO, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String noImmo; // must be a string because some number could contain letters (for Corse)

    @Column(name = COLUMN_NAME, notNull = true)
    private String name;

    @Column(name = COLUMN_UGI)
    private String ugi;

    @Column(name = COLUMN_LABEL_UGI)
    private String labelUgi;

    @Column(name = COLUMN_ADDR_STREET)
    private String street;

    @Column(name = COLUMN_ADDR_CODE)
    private Integer code;

    @Column(name = COLUMN_ADDR_CITY)
    private String city;

    @Override
    public String getNoImmo() {
        return noImmo;
    }

    public void setNoImmo(String noImmo) {
        this.noImmo = noImmo;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUgi() {
        return ugi;
    }

    public void setUgi(String ugi) {
        this.ugi = ugi;
    }

    @Override
    public String getLabelUgi() {
        return labelUgi;
    }

    public void setLabelUgi(String labelUgi) {
        this.labelUgi = labelUgi;
    }

    @Override
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Default constructor. Needed by ORM.
     */
    public SiteEntity() {
        super();
    }

    public static SiteEntity from(SiteModel item) {
        SiteEntity entity = null;
        if (item == null || item.getName() == null || item.getNoImmo() == null) {
            Timber.w("CacheMessage=One site is invalid;SiteNoImmo=%s", item.getNoImmo());
        } else {
            entity = new SiteEntity();
            entity.setNoImmo(item.getNoImmo());
            entity.setName(item.getName());
            entity.setUgi(item.getUgi());
            entity.setLabelUgi(item.getLabelUgi());
            entity.setStreet(item.getStreet());
            entity.setCode(item.getCode());
            entity.setCity(item.getCity());
        }
        return entity;
    }
}