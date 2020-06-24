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

import com.orange.ocara.data.cache.model.SiteEntity;
import com.orange.ocara.tools.StringUtils;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(resName = "site_item")
public class SiteItemView extends RelativeLayout {

    @ViewById(resName = "site_name")
    TextView siteName;
    @ViewById(resName = "site_address")
    TextView siteAdress;
    @ViewById(resName = "site_noimmo")
    TextView siteNoImmo;

    public SiteItemView(Context context) {
        super(context);
    }

    private static String buildAddress(SiteEntity site) {
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

    public void bind(SiteEntity site) {

        siteName.setText(site.getName());
        siteAdress.setText(buildAddress(site));
        siteNoImmo.setText(site.getNoImmo());
    }
}
