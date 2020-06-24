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

package com.orange.ocara.ui.intent.export.docx;

import com.orange.ocara.data.net.model.ProfileTypeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProfileTypePresenter extends Presenter<ProfileTypeEntity> {

    private final int index;
    private int stat;

    public ProfileTypePresenter(ProfileTypeEntity handicap, int stat, int index) {
        super(handicap);
        this.stat = stat;
        this.index = index;
    }

    public String getName() {
        return notNull(value.getName());
    }
}
