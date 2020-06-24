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

package com.orange.ocara.business.binding;

import lombok.Getter;

/**
 * Exception class for the domain layer
 */
@Getter
public class BizError implements ErrorBundle {

    private final String message;

    private final Throwable cause;

    private final static String DEFAULT_MESSAGE = "internal error";

    /**
     * instantiate with an error message and a cause
     * @param message the detail message
     */
    public BizError(String message) {
        this.message = message;
        this.cause = null;
    }

    /**
     * instantiate with an error message and a cause
     * @param message the detail message
     * @param cause the reason. Can be null.
     */
    public BizError(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    /**
     * instantiate with a cause
     * @param cause the reason. Can be null.
     */
    public BizError(Throwable cause) {
        this.message = cause == null ? DEFAULT_MESSAGE : cause.getMessage();
        this.cause = cause;
    }
}
