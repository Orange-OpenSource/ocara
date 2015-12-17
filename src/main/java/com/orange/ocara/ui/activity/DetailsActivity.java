/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orange.ocara.model.AuditObject;
import com.orange.ocara.model.Comment;
import com.orange.ocara.model.ModelManager;
import com.orange.ocara.model.RefreshStrategy;
import com.orange.ocara.modelStatic.Illustration;
import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.modelStatic.Response;
import com.orange.ocara.ui.adapter.ItemListAdapter;
import com.orange.ocara.ui.dialog.AudioPlayerDialog;
import com.orange.ocara.ui.dialog.SelectAuditObjectCharacteristicsDialogBuilder;
import com.orange.ocara.ui.view.CommentItemView;
import com.orange.ocara.ui.view.CommentItemView_;
import com.orange.ocara.ui.view.IllustrationCommentItemView;
import com.orange.ocara.ui.view.IllustrationCommentItemView_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@EActivity(resName="activity_details")
@OptionsMenu(resName="object_details")
/*package*/ class DetailsActivity extends BaseActivityManagingAudit implements SelectAuditObjectCharacteristicsDialogBuilder.UpdateAuditObjectCharacteristicsListener {

    @Extra("auditObjectId")
    Long auditObjectId;

    AuditObject auditObject;

    List<Comment> comments;


    @ViewById(resName="scrollview_container")
    ScrollView scrollContainer;



    @ViewById(resName="title")
    TextView customTitle;
    @ViewById(resName="subtitle")
    TextView customSubtitle;
    @ViewById(resName="audit_object_definitions_layout")
    ViewGroup auditObjectDefinitionLayout;
    @ViewById(resName="text_definition")
    TextView textDefinition;
    @ViewById(resName="audit_object_status_layout")
    ViewGroup auditObjectStatusLayout;
    @ViewById(resName="text_status")
    TextView textStatus;
    @ViewById(resName="button_status_accessibility")
    TextView buttonStatusAccessibility;
    @ViewById(resName="title_characteristic")
    TextView titleCharacteristic;
    @ViewById(resName="illustrations")
    ImageView illustration;
    @ViewById(resName="text_characteristic")
    TextView textCharacteristic;
    @ViewById(resName="audit_object_characteristic_layout")
    ViewGroup auditObjectCharacteristicLayout;
    @ViewById(resName="audit_object_illustrations_layout")
    ViewGroup auditObjectIllustrationsLayout;
    @ViewById(resName="audit_object_comments_layout")
    ViewGroup auditObjectCommentsLayout;

    @ViewById(resName="illustration_comments_section")
    ViewGroup illustrationsCommentsView;
    @ViewById(resName="comments_section")
    ViewGroup commentsView;

    private static final int ACTIVITY_COMMENT_REQUEST_CODE = 1;

    @Inject
    ModelManager modelManager;
    private ItemListAdapter auditObjectCommentListAdapter;


    @AfterViews
    void setUpAuditCommentList() {
        auditObjectCommentListAdapter = new CommentListAdapter();
    }

    private final AdapterView.OnItemClickListener auditOjectCommentListViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<Comment> comments = auditObject.getComments();
            Comment comment =(Comment) parent.getAdapter().getItem(position);
            final String auditObjectName = auditObject.getName();
            final Uri auditObjectIcon = Uri.parse(auditObject.getObjectDescription().getIcon().toString());
            onCommentClicked(auditObjectIcon, auditObjectName, comments, comment, view);
        }
    };

    private void onCommentClicked(Uri icon, String name, List<Comment> comments, Comment comment, View commentView) {

        switch (comment.getType()) {
            case AUDIO:
                startAudioPlayer(comment.getAttachment(), commentView);
                break;
            case TEXT :
                showComment(icon, name, comments, comment);
                break;

            case PHOTO:
                showComment(icon, name, comments, comment);
                break;

            default:
                break;
        }
    }

    private List<Comment> extractCommentPhotoAndText(List<Comment> comments) {
        List<Comment> ret = new ArrayList<Comment>();

        for (Comment comment : comments) {
            if (comment.getType().equals(Comment.Type.PHOTO) || comment.getType().equals(Comment.Type.TEXT)) {
                ret.add(comment);
            }
        }
        return(ret);
    }

    private void showComment(Uri icon, String name, List<Comment> commentList, Comment comment) {
        int selectedIndex =0;
        List<Comment> commentPhotoAndText = extractCommentPhotoAndText(commentList);
        int nbCommentPhotoAndText = commentPhotoAndText.size();
        String[] titles = new String[nbCommentPhotoAndText];
        String[] comments = new String[nbCommentPhotoAndText];
        String[] images = new String[nbCommentPhotoAndText];

        Timber.v("nbCommentPhotoAndText "+nbCommentPhotoAndText);
        for (int i = 0; i < nbCommentPhotoAndText; i++) {
            if (commentPhotoAndText.get(i).equals(comment)) {
                selectedIndex = i;
            }

            if (icon != null) {
                titles[i] = getString(com.orange.ocara.R.string.illustration_activity_comment_auditobject_title, i + 1, name );
            } else {
                titles[i] = getString(com.orange.ocara.R.string.illustration_activity_comment_audit_title, i + 1, name );
            }

            comments[i] = commentPhotoAndText.get(i).getContent();

            if (commentPhotoAndText.get(i).getType().equals(Comment.Type.PHOTO)) {
                images[i] = commentPhotoAndText.get(i).getAttachment();
            }

        }

        IllustrationsActivity_.intent(DetailsActivity.this)
                .selectedIndex(selectedIndex)
                .icon(icon)
                .titles(titles)
                .comments(comments)
                .images(images)
                .start();

    }

    private void showIllustration(Uri icon, String name, List<Illustration> illustrationList, Illustration illustration) {
        int selectedIndex =0;
        String[] titles = new String[illustrationList.size()];
        String[] comments = new String[illustrationList.size()];
        String[] images = new String[illustrationList.size()];

        for (int i = 0; i < illustrationList.size(); i++) {
            if (illustrationList.get(i).equals(illustration)) {
                selectedIndex = i;
            }

            if (illustrationList.get(i).getTitle() != null) {
                titles[i] = illustrationList.get(i).getTitle();
            }
            else {
                titles[i] = getString(com.orange.ocara.R.string.illustration_activity_auditobject_title, i + 1, name);
            }
            comments[i] = illustrationList.get(i).getComment();

            if (!illustrationList.get(i).getImage().toString().isEmpty()) {
               images[i] = illustrationList.get(i).getImage().toString();
            }

        }

        IllustrationsActivity_.intent(DetailsActivity.this)
                .selectedIndex(selectedIndex)
                .icon(icon)
                .titles(titles)
                .comments(comments)
                .images(images)
                .start();

    }

    private final AdapterView.OnItemClickListener auditOjectIllustrationCommentListViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<Illustration> illustrations = auditObject.getObjectDescription().getIllustrations();
            Illustration illustration =(Illustration) parent.getAdapter().getItem(position);
            final String auditObjectName = auditObject.getName();
            final Uri auditObjectIcon = Uri.parse(auditObject.getObjectDescription().getIcon().toString());
            onIllustrationCommentClicked(auditObjectIcon, auditObjectName, illustrations, illustration, view);
        }
    };

    private void onIllustrationCommentClicked(Uri icon, String name, List<Illustration> illustrations, Illustration illustration, View commentView) {
        showIllustration(icon,name,illustrations,illustration);
    }

    private void startAudioPlayer(String attachment, View commentView) {
        FragmentManager fm = getSupportFragmentManager();

        int[] location = new int[2];
        commentView.getLocationOnScreen(location);

        AudioPlayerDialog audioPlayerDialog = AudioPlayerDialog.newInstance(attachment, -1, location[1]);
        audioPlayerDialog.show(fm, "fragment_edit_name");
    }


    @AfterViews
    void hideLogo() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayUseLogoEnabled(false);
    }

    @Override
    void auditRefreshed() {
        super.auditRefreshed();


        auditObject = modelManager.getAuditObject(auditObjectId);
        modelManager.refresh(auditObject, RefreshStrategy.builder().commentsNeeded(true).build());

        updateAuditObjectDefinitions(auditObject);
        updateAuditObjectCharacteristics(auditObject);
        updateAuditObjectStatus(auditObject);
        updateAuditObjectComments(auditObject);
        updateAuditObjectIllustrations(auditObject);
        updateActionBar();
    }



    @OptionsItem(resName="action_rules")
        void showRuleSet(){
            ListRulesActivity_.intent(this)
                    .ruleSetId(audit.getRuleSetId())
                    .objectDescriptionId(auditObject.getObjectDescriptionId()).start();
    }

    private void updateAuditObjectDefinitions(AuditObject auditObject) {
        String definitions = auditObject.getObjectDescription().getDefinition();

        auditObjectDefinitionLayout.setVisibility(GONE);
            if(!definitions.isEmpty()){
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






    private void updateAuditObjectStatus(AuditObject auditObject) {
        Response responses = auditObject.getResponse();
            auditObjectStatusLayout.setVisibility(VISIBLE);
        switch (responses) {
            case NOK:{
                textStatus.setText(com.orange.ocara.R.string.status_NOK);
                buttonStatusAccessibility.setText(com.orange.ocara.R.string.detail_button_retry);
            }
            break;
            case OK:{
                textStatus.setText(com.orange.ocara.R.string.status_OK);
                buttonStatusAccessibility.setText(com.orange.ocara.R.string.detail_button_retry);
            }
            break;
            case NoAnswer:{
                textStatus.setText(com.orange.ocara.R.string.status_NoAnswer);
                buttonStatusAccessibility.setText(com.orange.ocara.R.string.detail_button_try);
            }
            break;
            case DOUBT:{
                textStatus.setText(com.orange.ocara.R.string.status_DOUBT);
                buttonStatusAccessibility.setText(com.orange.ocara.R.string.detail_button_retry);
            }
            break;
        }
    }


    @Click(resName="button_status_accessibility")
    void tryOrRetryClicked() {
        launchAuditObjectsTest(auditObject);
    }


    @Click(resName="action_update_characteristic")
    void updateCharacteristicClicked() {
        SelectAuditObjectCharacteristicsDialogBuilder.showCharacteriscticsAuditObject(this, auditObject, this);
    }

    @Click(resName="action_update_comment")
    void updateCommentClicked() {
        final Long auditId = auditObject.getAudit().getId();

        String attachmentDirectory = modelManager.getAttachmentDirectory(auditId).getAbsolutePath();
        ListAuditObjectCommentActivity_.intent(this)
                .auditObjectId(auditObject.getId())
                .attachmentDirectory(attachmentDirectory)
                .startForResult(ACTIVITY_COMMENT_REQUEST_CODE);
    }



     private void updateAuditObjectCharacteristics(AuditObject auditObject) {
        List<AuditObject> characteristics = auditObject.getChildren();
        String characteristicsNames = "";
        auditObjectCharacteristicLayout.setVisibility(GONE);
        for (AuditObject characteristic : characteristics){
            if(!characteristicsNames.isEmpty()){
                 characteristicsNames += " // ";
            }
            auditObjectCharacteristicLayout.setVisibility(VISIBLE);
            characteristicsNames  = characteristicsNames  + characteristic.getName();
        }
         textCharacteristic.setText(characteristicsNames);
    }


    private void updateAuditObjectIllustrations(AuditObject auditObject) {
       final List<Illustration> illustrations = auditObject.getObjectDescription().getIllustrations();
        if (illustrations.isEmpty()) {
            auditObjectIllustrationsLayout.setVisibility(GONE);
        }

        illustrationsCommentsView.removeAllViews();

        final String auditObjectName = auditObject.getName();
        final Uri auditObjectIcon = Uri.parse(auditObject.getObjectDescription().getIcon().toString());

        for(final Illustration illustration : illustrations) {
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


    private void updateAuditObjectComments(AuditObject auditObject) {
        comments = new ArrayList<Comment>();

        comments.addAll(auditObject.getComments());

        if (comments.isEmpty()) {
            auditObjectCommentsLayout.setVisibility(GONE);
        }

        commentsView.removeAllViews();

        final String auditObjectName = auditObject.getName();
        final Uri auditObjectIcon = Uri.parse(auditObject.getObjectDescription().getIcon().toString());

        for(final Comment comment : comments) {
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




    private void updateActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setDisplayUseLogoEnabled(true);
        updateTitle();
        updateLogo(Uri.parse(auditObject.getObjectDescription().getIcon().toString()));


    }

    private void updateTitle() {
        setTitle(modelManager.getAuditObject(auditObjectId).getName());
        setSubtitle(getAuditObjectCharacteristicsTitle(auditObject));
    }



    @Background
    @Override
    public void onUpdateAuditObjectChildren(final AuditObject parent, List<ObjectDescription> childrenToCreate, List<AuditObject> childrenToRemove) {
        modelManager.updateAuditObjectChildren(parent, childrenToCreate, childrenToRemove);
        refreshAudit();
    }

    @Override
    public void onLaunchAuditObjectsTestRequested(final AuditObject parent) {
        launchAuditObjectsTest(parent);
    }



    //    Comment list adapter
    private class CommentListAdapter extends ItemListAdapter<Comment> {

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            CommentItemView commentItemView;

            if (convertView == null) {
                commentItemView = CommentItemView_.build(DetailsActivity.this);
            } else {
                commentItemView = (CommentItemView) convertView;
            }

            Comment comment = getItem(position);
            commentItemView.bind(comment);
            return commentItemView;
        }
    }
}
