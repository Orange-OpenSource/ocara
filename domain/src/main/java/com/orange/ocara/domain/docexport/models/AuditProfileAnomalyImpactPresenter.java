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

package com.orange.ocara.domain.docexport.models;



import com.orange.ocara.utils.enums.Answer;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class AuditProfileAnomalyImpactPresenter extends Presenter<String> {

    public AuditProfileAnomalyImpactPresenter(String response) {
        super(response);
    }

    public String getColorCode() {
//        switch (value) {
//            case OK:
//                return "259B24";
//
//            case NOK:
//                return "DD2C00";
//
//            case DOUBT:
//                return "FFD600";
//
//            case NO_ANSWER:
//                return "FFFFFF";
//
//            default:
//                return "FFFFFF";
//        }
        return "FFFFFF";

    }

    public String getTitle() {
        return value;
//        switch (value) {
//            case OK:
//                return "OK";
//
//            case NOK:
//                return "KO";
//
//            case NO_ANSWER:
//            case NOT_APPLICABLE:
//                return "N/A";
//
//            default:
//                return "doute";
//        }
    }

    public boolean isOk() {
        return Answer.OK.equals(value);
    }
}
