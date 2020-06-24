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

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.data.net.model.ProfileTypeEntity;
import com.orange.ocara.ui.model.HandicapAggregateUiModel;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.File;

@EBean
public class ChartChoiceAdapter extends BaseAdapter {

    @RootContext
    Activity mActivity;

    private HandicapAggregateUiModel mHandicapAggregator;

    @Override
    public int getCount() {
        return mHandicapAggregator.getHandicapRef().size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.handicap_type_choice, null, false);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.handicap_type_icon);
        TextView title = (TextView) convertView.findViewById(R.id.handicap_type_title);

        if (position == 0) {
            icon.setImageResource(R.drawable.ic_handicap_all);
            title.setText(R.string.audit_results_table_all_handicap);
        } else {
            ProfileTypeEntity handicap = mHandicapAggregator.getHandicap(position - 1);
            final String path = mActivity.getExternalCacheDir() + File.separator + handicap.getIcon();
            File iconFile = new File(path);
            Picasso.with(mActivity).load(iconFile).placeholder(android.R.color.black).into(icon);
            title.setText(handicap.getName());
        }

        return convertView;
    }

    public void setHandicapAggregator(final HandicapAggregateUiModel handicapAggregator) {
        mHandicapAggregator = handicapAggregator;
        notifyDataSetChanged();
    }
}
