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

import androidx.room.EmptyResultSetException;

import com.orange.ocara.domain.repositories.EquipmentRepository;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetNumberOfSubObjectsInEquipment {
    EquipmentRepository repository;

    @Inject
    public GetNumberOfSubObjectsInEquipment(EquipmentRepository repository) {
        this.repository = repository;
    }

    public Single<Integer> execute(int auditEquipmentId) {
        return repository.getNumberOfSubObjects(auditEquipmentId).onErrorResumeNext(error -> {
            if (error instanceof EmptyResultSetException)
                return Single.just(0);
            else
                return Single.error(error);
        });
    }
}
