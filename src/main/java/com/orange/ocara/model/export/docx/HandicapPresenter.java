/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import com.orange.ocara.modelStatic.Handicap;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HandicapPresenter extends Presenter<Handicap> {

    private int stat;
    private final int index;

    public HandicapPresenter(Handicap handicap, int stat, int index) {
        super(handicap);

        this.stat = stat;
        this.index = index;
    }

    public String getName() {
        return notNull(value.getName());
    }
}
