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

import java.util.regex.Pattern;

import static timber.log.Timber.d;

/**
 * Implements {@link Validator}
 */
public class UserNamePatternValidator implements Validator<UserModel> {

    private final Pattern AUTHOR_PATTERN = Pattern.compile("^.{3,}+$");

    @Override
    public void validate(UserModel input, BindingResult result) {
        String name = input != null && input.getUserName() != null ? input.getUserName() : "";
        if (!AUTHOR_PATTERN.matcher(name).find()) {
            d("Message=Rejecting value against pattern;Pattern=%s;UserName=<%s>", AUTHOR_PATTERN.pattern(), name);
            result.rejectValue("Invalid author name");
        }
    }
}
