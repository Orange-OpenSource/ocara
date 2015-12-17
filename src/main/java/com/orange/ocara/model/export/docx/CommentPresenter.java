/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import com.orange.ocara.model.Comment;

import org.apache.commons.io.FilenameUtils;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class CommentPresenter extends Presenter<Comment> {

    public CommentPresenter(Comment value) {
        super(value);
    }

    public String getId() {
        return notNull("" + value.getId());
    }

    public Comment.Type getType() {
        return value.getType();
    }

    public boolean isTypeText() {
        return Comment.Type.TEXT.equals(getType());
    }

    public String getAuditObjectName() {
        return notNull(value.getAuditObject().getName());
    }

    public String getContent() {
        return notNull(value.getContent());
    }

    public String getAttachmentName() {

        switch (getType()) {
            case AUDIO: {
                String baseName = FilenameUtils.getBaseName(value.getAttachment());
                return notNull(String.format("%s.bin", baseName));
            }

            default:
                return notNull(FilenameUtils.getName(value.getAttachment()));
        }

    }

    public String getAuditObjectId() {
        return notNull("" + value.getAuditObject().getId());
    }
}
