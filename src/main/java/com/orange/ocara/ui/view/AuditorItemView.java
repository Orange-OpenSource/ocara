/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.view;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.model.Auditor;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(resName="auditor_item")
public class AuditorItemView extends RelativeLayout {

    @ViewById(resName="username")
    TextView usernameView;

    public AuditorItemView(Context context) {
        super(context);
    }

    public void bind(Auditor auditor) {
        usernameView.setText(auditor.getUserName());
    }

}
