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

package com.orange.ocara.data.net.client;


import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.data.net.model.RulesetWs;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Contract with webservices
 *
 * TODO remove this interface as soon as {@link ApiClient} is removed
 */
public interface RuleSetApi {

    /**
     * retrieves a holder for a HTTP request and its response, which is formatted as a {@link RulesetEntity}.
     *
     * @param ref  a {@link RulesetModel#getReference()}
     * @param version a {@link RulesetModel#getVersion()}
     *
     * @return a {@link Call} for a {@link RulesetModel}
     */
    @FormUrlEncoded
    @POST("ws/findruleset")
    Call<RulesetWs> getRulsetDetail(@Field("ref") String ref, @Field("version") Integer version);

}
