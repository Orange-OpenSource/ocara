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

package com.orange.ocara.data.cache.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.data.common.ConnectorException;
import com.orange.ocara.tools.LowercaseEnumTypeAdapterFactory;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

/**
 * Service that can manage rulesets as {@link SharedPreferences}
 * This is the default implementation of {@link DefaultRulesetPreferences}
 */
public class DefaultRulesetPreferencesImpl implements DefaultRulesetPreferences {

    static final String PREF_BUFFER_PACKAGE_NAME = "com.orange.ocara.data.cache.prefs";

    static final String DEFAULT_VALUE = "";

    private static final String PREF_KEY_DEFAULT_RULESET = "default_ruleset";

    private static final String PREF_KEY_LAST_CACHE = "ruleset_last_cache";

    private static final String PREF_KEY_DEFAULT_RULESET_LIST = "default_ruleset_list";

    /**
     * a reader for {@link SharedPreferences}
     */
    private final SharedPreferences reader;

    /**
     * instantiates
     *
     * @param context a {@link Context}
     */
    public DefaultRulesetPreferencesImpl(Context context) {

        reader = context
                .getSharedPreferences(PREF_BUFFER_PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void saveRuleset(RulesetModel ruleset) {
        Timber.d(
                "CacheMessage=Saving ruleset as default;RulesetReference=%s;RulesetVersion=%s;",
                ruleset.getReference(), ruleset.getVersion());

        try {
            String rulesetAsJson = makeGson().toJson(ruleset);

            reader
                    .edit()
                    .putString(PREF_KEY_DEFAULT_RULESET, rulesetAsJson)
                    .apply();
        } catch (Exception e) {
            Timber.e("CacheMessage=Could not save ruleset as preference", e);
            throw ConnectorException.from(e);
        }
    }

    /**
     *
     * @return a {@link RulesetModel} if it exists. null, if not
     * @throws ConnectorException when deserialization fails
     */
    @Override
    public RulesetModel retrieveRuleset() {

        String value = reader.getString(PREF_KEY_DEFAULT_RULESET, DEFAULT_VALUE);

        if (!DEFAULT_VALUE.equals(value)) {

            try {
                return makeGson().fromJson(value, RulesetModel.class);
            } catch (Exception e) {
                Timber.e("CacheMessage=Could not retrieve ruleset from preference", e);
                throw ConnectorException.from(e);
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean checkRulesetExists() {

        String value = reader.getString(PREF_KEY_DEFAULT_RULESET, DEFAULT_VALUE);
        boolean result = !DEFAULT_VALUE.equals(value);

        Timber.d("CacheMessage=Default ruleset is defined ? %b;", result);

        return result;
    }

    @Override
    public Long getLastCacheTime() {
        return reader.getLong(PREF_KEY_LAST_CACHE, 0);
    }

    @Override
    public void setLastCacheTime(long time) {
        reader
                .edit()
                .putLong(PREF_KEY_LAST_CACHE, time)
                .apply();
    }

    @Override
    public List<RulesetModel> findAll() {
        String value = reader.getString(PREF_KEY_DEFAULT_RULESET_LIST, DEFAULT_VALUE);
        if (DEFAULT_VALUE.equals(value)) {
            return Collections.emptyList();
        } else {

            Type type = new TypeToken<List<RulesetModel>>() {
            }.getType();

            return makeGson().fromJson(value, type);
        }
    }

    /** @return an pre-configured instance of {@link Gson} */
    private Gson makeGson() {

        return new GsonBuilder()
                .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
                .create();
    }
}
