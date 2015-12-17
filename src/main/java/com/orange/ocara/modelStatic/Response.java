/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.modelStatic;

import java.util.Collection;

public enum Response {
    // from lowest to highest

    NotApplicable,
    OK,
    NoAnswer,
    DOUBT,
    NOK;

    public static Response compute(Collection<Response> responses) {
        return compute(responses.toArray(new Response[responses.size()]));
    }

    public static Response compute(Response[] responses) {
        if (responses == null || responses.length <1) {
            return Response.OK;
        }

        Response result = Response.NotApplicable; // lowest
        for(Response response : responses) {
            if (response.equals(NOK)) {
                return response;
            }
            result = max(result, response);

        }
        return result;


    }

    public static Response max(Response first, Response second) {
        return (first.ordinal() >= second.ordinal() ? first : second);
    }
}
