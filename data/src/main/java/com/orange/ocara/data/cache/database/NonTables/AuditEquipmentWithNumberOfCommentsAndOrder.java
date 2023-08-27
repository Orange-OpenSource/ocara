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
import androidx.room.ColumnInfo;

public class AuditEquipmentWithNumberOfCommentsAndOrder extends AuditEquipmentWithAuditAndEquipment {
    @ColumnInfo(name = "cnt")
    int numberOfComments;
//    int order;

//    public int getOrder() {
//        return order;
//    }

//    public void setOrder(int order) {
//        this.order = order;
//    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }
}
