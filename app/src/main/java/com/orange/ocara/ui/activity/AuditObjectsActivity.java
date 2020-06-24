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

package com.orange.ocara.ui.activity;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.orange.ocara.R;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.data.cache.model.QuestionAnswerEntity;
import com.orange.ocara.data.cache.model.ResponseModel;
import com.orange.ocara.data.cache.model.RuleAnswerEntity;
import com.orange.ocara.ui.adapter.QuestionAndRulesAnswersAdapter;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.orange.ocara.ui.tools.RefreshStrategy;
import com.orange.ocara.ui.view.BadgeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
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

import timber.log.Timber;

/**
 * Activity dedicated to managing an {@link AuditObjectEntity}
 */
@EActivity(R.layout.activity_audit_object_novice_mode)
@OptionsMenu(R.menu.audit_objects)
public class AuditObjectsActivity extends BaseActivityManagingAudit
        implements QuestionAndRulesAnswersAdapter.QuestionAndRulesListener {

    private static final RefreshStrategy STRATEGY = RefreshStrategy.auditObjectRefreshStrategy();
    private static final RefreshStrategy RULE_REFRESH_STRATEGY = RefreshStrategy.ruleAnswerRefreshStrategy();
    private static final RefreshStrategy QUESTION_REFRESH_STRATEGY = RefreshStrategy.questionAnswerRefreshStrategy();

    @Bean
    QuestionAndRulesAnswersAdapter questionAndRulesAnswersAdapter;

    @ViewById(R.id.title)
    TextView customTitle;

    @ViewById(R.id.subtitle)
    TextView customSubtitle;

    @ViewById(R.id.buttonbar_left_button)
    Button leftButton;

    @ViewById(R.id.buttonbar_right_button)
    Button rightButton;

    @ViewById(R.id.rules_listview)
    ExpandableListView rulesListView;

    @Extra
    boolean editMode = false;

    @Extra
    long[] selectedObjects;

    int currentSelectedQuestion = 0;
    int currentSelectedObject = 0;

    private BadgeView badge;
    private List<QuestionAnswerEntity> allQuestions = new ArrayList<>();
    private boolean onConfigChanged = false;
    private boolean first = true;
    protected AuditObjectEntity auditObject;
    protected Set<RuleAnswerEntity> updatedRules = new HashSet<>();

    protected static String getAuditObjectName(AuditObjectEntity auditObject) {
        if (auditObject.getParent() == null) {
            return auditObject.getName();
        }
        return auditObject.getParent().getName() + " / " + auditObject.getName();
    }

    @Override
    RefreshStrategy getRefreshStrategy() {
        return STRATEGY;
    }

    @AfterViews
    void setUpRulesListView() {
        questionAndRulesAnswersAdapter.setDatas(this);
        rulesListView.setAdapter(questionAndRulesAnswersAdapter);
    }

    @AfterViews
    void hideLogo() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayUseLogoEnabled(false);
        }
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

    @OptionsItem(R.id.action_comment)
    void auditObjectCommentItemClicked() {
        AuditObjectEntity currentAuditObject = getCurrentAuditObject();

        if (currentAuditObject != null) {
            final Long auditId =currentAuditObject.getAudit().getId();

            String attachmentDirectory = modelManager.getAttachmentDirectory(auditId).getAbsolutePath();

            ListAuditObjectCommentActivity_
                    .intent(this)
                    .auditObjectId(currentAuditObject.getId())
                    .attachmentDirectory(attachmentDirectory)
                    .start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean b = super.onCreateOptionsMenu(menu);
        if (onConfigChanged) {

            new Handler().post(this::badgeNotifierCommentsAuditObject);
        }
        return b;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Handler handler = new Handler();


        handler.postDelayed(this::badgeNotifierCommentsAuditObject, 200);
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

        final View responseButtonBar = LayoutInflater.from(this).inflate(R.layout.audit_object_toolbar, null);
        int visibility = View.GONE;

        responseButtonBar.findViewById(R.id.response_ok_button).setVisibility(visibility);
        responseButtonBar.findViewById(R.id.response_no_answer_button).setVisibility(visibility);
        responseButtonBar.findViewById(R.id.response_nok_button).setVisibility(visibility);

        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Gravity.LEFT);
        responseButtonBar.setLayoutParams(lp);

        toolbar.addView(responseButtonBar);
    }

    @Click(R.id.buttonbar_left_button)
    void onLeftButton() {

        Timber.d("Clicking on the left button...");
        if ((currentSelectedQuestion <= 0 && audit.getLevel() == AuditEntity.Level.BEGINNER)
                || (currentSelectedObject <= 0 && audit.getLevel() == AuditEntity.Level.EXPERT)) {
            onBackPressed();
        } else {
            saveAuditObjectAndMove(audit.getLevel() == AuditEntity.Level.BEGINNER ?currentSelectedQuestion - 1: currentSelectedObject -1);
        }
    }

    @Click(R.id.buttonbar_right_button)
    void onRightButton() {

        Timber.d("Clicking on the right button...");
        if (currentSelectedQuestion >= allQuestions.size()) {
            Timber.d("Checking failed...");
            return; // blindage
        }

        Timber.d("Checking succeeded...");

        final AuditObjectEntity currentAuditObject = getCurrentAuditObject();
        if (audit.getLevel() == AuditEntity.Level.BEGINNER) {
            saveAuditObjectAndMove(currentSelectedQuestion + 1);
        } else {
            saveAuditObjectAndMove(currentSelectedObject + 1);
        }

        if (currentSelectedObject < selectedObjects.length
                && audit.getLevel() == AuditEntity.Level.EXPERT) {
            // object has changed ==> display a progress status for user
            showAuditObjectProgress(currentAuditObject, false);
        }

        if (currentSelectedQuestion < allQuestions.size()
                && audit.getLevel() == AuditEntity.Level.BEGINNER
                && getCurrentAuditObject() != currentAuditObject) {
            // object has changed ==> display a progress status for user
            showAuditObjectProgress(currentAuditObject, false);
        }
    }

    @Override
    public void onBackPressed() {

        Timber.d("On back pressed...");
        if (updatedRules.isEmpty()) {
            finishActivity();
            return;
        }

        AlertDialog confirmDialog = new OcaraDialogBuilder(this)
                .setTitle(com.orange.ocara.R.string.audit_object_cancel_title) // title
                .setMessage(com.orange.ocara.R.string.audit_object_cancel_message) // message
                .setPositiveButton(com.orange.ocara.R.string.action_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!editMode) {
                            removeQuestionAnswer();
                        } else {
                            finishActivity();
                        }
                    }
                })
                .setNegativeButton(com.orange.ocara.R.string.action_no, null)
                .create();

        confirmDialog.show();
    }

    @Background
    void removeQuestionAnswer() {

        updatedRules.clear();
        List<QuestionAnswerEntity> questionAnswers = new ArrayList<>();
        if (audit != null && AuditEntity.Level.BEGINNER == audit.getLevel() && !allQuestions.isEmpty()) {
            QuestionAnswerEntity questionAnswer = allQuestions.get(currentSelectedQuestion);
            questionAnswers.add(questionAnswer);
        } else {
            questionAnswers.addAll(allQuestions);
        }

        for (QuestionAnswerEntity questionAnswer : questionAnswers) {
            // pre-set all rules to 'doubt' value
            for (RuleAnswerEntity ruleAnswer : questionAnswer.getRuleAnswers()) {
                updateRuleAnswer(ruleAnswer, ResponseModel.NO_ANSWER);
            }
        }

        finishActivity();
    }

    @UiThread
    void finishActivity() {
        finish();
    }


    @UiThread(propagation = UiThread.Propagation.REUSE)
    public void badgeNotifierCommentsAuditObject() {

        AuditObjectEntity currentAuditObject = getCurrentAuditObject();

        View target = findViewById(com.orange.ocara.R.id.action_comment);
        if (target == null || currentAuditObject == null) {
            return;
        }
        if (badge != null) {
            badge.hide();
        }
        badge = new BadgeView(this, target);
        List<CommentEntity> nbrComments = currentAuditObject.getComments();
        if (!currentAuditObject.getComments().isEmpty()) {
            badge.setText(String.valueOf(nbrComments.size()));
            badge.setBadgeMargin(0, 4);
            badge.show();
        }
    }


    @Background
    void computeQuestions(long[] auditObjectIds) {

        for (Long id : auditObjectIds) {
            AuditObjectEntity auditObject = modelManager.getAuditObject(id);
            modelManager.refresh(auditObject, RULE_REFRESH_STRATEGY);

            final List<QuestionAnswerEntity> questionAnswers1 = auditObject.getQuestionAnswers();
            for (QuestionAnswerEntity questionAnswer : questionAnswers1) {
                questionAnswer.save();
            }

            // add all characteristics questions as well
            for (AuditObjectEntity characteristic : auditObject.getChildren()) {
                for (QuestionAnswerEntity questionAnswer : characteristic.getQuestionAnswers()) {
                    questionAnswer.save();
                }
            }
        }

        List<QuestionAnswerEntity> questionAnswers;
        if (audit.getLevel() == AuditEntity.Level.BEGINNER) {
            questionAnswers = new ArrayList<>();
            for (int i = 0; i < selectedObjects.length; i++) {
                questionAnswers.addAll(getQuestionCurrentObject(i));
            }
        } else {
            questionAnswers = getQuestionCurrentObject(currentSelectedObject);
        }

        Timber.d("computeQuestions Finish");
        questionsComputed(questionAnswers);

    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void questionsComputed(List<QuestionAnswerEntity> questionAnswers) {
        allQuestions.clear();
        allQuestions.addAll(questionAnswers);
        selectQuestion(currentSelectedQuestion);

    }

    /**
     * To findAll the current AuditObject based on the current QuestionAnswer
     *
     * @return current AuditObject
     */
    private AuditObjectEntity getCurrentAuditObject() {
        QuestionAnswerEntity currentQuestionAnswer = getCurrentQuestionAnswer();
        return currentQuestionAnswer != null ? getQuestionAuditObject(currentQuestionAnswer) : null;
    }

    private AuditObjectEntity getQuestionAuditObject(QuestionAnswerEntity currentQuestionAnswer) {
        AuditObjectEntity questionAnswerAuditObject = currentQuestionAnswer != null ? currentQuestionAnswer.getAuditObject() : null;
        if (questionAnswerAuditObject != null) {
            while (questionAnswerAuditObject.getParent() != null) {
                questionAnswerAuditObject = questionAnswerAuditObject.getParent();
            }
        }
        return questionAnswerAuditObject;
    }

    /**
     * To findAll the current QuestionAnswer
     *
     * @return current QuestionAnswer
     */
    private QuestionAnswerEntity getCurrentQuestionAnswer() {
        return allQuestions.isEmpty() ? null : allQuestions.get(currentSelectedQuestion);
    }

    /**
     * To select a QuestionAnswer from its index.
     *
     * @param questionIndex question index
     */
    private void selectQuestion(int questionIndex) {
        this.currentSelectedQuestion = questionIndex;


        updatedRules.clear();
        List<QuestionAnswerEntity> questionAnswers = new ArrayList<>();
        if (audit != null && AuditEntity.Level.BEGINNER == audit.getLevel() && !allQuestions.isEmpty()) {
            QuestionAnswerEntity questionAnswer = allQuestions.get(currentSelectedQuestion);
            questionAnswers.add(questionAnswer);
        } else {
            questionAnswers.addAll(allQuestions);
        }

        for (QuestionAnswerEntity questionAnswer : questionAnswers) {
            // pre-set all rules to 'doubt' value
            ResponseModel response = questionAnswer.getResponse();
            if (ResponseModel.NO_ANSWER.equals(response) && AuditEntity.Level.EXPERT != audit.getLevel()) {
                for (RuleAnswerEntity ruleAnswer : questionAnswer.getRuleAnswers()) {
                    updateRuleAnswer(ruleAnswer, ResponseModel.DOUBT);
                }
            } else if (ResponseModel.NO_ANSWER.equals(response) && AuditEntity.Level.EXPERT == audit.getLevel()) {
                for (RuleAnswerEntity ruleAnswer : questionAnswer.getRuleAnswers()) {
                    updateRuleAnswer(ruleAnswer, ResponseModel.OK);
                }
            }
        }

        refreshQuestion(questionAnswers);
    }

    private void refreshQuestion(List<QuestionAnswerEntity> questionAnswers) {
        questionAndRulesAnswersAdapter.update(questionAnswers);

        // Always 1 but to ensure all cases
        for (int i = 0; i < questionAndRulesAnswersAdapter.getGroupCount(); i++) {
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
        final AuditObjectEntity currentAuditObject = audit.getLevel() == AuditEntity.Level.BEGINNER ?getCurrentAuditObject():    modelManager.getAuditObject(selectedObjects[currentSelectedObject]);
        modelManager.refresh(currentAuditObject, RULE_REFRESH_STRATEGY);
        if (currentAuditObject != null && currentAuditObject.getObjectDescription() != null) {
            updateLogo(currentAuditObject.getObjectDescription().getIcon());
        }
        badgeNotifierCommentsAuditObject();
    }

    protected void updateTitle() {

        AuditObjectEntity currentAuditObject;
        if (audit.getLevel() == AuditEntity.Level.BEGINNER) {
            currentAuditObject = getCurrentAuditObject();
            if (currentAuditObject != null) {
                setTitle(getString(R.string.audit_object_novice_mode_title, getAuditObjectName(currentAuditObject), currentSelectedQuestion + 1, allQuestions.size()));
                setSubtitle(getAuditObjectCharacteristicsTitle(currentAuditObject));
            }
        } else {
            currentAuditObject = modelManager.getAuditObject(selectedObjects[currentSelectedObject]);
            modelManager.refresh(currentAuditObject, RULE_REFRESH_STRATEGY);
            if (currentAuditObject != null) {
                setTitle(getString(R.string.audit_object_expert_mode_title, getAuditObjectName(currentAuditObject), currentSelectedObject+1 , selectedObjects.length));
                setSubtitle(getAuditObjectCharacteristicsTitle(currentAuditObject));
            }
        }
    }


    private String getAuditObjectCharacteristicsTitle(AuditObjectEntity auditObject) {
        String result = "";
        for (AuditObjectEntity characteristics : auditObject.getChildren()) {
            if (!result.isEmpty()) {
                result += " ";
            }
            result += "+" + "\u00A0" + characteristics.getName();
        }
        return result;
    }

    @UiThread
    protected void updateButtonBar() {
        updateLeftButton();
        updateRightButton();
    }

    private void updateLeftButton() {

        if (currentSelectedQuestion <= 0 || (audit.getLevel() == AuditEntity.Level.EXPERT && currentSelectedObject <= 0)) {
            leftButton.setText(R.string.action_back);
        } else {
            leftButton.setText(R.string.audit_object_button_previous);
        }
    }

    private void updateRightButton() {

        Timber.d(" mode %s curentOb %d len %d", audit.getLevel(), currentSelectedObject, selectedObjects.length-1);

        if ((audit.getLevel() == AuditEntity.Level.BEGINNER &&currentSelectedQuestion >= allQuestions.size() - 1 )||
                (audit.getLevel() == AuditEntity.Level.EXPERT && currentSelectedObject >= selectedObjects.length - 1)) {
            rightButton.setText(com.orange.ocara.R.string.audit_object_button_terminate);
        } else {
            rightButton.setText(com.orange.ocara.R.string.audit_object_button_next);
        }
    }

    void saveAuditObjectAndMove(final int newCurrentSelectedQuestion) {

        final AuditObjectEntity currentAuditObject = getCurrentAuditObject();

        // check that audioObject can be saved

        if (currentAuditObject != null && currentAuditObject.getResponse().equals(ResponseModel.NOT_APPLICABLE)) {

            Timber.d("Could not move to next question %d", newCurrentSelectedQuestion);

            // cannot save an object with this status.
            // User MUST set at least one piece of information

            AlertDialog errorDialog = new AlertDialog.Builder(this)
                    .setTitle(com.orange.ocara.R.string.audit_object_badglobaleresponse_title) // title
                    .setMessage(com.orange.ocara.R.string.audit_object_badglobaleresponse_message) // message
                    .setPositiveButton(com.orange.ocara.R.string.action_ok, null)
                    .create();

            errorDialog.show();
            resetAndSelectAuditObject(currentAuditObject);
        } else {
            if (audit.getLevel() == AuditEntity.Level.BEGINNER) {
                currentSelectedQuestion = newCurrentSelectedQuestion;
            } else {
                currentSelectedObject = newCurrentSelectedQuestion;
            }
            // save audit object
            Timber.d("Save audit and move to next question %d", newCurrentSelectedQuestion);
            saveAuditObject(currentAuditObject);
        }

    }

    private void resetAndSelectAuditObject(AuditObjectEntity auditObject) {

        // reset all ruleAnswer to 'NO_ANSWER'
        List<RuleAnswerEntity> ruleAnswers = auditObject.computeAllRuleAnswers();
        for (RuleAnswerEntity r : ruleAnswers) {
            r.updateResponse(ResponseModel.NO_ANSWER);
        }
        // now select the first question of this object
        int indexOfFirstQuestion = 0;
        while (indexOfFirstQuestion < allQuestions.size()) {
            QuestionAnswerEntity qa = allQuestions.get(indexOfFirstQuestion);
            AuditObjectEntity questionAuditObject = getQuestionAuditObject(qa);
            if (auditObject.equals(questionAuditObject)) {
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
    void saveAuditObject(AuditObjectEntity auditObject) {
        modelManager.refresh(auditObject, QUESTION_REFRESH_STRATEGY);
        modelManager.updateAuditObject(auditObject, updatedRules);
        auditObjectSaved(auditObject);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditObjectSaved(AuditObjectEntity auditObject) {

        if (currentSelectedQuestion < allQuestions.size() && auditObject.getAudit().getLevel() == AuditEntity.Level.BEGINNER) {
            showAuditObjectProgress(auditObject, false);
            selectQuestion(currentSelectedQuestion);
        } else if (currentSelectedObject < selectedObjects.length && auditObject.getAudit().getLevel() == AuditEntity.Level.EXPERT) {
            showAuditObjectProgress(auditObject, false);
            selectAuditObject(currentSelectedObject);

        } else {
            // show progress and terminate
            showAuditObjectProgress(auditObject, true);
        }
    }


    @UiThread(propagation = UiThread.Propagation.REUSE)
    void selectAuditObject(int index) {
        currentSelectedObject = index;
        questionsComputed(getQuestionCurrentObject(currentSelectedObject));
    }


    @Override
    @Background
    public void updateRuleAnswer(RuleAnswerEntity ruleAnswer, ResponseModel response) {
        ruleAnswer.updateResponse(response);
        updatedRules.add(ruleAnswer);
        updateButtonBar();
        updateAdapter();
    }

    @UiThread
    void updateAdapter() {
        questionAndRulesAnswersAdapter.notifyDataSetChanged();
    }


    List<QuestionAnswerEntity> getQuestionCurrentObject(int position) {
        List<QuestionAnswerEntity> questionAnswers = new ArrayList<>();

        AuditObjectEntity selectedAuditObject = modelManager.getAuditObject(selectedObjects[position]);
        modelManager.refresh(selectedAuditObject, RULE_REFRESH_STRATEGY);


        final List<QuestionAnswerEntity> questionAnswers1 = selectedAuditObject.getQuestionAnswers();
        questionAnswers.addAll(questionAnswers1);

        // add all characteristics questions as well
        for (AuditObjectEntity characteristic : selectedAuditObject.getChildren()) {
            questionAnswers.addAll(characteristic.getQuestionAnswers());
        }
        return questionAnswers;
    }
}
