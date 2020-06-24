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

import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.PopupMenu;

import com.orange.ocara.R;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.SortCriteria;
import com.orange.ocara.ui.activity.ListAuditActivity;
import com.orange.ocara.ui.view.AuditItemView;
import com.orange.ocara.ui.view.AuditItemView_;

import java.util.Collection;
import java.util.List;

import lombok.Getter;

import static com.orange.ocara.tools.ListUtils.emptyList;

/**
 * adapter for a {@link List} of {@link AuditEntity}s
 *
 * Implements {@link ItemListAdapter}
 */
public class AuditListAdapter extends ItemListAdapter<AuditEntity> {

    /**
     * a {@link android.content.Context} where the adapter is applied
     */
    private ListAuditActivity mListAuditActivity;

    /**
     * a repository
     */
    private ModelManager mModelManager;
    private Filter filter;
    private AuditListAdapterListener mAuditListAdapterListener;

    @Getter
    private String queryFilter = "";

    @Getter
    private SortCriteria sortCriteria = SortCriteria.Type.STATUS.build();

    /**
     * instantiate
     *
     * @param listAuditActivity a {@link android.content.Context}
     * @param modelManager a repository
     * @param auditListAdapterListener a listener
     */
    public AuditListAdapter(final ListAuditActivity listAuditActivity, ModelManager modelManager, AuditListAdapterListener auditListAdapterListener) {
        mListAuditActivity = listAuditActivity;
        mModelManager = modelManager;
        mAuditListAdapterListener = auditListAdapterListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AuditItemView auditItemView;

        if (convertView != null && convertView instanceof AuditItemView) {
            auditItemView = (AuditItemView) convertView;
        } else {
            auditItemView = AuditItemView_.build(mListAuditActivity);
        }

        final AuditEntity audit = getItem(position);
        if (audit != null) {
            auditItemView.bind(audit);

            if (audit.getId() == mListAuditActivity.getSelectedAuditId()) {
                auditItemView.setBackgroundColor(mListAuditActivity.getResources().getColor(R.color.orange_light));
            } else {
                auditItemView.setBackgroundColor(mListAuditActivity.getResources().getColor(R.color.transparent));
            }

            auditItemView.getExportAuditDocx().setOnClickListener(v -> mListAuditActivity.exportAuditToWord(audit.getId()));

            auditItemView.getMore().setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(mListAuditActivity, v);
                if (AuditEntity.Status.TERMINATED.equals(audit.getStatus())) {
                    popupMenu.inflate(R.menu.list_all_audit_terminated);
                } else {
                    popupMenu.inflate(R.menu.list_all_audit_in_progress);
                }
                popupMenu.setOnMenuItemClickListener(item -> mListAuditActivity.onAuditSubMenuItemClick(item, position));
                popupMenu.show();
            });

        }

        return auditItemView;
    }

    /**
     * updates the content of the adapter with default parameters
     */
    public void refresh() {
        refresh(queryFilter, sortCriteria);
    }

    /**
     * updates the content of the adapter
     * @param sortCriteria a sorting attribute
     */
    public void refresh(SortCriteria sortCriteria) {
        refresh(queryFilter, sortCriteria);
    }

    /**
     * updates the content of the adapter
     *
     * @param query a filtering string
     */
    public void refresh(String query) {
        refresh(query, sortCriteria);
    }

    /**
     * updates the content of the adapter
     *
     * @param queryFilter a filtering string
     * @param sortCriteria a sorting attribute
     */
    public void refresh(String queryFilter, SortCriteria sortCriteria) {
        this.queryFilter = queryFilter;
        this.sortCriteria = sortCriteria;

        getFilter().filter(queryFilter);

        mAuditListAdapterListener.refreshAuditList(objects);
    }

    private Filter getFilter() {
        if (filter == null) {
            filter = new AuditFilter();
        }

        return filter;
    }

    /**
     * Behaviour of a listener on the adapter
     */
    public interface AuditListAdapterListener {

        /**
         * Updates the content with the given elements
         *
         * @param audits a {@link List} of {@link AuditEntity}s
         */
        void refreshAuditList(List<AuditEntity> audits);
    }

    /**
     * Internal user name Filter.
     */
    private class AuditFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                List<AuditEntity> audits = mModelManager.getAllAudits(constraint.toString(), sortCriteria);

                results.values = audits;
                results.count = audits.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            final Collection<AuditEntity> values = (Collection<AuditEntity>) results.values;
            update(values != null ? values : emptyList());
            if (results.count == 0) {
                notifyDataSetInvalidated();
            }
        }
    }

}
