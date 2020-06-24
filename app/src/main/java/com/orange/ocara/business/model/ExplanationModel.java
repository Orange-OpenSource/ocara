/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.business.model;

import com.orange.ocara.data.net.model.Explanation;

import java.io.File;

/**
 * a model for explanations
 */
public class ExplanationModel {

    private final long id;

    private final String label;

    private File image;

    private final String comment;

    public ExplanationModel(long id, String label, File image, String comment) {
        this.id = id;
        this.label = label;
        this.image = image;
        this.comment = comment;
    }

    public ExplanationModel(Explanation explanation, File image) {
        this.id = explanation.getId();
        this.label = explanation.getReference();
        this.image = image;
        this.comment = explanation.getComment();
    }

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public File getImage() {
        return image;
    }

    public String getComment() {
        return comment;
    }

    public boolean isIllustrated() {
        return image != null && image.isFile() && image.exists();
    }

    public boolean isTextOnly() {
        return !isIllustrated();
    }
}
