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

import com.orange.ocara.data.cache.model.AuditorEntity;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(resName = "auditor_item")
public class AuditorItemView extends RelativeLayout {

    @ViewById(resName = "username")
    TextView usernameView;

    public AuditorItemView(Context context) {
        super(context);
    }

    public void bind(AuditorEntity auditor) {
        usernameView.setText(auditor.getUserName());
    }

}
