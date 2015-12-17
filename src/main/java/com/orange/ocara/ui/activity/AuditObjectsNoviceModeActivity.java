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
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

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
import java.util.Set;

import javax.inject.Inject;

import lombok.Value;

@EActivity(resName="activity_audit_object_novice_mode")
@OptionsMenu(resName="audit_objects")
/*package*/ class AuditObjectsNoviceModeActivity extends BaseActivityManagingAudit {

    private static final RefreshStrategy STRATEGY = RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.AUDIT_OBJECT).commentsNeeded(true).build();
    private static final RefreshStrategy RULE_REFRESH_STRATEGY = RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.RULE_ANSWER).commentsNeeded(true).build();
    private static final RefreshStrategy QUESTION_REFRESH_STRATEGY = RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.QUESTION_ANSWER).commentsNeeded(true).build();

    private static final int ACTIVITY_COMMENT_REQUEST_CODE = 1;
    private BadgeView badge;
    private Menu menu;
    protected AuditObject auditObject;

    @ViewById(resName="title")
    TextView customTitle;
    @ViewById(resName="subtitle")
    TextView customSubtitle;
    @ViewById(resName="buttonbar_left_button")
    Button leftButton;
    @ViewById(resName="buttonbar_right_button")
    Button rightButton;
    @ViewById(resName="rules_listview")
    ExpandableListView rulesListView;

    @Inject
    Picasso picasso;

    @Extra("selectedObjects")
    long[] selectedObjects;

    int currentSelectedQuestion = 0;

    private List<QuestionAnswer> allQuestions = new ArrayList<QuestionAnswer>();
    private QuestionAndRulesAdapter questionAndRulesAdapter;

    protected Set<RuleAnswer> updatedRules = new HashSet<RuleAnswer>();


    private boolean onConfigChanged = false;
    private boolean first = true;

    @Override
    RefreshStrategy getRefreshStrategy() {
        return STRATEGY;
    }

    @AfterViews
    void setUpRulesListView() {
        questionAndRulesAdapter = new QuestionAndRulesAdapter();

        rulesListView.setAdapter(questionAndRulesAdapter);
    }


    @AfterViews
    void hideLogo() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayUseLogoEnabled(false);
    }

    @Override
    void refreshAudit(RefreshStrategy strategy) {
        if (first) {
            super.refreshAudit(strategy);
            first = false;
        } else {

            badgeNotifierCommentsAuditObject();
        }
    }

    @Override
    void auditRefreshed() {
        super.auditRefreshed();

        computeQuestions(selectedObjects);

    }

    @OptionsItem(resName="action_comment")
    void auditObjectCommentItemClicked() {
        final Long auditId = getCurrentAuditObject().getAudit().getId();

        String attachmentDirectory = modelManager.getAttachmentDirectory(auditId).getAbsolutePath();

        ListAuditObjectCommentActivity_.intent(this)
                .auditObjectId(getCurrentAuditObject().getId())
                .attachmentDirectory(attachmentDirectory)
                .start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean b = super.onCreateOptionsMenu(menu);
        this.menu = menu;
        if (onConfigChanged) {

        new Handler().post(new Runnable() {
                               @java.lang.Override
                               public void run() {
                                   badgeNotifierCommentsAuditObject();
                               }
                           }
        );
        }
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
    void setUpToolbar() {
        super.setUpToolbar();

        final View responseButtonBar = LayoutInflater.from(this).inflate(com.orange.ocara.R.layout.audit_object_toolbar, null);
       responseButtonBar.findViewById(com.orange.ocara.R.id.response_ok_button).setVisibility(View.GONE);

        responseButtonBar.findViewById(com.orange.ocara.R.id.response_nok_button).setVisibility(View.GONE);

        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Gravity.LEFT);
        responseButtonBar.setLayoutParams(lp);

        toolbar.addView(responseButtonBar);
    }


    @Click(resName="buttonbar_left_button")
    void onLeftButton() {

        if (currentSelectedQuestion <= 0) {
            onBackPressed();
        } else {
            saveAuditObjectAndMove(currentSelectedQuestion - 1);
        }
    }

    @Click(resName="buttonbar_right_button")
    void onRightButton() {

        if (currentSelectedQuestion >= allQuestions.size()) {
            return; // blindage
        }

        final AuditObject currentAuditObject = getCurrentAuditObject();



        saveAuditObjectAndMove(currentSelectedQuestion + 1);

        if (currentSelectedQuestion < allQuestions.size()
                && getCurrentAuditObject() != currentAuditObject) {
            // object has changed ==> display a progress status for user
            showAuditObjectProgress(currentAuditObject,   false );
        }
    }



    @Override
    public void onBackPressed() {

        if (updatedRules.isEmpty()) {
            finish();
            return;
        }

        AlertDialog confirmDialog = new OcaraDialogBuilder(this)
                .setTitle(com.orange.ocara.R.string.audit_object_cancel_title) // title
                .setMessage(com.orange.ocara.R.string.audit_object_cancel_message) // message
                .setPositiveButton(com.orange.ocara.R.string.action_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(com.orange.ocara.R.string.action_no, null)
                .create();

        confirmDialog.show();
    }




    @UiThread(propagation = UiThread.Propagation.REUSE)
    public  void badgeNotifierCommentsAuditObject(){

        AuditObject auditObject = getCurrentAuditObject();

        View target = findViewById(com.orange.ocara.R.id.action_comment);
        if(target ==null ||auditObject==null){
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


    @Background
    void computeQuestions(long[] auditObjectIds) {

        List<QuestionAnswer> questionAnswers = new ArrayList<QuestionAnswer>();

        for (long id : auditObjectIds) {
            AuditObject auditObject = modelManager.getAuditObject(id);
            modelManager.refresh(auditObject, RULE_REFRESH_STRATEGY);
            questionAnswers.addAll(auditObject.getQuestionAnswers());

            // add all characteristics questions as well
            for(AuditObject characteristic : auditObject.getChildren()) {
                questionAnswers.addAll(characteristic.getQuestionAnswers());
            }
        }

        questionsComputed(questionAnswers);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void questionsComputed(List<QuestionAnswer> questionAnswers) {

        allQuestions.clear();
        allQuestions.addAll(questionAnswers);

        selectQuestion(currentSelectedQuestion);
    }

    /**
     * To get the current AuditObject based on the current QuestionAnswer
     *
     * @return current AuditObject
     */
    private AuditObject getCurrentAuditObject() {
        QuestionAnswer currentQuestionAnswer = getCurrentQuestionAnswer();
        return getQuestionAuditObject(currentQuestionAnswer);
    }

    private AuditObject getQuestionAuditObject(QuestionAnswer currentQuestionAnswer) {
        AuditObject auditObject = currentQuestionAnswer.getAuditObject();
        while(auditObject.getParent() != null) {
            auditObject = auditObject.getParent();
        }
        return auditObject;
    }

    /**
     * To get the current QuestionAnswer
     *
     * @return current QuestionAnswer
     */
    private QuestionAnswer getCurrentQuestionAnswer() {
        return allQuestions.get(currentSelectedQuestion);
    }

    /**
     * To select a QuestionAnswer from its index.
     *
     * @param questionIndex question index
     */
    private void selectQuestion(int questionIndex) {
        this.currentSelectedQuestion = questionIndex;
        QuestionAnswer questionAnswer = allQuestions.get(currentSelectedQuestion);

        updatedRules.clear();


        // pre-set all rules to 'doubt' value
        Response response = questionAnswer.getResponse();
        if (response.equals(Response.NoAnswer)) {
            for (RuleAnswer ruleAnswer: questionAnswer.getRuleAnswers()) {
                updateRuleAnswer(ruleAnswer, Response.DOUBT);
            }
        }


        refreshQuestion(questionAnswer);

    }

    private void refreshQuestion(QuestionAnswer questionAnswer) {
        questionAndRulesAdapter.update(questionAnswer);

        // Always 1 but to ensure all cases
        for (int i = 0; i < questionAndRulesAdapter.getGroupCount(); i++) {
            rulesListView.expandGroup(i);
        }

        updateActionBar();
        updateButtonBar();
    }


    private void updateActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setDisplayUseLogoEnabled(true);
        updateTitle();
        updateLogo(Uri.parse(getCurrentAuditObject().getObjectDescription().getIcon().toString()));
        badgeNotifierCommentsAuditObject();
    }

    protected void updateTitle() {
        AuditObject auditObject = getCurrentAuditObject();
        setTitle(getString(com.orange.ocara.R.string.audit_object_novice_mode_title, getAuditObjectName(auditObject), currentSelectedQuestion + 1, allQuestions.size()));
        setSubtitle(getAuditObjectCharacteristicsTitle(auditObject));
    }


    private String getAuditObjectCharacteristicsTitle(AuditObject auditObject) {
        String result = "";
        for(AuditObject characteristics : auditObject.getChildren()) {
            if (!result.isEmpty()) {
                result += " ";
            }
            result += "+"+"\u00A0"+characteristics.getName();
        }
        return result;
    }


    protected static String getAuditObjectName(AuditObject auditObject) {
        if (auditObject.getParent() == null) {
            return auditObject.getName();
        }
        return auditObject.getParent().getName() + " / " + auditObject.getName();
    }

    protected void updateButtonBar() {
        updateLeftButton();
        updateRightButton();
    }

    private void updateLeftButton() {

        if (currentSelectedQuestion <= 0) {
            leftButton.setText(com.orange.ocara.R.string.action_back);
        } else {
            leftButton.setText(com.orange.ocara.R.string.audit_object_button_previous);
        }
    }

    private void updateRightButton() {

        if (currentSelectedQuestion >= allQuestions.size() - 1) {
            rightButton.setText(com.orange.ocara.R.string.audit_object_button_terminate);
        } else {
            rightButton.setText(com.orange.ocara.R.string.audit_object_button_next);
        }
    }



    void saveAuditObjectAndMove(int newCurrentSelectedQuestion) {

        final AuditObject auditObject = getCurrentAuditObject();

        // check that audioObject can be saved

        if (auditObject.getResponse().equals(Response.NotApplicable)) {

            // cannot save an object with this status.
            // User MUST set at least one piece of information

            AlertDialog errorDialog = new AlertDialog.Builder(this)
                    .setTitle(com.orange.ocara.R.string.audit_object_badglobaleresponse_title) // title
                    .setMessage(com.orange.ocara.R.string.audit_object_badglobaleresponse_message) // message
                    .setPositiveButton(com.orange.ocara.R.string.action_ok, null)
                    .create();

            errorDialog.show();
            resetAndSelectAuditObject(auditObject);
            return;
        }

        currentSelectedQuestion = newCurrentSelectedQuestion;
        // save audit object
        saveAuditObject(auditObject);
    }



    private void resetAndSelectAuditObject(AuditObject auditObject) {

        // reset all ruleAnswer to 'NoAnswer'
        List<RuleAnswer> ruleAnswers = auditObject.computeAllRuleAnswers();
        for(RuleAnswer r : ruleAnswers) {
            r.updateResponse(Response.NoAnswer);
        }
        // now select the first question of this object
        int indexOfFirstQuestion = 0;
        while(indexOfFirstQuestion < allQuestions.size() ) {
            QuestionAnswer qa = allQuestions.get(indexOfFirstQuestion);
            AuditObject questionAuditObject = getQuestionAuditObject(qa);
            if (auditObject == questionAuditObject) {
                break;
            }
            indexOfFirstQuestion++;
        }
        if (indexOfFirstQuestion == allQuestions.size()) {
            indexOfFirstQuestion = 0;
        }
        selectQuestion(indexOfFirstQuestion);
    }




    @Background
    void saveAuditObject(AuditObject auditObject) {
        modelManager.refresh(auditObject, QUESTION_REFRESH_STRATEGY);
        modelManager.updateAuditObject(auditObject, updatedRules);
        auditObjectSaved(auditObject);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditObjectSaved(AuditObject auditObject) {

        if (currentSelectedQuestion < allQuestions.size()) {
            selectQuestion(currentSelectedQuestion);
        } else {

            // show progress and terminate
            showAuditObjectProgress(auditObject, true );

        }
    }

    private void updateRuleAnswer(RuleAnswer ruleAnswer, Response response) {
        ruleAnswer.updateResponse(response);
        updatedRules.add(ruleAnswer);
    }



    /**
     * Adapter for all the QuestionAnswer and RuleAnswer
     */
    private class QuestionAndRulesAdapter extends BaseExpandableListAdapter {
        private QuestionAnswer questionAnswer = null;
        private final List<RuleAnswer> rules = new ArrayList<RuleAnswer>();

        void update(QuestionAnswer questionAnswer) {
            this.questionAnswer = questionAnswer;

            this.rules.clear();
            this.rules.addAll(questionAnswer.getRuleAnswers());

            notifyDataSetChanged();
        }

        @Override
        public int getGroupCount() {
            return questionAnswer == null ? 0 : 1;
        }

        @Override
        public QuestionAnswer getGroup(int groupPosition) {
            return questionAnswer;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return rules.size();
        }

        @Override
        public RuleAnswer getChild(int groupPosition, int childPosition) {
            return rules.get(childPosition);
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
                convertView = LayoutInflater.from(AuditObjectsNoviceModeActivity.this).inflate(com.orange.ocara.R.layout.question_item, parent, false);

                viewHolder = new QuestionViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (QuestionViewHolder) convertView.getTag();
            }

            final QuestionAnswer questionAnswer = getGroup(groupPosition);

            //Response computeResponse = questionAnswer.computeResponse();
            Response computeResponse = questionAnswer.getResponse();

            viewHolder.title.setText(questionAnswer.getQuestion().getTitle());
            // styling for 'Question item'
            final Drawable icon = getResources().getDrawable(isExpanded ? com.orange.ocara.R.drawable.ic_hardware_keyboard_arrow_up : com.orange.ocara.R.drawable.ic_hardware_keyboard_arrow_down);
            viewHolder.title.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

            viewHolder.responseOkButton.setOnClickListener(new QuestionStatusClickListener(questionAnswer, Response.OK));
            viewHolder.responseOkButton.setSelected(Response.OK.equals(computeResponse));

            viewHolder.responseDouteButton.setOnClickListener(new QuestionStatusClickListener(questionAnswer, Response.DOUBT));
            viewHolder.responseDouteButton.setSelected(Response.DOUBT.equals(computeResponse));

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
                convertView = LayoutInflater.from(AuditObjectsNoviceModeActivity.this).inflate(com.orange.ocara.R.layout.rule_item, parent, false);

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

            viewHolder.responseDouteButton.setOnClickListener(new RuleStatusClickListener(ruleAnswer, Response.DOUBT));
            viewHolder.responseDouteButton.setSelected(Response.DOUBT.equals(computeResponse));

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

        class QuestionViewHolder {
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
                responseNotApplicableButton  = convertView.findViewById(com.orange.ocara.R.id.response_notapplicable_button);
            }
        }

        class RuleViewHolder {
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
                responseNotApplicableButton  = convertView.findViewById(com.orange.ocara.R.id.response_notapplicable_button);
                infoButton = convertView.findViewById((com.orange.ocara.R.id.rule_button_info));
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
                        titles[i] = getString(com.orange.ocara.R.string.illustration_activity_title, i + 1, rule.getId() );
                    }

                    comments[i] = illustrations.get(i).getComment();
                    if (illustrations.get(i).getImage() != null) {
                        images[i] = illustrations.get(i).getImage().toString();
                    }
                }

                IllustrationsActivity_.intent(AuditObjectsNoviceModeActivity.this)
                        .titles(titles)
                        .comments(comments)
                        .images(images)
                        .start();

            }
        }
    }

}
