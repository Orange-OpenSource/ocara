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

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregator for validation errors
 */
public class BindingResult {

    /**
     * a {@link List} for validation messages
     */
    private List<String> errors = new ArrayList<>();

    /**
     * add some error
     *
     * @param message a reason
     */
    public void rejectValue(String message) {
        errors.add(message);
    }

    /**
     *
     * @return true if some errors have been added
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
