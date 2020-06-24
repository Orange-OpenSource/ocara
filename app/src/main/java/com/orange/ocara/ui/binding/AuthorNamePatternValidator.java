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

import com.orange.ocara.business.model.UserModel;
import com.orange.ocara.ui.model.AuditFormUiModel;

/**
 * an implementation for {@link AuditValidator} that checks if the given {@link AuditFormUiModel} has a
 * valid author or not.
 */
public class AuthorNamePatternValidator implements Validator<AuditFormUiModel> {

    /**
     * a {@link Validator} for {@link UserModel}s
     */
    private final Validator<UserModel> validator = new UserNamePatternValidator();

    @Override
    public void validate(AuditFormUiModel input, BindingResult result) {
        UserModel user = input.getActualAuthor();
        if (user != null) {
            validator.validate(user, result);
        } else {
            result.rejectValue("Author is missing");
        }
    }
}
