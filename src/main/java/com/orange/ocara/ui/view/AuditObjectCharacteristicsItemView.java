/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.view;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orange.ocara.ui.dialog.SelectAuditObjectCharacteristicsDialogBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(resName="object_description_selector_item")
public class AuditObjectCharacteristicsItemView extends RelativeLayout {


    @ViewById(resName="object_description_name")
    TextView name;

    @ViewById(resName="object_description_selector")
    SwitchCompat objectDescriptionSelectorSwitch;

    private SelectAuditObjectCharacteristicsDialogBuilder.ObjectDescriptionSelector characteristic;


    public AuditObjectCharacteristicsItemView(Context context) {
        super(context);
    }

    @AfterViews
    protected void setUp() {

        objectDescriptionSelectorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                characteristic.setSelected(objectDescriptionSelectorSwitch.isChecked());
            }
        });

    }


    public void setCharacteristic(SelectAuditObjectCharacteristicsDialogBuilder.ObjectDescriptionSelector characteristic) {
        this.characteristic = characteristic;
        name.setText(characteristic.getObject().getDescription());
        objectDescriptionSelectorSwitch.setChecked(characteristic.isSelected());
    }


}
