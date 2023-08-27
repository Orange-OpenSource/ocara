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

package com.orange.ocara.domain.docexport.models;

import com.orange.ocara.domain.models.CommentModel;

import com.orange.ocara.utils.FileUtils;
import com.orange.ocara.utils.enums.CommentType;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import lombok.EqualsAndHashCode;
import lombok.Value;
import timber.log.Timber;

@Value
@EqualsAndHashCode(callSuper = true)
class CommentPresenter extends Presenter<CommentModel> {

    public CommentPresenter(CommentModel value) {
        super(value);
    }

    public String getId() {
        return notNull("" + value.getId());
    }

    public CommentType getType() {
        return value.getType();
    }

    public String getContent() {
        return notNull(value.getContent());
    }

//    public String getAuditObjectId() {
//        return notNull("" + value.getAuditObject().getId());
//    }

    public String getAttachmentName() {
//        try {
            String name = new File(value.getAttachment()).getName();
            Timber.i("CommentType=%s;AttachmentName=%s", value.getType(), name);
            return notNull(name);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return "<pouet>";
//        }
    }
}
