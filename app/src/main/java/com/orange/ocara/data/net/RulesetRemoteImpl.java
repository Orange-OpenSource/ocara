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

package com.orange.ocara.data.net;

import com.orange.ocara.data.net.model.RulesetLightWs;
import com.orange.ocara.data.net.model.RulesetWs;
import com.orange.ocara.data.source.RulesetSource.RulesetRemote;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import timber.log.Timber;

/** default implementation of {@link RulesetRemote} */
public class RulesetRemoteImpl implements RulesetRemote {

    private final RulesetService service;

    RulesetRemoteImpl(RulesetService service) {
        this.service = service;
    }

    @Override
    public List<RulesetLightWs> findAll() {
        Call<List<RulesetLightWs>> response = service.getRuleSetList();
        List<RulesetLightWs> output;
        try {
            final List<RulesetLightWs> body = response.execute().body();

            if (body != null) {
                output = body;
            } else {
                output = Collections.emptyList();
            }
        } catch (IOException e) {
            Timber.e(e,"ErrorMessage=Error encountered while retrieving Rulesets from remote server.");
            output = Collections.emptyList();
        }
        return output;
    }

    @Override
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

    @Override
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
