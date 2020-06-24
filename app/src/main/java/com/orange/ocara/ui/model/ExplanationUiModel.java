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

package com.orange.ocara.ui.model;


import com.orange.ocara.business.model.ExplanationModel;

import java.io.File;

/**
 * a Ui-Model for explanations
 */
public class ExplanationUiModel {
    private final long id;

    private final String label;

    private final String comment;

    private File image;

    public ExplanationUiModel(ExplanationModel model) {
        this.id = model.getId();
        this.label = model.getLabel();
        this.comment = model.getComment();
        this.image = model.getImage();
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
        return image != null && image.exists() && image.isFile() && image.canRead();
    }
}
