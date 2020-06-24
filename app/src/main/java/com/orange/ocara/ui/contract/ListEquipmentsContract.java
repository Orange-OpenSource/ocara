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

package com.orange.ocara.ui.contract;

import com.orange.ocara.data.net.model.Equipment;

import java.util.List;

/**
 * Contract between the view and the presenter, that deal with the listing of equipments / object descriptions
 */
public interface ListEquipmentsContract {

    /**
     * Behaviour of the view
     */
    interface ListEquipmentsView {

        /**
         * displays the items
         * @param equipments a bunch of {@link Equipment}s
         */
        void showEquipments(List<Equipment> equipments);

        /**
         * displays an information
         */
        void showNoEquipments();
    }

    /**
     * behaviour of the presenter
     */
    interface ListEquipmentsUserActionsListener {

        /**
         * requests for data
         *
         * @param rulesetId an identifier
         */
        void loadAllEquipmentsByRulesetId(Long rulesetId);
    }
}
