/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import com.orange.ocara.modelStatic.Response;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper=true)
class ResponsePresenter extends Presenter<Response> {

    public ResponsePresenter(Response response) {
        super(response);
    }

    public String getColorCode() {
        switch (value) {
            case OK:
                return "259B24";

            case NOK:
                return "DD2C00";

            case DOUBT:
                return "FFD600";

            case NoAnswer:
                return "FFFFFF";

            default:
                return "FFFFFF";
        }
    }

    public String getTitle() {
        switch (value) {
            case OK:
                return "valide";

            case NOK:
                return "KO";

            default:
                return "doute";
        }
    }

    public boolean isOk() {
        return Response.OK.equals(value);
    }
}
