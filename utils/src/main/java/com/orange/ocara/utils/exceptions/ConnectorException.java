/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara.utils.exceptions;

import androidx.annotation.NonNull;

/** An unchecked exception wrapping all sub system exceptions. */
public class ConnectorException extends RuntimeException {

    /** Serial version uid for the class */
    private static final long serialVersionUID = 1L;

    /** Empty constructor */
    public ConnectorException() {
    }

    /**
     * The constructor with a message.
     *
     * @param message See {@link #getMessage()}.
     */
    public ConnectorException(@NonNull String message) {
        super(message);
    }

    /**
     * The constructor with a message and the wrapped exception
     *
     * @param message   See {@link #getMessage()}.
     * @param wrappedEx The exception to wrap
     */
    public ConnectorException(@NonNull String message, @NonNull Exception wrappedEx) {
        super(message, wrappedEx);
    }

    /**
     * @param ex Any exception
     * @return The corresponding connector exception
     */
    public static ConnectorException from(Exception ex) {
        return new ConnectorException("Exception in connector: " + ex.getMessage(), ex);
    }

}
