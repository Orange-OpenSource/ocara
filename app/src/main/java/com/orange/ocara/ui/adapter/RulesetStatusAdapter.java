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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.orange.ocara.R;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.data.net.model.Ruleset;
import com.orange.ocara.tools.StringUtils;

import java.util.List;

import timber.log.Timber;

import static com.orange.ocara.tools.ListUtils.newArrayList;

/**
 * Bridge between a {@link android.widget.Spinner} and its data that are of type {@link Ruleset}
 */
public class RulesetStatusAdapter extends BaseAdapter {

    private Context mContext;

    private List<RulesetModel> rulesets;

    /**
     * Default and only constructor
     *
     * @param context the {@link Context} where to apply the instance
     * @param ruleSets the content
     */
    public RulesetStatusAdapter(Context context, List<RulesetModel> ruleSets) {
        mContext = context;
        rulesets = newArrayList(ruleSets);
    }

    /**
     * Retrieves a View that displays the data at the specified position in the data set.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(mContext).inflate(R.layout.rulesset_spinner_dropdown_item, parent, false);
            final ViewHolder viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder) v.getTag();

        Timber.d("Position=%d;Count=%d", position, getCount());
        if (position == getCount()) {
            viewHolder.bindNothing();
        } else {
            viewHolder.bind(getItem(position));
        }

        return v;
    }

    /**
     * Retrieves a {@link android.view.View} that displays in the drop down popup the data at the
     * specified position in the data set. Creates or Updates the {@link ViewHolder} dedicated to
     * the position.
     *
     * @param position    index of the item whose view we want.
     * @param convertView the old view to reuse, if possible.
     * @param parent      the parent that this view will eventually be attached to
     * @return a {@link android.view.View} corresponding to the data at the specified position.
     * <p>
     * see {@link android.widget.SpinnerAdapter#getDropDownView(int, View, ViewGroup)}
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(mContext).inflate(R.layout.rulesset_spinner_dropdown_item, parent, false);

            ViewHolder viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder) v.getTag();
        viewHolder.bind(getItem(position));

        return v;
    }

    /**
     * Retrieves how many items are in the data set represented by this Adapter.
     *
     * @return the count of items.
     */
    @Override
    public int getCount() {
        return rulesets.size();
    }

    /**
     * Retrieves the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position. If position does not exist, returns an invalid {@link RulesetModel}
     */
    @Override
    public RulesetModel getItem(final int position) {
        RulesetModel item = RulesetModel.emptyRuleSetInfo();
        if (position >= 0 && position < getCount()) {
            item = rulesets.get(position);
        }
        return item;
    }

    /**
     * Retrieves a {@link RulesetModel} based on its versionName (reference + "_" + version).
     *
     * @param versionName unique identifier of an item
     * @return the {@link RulesetModel} that matches the input. Otherwise, returns an invalid {@link RulesetModel}.
     */
    public RulesetModel getItem(final String versionName) {
        RulesetModel output = RulesetModel.emptyRuleSetInfo();
        for(int i = 0; i < getCount(); i++) {
            RulesetModel item = getItem(i);
            if (item.getVersionName().equals(versionName)) {
                output = item;
            }
        }
        return output;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    /**
     * Retrieves the position of an item
     *
     * @param ruleSetInfo1 the reference
     * @return the position of the item that match the input
     */
    public int getPosition(final RulesetModel ruleSetInfo1) {
        for (RulesetModel element : rulesets) {
            if (element.getReference().equals(ruleSetInfo1.getReference())) {
                return rulesets.indexOf(element);
            }
        }
        return -1;
    }

    /**
     * Describes an item view within the {@link android.widget.Spinner}
     */
    private class ViewHolder {
        private final TextView mName;
        private final TextView mStatus;

        ViewHolder(View view) {
            mName = view.findViewById(R.id.ruleset_name);
            mStatus = view.findViewById(R.id.ruleset_status);
        }

        /**
         * Updates the ViewHolder according to the given {@link RulesetModel}
         *
         * @param item the data item
         */
        void bind(RulesetModel item) {
            String name = mContext
                    .getString(R.string.audit_item_ruleset_format, item.getType(), item.getVersion());
            mName.setText(name);

            int drawableId;
            int statusValue;
            switch (item.getStat()) {
                case OFFLINE:
                    drawableId = R.drawable.ruleset_offline_ok_check;
                    statusValue = R.string.ruleset_offline;
                    break;
                case ONLINE:
                    drawableId = R.drawable.ruleset_online;
                    statusValue = R.string.ruleset_online;
                    break;
                case OFFLINE_WITH_NEW_VERSION:
                    drawableId = R.drawable.ruleset_offline_update;
                    statusValue = R.string.ruleset_should_be_update;
                    break;
                default:
                    throw new IllegalStateException("Should not findAll here");
            }
            Timber.d("Change the viewHolder to %s with status %s", name, statusValue);

            mStatus.setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0);
            mStatus.setText(statusValue);
        }

        void bindNothing() {
            mName.setText(StringUtils.EMPTY);
            mName.setHint(mContext.getString(R.string.edit_audit_rule_choice_hint));
        }
    }
}
