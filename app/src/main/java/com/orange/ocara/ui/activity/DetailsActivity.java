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

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.orange.ocara.R;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.business.service.impl.RuleSetServiceImpl;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.db.ModelManagerImpl;
import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.data.cache.model.ResponseModel;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.data.net.model.IllustrationEntity;
import com.orange.ocara.ui.adapter.CommentListAdapter;
import com.orange.ocara.ui.dialog.AudioPlayerDialog;
import com.orange.ocara.ui.dialog.SelectAuditObjectCharacteristicsDialogBuilder;
import com.orange.ocara.ui.tools.RefreshStrategy;
import com.orange.ocara.ui.view.CommentItemView;
import com.orange.ocara.ui.view.CommentItemView_;
import com.orange.ocara.ui.view.IllustrationCommentItemView;
import com.orange.ocara.ui.view.IllustrationCommentItemView_;

import org.androidannotations.annotations.AfterExtras;
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
import java.util.List;

import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.orange.ocara.R.id.illustrations;

@EActivity(R.layout.activity_details)
@OptionsMenu(R.menu.object_details)
        /*package*/ class DetailsActivity extends BaseActivityManagingAudit implements SelectAuditObjectCharacteristicsDialogBuilder.UpdateAuditObjectCharacteristicsListener {

    private static final int ACTIVITY_COMMENT_REQUEST_CODE = 1;
    @Extra("auditObjectId")
    Long auditObjectId;
    AuditObjectEntity auditObject;
    private final AdapterView.OnItemClickListener auditOjectCommentListViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<CommentEntity> comments = auditObject.getComments();
            CommentEntity comment = (CommentEntity) parent.getAdapter().getItem(position);
            final String auditObjectName = auditObject.getName();
            final String auditObjectIcon = auditObject.getObjectDescription().getIcon();
            onCommentClicked(auditObjectIcon, auditObjectName, comments, comment, view);
        }
    };
    List<CommentEntity> comments;
    @ViewById(R.id.scrollview_container)
    ScrollView scrollContainer;
    @ViewById(R.id.title)
    TextView customTitle;
    @ViewById(R.id.subtitle)
    TextView customSubtitle;
    @ViewById(R.id.audit_object_definitions_layout)
    ViewGroup auditObjectDefinitionLayout;
    @ViewById(R.id.text_definition)
    TextView textDefinition;
    @ViewById(R.id.audit_object_status_layout)
    ViewGroup auditObjectStatusLayout;
    @ViewById(R.id.text_status)
    TextView textStatus;
    @ViewById(R.id.button_status_accessibility)
    TextView buttonStatusAccessibility;
    @ViewById(R.id.title_characteristic)
    TextView titleCharacteristic;
    @ViewById(illustrations)
    ImageView illustration;
    @ViewById(R.id.text_characteristic)
    TextView textCharacteristic;
    @ViewById(R.id.audit_object_characteristic_layout)
    ViewGroup auditObjectCharacteristicLayout;
    @ViewById(R.id.audit_object_illustrations_layout)
    ViewGroup auditObjectIllustrationsLayout;
    @ViewById(R.id.audit_object_comments_layout)
    ViewGroup auditObjectCommentsLayout;
    @ViewById(R.id.illustration_comments_section)
    ViewGroup illustrationsCommentsView;
    @ViewById(R.id.comments_section)
    ViewGroup commentsView;
    @Bean(ModelManagerImpl.class)
    ModelManager modelManager;
    @Bean(RuleSetServiceImpl.class)
    RuleSetService mRuleSetService;
    private final AdapterView.OnItemClickListener auditOjectIllustrationCommentListViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final EquipmentEntity objectDescriptionFormRef = mRuleSetService.getObjectDescriptionFromRef(auditObject.getAudit().getRuleSetRef(), auditObject.getAudit().getRuleSetVersion(), auditObject.getObjectDescriptionId());
            List<IllustrationEntity> illustrations = mRuleSetService.getIllutrationsFormRef(auditObject.getAudit().getRuleSetRef(), auditObject.getAudit().getRuleSetVersion(), objectDescriptionFormRef.getIllustration());
            IllustrationEntity illustration = (IllustrationEntity) parent.getAdapter().getItem(position);
            final String auditObjectName = auditObject.getName();
            final String auditObjectIcon = auditObject.getObjectDescription().getIcon();
            onIllustrationCommentClicked(auditObjectIcon, auditObjectName, illustrations, illustration, view);
        }
    };
    @Bean
    CommentListAdapter auditObjectCommentListAdapter;

    private void onCommentClicked(String iconName, String name, List<CommentEntity> comments, CommentEntity comment, View commentView) {
        switch (comment.getType()) {
            case AUDIO:
                startAudioPlayer(comment.getAttachment(), commentView);
                break;
            case TEXT:
            case FILE:
            case PHOTO:
                showComment(iconName, name, comments, comment);
                break;
            default:
                break;
        }
    }

    private List<CommentEntity> extractCommentPhotoAndText(List<CommentEntity> comments) {
        List<CommentEntity> ret = new ArrayList<CommentEntity>();

        for (CommentEntity comment : comments) {
            if (comment.getType().equals(CommentEntity.Type.PHOTO) || comment.getType().equals(CommentEntity.Type.TEXT) || comment.getType().equals(CommentEntity.Type.FILE)) {
                ret.add(comment);
            }
        }
        return (ret);
    }

    private void showComment(String iconName, String name, List<CommentEntity> commentList, CommentEntity comment) {
        int selectedIndex = 0;
        List<CommentEntity> commentPhotoAndText = extractCommentPhotoAndText(commentList);
        int nbCommentPhotoAndText = commentPhotoAndText.size();
        String[] titles = new String[nbCommentPhotoAndText];
        String[] comments = new String[nbCommentPhotoAndText];
        String[] images = new String[nbCommentPhotoAndText];

        Timber.v("nbCommentPhotoAndText " + nbCommentPhotoAndText);
        for (int i = 0; i < nbCommentPhotoAndText; i++) {
            if (commentPhotoAndText.get(i).equals(comment)) {
                selectedIndex = i;
            }

            if (iconName != null) {
                titles[i] = getString(R.string.illustration_activity_comment_auditobject_title, i + 1, name);
            } else {
                titles[i] = getString(R.string.illustration_activity_comment_audit_title, i + 1, name);
            }

            comments[i] = commentPhotoAndText.get(i).getContent();

            if (commentPhotoAndText.get(i).getType().equals(CommentEntity.Type.PHOTO)
                    || commentPhotoAndText.get(i).getType().equals(CommentEntity.Type.FILE)) {
                images[i] = commentPhotoAndText.get(i).getAttachment();
            }

        }

        BrowseIllustrationsActivity_.intent(DetailsActivity.this)
                .selectedIndex(selectedIndex)
                .iconName(iconName)
                .titles(titles)
                .comments(comments)
                .images(images)
                .start();

    }

    private void showIllustration(String iconName, String name, List<IllustrationEntity> illustrationList, IllustrationEntity illustration) {
        int selectedIndex = 0;
        String[] titles = new String[illustrationList.size()];
        String[] comments = new String[illustrationList.size()];
        String[] images = new String[illustrationList.size()];

        for (int i = 0; i < illustrationList.size(); i++) {
            final IllustrationEntity illustrationTemp = illustrationList.get(i);
            if (illustrationTemp != null) {
                if (illustrationTemp.equals(illustration)) {
                    selectedIndex = i;
                }

                if (illustrationTemp.getComment() != null) {
                    titles[i] = illustrationTemp.getComment();
                } else {
                    titles[i] = getString(R.string.illustration_activity_auditobject_title, i + 1, name);
                }
                comments[i] = illustrationTemp.getComment();

                if (illustrationTemp.getImage() != null && !illustrationTemp.getImage().isEmpty()) {
                    images[i] = illustrationTemp.getImage();
                }
            }

        }

        BrowseIllustrationsActivity_.intent(DetailsActivity.this)
                .selectedIndex(selectedIndex)
                .iconName(iconName)
                .titles(titles)
                .comments(comments)
                .images(images)
                .start();

    }

    private void onIllustrationCommentClicked(String iconName, String name, List<IllustrationEntity> illustrations, IllustrationEntity illustration, View commentView) {
        showIllustration(iconName, name, illustrations, illustration);
    }

    private void startAudioPlayer(String attachment, View commentView) {
        FragmentManager fm = getSupportFragmentManager();

        int[] location = new int[2];
        commentView.getLocationOnScreen(location);

        AudioPlayerDialog audioPlayerDialog = AudioPlayerDialog.newInstance(attachment, -1, location[1]);
        audioPlayerDialog.show(fm, "fragment_edit_name");
    }

    @AfterExtras
    void initAudit() {
        refreshAuditBackground();
    }

    @AfterViews
    void hideLogo() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayUseLogoEnabled(false);
    }

    @Override
    void auditRefreshed() {
        super.auditRefreshed();

        refreshAuditBackground();
    }

    @Background
    void refreshAuditBackground() {
        auditObject = modelManager.getAuditObject(auditObjectId);
        modelManager.refresh(auditObject, RefreshStrategy.builder().commentsNeeded(true).build());

        refreshAuditFinished();
    }

    @UiThread
    void refreshAuditFinished() {
        updateAuditObjectDefinitions(auditObject);
        updateAuditObjectCharacteristics(auditObject);
        updateAuditObjectStatus(auditObject);
        updateAuditObjectComments(auditObject);
        updateAuditObjectIllustrations(auditObject);
        updateActionBar();
    }

    @OptionsItem(R.id.action_rules)
    void showRuleSet() {
        BrowseRulesetsActivity_
                .intent(this)
                .rulesetReference(auditObject.getAudit().getRuleSetRef())
                .ruleSetVersion(auditObject.getAudit().getVersion()).start();
    }

    @Background
    void updateAuditObjectDefinitions(AuditObjectEntity auditObject) {
        final EquipmentEntity objectDescription = mRuleSetService.getObjectDescriptionFromRef(auditObject.getAudit().getRuleSetRef(), auditObject.getAudit().getRuleSetVersion(), auditObject.getObjectDescriptionId());
        updateDefinitionLayout(objectDescription);
    }

    @UiThread
    void updateDefinitionLayout(final EquipmentEntity objectDescription) {
        String definitions = objectDescription.getDefinition();

        auditObjectDefinitionLayout.setVisibility(GONE);
        if (!definitions.isEmpty()) {
            auditObjectDefinitionLayout.setVisibility(VISIBLE);
            textDefinition.setText(definitions);
        }
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
            customSubtitle.setVisibility(VISIBLE);
            customSubtitle.setText(subtitle);
        } else {
            customSubtitle.setVisibility(GONE);
        }
    }

    @Override
    void setUpToolbar() {
        super.setUpToolbar();

        final View responseButtonBar = LayoutInflater.from(this).inflate(R.layout.audit_object_toolbar, null);
        responseButtonBar.findViewById(R.id.response_ok_button).setVisibility(GONE);
        responseButtonBar.findViewById(R.id.response_no_answer_button).setVisibility(GONE);
        responseButtonBar.findViewById(R.id.response_nok_button).setVisibility(GONE);

        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Gravity.LEFT);
        responseButtonBar.setLayoutParams(lp);

        toolbar.addView(responseButtonBar);
    }

    private void updateAuditObjectStatus(AuditObjectEntity auditObject) {
        ResponseModel responses = auditObject.getResponse();
        auditObjectStatusLayout.setVisibility(VISIBLE);
        switch (responses) {
            case NOK: {
                textStatus.setText(R.string.auditing_progress_status_nok);
                textStatus.setTextColor(getResources().getColor(R.color.red));
                buttonStatusAccessibility.setText(R.string.detail_button_retry);
            }
            break;
            case OK: {
                textStatus.setText(R.string.auditing_progress_status_ok);
                textStatus.setTextColor(getResources().getColor(R.color.green));
                buttonStatusAccessibility.setText(R.string.detail_button_retry);
            }
            break;
            case NO_ANSWER: {
                textStatus.setText(R.string.auditing_progress_status_no_answer);
                textStatus.setTextColor(getResources().getColor(R.color.black));
                buttonStatusAccessibility.setText(R.string.detail_button_try);
            }
            break;
            case DOUBT: {
                textStatus.setText(R.string.auditing_progress_status_doubt);
                textStatus.setTextColor(getResources().getColor(R.color.yellow));
                buttonStatusAccessibility.setText(R.string.detail_button_retry);
            }
            break;
        }
    }

    @Click(R.id.button_status_accessibility)
    void tryOrRetryClicked() {
        launchAuditObjectsTest(true, auditObject);
    }

    @Click(R.id.action_update_characteristic)
    void updateCharacteristicClicked() {
        SelectAuditObjectCharacteristicsDialogBuilder.showCharacteriscticsAuditObject(this, auditObject, mRuleSetService, this);
    }

    @Click(R.id.action_update_comment)
    void updateCommentClicked() {
        final Long auditId = auditObject.getAudit().getId();

        String attachmentDirectory = modelManager.getAttachmentDirectory(auditId).getAbsolutePath();
        ListAuditObjectCommentActivity_.intent(this)
                .auditObjectId(auditObject.getId())
                .attachmentDirectory(attachmentDirectory)
                .startForResult(ACTIVITY_COMMENT_REQUEST_CODE);
    }

    private void updateAuditObjectCharacteristics(AuditObjectEntity auditObject) {
        final List<String> subObject = mRuleSetService.getObjectDescriptionFromRef(auditObject.getAudit().getRuleSetRef(), auditObject.getAudit().getRuleSetVersion(), auditObject.getObjectDescriptionId()).subObject;
        List<AuditObjectEntity> characteristics = auditObject.getChildren();
        String characteristicsNames = "";
        auditObjectCharacteristicLayout.setVisibility(subObject.isEmpty() ? GONE : VISIBLE);
        for (AuditObjectEntity characteristic : characteristics) {
            if (!characteristicsNames.isEmpty()) {
                characteristicsNames += " // ";
            }
            auditObjectCharacteristicLayout.setVisibility(VISIBLE);
            characteristicsNames = characteristicsNames + characteristic.getName();
        }
        textCharacteristic.setText(characteristicsNames);
        updateTitle();
    }


    private void updateAuditObjectIllustrations(AuditObjectEntity auditObject) {
        final EquipmentEntity objectDescription = mRuleSetService.getObjectDescriptionFromRef(auditObject.getAudit().getRuleSetRef(), auditObject.getAudit().getRuleSetVersion(), auditObject.getObjectDescriptionId());
        final List<IllustrationEntity> illustrations = mRuleSetService.getIllutrationsFormRef(auditObject.getAudit().getRuleSetRef(), auditObject.getAudit().getRuleSetVersion(), objectDescription.getIllustration());
        if (illustrations.isEmpty()) {
            auditObjectIllustrationsLayout.setVisibility(GONE);
        }

        illustrationsCommentsView.removeAllViews();

        final String auditObjectName = auditObject.getName();
        final String auditObjectIcon = objectDescription.getIcon();

        for (final IllustrationEntity illustration : illustrations) {
            final IllustrationCommentItemView illustrationCommentsView = IllustrationCommentItemView_.build(this);
            illustrationCommentsView.bind(illustration);
            illustrationsCommentsView.addView(illustrationCommentsView);
            illustrationCommentsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (auditOjectIllustrationCommentListViewClickListener != null) {
                        onIllustrationCommentClicked(auditObjectIcon, auditObjectName, illustrations, illustration, illustrationCommentsView);
                    }
                }
            });
        }
    }


    private void updateAuditObjectComments(AuditObjectEntity auditObject) {
        comments = new ArrayList<>();

        comments.addAll(auditObject.getComments());

        if (comments.isEmpty()) {
            auditObjectCommentsLayout.setVisibility(GONE);
        }

        commentsView.removeAllViews();

        final String auditObjectName = auditObject.getName();
        final EquipmentEntity objectDescription = mRuleSetService.getObjectDescriptionFromRef(auditObject.getAudit().getRuleSetRef(), auditObject.getAudit().getRuleSetVersion(), auditObject.getObjectDescriptionId());
        final String auditObjectIcon = objectDescription.getIcon();

        for (final CommentEntity comment : comments) {
            final CommentItemView commentView = CommentItemView_.build(this);
            commentView.bind(comment);
            commentsView.addView(commentView);
            commentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (auditOjectCommentListViewClickListener != null) {
                        onCommentClicked(auditObjectIcon, auditObjectName, comments, comment, commentView);
                    }
                }
            });
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


    private void updateActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setDisplayUseLogoEnabled(true);
        updateTitle();
        final EquipmentEntity objectDescription = mRuleSetService.getObjectDescriptionFromRef(auditObject.getAudit().getRuleSetRef(), auditObject.getAudit().getRuleSetVersion(), auditObject.getObjectDescriptionId());
        updateLogo(objectDescription.getIcon());


    }

    private void updateTitle() {
        setTitle(auditObject.getName());
        final String auditObjectCharacteristicsTitle = getAuditObjectCharacteristicsTitle(auditObject);
        setSubtitle(auditObjectCharacteristicsTitle);
    }


    @Background
    @Override
    public void onUpdateAuditObjectChildren(final AuditObjectEntity parent, List<EquipmentEntity> childrenToCreate, List<AuditObjectEntity> childrenToRemove) {
        modelManager.updateAuditObjectChildren(parent, childrenToCreate, childrenToRemove);
        refreshAudit();
    }

    @Override
    public void onLaunchAuditObjectsTestRequested(final AuditObjectEntity parent) {
        launchAuditObjectsTest(true, parent);
    }


}
