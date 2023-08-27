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


package com.orange.ocara.domain.repositories;

import android.util.Log;

import com.orange.ocara.data.network.models.RulesetLightWs;
import com.orange.ocara.data.network.models.RulesetWs;
import com.orange.ocara.data.network.remoteContracts.RulesetWebServiceRx;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RulesetRemoteRxRepository {

    private final RulesetWebServiceRx rulesetWebService;

    @Inject
    public RulesetRemoteRxRepository(RulesetWebServiceRx rulesetWebService) {
        this.rulesetWebService = rulesetWebService;
    }

    public Single<List<RulesetLightWs>> findAll() {
        Single<List<RulesetLightWs>> response = rulesetWebService.getRuleSetList();

       return response.observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
               .doOnError(e -> Timber.e(e,"ErrorMessage=Error encountered while retrieving Rulesets from remote server.") )
               .onErrorReturnItem( Collections.emptyList())
               .doOnSuccess(rulesetLightWs -> Log.d("test","remote response = "+rulesetLightWs.size()));
    }

    public Single<RulesetWs> findOne(String reference, Integer version) {
        Single<RulesetWs> response = rulesetWebService.getRulsetDetail(reference, version);

        return response.observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .doOnError(e -> Timber.e(e,"ErrorMessage=Error encountered while retrieving Rulesets from remote server."));
    }

    public Single<List<RulesetLightWs>> findLast(String reference) {
        Single<List<RulesetLightWs>> response = rulesetWebService.getRuleSetList();
        RulesetLightWs output = null;

        return response.observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .doOnError(e -> Timber.e(e,"ErrorMessage=Error encountered while retrieving Rulesets from remote server.") )
                .onErrorReturnItem( Collections.emptyList())
                .doOnSuccess(rulesetLightWs -> firstMatch(rulesetLightWs,reference));

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
