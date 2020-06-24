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
import android.widget.BaseAdapter;

import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.ui.view.RuleSpinnerView;
import com.orange.ocara.ui.view.RuleSpinnerView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by flfb1094 on 14/04/2017.
 */
@EBean
public class RulesetSpinnerAdapter extends BaseAdapter {

    @RootContext
    Context mContext;

    private List<RulesetEntity> items;

    public void update(List<RulesetEntity> list) {
        items = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public RulesetEntity getItem(final int position) {
        return items != null ? items.get(position) : null;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        RuleSpinnerView ruleSpinnerView;

        if (convertView != null && convertView instanceof RuleSpinnerView) {
            ruleSpinnerView = (RuleSpinnerView) convertView;
        } else {
            ruleSpinnerView = RuleSpinnerView_.build(mContext);
        }

        ruleSpinnerView.bind(getItem(position));
        return ruleSpinnerView;
    }
}
