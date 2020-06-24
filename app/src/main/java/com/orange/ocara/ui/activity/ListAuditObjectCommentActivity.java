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

import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.activeandroid.Model;
import com.orange.ocara.R;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.db.ModelManagerImpl;
import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.ui.adapter.ItemListAdapter;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.orange.ocara.ui.intent.EditCommentIntentBuilder;
import com.orange.ocara.ui.tools.RefreshStrategy;
import com.orange.ocara.ui.view.CommentItemView;
import com.orange.ocara.ui.view.CommentItemView_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import static android.view.View.GONE;
import static com.orange.ocara.R.id.buttonbar_left_button;

@EActivity(R.layout.activity_list_comment)
@OptionsMenu(R.menu.comment_list)
public class ListAuditObjectCommentActivity extends BaseActivity {

    private static final int EDIT_COMMENT_REQUEST_CODE = 1;

    @Extra("auditObjectId")
    Long auditObjectId;

    @ViewById(R.id.title)
    TextView customTitle;
    @ViewById(R.id.subtitle)
    TextView customSubtitle;

    String title;
    String subTitle;

    protected AuditObjectEntity entity;

    @Bean(ModelManagerImpl.class)
    ModelManager modelManager;

    /**
     * Comment list adapter.
     */
    private final ItemListAdapter<CommentEntity> commentListAdapter = new ItemListAdapter<CommentEntity>() {

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final CommentItemView commentItemView;

            if (convertView != null && convertView instanceof CommentItemView) {
                commentItemView = (CommentItemView) convertView;
            } else {
                commentItemView = CommentItemView_.build(ListAuditObjectCommentActivity.this);
            }

            commentItemView.setDeleteListener(v -> {

                AlertDialog confirmDialog = new OcaraDialogBuilder(ListAuditObjectCommentActivity.this)
                        .setTitle(R.string.audit_list_delete_audit_title) // title
                        .setMessage(getString(R.string.comment_list_delete_comment_message)) // message
                        .setPositiveButton(R.string.action_remove, (dialog, which) -> deleteComment(commentListAdapter.getItem(position)))
                        .setNegativeButton(R.string.action_cancel, null)
                        .create();

                confirmDialog.show();
            });

            CommentEntity comment = getItem(position);
            commentItemView.bind(comment);

            return commentItemView;
        }
    };

    @Extra("attachmentDirectory")
    String attachmentDirectory;

    @ViewById(R.id.comment_list)
    ListView commentListView;

    @ViewById(R.id.comment_list_empty)
    View emptyView;

    @ViewById(buttonbar_left_button)
    Button leftButton;

    @ViewById(R.id.buttonbar_right_button)
    Button rightButton;

    @AfterViews
    void setUpCommentList() {
        commentListView.setEmptyView(emptyView);
        commentListView.setAdapter(commentListAdapter);

        registerForContextMenu(commentListView);
    }

    @AfterViews
    void setUpButtonBar() {
        leftButton.setText(R.string.action_back);
        rightButton.setVisibility(View.GONE);
    }

    @ItemClick(R.id.comment_list)
    void commentListItemClicked(CommentEntity comment) {

        showComment(comment);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.comment_context_menu, menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (entity != null) {
            setLoading(true);
            refreshComments();
        }
    }

    @Click(R.id.buttonbar_left_button)
    void onLeftButtonClicked() {
        onBackPressed();
    }

    @OptionsItem(R.id.action_comment_text)
    void onCreateTextComment() {
        showNewComment(CommentEntity.Type.TEXT);
    }

    @OptionsItem(R.id.action_comment_audio)
    void onCreateAudioComment() {
        showNewComment(CommentEntity.Type.AUDIO);
    }

    @OptionsItem(R.id.action_comment_photo)
    void onCreatePhotoComment() {
        showNewComment(CommentEntity.Type.PHOTO);
    }

    @OptionsItem(R.id.action_comment_file)
    void onCreateFileComment() {
        showNewComment(CommentEntity.Type.FILE);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_remove) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            setLoading(true);
            deleteComment(commentListAdapter.getItem(info.position));
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    @OnActivityResult(EDIT_COMMENT_REQUEST_CODE)
    void onResult(int resultCode, @OnActivityResult.Extra(value = "commentId") Long commentId) {

        if (resultCode == RESULT_OK) {
            updateComment(commentId);
        }
    }

    @Background(serial = "serial")
    void refreshComments() {
        modelManager.refresh(entity, RefreshStrategy.builder().commentsNeeded(true).build());
        List<CommentEntity> comments = entity.getComments();
        showComments(comments);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    public void showComments(List<CommentEntity> comments) {
        setLoading(false);
        commentListAdapter.update(comments);
    }

    @Background(serial = "serial")
    void updateComment(Long commentId) {
        CommentEntity comment = Model.load(CommentEntity.class, commentId);
        entity.attachComment(comment);
        comment.save();
    }

    @Background
    protected void deleteAllComments(List<CommentEntity> comments) {
        modelManager.deleteAllComments(comments);
        refreshComments();
    }

    @Background
    protected void deleteComment(CommentEntity comment) {
        modelManager.deleteComment(comment);
        refreshComments();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadEntityBackground();
    }

    @Background
    void loadEntityBackground() {
        this.entity = modelManager.getAuditObject(auditObjectId);
        loadEntityFinished();
    }

    @UiThread
    void loadEntityFinished() {
        setUpView();
    }

    @Override
    void setUpToolbar() {
        super.setUpToolbar();

        final View responseButtonBar = LayoutInflater.from(this).inflate(com.orange.ocara.R.layout.audit_object_toolbar, null);
        responseButtonBar.findViewById(com.orange.ocara.R.id.response_ok_button).setVisibility(View.GONE);
        responseButtonBar.findViewById(R.id.response_no_answer_button).setVisibility(GONE);
        responseButtonBar.findViewById(com.orange.ocara.R.id.response_nok_button).setVisibility(View.GONE);

        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Gravity.LEFT);
        responseButtonBar.setLayoutParams(lp);

        toolbar.addView(responseButtonBar);
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
        getTitlesBackground();
        final EquipmentEntity objectDescription = entity.getObjectDescription();
        if (objectDescription != null) {
            updateLogo(objectDescription.getIcon());
        }
    }

    @Background
    void getTitlesBackground() {
        title = modelManager.getAuditObject(auditObjectId).getName();
        subTitle = getAuditObjectCharacteristicsTitle(entity);
        updateTitle();
    }

    @UiThread
    void updateTitle() {
        setTitle(title);
        setSubtitle(subTitle);
    }

    private void setUpView() {
        updateActionBar();
    }

    @OptionsItem(R.id.delete_all_comments)
    void onDeleteAllComments() {
        AlertDialog confirmDialog = new OcaraDialogBuilder(ListAuditObjectCommentActivity.this)
                .setTitle(com.orange.ocara.R.string.comment_list_delete_all_comment_message_title)
                .setMessage(getString(com.orange.ocara.R.string.comment_list_delete_all_comment_object_message))
                .setPositiveButton(com.orange.ocara.R.string.action_remove, (dialog, which) -> deleteAllComments(entity.getComments()))
                .setNegativeButton(com.orange.ocara.R.string.action_cancel, null)
                .create();
        confirmDialog.show();
    }

    public void showNewComment(CommentEntity.Type type) {
        new EditCommentIntentBuilder(this, type, attachmentDirectory)
                .auditId(entity.getAudit().getId())
                .title(title)
                .subtitle(subTitle)
                .startIntent(EDIT_COMMENT_REQUEST_CODE);
    }

    public void showComment(CommentEntity c) {
        new EditCommentIntentBuilder(this, c.getType(), attachmentDirectory)
                .commentId(c.getId())
                .auditId(entity.getAudit().getId())
                .title(title)
                .subtitle(subTitle)
                .startIntent(EDIT_COMMENT_REQUEST_CODE);
    }
}
