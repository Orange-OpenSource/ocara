package com.orange.ocara.data.cache.database.NonTables;
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
import androidx.room.Embedded;

import com.orange.ocara.data.cache.database.Tables.Equipment;

public class EquipmentWithSubEquipment {
    private String parentRef;
    @Embedded
    private Equipment subequipment;

    public String getParentRef() {
        return parentRef;
    }

    public void setParentRef(String parentRef) {
        this.parentRef = parentRef;
    }

    public Equipment getSubequipment() {
        return subequipment;
    }

    public void setSubequipment(Equipment subequipment) {
        this.subequipment = subequipment;
    }
}
