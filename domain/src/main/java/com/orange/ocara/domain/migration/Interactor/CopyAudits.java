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


package com.orange.ocara.domain.migration.Interactor;

import com.orange.ocara.domain.repositories.AuditRepository;
import com.orange.ocara.data.oldEntities.AuditEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyAudits extends Interactor<AuditEntity>{
    AuditRepository repository;
    @Inject
    public CopyAudits(AuditRepository repository){
        this.repository=repository;
    }
    public Completable execute(){
        List<AuditEntity> auditEntities=getAll(AuditEntity.class);

        return repository.insertAudits(auditEntities);
    }
}
