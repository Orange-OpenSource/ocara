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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.orange.ocara.R;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.orange.ocara.ui.view.CommentItemView;
import com.orange.ocara.ui.view.CommentItemView_;

import lombok.RequiredArgsConstructor;

import static com.orange.ocara.ui.contract.ListAuditCommentsContract.ListAuditCommentsUserActionsListener;

/**
 * Adapter that manages a list of {@link CommentEntity}s
 */
@RequiredArgsConstructor
public class AuditCommentsAdapter extends ItemListAdapter<CommentEntity> {

    /**
     * the current context
     */
    private final Context context;

    /**
     * a presenter
     */
    private final ListAuditCommentsUserActionsListener actionsListener;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommentItemView commentItemView;

        if (convertView != null && convertView instanceof CommentItemView) {
            commentItemView = (CommentItemView) convertView;
        } else {
            commentItemView = CommentItemView_.build(context);
        }

        CommentEntity comment = getItem(position);

        commentItemView.setDeleteListener(new DeleteCommentClickListener(context, actionsListener, comment.getId()));
        commentItemView.bind(comment);

        return commentItemView;
    }

    /**
     * Listener that handles the deletion of a comment
     */
    @RequiredArgsConstructor
    class DeleteCommentClickListener implements View.OnClickListener {

        private final Context context;

        private final ListAuditCommentsUserActionsListener actionsListener;

        private final Long commentId;

        @Override
        public void onClick(View v) {
            new OcaraDialogBuilder(context)
                    .setTitle(R.string.audit_list_delete_audit_title) // title
                    .setMessage(context.getString(R.string.comment_list_delete_comment_message)) // message
                    .setPositiveButton(R.string.action_remove, (dialog, which) -> actionsListener.deleteComment(commentId))
                    .setNegativeButton(R.string.action_cancel, null)
                    .create()
                    .show();
        }
    }
}
