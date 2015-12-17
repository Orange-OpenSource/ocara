/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Table(name = Comment.TABLE_NAME)
@ToString(exclude = {"audit", "auditObject"})
@EqualsAndHashCode(callSuper = true, exclude = {"audit", "auditObject"})
public class Comment extends Model {

    static final String TABLE_NAME = "comments";

    static final String COLUMN_TYPE = "type";
    static final String COLUMN_DATE = "date";
    static final String COLUMN_CONTENT = "content";
    static final String COLUMN_ATTACHMENT = "attachment";
    static final String COLUMN_AUDIT = "audit";
    static final String COLUMN_AUDIT_OBJECT = "auditObject";



    /**
     * Comment type.
     */
    public enum Type {

        AUDIO,
        PHOTO,
        TEXT;
    }

    @Column(name = COLUMN_TYPE, notNull = true)
    private Type type;
    @Column(name = COLUMN_DATE, notNull = true)
    private Date date;
    @Column(name = COLUMN_CONTENT, notNull = false)
    private String content;
    @Column(name = COLUMN_ATTACHMENT, notNull = false)
    private String attachment;
    @Column(name = COLUMN_AUDIT, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private Audit audit;
    @Column(name = COLUMN_AUDIT_OBJECT, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private AuditObject auditObject;

    /**
     * Default constructor. Needed by ORM.
     */
    public Comment() {
        super();
    }

    /**
     * Constructor.
     *
     * @param type Comment type
     */
    public Comment(Type type) {
        this(type, StringUtils.EMPTY);
    }

    /**
     * Constructor.
     *
     * @param type    Comment type
     * @param content Content
     */
    public Comment(Type type, String content) {
        this.type = type;
        this.content = content;
        this.date = Calendar.getInstance().getTime();
    }

    public Audit getAudit() {
        if (audit != null) {
            return audit;
        }
        return auditObject.getAudit();
    }
}