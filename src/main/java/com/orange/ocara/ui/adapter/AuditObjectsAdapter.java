/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.orange.ocara.model.AuditObject;
import com.orange.ocara.ui.view.AuditObjectItemView;
import com.orange.ocara.ui.view.AuditObjectItemView_;

public class AuditObjectsAdapter extends ItemListAdapter<AuditObject> {

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

        if (convertView == null) {
            auditObjectItemView = AuditObjectItemView_.build(context);
        } else {
            auditObjectItemView = (AuditObjectItemView) convertView;
        }

        auditObjectItemView.bind(getItem(position));
        return auditObjectItemView;
    }
}
