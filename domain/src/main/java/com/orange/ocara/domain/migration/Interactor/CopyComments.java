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


package com.orange.ocara.domain.migration.Interactor;

import com.orange.ocara.domain.repositories.CommentRepository;
import com.orange.ocara.data.cache.database.Tables.Comment;
import com.orange.ocara.data.oldEntities.CommentEntity;
import com.orange.ocara.utils.enums.CommentType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyComments extends Interactor<CommentEntity> {
    CommentRepository repository;

    @Inject
    public CopyComments(CommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable execute() {
        List<CommentEntity> commentEntities = getAll(CommentEntity.class);
        List<Comment> comments = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntities) {
            Comment comment = new Comment(CommentType.valueOf(commentEntity.getType().toString()), commentEntity.getDate().toString(), commentEntity.getAttachment(), commentEntity.getContent());
            if (commentEntity.getAuditObject() == null) {
                comment.setAudit_equipment_id(-1);
                comment.setAudit_id(commentEntity.getAudit().getId().intValue());
            } else {
                comment.setAudit_id(-1);
                comment.setAudit_equipment_id(commentEntity.getAuditObject().getId().intValue());
            }
            comments.add(comment);
        }
        return repository.insertNewComment(comments);
    }
}
