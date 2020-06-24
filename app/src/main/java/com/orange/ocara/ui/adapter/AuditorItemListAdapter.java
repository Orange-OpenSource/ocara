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
import android.widget.Filter;
import android.widget.Filterable;

import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.model.AuditorEntity;
import com.orange.ocara.tools.StringUtils;
import com.orange.ocara.ui.view.AuditorItemView;
import com.orange.ocara.ui.view.AuditorItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adapter for user name
 */
@EBean
public class AuditorItemListAdapter extends ItemListAdapter<AuditorEntity> implements Filterable {

    @RootContext
    Context mContext;

    private ModelManager mModelManager;

    private final Filter filter = new AuditorFilter();


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AuditorItemView auditorItemView;

        if (convertView != null && convertView instanceof AuditorItemView) {
            auditorItemView = (AuditorItemView) convertView;
        } else {
            auditorItemView = AuditorItemView_.build(mContext);
        }

        auditorItemView.bind(getItem(position));
        return auditorItemView;
    }

    @Override
    public Filter getFilter() {
//        if (filter == null) {
//            filter = new AuditorFilter();
//        }

        return filter;
    }

    public void setModelManager(final ModelManager modelManager) {
        mModelManager = modelManager;
    }

    /**
     * Internal user name Filter.
     */
    private class AuditorFilter extends Filter {

        private final Pattern AUTHOR_PATTERN = Pattern.compile("^.{3,}+$");

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                Matcher matcher = AUTHOR_PATTERN.matcher(constraint);
                if (matcher.find()) {
                    List<AuditorEntity> auditors = mModelManager.searchAuditors(StringUtils.trim(matcher.group(0)));
                    results.values = auditors;
                    results.count = auditors.size();
                }
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                update((Collection<AuditorEntity>) results.values);
            }
        }
    }
}
