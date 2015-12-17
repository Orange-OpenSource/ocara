/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.model.AuditObject;
import com.orange.ocara.model.Comment;
import com.orange.ocara.model.QuestionAnswer;
import com.orange.ocara.model.RefreshStrategy;
import com.orange.ocara.model.RuleAnswer;
import com.orange.ocara.modelStatic.Illustration;
import com.orange.ocara.modelStatic.Response;
import com.orange.ocara.modelStatic.Rule;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.orange.ocara.ui.view.BadgeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Value;

@EActivity(resName="activity_audit_object_expert_mode")
@OptionsMenu(resName="audit_objects")
/*package*/ class AuditObjectsExpertModeActivity extends BaseActivityManagingAudit {

    private static final RefreshStrategy STRATEGY = RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.AUDIT_OBJECT).commentsNeeded(true).build();
    private static final RefreshStrategy RULE_REFRESH_STRATEGY = RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.RULE_ANSWER).commentsNeeded(true).build();
    private static final RefreshStrategy QUESTION_REFRESH_STRATEGY = RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.QUESTION_ANSWER).commentsNeeded(true).build();


    @ViewById(resName="title")
    TextView customTitle;
    @ViewById(resName="subtitle")
    TextView customSubtitle;

    @ViewById(resName="buttonbar_left_button")
    Button leftButton;
    @ViewById(resName="buttonbar_right_button")
    Button rightButton;

    @ViewById(resName="questions_by_subject_view")
    ExpandableListView questionsBySubjectView;

    @Extra("selectedObjects")
    long[] selectedObjects;

    int currentSelectedObject = 0;

    private View responseOkButton;
    private View responseNokButton;

    protected AuditObject auditObject;
    protected QuestionsBySubjectAdapter questionsBySubjectAdapter;
    protected Set<RuleAnswer> updatedRules = new HashSet<RuleAnswer>();

    private BadgeView badge;
    private Menu menu;

    private boolean first = true;

    @Override
    RefreshStrategy getRefreshStrategy() {
        return STRATEGY;
    }

    @Override
    void setUpToolbar() {
        super.setUpToolbar();


        final View responseButtonBar = LayoutInflater.from(this).inflate(R.layout.audit_object_toolbar, null);
        responseOkButton = responseButtonBar.findViewById(R.id.response_ok_button);
        responseOkButton.setOnClickListener(new AuditObjectStatusClickListener(Response.OK));

        responseNokButton = responseButtonBar.findViewById(R.id.response_nok_button);
        responseNokButton.setOnClickListener(new AuditObjectStatusClickListener(Response.NOK));


        toolbar.addView(responseButtonBar);
    }

    @AfterViews
    void setUpRules() {

        questionsBySubjectAdapter = new QuestionsBySubjectAdapter();
        questionsBySubjectView.setAdapter(questionsBySubjectAdapter);

    }

    @AfterViews
    void hideLogo() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayUseLogoEnabled(false);
    }

    @Override
    public void setTitle(int id) {
        setTitle(getString(id));
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle("");
        customTitle.setText(title);
    }

    void setSubtitle(String subtitle) {
        if (!TextUtils.isEmpty(subtitle)) {
            customSubtitle.setVisibility(View.VISIBLE);
            customSubtitle.setText(subtitle);
        }
    }

    @Override
    void refreshAudit(RefreshStrategy strategy) {
        if (first) {
            super.refreshAudit(strategy);
            first = false;
        } else {
            auditObject.refresh(QUESTION_REFRESH_STRATEGY);
            badgeNotifierCommentsAuditObject();
        }
    }

    @Override
    void auditRefreshed() {
        super.auditRefreshed();
        selectAuditObject(currentSelectedObject);
    }



    @OptionsItem(resName="action_comment")
    void auditObjectCommentItemClicked() {
        final Long auditId = auditObject.getAudit().getId();

        String attachmentDirectory = modelManager.getAttachmentDirectory(auditId).getAbsolutePath();
        ListAuditObjectCommentActivity_.intent(this)
                .auditObjectId(auditObject.getId())
                .attachmentDirectory(attachmentDirectory).start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        boolean b = super.onCreateOptionsMenu(menu);
        this.menu = menu;

        new Handler().post(new Runnable() {
                               @java.lang.Override
                               public void run() {
                                   badgeNotifierCommentsAuditObject();
                               }
                           }
        );

        return b;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Handler handler = new Handler();


        handler.postDelayed(new Runnable() {
                                @java.lang.Override
                                public void run() {

                                    badgeNotifierCommentsAuditObject();
                                }
                            }, 200
        );
    }


    @Click(resName="buttonbar_left_button")
    void onLeftButton() {

        if (currentSelectedObject <= 0) {
            onBackPressed();
        } else {
            saveAuditObjectAndMove(currentSelectedObject - 1);
        }
    }

    @Click(resName="buttonbar_right_button")
    void onRightButton() {
        saveAuditObjectAndMove(currentSelectedObject + 1);
    }

    @Override
    public void onBackPressed() {

        if (updatedRules.isEmpty()) {
            finish();
            return;
        }

        AlertDialog confirmDialog = new OcaraDialogBuilder(this)
                .setTitle(R.string.audit_object_cancel_title) // title
                .setMessage(R.string.audit_object_cancel_message) // message
                .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.action_no, null)
                .create();

        confirmDialog.show();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    public  void badgeNotifierCommentsAuditObject(){

        View target = findViewById(R.id.action_comment);

        if(target ==null ||auditObject==null ){
            return;
        }

        if (badge != null) {
            badge.hide();
        }

        badge = new BadgeView(this, target);
        List<Comment> nbrComments=auditObject.getComments();
        if (!auditObject.getComments().isEmpty()) {
            badge.setText(String.valueOf(nbrComments.size()));
            badge.setBadgeMargin(0, 4);
            badge.show();
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void selectAuditObject(int index) {
        this.auditObject = modelManager.getAuditObject(selectedObjects[index]);
        refreshAuditObject();
    }

    @Background
    void refreshAuditObject() {
        modelManager.refresh(auditObject, RULE_REFRESH_STRATEGY);
        auditObjectRefreshed();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditObjectRefreshed() {
        updatedRules.clear();

        if (!auditObject.isAudited()) {
            for (RuleAnswer ruleAnswer : auditObject.computeAllRuleAnswers()) {
                updateRuleAnswer(ruleAnswer, Response.OK);
            }
        }

        questionsBySubjectAdapter.update(auditObject);

        for(int i = 0; i < questionsBySubjectAdapter.getGroupCount(); i++) {
            questionsBySubjectView.expandGroup(i);
        }
        updateActionBar();
        updateButtonBar();

    }

    private void updateActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setDisplayUseLogoEnabled(true);
        updateTitle();
        updateLogo(Uri.parse(auditObject.getObjectDescription().getIcon().toString()));

        updateGlobalResponse();
        badgeNotifierCommentsAuditObject();
    }

    private void updateTitle() {

        if (hasSingleAuditObject()) {
            setTitle(getAuditObjectName(auditObject));
        } else {
            setTitle(getString(R.string.audit_object_sequential_title, getAuditObjectName(auditObject), currentSelectedObject + 1, selectedObjects.length));
        }
        setSubtitle(getAuditObjectCharacteristicsTitle(auditObject));
    }


    private boolean hasSingleAuditObject() {
        return selectedObjects.length <= 1;
    }

    private static String getAuditObjectName(AuditObject auditObject) {
        if (auditObject.getParent() == null) {
            return auditObject.getName();
        }
        return auditObject.getParent().getName() + " / " + auditObject.getName();
    }


    private String getAuditObjectCharacteristicsTitle(AuditObject auditObject) {
        String result = "";
        for (AuditObject characteristics : auditObject.getChildren()) {
            if (!result.isEmpty()) {
                result += " ";
            }
            result += "+" + "\u00A0" + characteristics.getName();
        }
        return result;
    }

    protected void updateButtonBar() {
        updateLeftButton();
        updateRightButton();
    }

    private void updateLeftButton() {

        if (hasSingleAuditObject()) {
            leftButton.setText(R.string.audit_object_button_back);
        } else {
            if (currentSelectedObject <= 0) {
                leftButton.setText(R.string.action_back);
            } else {
                leftButton.setText(R.string.audit_object_button_previous);
            }
        }
    }

    private void updateRightButton() {

        if (hasSingleAuditObject()) {
            rightButton.setText(R.string.audit_object_button_validate);
            rightButton.setEnabled(!updatedRules.isEmpty());

        } else {

            if (currentSelectedObject >= selectedObjects.length - 1) {
                rightButton.setText(R.string.audit_object_button_terminate);
            } else {
                rightButton.setText(R.string.audit_object_button_next);
            }

            rightButton.setEnabled(true);
        }
    }

    @Value
    private final class AuditObjectStatusClickListener implements View.OnClickListener {
        private Response response;

        @Override
        public void onClick(View v) {
            List<RuleAnswer> allRuleAnswers = auditObject.computeAllRuleAnswers();
            for (RuleAnswer ruleAnswer : allRuleAnswers) {
                Response ruleResponse = ruleAnswer.getResponse();
                if (ruleResponse != Response.NotApplicable) {
                    updateRuleAnswer(ruleAnswer, response);
                }
            }

            updateGlobalResponse();
            questionsBySubjectAdapter.notifyDataSetChanged();
        }
    }

    void saveAuditObjectAndMove(int newCurrentSelectedObject) {

        // check that audioObject can be saved

        if (auditObject.getResponse().equals(Response.NotApplicable)) {

            // cannot save an object with this status.
            // User MUST set at least one piece of information

            AlertDialog errorDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.audit_object_badglobaleresponse_title) // title
                    .setMessage(R.string.audit_object_badglobaleresponse_message) // message
                    .setPositiveButton(R.string.action_ok, null)
                    .create();

            errorDialog.show();

            return;
        }

        currentSelectedObject = newCurrentSelectedObject;
        // save audit object
        saveAuditObject();
    }


    @Background
    void saveAuditObject() {
        modelManager.refresh(auditObject, QUESTION_REFRESH_STRATEGY);
        modelManager.updateAuditObject(auditObject, updatedRules);
        auditObjectSaved();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditObjectSaved() {


        if (currentSelectedObject < selectedObjects.length) {
            showAuditObjectProgress(auditObject, false);
            selectAuditObject(currentSelectedObject);
        } else {
            showAuditObjectProgress(auditObject, true);
        }
    }

    private void updateRuleAnswer(RuleAnswer ruleAnswer, Response response) {
        ruleAnswer.updateResponse(response);
        updatedRules.add(ruleAnswer);

        updateButtonBar();
    }

    private void updateGlobalResponse() {

        final Response auditObjectResponse = auditObject.getResponse();

        responseOkButton.setSelected(Response.OK.equals(auditObjectResponse));
        responseNokButton.setSelected(Response.NOK.equals(auditObjectResponse));
    }


    /**
     * Adapter for all the QuestionAnswer and RuleAnswer
     */
    private class QuestionsBySubjectAdapter extends BaseExpandableListAdapter {

        private final List<SubjectOrQuestionGroup> groups = new ArrayList<SubjectOrQuestionGroup>();

        private class SubjectOrQuestionGroup {
            String subject;
            QuestionAnswer questionAnswer;

            public boolean isSubject() { return subject != null; }
        }


        public void update(AuditObject auditObject) {
            Map<String, List<QuestionAnswer>> questionAnswersBySubject = auditObject.computeQuestionAnswersBySubject();

            groups.clear();

            for(String subject : questionAnswersBySubject.keySet()) {
                SubjectOrQuestionGroup subjectGroup = new SubjectOrQuestionGroup();
                subjectGroup.subject = subject;
                groups.add(subjectGroup);

                List<QuestionAnswer> questionAnswers = questionAnswersBySubject.get(subject);
                for(QuestionAnswer questionAnswer : questionAnswers ) {
                    SubjectOrQuestionGroup qaGroup = new SubjectOrQuestionGroup();
                    qaGroup.questionAnswer = questionAnswer;
                    groups.add(qaGroup);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public SubjectOrQuestionGroup getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            SubjectOrQuestionGroup group = getGroup(groupPosition);
            if (group.isSubject()) {
                return 0;
            }
            return group.questionAnswer.getRuleAnswers().size();
        }

        @Override
        public RuleAnswer getChild(int groupPosition, int childPosition) {
            SubjectOrQuestionGroup group = getGroup(groupPosition);
            if (group.isSubject()) {
                return null;
            }
            return group.questionAnswer.getRuleAnswers().get(childPosition);
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

            SubjectOrQuestionViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(AuditObjectsExpertModeActivity.this).inflate(R.layout.question_item, parent, false);

                viewHolder = new SubjectOrQuestionViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (SubjectOrQuestionViewHolder) convertView.getTag();
            }

            final SubjectOrQuestionGroup group = getGroup(groupPosition);
            if (group.isSubject()) {

                // styling for 'Subject item'

                viewHolder.title.setText(group.subject);
                viewHolder.buttonBar.setVisibility(View.GONE);
                viewHolder.title.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                viewHolder.title.setTextColor(getResources().getColor(R.color.white));
                viewHolder.title.setTextSize(getResources().getDimension(R.dimen.textSizeMedium));
                convertView.setBackgroundDrawable(getResources().getDrawable(R.color.primaryDark));
                return convertView;
            }

            // styling for 'Question item'
            final Drawable icon = getResources().getDrawable(isExpanded ? R.drawable.ic_hardware_keyboard_arrow_up : R.drawable.ic_hardware_keyboard_arrow_down );
            viewHolder.title.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
            viewHolder.title.setTextColor(getResources().getColor(R.color.text_color_default));
            convertView.setBackgroundDrawable(getResources().getDrawable(R.color.orange_light));




            // bind

            QuestionAnswer questionAnswer = group.questionAnswer;

            // Response computeResponse = questionAnswer.computeResponse();
            Response computeResponse = questionAnswer.getResponse();

            viewHolder.buttonBar.setVisibility(View.VISIBLE);

            viewHolder.title.setText(questionAnswer.getQuestion().getTitle());

            viewHolder.responseOkButton.setOnClickListener(new QuestionStatusClickListener(questionAnswer, Response.OK));
            viewHolder.responseOkButton.setSelected(Response.OK.equals(computeResponse));

            viewHolder.responseNokButton.setOnClickListener(new QuestionStatusClickListener(questionAnswer, Response.NOK));
            viewHolder.responseNokButton.setSelected(Response.NOK.equals(computeResponse));

            viewHolder.responseNotApplicableButton.setVisibility(View.VISIBLE);
            viewHolder.responseNotApplicableButton.setOnClickListener(new QuestionStatusClickListener(questionAnswer, Response.NotApplicable));
            viewHolder.responseNotApplicableButton.setSelected(Response.NotApplicable.equals(computeResponse));

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            RuleViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(AuditObjectsExpertModeActivity.this).inflate(R.layout.rule_item, parent, false);

                viewHolder = new RuleViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (RuleViewHolder) convertView.getTag();
            }

            final RuleAnswer ruleAnswer = getChild(groupPosition, childPosition);

            Response computeResponse = ruleAnswer.getResponse();

            viewHolder.title.setText(Html.fromHtml(ruleAnswer.getRule().getDescription()));

            viewHolder.responseOkButton.setOnClickListener(new RuleStatusClickListener(ruleAnswer, Response.OK));
            viewHolder.responseOkButton.setSelected(Response.OK.equals(computeResponse));

            viewHolder.responseNokButton.setOnClickListener(new RuleStatusClickListener(ruleAnswer, Response.NOK));
            viewHolder.responseNokButton.setSelected(Response.NOK.equals(computeResponse));

            viewHolder.responseNotApplicableButton.setVisibility(View.VISIBLE);
            viewHolder.responseNotApplicableButton.setOnClickListener(new RuleStatusClickListener(ruleAnswer, Response.NotApplicable));
            viewHolder.responseNotApplicableButton.setSelected(Response.NotApplicable.equals(computeResponse));

            final List<Illustration> illustrations = ruleAnswer.getRule().getIllustrations();
            if (illustrations.isEmpty()) {
                viewHolder.infoButton.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.infoButton.setVisibility(View.VISIBLE);
                viewHolder.infoButton.setOnClickListener(new RuleInfoClickListener(ruleAnswer.getRule()));
            }

            return convertView;
        }


        class SubjectOrQuestionViewHolder {
            TextView title;
            View responseOkButton;
            View responseNokButton;
            View responseNotApplicableButton;
            View buttonBar;

            SubjectOrQuestionViewHolder(View convertView) {
                title = (TextView) convertView.findViewById(R.id.title);
                responseOkButton = convertView.findViewById(R.id.response_ok_button);
                responseNokButton = convertView.findViewById(R.id.response_nok_button);
                responseNotApplicableButton = convertView.findViewById(R.id.response_notapplicable_button);
                buttonBar = convertView.findViewById(R.id.response_buttonbar);
            }
        }

        class RuleViewHolder {
            TextView title;
            View responseOkButton;
            View responseNokButton;
            View responseNotApplicableButton;
            View infoButton;

            RuleViewHolder(View convertView) {
                title = (TextView) convertView.findViewById(R.id.rule_title);
                responseOkButton = convertView.findViewById(R.id.response_ok_button);
                responseNokButton = convertView.findViewById(R.id.response_nok_button);
                responseNotApplicableButton = convertView.findViewById(R.id.response_notapplicable_button);
                infoButton = convertView.findViewById((R.id.rule_button_info));
            }
        }

        @Value
        private class QuestionStatusClickListener implements View.OnClickListener {
            private QuestionAnswer questionAnswer;
            private Response response;

            @Override
            public void onClick(View v) {
                for (RuleAnswer ruleAnswer : questionAnswer.getRuleAnswers()) {
                    updateRuleAnswer(ruleAnswer, response);
                }

                updateGlobalResponse();
                notifyDataSetChanged();
            }
        }

        @Value
        private class RuleStatusClickListener implements View.OnClickListener {
            private RuleAnswer ruleAnswer;
            private Response response;

            @Override
            public void onClick(View v) {
                updateRuleAnswer(ruleAnswer, response);

                updateGlobalResponse();
                notifyDataSetChanged();
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
                        titles[i] = getString(R.string.illustration_activity_title, i + 1, rule.getId() );
                    }

                    comments[i] = illustrations.get(i).getComment();
                    if (illustrations.get(i).getImage() != null) {
                        images[i] = illustrations.get(i).getImage().toString();
                    }
                }
                IllustrationsActivity_.intent(AuditObjectsExpertModeActivity.this)
                        .titles(titles)
                        .comments(comments)
                        .images(images)
                        .start();

            }
        }
    }

}
