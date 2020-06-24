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

/**
 * Exception dedicated to the domain layer
 */
public class BizException extends RuntimeException {

    /**
     * instantiate with a specified detail message
     * @param message detail message
     */
    public BizException(String message) {
        super(message);
    }

    /**
     * instantiate with a specified detail message and a cause
     * @param message detail message
     * @param cause reason why it happens
     */
    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * instantiate with a cause
     * @param cause reason why it happens
     */
    public BizException(Throwable cause) {
        super(cause);
    }
}
