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

package com.orange.ocara.data.cache.model;

/**
 * Some response types
 */
public enum ResponseModel {
    // from lowest to highest
    NOT_APPLICABLE,
    OK,
    NO_ANSWER,
    DOUBT,
    ANNOYING,
    BLOCKING,
    NOK;

    /**
     *
     * @param first a {@link ResponseModel}
     * @param second another {@link ResponseModel}
     * @return the one {@link ResponseModel} of both input that has the higher priority
     */
    public static ResponseModel max(ResponseModel first, ResponseModel second) {
        return (first.ordinal() >= second.ordinal() ? first : second);
    }

    /**
     *
     * @param first a {@link WithResponse}
     * @param second another {@link WithResponse}
     * @return the one {@link WithResponse} of both input that has the higher priority
     */
    public static ResponseModel max(WithResponse first, WithResponse second) {
        return max(first.getResponse(), second.getResponse());
    }
}
