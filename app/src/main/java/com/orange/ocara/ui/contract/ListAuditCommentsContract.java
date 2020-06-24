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

package com.orange.ocara.ui.contract;

import com.orange.ocara.data.cache.model.CommentEntity;

import java.util.List;

/**
 * Contract between a view and the listener for its user's possible actions
 */
public interface ListAuditCommentsContract {

    /**
     * Behaviour of the view
     */
    interface ListAuditCommentView {

        void showNewComment(CommentEntity.Type type);

        void showComment(CommentEntity comment);

        void showComments(List<CommentEntity> comments);

        void refreshComments();
    }

    /**
     * Behaviour of the listener
     */
    interface ListAuditCommentsUserActionsListener {

        void deleteComment(Long commentId);

        void deleteAllComments(Long auditId);

        void loadAllComments(Long auditId);
    }
}
