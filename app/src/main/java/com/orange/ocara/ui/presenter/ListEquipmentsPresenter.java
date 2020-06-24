/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.service.EquipmentService;
import com.orange.ocara.data.net.model.Equipment;
import com.orange.ocara.ui.contract.ListEquipmentsContract;

import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link ListEquipmentsContract.ListEquipmentsUserActionsListener}
 *
 * Presenter related to a view that implements {@link ListEquipmentsContract.ListEquipmentsView}
 */
@RequiredArgsConstructor
public class ListEquipmentsPresenter implements ListEquipmentsContract.ListEquipmentsUserActionsListener {

    private final EquipmentService equipmentService;

    private final ListEquipmentsContract.ListEquipmentsView view;

    @Override
    public void loadAllEquipmentsByRulesetId(Long rulesetId) {
        List<Equipment> equipments = equipmentService.retrieveObjectDescriptionsByRulesetId(rulesetId);
        if (equipments.isEmpty()) {
            view.showNoEquipments();
        } else {
            view.showEquipments(equipments);
        }
    }
}
