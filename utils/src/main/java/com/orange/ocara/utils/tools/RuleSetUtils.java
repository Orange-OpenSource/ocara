/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara.utils.tools;


import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RuleSetUtils {

    private static final String WEBSERVICE_DATE_FORMAT = "yyyyMMddHHmmss";
    private static final String DISPLAY_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

    @NonNull
    public static String formatRulesetDate(@NonNull String rulesetDate, Locale locale) {
        DateFormat formatter = new SimpleDateFormat(WEBSERVICE_DATE_FORMAT, locale);
        try {
            Date date = formatter.parse(rulesetDate);

            SimpleDateFormat newFormat = new SimpleDateFormat(DISPLAY_DATE_FORMAT, locale);
            return newFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
