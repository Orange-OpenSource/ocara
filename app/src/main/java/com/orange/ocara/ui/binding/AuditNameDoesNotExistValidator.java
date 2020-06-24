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

/**
 * an implementation for {@link Validator} that checks if the given {@link AuditFormUiModel} has a
 * valid audit or not.
 */
public class AuditNameDoesNotExistValidator implements Validator<AuditFormUiModel> {

    /**
     * a {@link Validator} for {@link AuditModel}s
     */
    private final Validator<AuditModel> validator;

    /**
     * instantiate
     *
     * @param modelManager a manager for {@link com.activeandroid.Model}s
     */
    public AuditNameDoesNotExistValidator(ModelManager modelManager) {
        validator = new UniqueReviewValidator(modelManager);
    }

    @Override
    public void validate(AuditFormUiModel input, BindingResult result) {
        AuditModel audit = input.getActualAudit();
        validator.validate(audit, result);
    }
}
