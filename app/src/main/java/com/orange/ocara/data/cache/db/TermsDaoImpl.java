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

package com.orange.ocara.data.cache.db;

import android.content.Context;

import com.orange.ocara.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lombok.RequiredArgsConstructor;

import static timber.log.Timber.e;

@RequiredArgsConstructor
public class TermsDaoImpl implements TermsDao {

    private final Context context;

    @Override
    public String get() {
        String line;
        StringBuilder sb = new StringBuilder();
        int resource = R.raw.cgu;

        try(InputStream is = context
                    .getResources()
                .openRawResource(resource);

        InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr)) {

            while ((line = reader.readLine()) != null) {
                sb
                        .append(line)
                        .append("\n");
            }
        } catch (IOException e) {
            e(e, "WarningMessage=%s", e.getMessage());
            sb.append("\n");
        }

        return sb.toString();
    }
}
