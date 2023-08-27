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

package com.orange.ocara.data.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orange.ocara.utils.models.WithReference;

import java.io.Serializable;

/** a model for remote illustrations */
public class IllustrationWs implements Serializable, WithIcon, WithReference {

    private static final String REFERENCE = "reference";
    private static final String IMAGE = "image";
    private static final String COMMENT = "comment";
    private static final String DATE = "date";

    @SerializedName(REFERENCE)
    @Expose
    public String reference;

    @SerializedName(IMAGE)
    @Expose
    public String image;

    @SerializedName(COMMENT)
    @Expose
    public String comment;

    @SerializedName(DATE)
    @Expose
    public String date;

    @Override
    public String getIcon() {
        return getImage();
    }

    @Override
    public boolean hasIcon() {
        return getIcon() != null && !getIcon().isEmpty();
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
