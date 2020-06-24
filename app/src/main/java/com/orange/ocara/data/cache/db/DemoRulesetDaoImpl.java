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

import com.google.gson.Gson;
import com.orange.ocara.R;
import com.orange.ocara.data.common.ConnectorException;
import com.orange.ocara.data.net.model.RulesetWs;

import java.io.InputStream;
import java.io.InputStreamReader;

import timber.log.Timber;

public class DemoRulesetDaoImpl implements DemoRulesetDao {

    private final Context context;

    private final int DEMO_RESOURCE = R.raw.ruleset_demo;


    public DemoRulesetDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean exists() {
        try (InputStream is = context
                .getResources()
                .openRawResource(DEMO_RESOURCE);

             InputStreamReader isr = new InputStreamReader(is)) {

            return new Gson().fromJson(isr, RulesetWs.class) != null;
        } catch (Exception e) {
            Timber.w(e,"Demo ruleset reading failed for resourceId %d  " + DEMO_RESOURCE);
            return false;
        }
    }

    @Override
    public RulesetWs get() {

        try (InputStream is = context
                .getResources()
                .openRawResource(DEMO_RESOURCE);

             InputStreamReader isr = new InputStreamReader(is)) {

            return new Gson().fromJson(isr, RulesetWs.class);
        } catch (Exception e) {
            throw new ConnectorException("Demo ruleset reading failed for resourceId %d  " + DEMO_RESOURCE, e);
        }
    }
}
