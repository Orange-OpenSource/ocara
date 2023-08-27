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
package com.orange.ocara.data.network.remoteContracts;

import com.orange.ocara.data.network.models.RulesetLightWs;
import com.orange.ocara.data.network.models.RulesetWs;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RulesetWebServiceRx {
    /**
     * retrieves a holder for a HTTP request and its response, which format is a {@link List} of { RulesetModel}s.
     *
     * @return a {@link Call} for a {@link List} of { RulesetModel}s
     */
    @GET("ws/rulesetlist")
    Single<List<RulesetLightWs>> getRuleSetList();

    /**
     * retrieves a holder for a HTTP request and its response, which is formatted as a { RulesetEntity}.
     *
     * @param ref  a { RulesetModel#getReference()}
     * @param version a { RulesetModel#getVersion()}
     *
     * @return a {@link Call} for a { RulesetModel}
     */
    @FormUrlEncoded
    @POST("ws/findruleset")
    Single<RulesetWs> getRulsetDetail(@Field("ref") String ref, @Field("version") Integer version);

}
