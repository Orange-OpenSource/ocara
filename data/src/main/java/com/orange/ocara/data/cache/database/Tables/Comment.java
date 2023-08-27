package com.orange.ocara.data.cache.database.Tables;
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
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.orange.ocara.utils.enums.CommentType;

@Entity(tableName = "comment")
public class Comment {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "comment_id")
    private int id;
    private CommentType type;
    private String date;
    private String attachment;
    private String content;
    @ColumnInfo(name = "audit_id")
    private int audit_id = -1;
    @ColumnInfo(name = "audit_equipment_id")
    private int audit_equipment_id = -1;
    public Comment(CommentType type, String date, String attachment, String content) {
        this.type = type;
        this.date = date;
        this.attachment = attachment;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAudit_equipment_id() {
        return audit_equipment_id;
    }

    public void setAudit_equipment_id(int audit_equipment_id) {
        this.audit_equipment_id = audit_equipment_id;
    }

    public int getAudit_id() {
        return audit_id;
    }

    public void setAudit_id(int audit_id) {
        this.audit_id = audit_id;
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

//    public enum Type {
//        AUDIO,
//        PHOTO,
//        TEXT,
//        FILE
//    }

}
