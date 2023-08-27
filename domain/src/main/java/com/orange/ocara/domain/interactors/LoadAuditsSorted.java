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

import com.orange.ocara.domain.models.AuditModel;
import com.orange.ocara.domain.models.AuditSortConfig;
import com.orange.ocara.domain.repositories.AuditRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class LoadAuditsSorted {
    AuditRepository auditRepository;

    @Inject
    public LoadAuditsSorted(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public Flowable<List<AuditModel>> execute(AuditSortConfig sortConfig, String key) {
        if (sortConfig.isAscending()) {
            switch (sortConfig.getType()) {
                case DATE:
                    return auditRepository.getAuditsOrderedByDateASC(key);
                case SITE:
                    return auditRepository.getAuditsOrderedBySiteAsc(key);
                case NAME:
                    return auditRepository.getAuditsOrderedByNameASC(key);
                case STATUS:
                    return auditRepository.getAuditsOrderedByStatusASC(key);
            }
        } else {
            switch (sortConfig.getType()) {
                case DATE:
                    return auditRepository.getAuditsOrderedByDateDESC(key);
                case SITE:
                    return auditRepository.getAuditsOrderedBySiteDESC(key);
                case NAME:
                    return auditRepository.getAuditsOrderedByNameDESC(key);
                case STATUS:
                    return auditRepository.getAuditsOrderedByStatusDESC(key);
            }
        }
        return auditRepository.getAuditsOrderedByDateASC(key);
    }
}
