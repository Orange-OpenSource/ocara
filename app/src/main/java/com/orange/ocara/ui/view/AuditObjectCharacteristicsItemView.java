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

package com.orange.ocara.ui.view;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.orange.ocara.R;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.ui.dialog.SelectAuditObjectCharacteristicsDialogBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.object_description_selector_item)
public class AuditObjectCharacteristicsItemView extends RelativeLayout {


    @ViewById(R.id.object_description_name)
    TextView name;

    @ViewById(R.id.object_description_selector)
    SwitchCompat objectDescriptionSelectorSwitch;

    private SelectAuditObjectCharacteristicsDialogBuilder.ObjectDescriptionSelector characteristic;


    public AuditObjectCharacteristicsItemView(Context context) {
        super(context);
    }

    @AfterViews
    protected void setUp() {

        objectDescriptionSelectorSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> characteristic.setSelected(objectDescriptionSelectorSwitch.isChecked()));

    }


    public void setCharacteristic(SelectAuditObjectCharacteristicsDialogBuilder.ObjectDescriptionSelector characteristic) {
        this.characteristic = characteristic;
        final EquipmentEntity object = characteristic.getObject();
        if (object != null) {
            name.setText(object.getName());
        }
        objectDescriptionSelectorSwitch.setChecked(characteristic.isSelected());
    }


}
