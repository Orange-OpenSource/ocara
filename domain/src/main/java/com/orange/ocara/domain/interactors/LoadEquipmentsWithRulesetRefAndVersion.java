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

import com.orange.ocara.domain.models.EquipmentModel;
import com.orange.ocara.domain.repositories.EquipmentRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class LoadEquipmentsWithRulesetRefAndVersion {
    private final EquipmentRepository equipmentRepository;

    @Inject
    public LoadEquipmentsWithRulesetRefAndVersion(EquipmentRepository equipmentRepository){
        this.equipmentRepository=equipmentRepository;
    }
    public Single<List<EquipmentModel>> execute(String rulesetRef, int version){
        return Single.fromObservable(equipmentRepository.findAll(rulesetRef, version));
    }
}
