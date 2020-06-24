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

import com.orange.ocara.business.model.AuditModel;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.ui.model.AuditFormUiModel;

import lombok.RequiredArgsConstructor;
import timber.log.Timber;

/**
 * an implementation for {@link AuditValidator} that checks if the given {@link AuditFormUiModel} has a
 * valid audit or not.
 *
 * Implements {@link Validator}
 */
@RequiredArgsConstructor
public class UniqueReviewValidator implements Validator<AuditModel> {

    private final ModelManager modelManager;

    @Override
    public void validate(AuditModel input, BindingResult result) {
        if (input == null) {
            result.rejectValue("audit shall not be null");
        } else if (input.getName() == null || input.getName().isEmpty()) {
            result.rejectValue("audit name shall not be null/empty");
        } else {
            Timber.d(
                    "Message=Checking audit existence in repository;AuditId=%d;AuditName=%s;AuditVersion=%d;",
                    input.getId(), input.getName(), input.getVersion());

            boolean auditAlreadyExists = modelManager.checkAuditExistence(input.getId(), input.getName(), input.getVersion());
            if (auditAlreadyExists) {
                result.rejectValue("audit name already exist for these version and site");
            }
        }
    }
}
