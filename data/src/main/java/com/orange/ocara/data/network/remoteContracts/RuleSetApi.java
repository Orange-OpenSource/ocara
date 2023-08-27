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




import com.orange.ocara.data.network.client.ApiClient;
import com.orange.ocara.data.network.models.RulesetWs;
import com.orange.ocara.data.oldEntities.RulesetEntity;

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
     * @param ref  a { RulesetModel#getReference()}
     * @param version a { RulesetModel#getVersion()}
     *
     * @return a {@link Call} for a { RulesetModel}
     */
    @FormUrlEncoded
    @POST("ws/findruleset")
    Call<RulesetWs> getRulsetDetail(@Field("ref") String ref, @Field("version") Integer version);

}
