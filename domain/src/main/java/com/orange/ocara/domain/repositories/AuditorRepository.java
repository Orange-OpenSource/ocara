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


package com.orange.ocara.domain.repositories;

import android.content.Context;

import com.orange.ocara.data.cache.database.DAOs.AuditorDAO;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.Audit;
import com.orange.ocara.data.cache.database.Tables.Auditor;
import com.orange.ocara.domain.models.AuditorModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;
import io.reactivex.Completable;
import io.reactivex.Single;

public class AuditorRepository {

    private final AuditorDAO auditorDAO;

    @Inject
    public AuditorRepository(OcaraDB ocaraDB) {
        this.auditorDAO = ocaraDB.auditorDAO();
    }

    public Single<List<String>> getAllAuditorsNames() {
        return auditorDAO.getAllAuditorsNames();
    }

    private void insertAuditor(String name) {
        auditorDAO.insertAuditor(name);
    }

    public Completable insertAuditorIfNotExists(String name) {
        return Completable
                .fromSingle(getAllAuditorsNames()
                        .doOnSuccess(names -> {
                            if (!names.contains(name))
                                insertAuditor(name);
                        }));
    }

    public Single<List<AuditorModel>> getAllAuditors() {
        return auditorDAO.getAllAuditors()
                .map(auditors -> {

                    List<AuditorModel> models = new ArrayList<>();
                    for (Auditor item : auditors) {
                        models.add(new AuditorModel(item));

                    }
                    return models;
                });

    }


    public Single<List<AuditorModel>> getAllAuditorsSortedDesc() {
        return auditorDAO.getAllAuditorsSortedDesc()
                .map(auditors -> {

                    List<AuditorModel> models = new ArrayList<>();
                    for (Auditor item : auditors) {
                        models.add(new AuditorModel(item));

                    }
                    return models;
                });

    }

    public Single<Long> insertNewAuditor(AuditorModel model){
        return auditorDAO.insertAuditor(mapModelToEntity(model));
    }

    public Completable updateAuditor(AuditorModel model){
        Auditor newEntity = mapModelToEntity(model);
        newEntity.setId(model.getId());
        return auditorDAO.updateAuditor(newEntity);
    }

    private Auditor mapModelToEntity(AuditorModel model){

        return new Auditor(model.getFirstName(),
                model.getLastName(),
                model.getEmail(),
                model.getSubscribe());
    }

    public Single<Integer> getNumberOfAudits(int id) {
        return auditorDAO.getNumberOfRelatedAudits(id);
    }

    public Completable deleteAuditor(int id) {
        return auditorDAO.delete(id) ;
    }
}
