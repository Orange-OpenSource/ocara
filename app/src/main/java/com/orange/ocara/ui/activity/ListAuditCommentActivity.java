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
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import com.activeandroid.Model;
import com.orange.ocara.R;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.db.ModelManagerImpl;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.ui.ListAuditCommentsUiConfig;
import com.orange.ocara.ui.adapter.AuditCommentsAdapter;
import com.orange.ocara.ui.adapter.ItemListAdapter;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.orange.ocara.ui.intent.EditCommentIntentBuilder;
import com.orange.ocara.ui.tools.RefreshStrategy;

import org.androidannotations.annotations.AfterExtras;
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

import static com.orange.ocara.R.id.buttonbar_left_button;
import static com.orange.ocara.ui.contract.ListAuditCommentsContract.ListAuditCommentView;
import static com.orange.ocara.ui.contract.ListAuditCommentsContract.ListAuditCommentsUserActionsListener;

/**
 * Activity dedicated to the display of a list of {@link CommentEntity}s
 */
@EActivity(R.layout.activity_list_comment)
@OptionsMenu(R.menu.comment_list)
public class ListAuditCommentActivity extends BaseActivity
        implements ListAuditCommentView {

    private static final int EDIT_COMMENT_REQUEST_CODE = 1;

    @Extra("auditId")
    Long auditId;

    protected AuditEntity entity;

    @Bean(ModelManagerImpl.class)
    ModelManager modelManager;

    @Bean(ListAuditCommentsUiConfig.class)
    ListAuditCommentsUiConfig uiConfig;

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

    /**
     * Presenter dedicated to the view
     */
    private ListAuditCommentsUserActionsListener userActionsListener;

    /**
     * Adapter that manages the content of the list.
     */
    private ItemListAdapter<CommentEntity> commentListAdapter;

    @AfterExtras
    void initActionListener() {

        userActionsListener = uiConfig.actionsListener(ListAuditCommentActivity.this);
        commentListAdapter = new AuditCommentsAdapter(
                ListAuditCommentActivity.this, userActionsListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadEntityBackground();
    }

    @Background
    void loadEntityBackground() {
        this.entity = modelManager.getAudit(auditId);
        initView();
        refreshComments();
    }

    @UiThread
    void initView() {
        String auditName = this.entity.getName();
        String title = getString(com.orange.ocara.R.string.list_comment_title, auditName);

        setTitle(title);
    }

    @OptionsItem(R.id.delete_all_comments)
    void onDeleteAllComments() {
        AlertDialog confirmDialog = new OcaraDialogBuilder(ListAuditCommentActivity.this)
                .setTitle(com.orange.ocara.R.string.comment_list_delete_all_comment_message_title) // title
                .setMessage(getString(com.orange.ocara.R.string.comment_list_delete_all_comment_audit_message)) // message
                .setPositiveButton(com.orange.ocara.R.string.action_remove, (dialog, which) -> deleteAllComments(entity.getComments()))
                .setNegativeButton(com.orange.ocara.R.string.action_cancel, null)
                .create();
        confirmDialog.show();
    }

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
        createComment(CommentEntity.Type.TEXT);
    }

    @OptionsItem(R.id.action_comment_audio)
    void onCreateAudioComment() {
        createComment(CommentEntity.Type.AUDIO);
    }

    @OptionsItem(R.id.action_comment_photo)
    void onCreatePhotoComment() {
        createComment(CommentEntity.Type.PHOTO);
    }

    @OptionsItem(R.id.action_comment_file)
    void onCreateFileComment() {
        createComment(CommentEntity.Type.FILE);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_remove) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            setLoading(true);
            userActionsListener.deleteComment(commentListAdapter.getItem(info.position).getId());
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    /**
     * To create a new comment.
     *
     * @param type comment type
     */
    private void createComment(CommentEntity.Type type) {
        showNewComment(type);
    }

    @OnActivityResult(EDIT_COMMENT_REQUEST_CODE)
    void onResult(int resultCode, @OnActivityResult.Extra(value = "commentId") Long commentId) {

        if (resultCode == RESULT_OK) {
            updateComment(commentId);
        }
    }

    @Background(serial = "serial")
    public void refreshComments() {
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
    public void showNewComment(CommentEntity.Type type) {
        new EditCommentIntentBuilder(this, type, attachmentDirectory)
                .auditId(auditId)
                .startIntent(EDIT_COMMENT_REQUEST_CODE);
    }

    @Override
    public void showComment(CommentEntity c) {
        new EditCommentIntentBuilder(this, c.getType(), attachmentDirectory)
                .commentId(c.getId())
                .auditId(auditId)
                .startIntent(EDIT_COMMENT_REQUEST_CODE);
    }

}
