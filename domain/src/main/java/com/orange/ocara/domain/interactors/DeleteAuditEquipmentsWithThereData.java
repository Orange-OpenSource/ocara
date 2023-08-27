package com.orange.ocara.domain.interactors;

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


import com.orange.ocara.domain.repositories.AuditEquipmentRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

/*
    this interactor deletes the audit equipment with its ruleAnswers
    and comments and subObjects
 */
public class DeleteAuditEquipmentsWithThereData {
    private final AuditEquipmentRepository repository;

    @Inject
    public DeleteAuditEquipmentsWithThereData(AuditEquipmentRepository repository) {
        this.repository = repository;
    }

    public Completable execute(List<Integer> auditEquipmentId) {
        return repository.deleteAuditEquipmentsAndThereData(auditEquipmentId);
    }
}
