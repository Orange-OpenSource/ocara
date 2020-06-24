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

import com.orange.ocara.business.binding.BizException;
import com.orange.ocara.data.source.TermsSource.TermsRemote;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/** default implementation of {@link TermsRemote}*/
public class TermsRemoteImpl implements TermsRemote {

    private final TermsService service;

    TermsRemoteImpl(TermsService service) {
        this.service = service;
    }

    @Override
    public String find() {
        Call<String> terms = service.terms();
        String output = "";
        try {

            Response<String> response = terms.execute();

            if (response.isSuccessful()) {
                Timber.d("Message=Response is success;ResponseCode=%d", response.code());
                output = response.body();
            } else {
                Timber.w("Message=Response is failure;ResponseCode=%d", response.code());
            }
        } catch (IOException e) {
            throw new BizException(e);
        }
        return output;
    }
}
