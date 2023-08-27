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
import androidx.room.TypeConverter;

import com.orange.ocara.utils.enums.RuleSetStat;


public class StatusConverter {
    @TypeConverter
    public static int fromStatusToInt(RuleSetStat value) {
        return value.ordinal();
    }

    @TypeConverter
    public static RuleSetStat fromIntToStatus(int value) {
        return (RuleSetStat.values()[value]);
    }
}
