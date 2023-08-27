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

package com.orange.ocara.data.oldEntities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.orange.ocara.utils.StringUtils;

import java.util.Calendar;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@Table(name = CommentEntity.TABLE_NAME)
@ToString(exclude = {"audit", "auditObject"})
@EqualsAndHashCode(callSuper = true, exclude = {"audit", "auditObject"})
public class CommentEntity extends Model {

    static final String TABLE_NAME = "comments";
    static final String COLUMN_TYPE = "type";
    static final String COLUMN_DATE = "date";
    static final String COLUMN_CONTENT = "content";
    static final String COLUMN_ATTACHMENT = "attachment";
    static final String COLUMN_AUDIT = "audit";
    static final String COLUMN_AUDIT_OBJECT = "auditObject";
    @Column(name = COLUMN_TYPE, notNull = true)
    private Type type;
    @Column(name = COLUMN_DATE, notNull = true)
    private Date date;
    @Column(name = COLUMN_CONTENT, notNull = false)
    private String content;
    @Column(name = COLUMN_ATTACHMENT, notNull = false)
    private String attachment;
    @Column(name = COLUMN_AUDIT, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private AuditEntity audit;
    @Column(name = COLUMN_AUDIT_OBJECT, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private AuditObjectEntity auditObject;

    /**
     * Default constructor. Needed by ORM.
     */
    public CommentEntity() {
        super();
    }

    /**
     * Constructor.
     *
     * @param type Comment type
     */
    public CommentEntity(Type type) {
        this(type, StringUtils.EMPTY);
    }

    /**
     * Constructor.
     *
     * @param type    Comment type
     * @param content Content
     */
    public CommentEntity(Type type, String content) {
        this.type = type;
        this.content = content;
        this.date = Calendar.getInstance().getTime();
    }

    public static CommentEntity.Type getTypeFromInt(int val) {
        if (val == 0) return CommentEntity.Type.AUDIO;
        if (val == 1) return CommentEntity.Type.PHOTO;
        if (val == 2) return CommentEntity.Type.TEXT;
        if (val == 3) return CommentEntity.Type.FILE;
        return CommentEntity.Type.TEXT;
    }

    public static int getIntFromTypeFrom(CommentEntity.Type val) {
        if (val == CommentEntity.Type.AUDIO) return 0;
        if (val == CommentEntity.Type.PHOTO) return 1;
        if (val == CommentEntity.Type.TEXT) return 2;
        if (val == CommentEntity.Type.FILE) return 3;
        return 2;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public AuditObjectEntity getAuditObject() {
        return auditObject;
    }

    public void setAuditObject(AuditObjectEntity auditObject) {
        this.auditObject = auditObject;
    }

    public AuditEntity getAudit() {
        if (audit != null) {
            return audit;
        }
        return auditObject.getAudit();
    }

    public void setAudit(AuditEntity audit) {
        this.audit = audit;
    }


    /**
     * Comment type.
     */
    public enum Type {
        AUDIO,
        PHOTO,
        TEXT,
        FILE
    }
}