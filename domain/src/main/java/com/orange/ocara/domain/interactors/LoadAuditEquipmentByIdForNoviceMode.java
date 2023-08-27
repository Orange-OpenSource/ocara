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
import com.orange.ocara.domain.models.QuestionModel;
import com.orange.ocara.domain.repositories.AuditEquipmentRepository;

import javax.inject.Inject;

import io.reactivex.Single;
import timber.log.Timber;

public class LoadAuditEquipmentByIdForNoviceMode {
    AuditEquipmentRepository repository;
    @Inject
    public LoadAuditEquipmentByIdForNoviceMode(AuditEquipmentRepository repository) {
        this.repository = repository;
    }
    /*
        in this mode we only need the list of questions
        we don't care about their equipments , so we can group
        them in the main equipment
     */
    public Single<AuditEquipmentModel> execute(int id){
        Timber.d(id+"");
        return Single.fromObservable(repository.loadAuditEqById(id)).map(auditEquipment -> {
            for(AuditEquipmentModel auditSubEquipment:auditEquipment.getChildren()){
                for(QuestionModel question:auditSubEquipment.getEquipment().getQuestions()){
                    auditEquipment.getEquipment().addQuestion(question);
                }
            }
            return auditEquipment;
        });
    }
}
