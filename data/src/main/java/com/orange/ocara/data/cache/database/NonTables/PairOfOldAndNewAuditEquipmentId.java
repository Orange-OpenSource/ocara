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
public class PairOfOldAndNewAuditEquipmentId {
    int oldAuditEquipmentId, newAuditEquipmentId;

    public PairOfOldAndNewAuditEquipmentId(int oldAuditEquipmentId, int newAuditEquipmentId) {
        this.oldAuditEquipmentId = oldAuditEquipmentId;
        this.newAuditEquipmentId = newAuditEquipmentId;
    }

    public int getOldAuditEquipmentId() {
        return oldAuditEquipmentId;
    }

    public void setOldAuditEquipmentId(int oldAuditEquipmentId) {
        this.oldAuditEquipmentId = oldAuditEquipmentId;
    }

    public int getNewAuditEquipmentId() {
        return newAuditEquipmentId;
    }

    public void setNewAuditEquipmentId(int newAuditEquipmentId) {
        this.newAuditEquipmentId = newAuditEquipmentId;
    }
}
