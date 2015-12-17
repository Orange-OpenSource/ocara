/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.orange.ocara.BuildConfig;
import com.orange.ocara.R;
import com.orange.ocara.conf.OcaraConfiguration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity()
public class AboutActivity extends BaseActivity {


    @ViewById(resName="version")
    TextView version;


    @AfterViews
    void initVersion() {
        version.setText(getString(R.string.about_version, BuildConfig.VERSION_NAME));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean b = super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_help).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);
        return b;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(OcaraConfiguration.get().getAboutXmlId());

    }


}
