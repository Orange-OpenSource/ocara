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

import com.orange.ocara.ui.model.AuditFormUiModel;

/**
 * Behaviour for classes that validate {@link AuditFormUiModel}s
 */
public interface AuditValidator {

    /**
     * Checks that the given {@link AuditFormUiModel} matches controls and put errors in the
     * {@link BindingResult}
     *
     * @param input element to validate
     * @param result container for validation errors
     */
    void validate(AuditFormUiModel input, BindingResult result);
}
