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

import com.orange.ocara.model.Site;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

@EViewGroup(resName="site_item")
public class SiteItemView extends RelativeLayout {

    @ViewById(resName="site_name")
    TextView siteName;
    @ViewById(resName="site_address")
    TextView siteAdress;
    @ViewById(resName="site_noimmo")
    TextView siteNoImmo;

    public SiteItemView(Context context) {
        super(context);
    }

    public void bind(Site site) {

        siteName.setText(site.getName());
        siteAdress.setText(buildAddress(site));
        siteNoImmo.setText(site.getNoImmo());
    }

    private static String buildAddress(Site site) {
        StringBuilder result = new StringBuilder();
        if (StringUtils.isNotBlank(site.getStreet())) {
            result.append(site.getStreet());
        }
        if (site.getCode() != null) {
            result.append(' ').append(site.getCode());
        }
        if (StringUtils.isNotBlank(site.getCity())) {
            result.append(' ').append(site.getCity());
        }
        return result.toString();
    }
}
