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
package com.orange.ocara.domain.docexport.models;


import com.orange.ocara.domain.models.ProfileTypeModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProfileTypePresenter extends Presenter<ProfileTypeModel> {

    private final int index;
    private int stat;

    public ProfileTypePresenter(ProfileTypeModel handicap, int stat, int index) {
        super(handicap);
        this.stat = stat;
        this.index = index;
    }

    public String getName() {
        return notNull(value.getName());
    }

    public int getIndex() {
        return index;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }
}