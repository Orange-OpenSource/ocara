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

package com.orange.ocara.ui.presenter;

import com.activeandroid.Model;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.ui.contract.ListAuditCommentsContract;

import lombok.RequiredArgsConstructor;

/** default implementation of {@link com.orange.ocara.ui.contract.ListAuditCommentsContract.ListAuditCommentsUserActionsListener} */
public class ListAuditCommentsPresenter implements ListAuditCommentsContract.ListAuditCommentsUserActionsListener {

    private final ListAuditCommentsContract.ListAuditCommentView view;

    private final ModelManager modelManager;

    public ListAuditCommentsPresenter(ListAuditCommentsContract.ListAuditCommentView view, ModelManager modelManager) {
        this.view = view;
        this.modelManager = modelManager;
    }

    @Override
    public void deleteComment(Long commentId) {
        CommentEntity comment = Model.load(CommentEntity.class, commentId);
        modelManager.deleteComment(comment);
        view.refreshComments();
    }

    @Override
    public void deleteAllComments(Long auditId) {

    }

    @Override
    public void loadAllComments(Long auditId) {

    }
}
