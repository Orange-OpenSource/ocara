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

package com.orange.ocara.ui.intent.export.docx;

import com.orange.ocara.data.cache.model.ResponseModel;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class ResponsePresenter extends Presenter<ResponseModel> {

    public ResponsePresenter(ResponseModel response) {
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

            case NO_ANSWER:
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
        return ResponseModel.OK.equals(value);
    }
}
