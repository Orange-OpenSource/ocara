/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.orange.ocara.modelStatic.Illustration;
import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.modelStatic.Question;
import com.orange.ocara.modelStatic.Rule;
import com.orange.ocara.modelStatic.RuleSet;
import com.orange.ocara.ui.activity.IllustrationsActivity_;
import com.orange.ocara.ui.adapter.ItemListAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@EFragment(resName="fragment_object_list_by_rules")
public class ObjectsByRuleSetFragment extends BaseFragment {

    @Setter
    @Getter
    private RuleSet ruleSet;

    @Setter
    private String objectDescriptionId;

    @Inject
    Picasso picasso;

    @ViewById(resName="objects_listview")
    ListView objectsListView;
    @ViewById(resName="rules_listview")
    ExpandableListView rulesListView;


    private RuleSetObjectsAdapter ruleSetObjectsAdapter;
    private QuestionAndRulesAdapter questionAndRulesAdapter;


    @AfterViews
    void setUpObjectsListView() {

        ruleSetObjectsAdapter = new RuleSetObjectsAdapter(ruleSet);

        int position = 0;
        if (objectDescriptionId != null) {
            for (int i = 0; i < ruleSetObjectsAdapter.getCount(); i++) {
                if (ruleSetObjectsAdapter.getItem(i).getName().equals(objectDescriptionId)) {
                    position = i;
                    break;
                }
            }
        }

        objectsListView.setOnItemClickListener(objectsListViewClickListener);
        objectsListView.setAdapter(ruleSetObjectsAdapter);

        objectsListView.setSelection(position);
        objectsListView.setItemChecked(position, true);

        final ObjectDescription objectDescription = ruleSetObjectsAdapter.getItem(position);
        questionAndRulesAdapter = new QuestionAndRulesAdapter(objectDescription);
        rulesListView.setAdapter(questionAndRulesAdapter);
    }

    private final AdapterView.OnItemClickListener objectsListViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ObjectDescription objectDescription = (ObjectDescription) objectsListView.getItemAtPosition(i);

            questionAndRulesAdapter.update(objectDescription);
        }
    };


    /**
     * BaseAdapter.
     */
    private class RuleSetObjectsAdapter extends ItemListAdapter<ObjectDescription> {

        public class DescriptionComparator implements Comparator<ObjectDescription> {
            public int compare(ObjectDescription objectDescription1, ObjectDescription objectDescription2) {
                Collator myCollator = Collator.getInstance();
                return myCollator.compare(objectDescription1.getDescription(), objectDescription2.getDescription());
            }

        }

        @Override
        public void update(Collection<ObjectDescription> objects) {
            this.objects.clear();
            Collections.sort((List<ObjectDescription>) objects, new DescriptionComparator());
            this.objects.addAll(objects);

            notifyDataSetChanged();
        }

        /**
         * Constructor.
         *
         * @param ruleSet RuleSet
         */
        protected RuleSetObjectsAdapter(RuleSet ruleSet) {
            update(ruleSet.getObjectDescriptions());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ObjectViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(com.orange.ocara.R.layout.object_description_item_for_rule, parent, false);

                viewHolder = new ObjectViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ObjectViewHolder) convertView.getTag();
            }

            final ObjectDescription objectDescription = getItem(position);

            // Trigger the download of the URL asynchronously into the image view.
            String name = objectDescription.getDescription();
            String imageUri = objectDescription.getIcon().toString();
            final TextView objectDescriptionTitle = (TextView) convertView.findViewById(com.orange.ocara.R.id.object_description_title);
            objectDescriptionTitle.setText(name);
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    objectDescriptionTitle.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(getResources(), bitmap), null, null, null);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
            final int maxSize = getResources().getDimensionPixelSize(com.orange.ocara.R.dimen.abc_action_button_min_height_material);
            picasso.load(Uri.parse(imageUri)).placeholder(android.R.color.black).resize(maxSize, maxSize).into(target);


            viewHolder.title.setText(name);


            return convertView;
        }
    }

    private class ObjectViewHolder {

        final TextView title;

        ObjectViewHolder(View convertView) {
            this.title = (TextView) convertView.findViewById(com.orange.ocara.R.id.object_description_title);
        }
    }

    /**
     * Adapter for all the Question and Rule
     */
    private class QuestionAndRulesAdapter extends BaseExpandableListAdapter {
        private final List<Question> questions = new ArrayList<Question>();
        private final SparseArray<List<Rule>> rulesByQuestionMap = new SparseArray<List<Rule>>();


        /**
         * Constructor.
         *
         * @param objectDescription ObjectDescription
         */
        public QuestionAndRulesAdapter(ObjectDescription objectDescription) {
            update(objectDescription);

        }

        void update(ObjectDescription objectDescription) {
            this.questions.clear();
            this.questions.addAll(objectDescription.computeAllQuestions());

            this.rulesByQuestionMap.clear();

            for (int i = 0; i < questions.size(); i++) {
                Question question = this.questions.get(i);

                Map<String, Rule> rules = question.getRulesById();
                List<Rule> ruleList = new ArrayList<Rule>(rules.values());

                this.rulesByQuestionMap.put(i, ruleList);
            }

            notifyDataSetChanged();
        }

        @Override
        public int getGroupCount() {
            return questions.size();
        }

        @Override
        public Question getGroup(int groupPosition) {
            return questions.get(groupPosition);
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return rulesByQuestionMap.get(groupPosition).size();
        }

        @Override
        public Rule getChild(int groupPosition, int childPosition) {
            return rulesByQuestionMap.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return (groupPosition * 100000) + childPosition;
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

            QuestionViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(com.orange.ocara.R.layout.question_item_for_rules, parent, false);

                viewHolder = new QuestionViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (QuestionViewHolder) convertView.getTag();
            }

            final Question question = getGroup(groupPosition);

            viewHolder.title.setText(question.getTitle());

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            RuleViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(com.orange.ocara.R.layout.rule_item_for_rules, parent, false);

                viewHolder = new RuleViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (RuleViewHolder) convertView.getTag();
            }

            final Rule rule = getChild(groupPosition, childPosition);
            viewHolder.title.setText(Html.fromHtml(rule.getDescription()));

            final List<Illustration> illustrations = rule.getIllustrations();

            if (illustrations.isEmpty()) {
                viewHolder.infoButton.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.infoButton.setVisibility(View.VISIBLE);
                viewHolder.infoButton.setOnClickListener(new RuleInfoClickListener(rule));
            }

            return convertView;
        }

        class QuestionViewHolder {
            TextView title;

            QuestionViewHolder(View convertView) {
                title = (TextView) convertView.findViewById(com.orange.ocara.R.id.question_title);
            }
        }

        class RuleViewHolder {
            TextView title;
            View infoButton;

            RuleViewHolder(View convertView) {
                title = (TextView) convertView.findViewById(com.orange.ocara.R.id.rule_title);
                infoButton = convertView.findViewById((com.orange.ocara.R.id.rule_button_info));
            }
        }

        @Value
        private class RuleInfoClickListener implements View.OnClickListener {
            Rule rule;


            @Override
            public void onClick(View v) {
                List<Illustration> illustrations = rule.getIllustrations();

                String[] titles = new String[illustrations.size()];
                String[] comments = new String[illustrations.size()];
                String[] images = new String[illustrations.size()];

                for (int i = 0; i < illustrations.size(); i++) {
                    if (illustrations.get(i).getTitle() != null) {
                        titles[i] = illustrations.get(i).getTitle();
                    } else {
                        titles[i] = getString(com.orange.ocara.R.string.illustration_activity_title, i + 1, rule.getId() );
                    }

                    comments[i] = illustrations.get(i).getComment();
                    if (illustrations.get(i).getImage() != null) {
                        images[i] = illustrations.get(i).getImage().toString();
                    }
                }

                IllustrationsActivity_.intent(ObjectsByRuleSetFragment.this)
                        .titles(titles)
                        .comments(comments)
                        .images(images)
                        .start();


            }
        }
    }



}