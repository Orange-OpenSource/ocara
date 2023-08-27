/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */


package com.orange.ocara.domain.interactors;

import com.orange.ocara.domain.models.CommentForCommentListModel;
import com.orange.ocara.domain.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class DeleteMultipleComment {
    CommentRepository repository;

    @Inject
    public DeleteMultipleComment(CommentRepository repository) {
        this.repository = repository;
    }

    public Completable execute(List<CommentForCommentListModel> comments) {
        List<Integer> idsToDelete = new ArrayList<>();
        for (CommentForCommentListModel comment : comments) {
            if (comment.isSelected()) idsToDelete.add(comment.getId());
        }
        return repository.deleteMultipleCommments(idsToDelete);
    }
}
