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

package com.orange.ocara.data.network.client;

import android.util.Log;


import com.orange.ocara.data.network.models.RulesetLightWs;
import com.orange.ocara.data.network.models.RulesetWs;
import com.orange.ocara.data.network.remoteContracts.RulesetService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


import javax.inject.Inject;

import retrofit2.Call;
import timber.log.Timber;

public class NewRulesetRemote {

    private final RulesetService service;

    @Inject
    public NewRulesetRemote(RulesetService service) {
        this.service = service;
    }
    public List<RulesetLightWs> findAll() {
        Call<List<RulesetLightWs>> response = service.getRuleSetList();
        List<RulesetLightWs> output;
        try {
            final List<RulesetLightWs> body = response.execute().body();
            if (body != null) {
                Log.d("test","remote response = "+body.size());
                output = body;
            } else {
                Log.d("test","empty set");
                output = Collections.emptyList();
            }
        } catch (IOException e) {
            Timber.e(e,"ErrorMessage=Error encountered while retrieving Rulesets from remote server.");
            output = Collections.emptyList();
        }
        return output;
    }

    public RulesetWs findOne(String reference, Integer version) {
        Call<RulesetWs> ruleset = service.getRulsetDetail(reference, version);
        RulesetWs output = null;
        try {
            output = ruleset.execute().body();
        } catch (IOException e) {
            Timber.e(e,"ErrorMessage=Error encountered while retrieving Rulesets from remote server.");
        }
        return output;
    }

    public RulesetLightWs findLast(String reference) {
        Call<List<RulesetLightWs>> response = service.getRuleSetList();
        RulesetLightWs output = null;
        try {
            final List<RulesetLightWs> body = response.execute().body();

            if (body != null) {
                output = firstMatch(body, reference);
            }
        } catch (IOException e) {
            Timber.e(e,"ErrorMessage=Error encountered while retrieving Rulesets from remote server.");
        }
        return output;
    }


    /**
     *
     * @param list a bunch of {@link RulesetLightWs}s
     * @param reference a filter
     * @return a {@link RulesetLightWs} | null if none matches the expected reference
     */
    private RulesetLightWs firstMatch(Iterable<RulesetLightWs> list, String reference) {

        RulesetLightWs result =null;

        for (RulesetLightWs item : list) {
            if (reference.equals(item.getReference())) {
                result = item;
                break;
            }
        }
        return result;
    }
}
