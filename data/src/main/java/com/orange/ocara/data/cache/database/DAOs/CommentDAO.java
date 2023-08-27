package com.orange.ocara.data.cache.database.DAOs;
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
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.orange.ocara.data.cache.database.Tables.Comment;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface CommentDAO {
    @Insert
    Single<Long> insert(Comment comment);

    @Insert
    Completable insert(List<Comment> comment);

    @Update
    Completable update(Comment comment);

    @Query("update comment set attachment=:attach where comment_id = :id")
    Completable updateAttachment(int id,String attach);

    @Query("select attachment from comment where comment_id = :id")
    Single<String> getAttachment(int id);

    @Query("UPDATE comment SET content =:content , attachment =:attach , date =:date where comment_id =:id ")
    Completable updateCommentById(int id, String content, String date, String attach);

    @Query("select * from comment where audit_id=:auditId")
    Single<List<Comment>> getAuditComments(int auditId);


    @Query("select * from comment where audit_equipment_id =:objectId")
    Single<List<Comment>> getAuditObjectComments(Long objectId);

    @Query("delete from comment where comment_id =:commentId")
    Completable deleteSingleComment(int commentId);

    @Query("delete from comment where audit_equipment_id =:objId")
    Completable deleteAllEquipmentComments(int objId);

    @Query("delete from comment where audit_equipment_id in (:objId)")
    Completable deleteAllEquipmentsComments(List<Integer> objId);


    @Query("delete from comment where audit_id =:auditId")
    Completable deleteAllAuditComments(int auditId);

    @Query("select * from comment where comment_id =:commentId ")
    Single<Comment> getCommentById(int commentId);

    @Query("UPDATE comment SET content=:content where comment_id=:id")
    Completable updateCommentContent(int id, String content);

    @Query("delete from comment where comment_id in (:ids)")
    Completable deleteComments(List<Integer> ids);
}
