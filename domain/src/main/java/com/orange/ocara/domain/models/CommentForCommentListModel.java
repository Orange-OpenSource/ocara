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

import com.orange.ocara.utils.enums.CommentType;

/*
this model is used in the comments list fragment
 */
public class CommentForCommentListModel extends CommentModel {
    private boolean isSelected = false;

    public CommentForCommentListModel(int id, CommentType type, String date, String attachment, String content) {
        super(id, type, date, attachment, content);
    }

    public CommentForCommentListModel(CommentModel comment) {
        super(comment.getId(), comment.getType(), comment.getDate(), comment.getAttachment(), comment.getContent());
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}
