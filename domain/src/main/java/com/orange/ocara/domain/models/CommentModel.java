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


package com.orange.ocara.domain.models;

import com.orange.ocara.data.cache.database.Tables.Comment;
import com.orange.ocara.utils.enums.CommentType;

public class CommentModel {
    private int id;
    private CommentType type;
    private String date;
    private String attachment;
    private String content;
    private Long auditId = -1L;
    private Long equipmentId = -1L;

    public CommentModel(int id, CommentType type, String date, String attachment, String content) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.attachment = attachment;
        this.content = content;
    }
    public CommentModel(Comment comment){
        this(comment.getId(),comment.getType(),comment.getDate(),comment.getAttachment(),comment.getContent());
        this.equipmentId = Long.valueOf(comment.getAudit_equipment_id());
        this.auditId = Long.valueOf(comment.getAudit_id());

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CommentType getType() {
        return type;
    }

    public void setType(CommentType type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAuditId() {
        return auditId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public CommentModel auditIdBuilder(Long auditId) {
        this.auditId = auditId;
        return this;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public CommentModel equipmentIdBuider(Long equipmentId) {
        this.equipmentId = equipmentId;
        return this;
    }


}

