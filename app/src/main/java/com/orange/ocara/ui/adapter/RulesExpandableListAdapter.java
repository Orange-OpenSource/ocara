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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.business.model.RuleGroupModel;
import com.orange.ocara.business.model.RuleModel;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.RuleEntity;
import com.orange.ocara.ui.activity.BrowseExplanationsActivity_;
import com.orange.ocara.ui.model.OrderedRuleGroupUiModel;
import com.orange.ocara.ui.model.OrderedRuleUiModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bridge between a list of {@link QuestionEntity}s and their displaying in the view .
 * <p>
 * Each question has an expandable list of {@link RuleEntity}s.
 * <p>
 * Implementation of {@link BaseExpandableListAdapter}
 */
public class RulesExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;

    public RulesExpandableListAdapter(Context context) {
        this.context = context;
    }

    /**
     * Coefficient used in arithmetic operations.
     * Identified as long, in order to prevent unexpected results.
     */
    private static final long ID_FACTOR = 100000L;

    private List<OrderedRuleGroupUiModel> groups = new ArrayList<>();

    private SparseArray<List<OrderedRuleUiModel>> children = new SparseArray<>();

    /**
     * Updates the content of the adapter
     *
     * @param groups a {@link List} of {@link RuleGroupModel}s
     * @param rules  a {@link List} of {@link RuleModel}s
     */
    public void update(List<RuleGroupModel> groups, List<RuleModel> rules) {
        this.groups = new ArrayList<>();
        children = new SparseArray<>();
        for (int i = 0; i < groups.size(); i++) {
            RuleGroupModel item = groups.get(i);
            this.groups.add(new OrderedRuleGroupUiModel(item));
            children.put(i, new ArrayList<>());

            for (RuleModel model : rules) {
                if (model.getGroupId() == item.getId()) {
                    children.get(i).add(new OrderedRuleUiModel(model));
                }
            }
        }

        notifyDataSetChanged();
    }

    public void reset() {
        update(Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public OrderedRuleGroupUiModel getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(groupPosition).size();
    }

    @Override
    public OrderedRuleUiModel getChild(int groupPosition, int childPosition) {
        return children.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (groupPosition * ID_FACTOR) + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        RuleGroupViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.question_item_for_rules, parent, false);

            viewHolder = new RuleGroupViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RuleGroupViewHolder) convertView.getTag();
        }

        viewHolder.bind(getGroup(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        RuleViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.rule_item_for_rules, parent, false);

            viewHolder = new RuleViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RuleViewHolder) convertView.getTag();
        }

        viewHolder.bind(getChild(groupPosition, childPosition));

        return convertView;
    }

    /**
     * a ViewHolder for {@link OrderedRuleGroupUiModel}s
     */
    private class RuleGroupViewHolder {
        TextView title;

        RuleGroupViewHolder(View convertView) {
            title = convertView.findViewById(R.id.question_title);
        }

        void bind(OrderedRuleGroupUiModel q) {
            title.setText(q.getGroupText());
        }
    }

    /**
     * a ViewHolder for {@link OrderedRuleUiModel}s
     */
    private class RuleViewHolder {
        TextView title;
        View infoButton;

        RuleViewHolder(View convertView) {
            title = convertView.findViewById(R.id.rule_title);
            infoButton = convertView.findViewById(R.id.rule_button_info);
        }

        void bind(OrderedRuleUiModel r) {
            title.setText(r.getRuleText());

            if (!r.isIllustrated()) {
                infoButton.setVisibility(View.GONE);
            } else {
                infoButton.setVisibility(View.VISIBLE);
                infoButton.setOnClickListener(new RuleInfoClickListener(r));
            }
        }
    }

    /**
     * Callback that should be invoked when a uiModel is clicked
     * <p>
     * Implementation of {@link View.OnClickListener}
     */
    private class RuleInfoClickListener implements View.OnClickListener {

        private final OrderedRuleUiModel uiModel;

        /**
         * Instantiate
         *
         * @param rule a {@link RuleEntity}
         */
        RuleInfoClickListener(OrderedRuleUiModel rule) {
            this.uiModel = rule;
        }

        @Override
        public void onClick(View v) {

            BrowseExplanationsActivity_
                    .intent(context)
                    .ruleId(uiModel.getRuleId())
                    .start();
        }
    }



}
