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
import com.orange.ocara.data.cache.model.SiteEntity;
import com.orange.ocara.tools.StringUtils;
import com.orange.ocara.ui.view.SiteItemView;
import com.orange.ocara.ui.view.SiteItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Collection;
import java.util.List;

/**
 * Adapter for site
 */
@EBean
public class SiteItemListAdapter extends ItemListAdapter<SiteEntity> implements Filterable {

    @RootContext
    Context mContext;

    private ModelManager mModelManager;

    private Filter filter;


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SiteItemView siteItemView;

        if (convertView != null && convertView instanceof SiteItemView) {
            siteItemView = (SiteItemView) convertView;
        } else {
            siteItemView = SiteItemView_.build(mContext);
        }

        siteItemView.bind(getItem(position));
        return siteItemView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new SiteFilter();
        }

        return filter;
    }


    public void setModelManager(final ModelManager modelManager) {
        mModelManager = modelManager;
    }

    /**
     * Internal Site Filter.
     */
    private class SiteFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<SiteEntity> sites = mModelManager.searchSites(StringUtils.trim(constraint.toString()));
                results.values = sites;
                results.count = sites.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                update((Collection<SiteEntity>) results.values);
            }
        }
    }

}
