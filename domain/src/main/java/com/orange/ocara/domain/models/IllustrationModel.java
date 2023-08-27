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

import com.orange.ocara.data.cache.database.Tables.Illustration;

public class IllustrationModel {
    private String reference;
    private String image;
    private String comment;
    private String date;
    public IllustrationModel(Illustration illustration){
        this.reference = illustration.getReference();
        this.image = illustration.getImage();
        this.comment = illustration.getComment();
        this.date = illustration.getDate();
    }

    public IllustrationModel(String image, String comment, String date) {
        this.image = image;
        this.comment = comment;
        this.date = date;
    }

    public static IllustrationModel getInstanceBuilderFromCommentModel(CommentModel commentModel){

        return new IllustrationModel(commentModel.getAttachment(),commentModel.getContent(),commentModel.getDate());

    }
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
