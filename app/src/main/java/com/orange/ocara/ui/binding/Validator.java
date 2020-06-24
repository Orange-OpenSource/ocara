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

package com.orange.ocara.ui.binding;

/**
 * Behaviour of helper classes for objects validation
 *
 * @param <T> type of the targets
 */
public interface Validator<T> {

    /**
     * Checks that the given target matches controls and put errors in the
     * {@link BindingResult}
     *
     * @param target element to validate
     * @param errors container for validation errors
     */
    void validate(T target, BindingResult errors);
}
