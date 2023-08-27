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

import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentNameAndIcon;
import com.orange.ocara.domain.repositories.AuditEquipmentRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetAuditEquipmentNameAndIcon {
    AuditEquipmentRepository repository;

    @Inject
    public GetAuditEquipmentNameAndIcon(AuditEquipmentRepository repository) {
        this.repository = repository;
    }

    public Single<AuditEquipmentNameAndIcon> execute(int auditEqId) {
        return repository.getAuditEquipmentInfo(auditEqId);
    }
}
