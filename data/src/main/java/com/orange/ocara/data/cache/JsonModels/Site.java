package com.orange.ocara.data.cache.JsonModels;
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
public class Site {
    private String idUGI;
    private String labelUGI;
    private String NOIMMO;
    private String name;
    private String type;
    private Address addr;

    public String getIdUGI() {
        return idUGI;
    }

    public void setIdUGI(String idUGI) {
        this.idUGI = idUGI;
    }

    public String getLabelUGI() {
        return labelUGI;
    }

    public void setLabelUGI(String labelUGI) {
        this.labelUGI = labelUGI;
    }

    public String getNOIMMO() {
        return NOIMMO;
    }

    public void setNOIMMO(String NOIMMO) {
        this.NOIMMO = NOIMMO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Address getAddr() {
        return addr;
    }

    public void setAddr(Address addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "Site{" +
                "idUGI='" + idUGI + '\'' +
                ", labelUGI='" + labelUGI + '\'' +
                ", NOIMMO='" + NOIMMO + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", addr=" + addr +
                '}';
    }
}
