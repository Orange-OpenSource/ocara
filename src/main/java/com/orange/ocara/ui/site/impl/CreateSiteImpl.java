/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.site.impl;

import android.content.Context;

import com.orange.ocara.ui.activity.CreateSiteActivity_;
import com.orange.ocara.ui.site.CreateSite;

public class CreateSiteImpl implements CreateSite {

    private static final int CREATE_SITE_REQUEST_CODE = 1;

    @Override
    public void createSite(Context context) {
        CreateSiteActivity_.intent(context).startForResult(CREATE_SITE_REQUEST_CODE);
    }
}
