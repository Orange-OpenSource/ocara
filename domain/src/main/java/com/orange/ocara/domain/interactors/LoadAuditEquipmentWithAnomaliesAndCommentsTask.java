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

import com.orange.ocara.domain.models.AuditEquipmentModel;
import com.orange.ocara.domain.repositories.AuditEquipmentRepository;
import com.orange.ocara.domain.repositories.AuditScoresRepository;
import com.orange.ocara.domain.repositories.CommentRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class LoadAuditEquipmentWithAnomaliesAndCommentsTask {

    AuditScoresRepository auditScoresRepository;
    AuditEquipmentRepository auditEquipmentRepository;
    CommentRepository commentRepository;

    @Inject
    public LoadAuditEquipmentWithAnomaliesAndCommentsTask(AuditScoresRepository auditScoresRepository,
                                                          AuditEquipmentRepository auditEquipmentRepository,
                                                          CommentRepository commentRepository) {
        this.auditScoresRepository = auditScoresRepository;
        this.auditEquipmentRepository = auditEquipmentRepository;
        this.commentRepository = commentRepository;
    }

    public Observable<List<AuditEquipmentModel>> execute(Long auditId) {

       return auditEquipmentRepository.loadAuditEquipments(auditId)
                .toObservable()
                .concatMap(auditEquipmentModels -> {
                    return Observable
                            .fromIterable(auditEquipmentModels)
                            .flatMap(auditEquipmentModel -> {
                                return commentRepository.getAuditObjectComments((new Long(auditEquipmentModel.getId())))
                                        .map(commentModels -> {
                                            auditEquipmentModel.setComments(commentModels);
                                            return auditEquipmentModel;
                                        })
                                        .toObservable();
                            })
                            .flatMap(auditEquipmentModel ->{

                                return auditScoresRepository.getAnomaliesForAuditEquipment(new Long(auditEquipmentModel.getId()))
                                        .map(ruleModels -> {
                                            auditEquipmentModel.setAnomalies(ruleModels);
                                            return auditEquipmentModel;
                                        }).toObservable();
                            })
                            .toList()
                            .toObservable();

                });



    }
}
