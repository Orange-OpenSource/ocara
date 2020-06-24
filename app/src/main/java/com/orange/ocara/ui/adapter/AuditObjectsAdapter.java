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

package com.orange.ocara.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.ui.view.AuditObjectItemView;
import com.orange.ocara.ui.view.AuditObjectItemView_;

public class AuditObjectsAdapter extends ItemListAdapter<AuditObjectEntity> {

    private final Context context;

    /**
     * Constructor.
     *
     * @param context Context
     */
    public AuditObjectsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AuditObjectItemView auditObjectItemView;

        if (convertView != null && convertView instanceof AuditObjectItemView) {
            auditObjectItemView = (AuditObjectItemView) convertView;
        } else {
            auditObjectItemView = AuditObjectItemView_.build(context);
        }

        auditObjectItemView.bind(getItem(position));
        return auditObjectItemView;
    }
}
