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
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.business.service.impl.RuleSetServiceImpl;
import com.orange.ocara.data.cache.model.QuestionAnswerEntity;
import com.orange.ocara.data.cache.model.ResponseModel;
import com.orange.ocara.data.cache.model.RuleAnswerEntity;
import com.orange.ocara.data.net.model.IllustrationEntity;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.RuleEntity;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.ui.activity.BrowseIllustrationsActivity_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

import lombok.Value;

/**
 * Adapter for all the QuestionAnswer and RuleAnswer
 */
@EBean
public class QuestionAndRulesAnswersAdapter extends BaseExpandableListAdapter {

    @RootContext
    Activity mActivity;

    @Bean(RuleSetServiceImpl.class)
    RuleSetService mRuleSetService;

    private QuestionAndRulesListener mQuestionAndRulesListener;

    private List<QuestionAnswerEntity> mQuestionAnswerList = null;

    public void update(List<QuestionAnswerEntity> questionAnswer) {
        this.mQuestionAnswerList = questionAnswer;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mQuestionAnswerList == null ? 0 : mQuestionAnswerList.size();
    }

    @Override
    public QuestionAnswerEntity getGroup(int groupPosition) {
        return mQuestionAnswerList.get(groupPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mQuestionAnswerList.get(groupPosition).getRuleAnswers().size();
    }

    @Override
    public RuleAnswerEntity getChild(int groupPosition, int childPosition) {
        return mQuestionAnswerList.get(groupPosition).getRuleAnswers().get(childPosition);
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
            convertView = LayoutInflater.from(mActivity).inflate(com.orange.ocara.R.layout.question_item, parent, false);

            viewHolder = new QuestionViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (QuestionViewHolder) convertView.getTag();
        }

        final QuestionAnswerEntity questionAnswer = getGroup(groupPosition);

        //Response computeResponse = mQuestionAnswerList.computeResponse();
        ResponseModel computeResponse = questionAnswer.getResponse();

        final QuestionEntity question = questionAnswer.getQuestion();

        if (question != null) {
            viewHolder.title.setText(question.getLabel());
        }
        // styling for 'Question item'
        final Drawable icon = mActivity.getResources().getDrawable(isExpanded ? com.orange.ocara.R.drawable.ic_hardware_keyboard_arrow_up : com.orange.ocara.R.drawable.ic_hardware_keyboard_arrow_down);
        viewHolder.title.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

        viewHolder.responseOkButton.setOnClickListener(new QuestionStatusClickListener(questionAnswer, ResponseModel.OK));
        viewHolder.responseOkButton.setSelected(ResponseModel.OK.equals(computeResponse));

        viewHolder.responseDouteButton.setOnClickListener(new QuestionStatusClickListener(questionAnswer, ResponseModel.DOUBT));
        viewHolder.responseDouteButton.setSelected(ResponseModel.DOUBT.equals(computeResponse));


        viewHolder.responseNokButton.setOnClickListener(new QuestionStatusClickListener(questionAnswer, ResponseModel.NOK));
        viewHolder.responseNokButton.setSelected(ResponseModel.NOK.equals(computeResponse));

        viewHolder.responseNotApplicableButton.setOnClickListener(new QuestionStatusClickListener(questionAnswer, ResponseModel.NOT_APPLICABLE));
        viewHolder.responseNotApplicableButton.setSelected(ResponseModel.NOT_APPLICABLE.equals(computeResponse));


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        RuleViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.rule_item, parent, false);

            viewHolder = new RuleViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RuleViewHolder) convertView.getTag();
        }

        final RuleAnswerEntity ruleAnswer = getChild(groupPosition, childPosition);
        ResponseModel computeResponse = ruleAnswer.getResponse();

        viewHolder.title.setText(Html.fromHtml(ruleAnswer.getRule().getLabel()));

        viewHolder.responseOkButton.setOnClickListener(new RuleStatusClickListener(ruleAnswer, ResponseModel.OK));
        viewHolder.responseOkButton.setSelected(ResponseModel.OK.equals(computeResponse));


        viewHolder.responseDouteButton.setOnClickListener(new RuleStatusClickListener(ruleAnswer, ResponseModel.DOUBT));
        viewHolder.responseDouteButton.setSelected(ResponseModel.DOUBT.equals(computeResponse));

        viewHolder.responseNokButton.setOnClickListener(new RuleStatusClickListener(ruleAnswer, ResponseModel.NOK));
        viewHolder.responseNokButton.setSelected(ResponseModel.NOK.equals(computeResponse));

        viewHolder.responseNotApplicableButton.setOnClickListener(new RuleStatusClickListener(ruleAnswer, ResponseModel.NOT_APPLICABLE));
        viewHolder.responseNotApplicableButton.setSelected(ResponseModel.NOT_APPLICABLE.equals(computeResponse));

        final RulesetEntity ruleSet = ruleAnswer.getRuleSet();
        final RuleEntity ruleFromRef = mRuleSetService.getRuleFromRef(ruleSet.getReference(), Integer.valueOf(ruleSet.getVersion()), ruleAnswer.getRule().getReference());

        final List<IllustrationEntity> illustrations = mRuleSetService.getIllutrationsFormRef(ruleSet.getReference(), Integer.valueOf(ruleSet.getVersion()), ruleFromRef.getIllustration());
        if (illustrations.isEmpty()) {
            viewHolder.infoButton.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.infoButton.setVisibility(View.VISIBLE);
            viewHolder.infoButton.setOnClickListener(new RuleInfoClickListener(ruleFromRef));
        }
        return convertView;
    }

    public void setDatas(final QuestionAndRulesListener questionAndRulesListener) {
        mQuestionAndRulesListener = questionAndRulesListener;
    }

    public interface QuestionAndRulesListener {
        void updateRuleAnswer(RuleAnswerEntity ruleAnswer, ResponseModel response);
    }

    private class QuestionViewHolder {
        TextView title;
        View responseOkButton;
        View responseDouteButton;
        View responseNokButton;
        View responseNotApplicableButton;

        QuestionViewHolder(View convertView) {
            title = (TextView) convertView.findViewById(com.orange.ocara.R.id.title);
            responseOkButton = convertView.findViewById(com.orange.ocara.R.id.response_ok_button);
            responseDouteButton = convertView.findViewById(com.orange.ocara.R.id.response_no_answer_button);
            responseDouteButton.setVisibility(View.VISIBLE);
            responseNokButton = convertView.findViewById(com.orange.ocara.R.id.response_nok_button);
            responseNotApplicableButton = convertView.findViewById(com.orange.ocara.R.id.response_notapplicable_button);
            responseNotApplicableButton.setVisibility(View.VISIBLE);
        }
    }

    private class RuleViewHolder {
        TextView title;
        View responseOkButton;
        View responseDouteButton;
        View responseNokButton;
        View responseNotApplicableButton;
        View infoButton;

        RuleViewHolder(View convertView) {
            title = (TextView) convertView.findViewById(com.orange.ocara.R.id.rule_title);
            responseOkButton = convertView.findViewById(com.orange.ocara.R.id.response_ok_button);
            responseDouteButton = convertView.findViewById(com.orange.ocara.R.id.response_no_answer_button);
            responseDouteButton.setVisibility(View.VISIBLE);
            responseNokButton = convertView.findViewById(com.orange.ocara.R.id.response_nok_button);
            responseNotApplicableButton = convertView.findViewById(com.orange.ocara.R.id.response_notapplicable_button);
            responseNotApplicableButton.setVisibility(View.VISIBLE);
            infoButton = convertView.findViewById((com.orange.ocara.R.id.rule_button_info));
        }
    }

    @Value
    private class QuestionStatusClickListener implements View.OnClickListener {
        private QuestionAnswerEntity questionAnswer;
        private ResponseModel response;

        @Override
        public void onClick(View v) {
            questionAnswer.setResponse(response);
            final List<RuleAnswerEntity> ruleAnswers = questionAnswer.getRuleAnswers();

            for (RuleAnswerEntity ruleAnswer : ruleAnswers) {
                mQuestionAndRulesListener.updateRuleAnswer(ruleAnswer, response);
            }
        }
    }

    @Value
    private class RuleStatusClickListener implements View.OnClickListener {
        private RuleAnswerEntity ruleAnswer;
        private ResponseModel response;

        @Override
        public void onClick(View v) {
            mQuestionAndRulesListener.updateRuleAnswer(ruleAnswer, response);
        }
    }

    @Value
    private class RuleInfoClickListener implements View.OnClickListener {
        RuleEntity rule;

        @Override
        public void onClick(View v) {
            final RulesetEntity ruleSeDetail = rule.getRuleSeDetail();
            List<IllustrationEntity> illustrations = mRuleSetService.getIllutrationsFormRef(ruleSeDetail.getReference(), Integer.valueOf(ruleSeDetail.getVersion()) , rule.getIllustration());

            String[] titles = new String[illustrations.size()];
            String[] comments = new String[illustrations.size()];
            String[] images = new String[illustrations.size()];

            for (int i = 0; i < illustrations.size(); i++) {
                if (illustrations.get(i).getComment() != null) {
                    titles[i] = illustrations.get(i).getComment();
                } else {
                    titles[i] = mActivity.getString(R.string.illustration_activity_title, i + 1, rule.getReference());
                }

                comments[i] = illustrations.get(i).getComment();
                if (illustrations.get(i).getImage() != null) {
                    images[i] = illustrations.get(i).getImage();
                }
            }

            BrowseIllustrationsActivity_.intent(mActivity)
                    .titles(titles)
                    .comments(comments)
                    .images(images)
                    .start();
        }
    }
}
