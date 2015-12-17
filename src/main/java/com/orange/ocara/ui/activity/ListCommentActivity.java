/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.activeandroid.Model;
import com.orange.ocara.R;
import com.orange.ocara.model.Comment;
import com.orange.ocara.model.Commentable;
import com.orange.ocara.model.ModelManager;
import com.orange.ocara.model.RefreshStrategy;
import com.orange.ocara.model.Refreshable;
import com.orange.ocara.ui.adapter.ItemListAdapter;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.orange.ocara.ui.view.CommentItemView;
import com.orange.ocara.ui.view.CommentItemView_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
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

import javax.inject.Inject;

@EActivity
@OptionsMenu(resName="comment_list")
public abstract class ListCommentActivity<T extends Refreshable & Commentable> extends BaseActivity {

    private static final int EDIT_COMMENT_REQUEST_CODE = 1;

    @Inject
    ModelManager modelManager;

    @Extra("attachmentDirectory")
    String attachmentDirectory;

    @ViewById(resName="comment_list")
    ListView commentList;
    @ViewById(resName="comment_list_empty")
    View emptyCommentList;

    @ViewById(resName="buttonbar_left_button")
    Button leftButton;
    @ViewById(resName="buttonbar_right_button")
    Button rightButton;

    protected T entity;

    @AfterViews
    void setUpCommentList() {
        commentList.setEmptyView(emptyCommentList);
        commentList.setAdapter(commentListAdapter);

        registerForContextMenu(commentList);
    }

    @AfterViews
    void setUpButtonBar() {
        leftButton.setText(R.string.action_back);
        rightButton.setVisibility(View.GONE);
    }

    @ItemClick(resName="comment_list")
    void commentListItemClicked(Comment comment) {
        Comment.Type commentType = comment.getType();
        Long commentId = comment.getId();
        startEditCommentActivity(commentType, commentId);
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

        setLoading(true);
        refreshComments();
    }

    @Click(resName="buttonbar_left_button")
    void onLeftButtonClicked() {
        onBackPressed();
    }

    @OptionsItem(resName="action_comment_text")
    void onCreateTextComment() {
        createComment(Comment.Type.TEXT);
    }

    @OptionsItem(resName="action_comment_audio")
    void onCreateAudioComment() {
        createComment(Comment.Type.AUDIO);
    }

    @OptionsItem(resName="action_comment_photo")
    void onCreatePhotoComment() {
        createComment(Comment.Type.PHOTO);
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

    /**
     * To create a new comment.
     *
     * @param type comment type
     */
    private void createComment(Comment.Type type) {
        startEditCommentActivity(type, null);
    }

    @OnActivityResult(EDIT_COMMENT_REQUEST_CODE)
    void onResult(int resultCode, @org.androidannotations.annotations.OnActivityResult.Extra(value = "commentId") Long commentId) {

        if (resultCode == RESULT_OK) {
            updateComment(commentId);
        }
    }

    @Background(serial = "serial")
    void refreshComments() {
        modelManager.refresh(entity, RefreshStrategy.builder().commentsNeeded(true).build());
        List<Comment> comments = entity.getComments();
        updateComments(comments);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void updateComments(List<Comment> comments) {
        setLoading(false);
        commentListAdapter.update(comments);
    }

    @Background(serial = "serial")
    void updateComment(Long commentId) {
        Comment comment = Model.load(Comment.class, commentId);
        entity.attachComment(comment);
        comment.save();
    }

    @Background
    protected void deleteAllComments(List<Comment> comments) {
        modelManager.deleteAllComments(comments);
        refreshComments();
    }

    @Background
    protected void deleteComment(Comment comment) {
        modelManager.deleteComment(comment);
        refreshComments();
    }

    /**
     * Comment list adapter.
     */
    private final ItemListAdapter<Comment> commentListAdapter = new ItemListAdapter<Comment>() {

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final CommentItemView commentItemView;

            if (convertView == null) {
                commentItemView = CommentItemView_.build(ListCommentActivity.this);
            } else {
                commentItemView = (CommentItemView) convertView;
            }

            commentItemView.getDeleteComment().setVisibility(View.VISIBLE);
            commentItemView.getDeleteComment().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog confirmDialog = new OcaraDialogBuilder(ListCommentActivity.this)
                            .setTitle(R.string.audit_list_delete_audit_title) // title
                            .setMessage(getString(R.string.comment_list_delete_comment_message)) // message
                            .setPositiveButton(R.string.action_remove, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteComment(commentListAdapter.getItem(position));
                                }
                            })
                            .setNegativeButton(R.string.action_cancel, null)
                            .create();

                    confirmDialog.show();


                }
            });
            Comment comment = getItem(position);
            commentItemView.bind(comment);
            return commentItemView;
        }
    };


    /**
     * Start creation / editing comment activity of given type
     *
     * @param commentType
     * @param commentId   id of comment to edit, null for creation
     */
    private void startEditCommentActivity(Comment.Type commentType, Long commentId) {
        switch (commentType) {
            case TEXT:
                createEditCommentTextActivity(commentId)
                        .startForResult(EDIT_COMMENT_REQUEST_CODE);
                break;
            case PHOTO:
                createEditCommentPhotoActivity(commentId)
                        .startForResult(EDIT_COMMENT_REQUEST_CODE);
                break;
            case AUDIO:
                createEditCommentAudioActivity(commentId)
                        .startForResult(EDIT_COMMENT_REQUEST_CODE);
                break;
        }
    }


    protected EditCommentTextActivity_.IntentBuilder_ createEditCommentTextActivity(Long commentId) {
        return EditCommentTextActivity_.intent(this)
                .commentId(commentId)
                .commentType(Comment.Type.TEXT)
                .attachmentDirectory(attachmentDirectory);
    }

    protected EditCommentAudioActivity_.IntentBuilder_ createEditCommentAudioActivity(Long commentId) {
        return EditCommentAudioActivity_.intent(this)
                .commentId(commentId)
                .commentType(Comment.Type.AUDIO)
                .attachmentDirectory(attachmentDirectory);
    }


    protected EditCommentPhotoActivity_.IntentBuilder_ createEditCommentPhotoActivity(Long commentId) {
        return EditCommentPhotoActivity_.intent(this)
                .commentId(commentId)
                .commentType(Comment.Type.PHOTO)
                .attachmentDirectory(attachmentDirectory);
    }


}
