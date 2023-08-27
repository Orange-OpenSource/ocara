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

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Contract with webservices
 *
 * TODO remove this interface as soon as {@link ApiClient} is removed
 */
public interface TermsOfUseApi {

    /**
     * Remotely requests for the text of the terms-of-use
     *
     * @return the html content of terms-of-use
     */
    @GET("termsOfUse.html")
    Call<String> terms();
}
