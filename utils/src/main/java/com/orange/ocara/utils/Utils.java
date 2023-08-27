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

package com.orange.ocara.utils;

import android.content.Context;

import com.orange.ocara.utils.enums.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Utils {
    public static String getSite(Context context) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open("sites_open-source.json");
            jsonString = getJsonFromInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }
    public static String getRuleset(Context context){
        String jsonString;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.ruleset_demo);
            jsonString = getJsonFromInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }
    private static String getJsonFromInputStream(InputStream is) throws IOException{
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }
}
