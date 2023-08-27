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


package com.orange.ocara.domain.repositories;

import com.orange.ocara.data.cache.database.DAOs.CommentDAO;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.Comment;
import com.orange.ocara.domain.models.CommentModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class CommentRepository {
    private final CommentDAO commentDAO;

    @Inject
    public CommentRepository(OcaraDB ocaraDB) {
        commentDAO = ocaraDB.commentDao();
    }

    public Single<Integer> insertNewComment(CommentModel commentModel) {
        Comment comment = new Comment(
                commentModel.getType(),
                commentModel.getDate(),
                commentModel.getAttachment(),
                commentModel.getContent());
        comment.setAudit_equipment_id(commentModel.getEquipmentId().intValue());
        comment.setAudit_id(commentModel.getAuditId().intValue());

        return commentDAO.insert(comment)
                .map(Long::intValue);
    }

    public Completable deleteMultipleCommments(List<Integer> ids) {
        return commentDAO.deleteComments(ids);
    }

    public Completable updateContent(int id, String content) {
        return commentDAO.updateCommentContent(id, content);
    }

    public Single<String> getAttachment(int id) {
        return commentDAO.getAttachment(id);
    }

    public Completable updateAttachment(int id, String attach) {
        return commentDAO.updateAttachment(id, attach);
    }

    public Completable insertNewComment(List<Comment> comments) {
        return commentDAO.insert(comments);
    }

    public Completable updateSingleComment(CommentModel commentModel) {
        return commentDAO.updateCommentById(
                commentModel.getId(),
                commentModel.getContent(),
                commentModel.getDate(),
                commentModel.getAttachment());
    }


    public Single<List<CommentModel>> getCommentsForAudit(int auditId) {
        return commentDAO.getAuditComments(auditId).map(comments -> {
            List<CommentModel> commentModels = new ArrayList<>();
            for (Comment comment : comments) {
                commentModels.add(new CommentModel(comment));
            }
            return commentModels;
        });
    }

    public Single<List<CommentModel>> getAuditObjectComments(Long objectId) {

        return commentDAO.getAuditObjectComments(objectId)
                .map(comments -> {
                    List<CommentModel> commentModels = new ArrayList<>();
                    for (Comment comment : comments) {
                        commentModels.add(new CommentModel(comment));
                    }
                    return commentModels;
                });
    }

    public Completable deleteSingleComment(int commentId) {
        return commentDAO.deleteSingleComment(commentId);
    }

    public Completable deleteAllEquipmentComments(int objId) {
        return commentDAO.deleteAllEquipmentComments(objId);
    }

    public Completable deleteAllAuditComments(int auditId) {
        return commentDAO.deleteAllAuditComments(auditId);
    }

    public Single<CommentModel> getCommentById(long commentId) {
        return commentDAO.getCommentById((int) commentId)
                .map(CommentModel::new);
    }
}
