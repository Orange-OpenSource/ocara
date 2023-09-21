/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */


package com.orange.ocara.domain.interactors;

import com.orange.ocara.domain.models.AuditorModel;
import com.orange.ocara.domain.repositories.AuditorRepository;

import javax.inject.Inject;

import io.reactivex.Completable;

public class UpdateAuditorTask {
    AuditorRepository auditorRepository;
    @Inject
    public UpdateAuditorTask(AuditorRepository auditorRepository){
        this.auditorRepository=auditorRepository;
    }
    public Completable execute(AuditorModel auditorModel){
        return auditorRepository.updateAuditor(auditorModel);
    }
}