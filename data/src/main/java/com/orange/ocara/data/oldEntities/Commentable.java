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

import java.util.List;

public interface Commentable {

    /**
     * To retrieve all Comment
     *
     * @return list of Comment
     */
    List<CommentEntity> getComments();

    /**
     * To attach a new comment.
     *
     * @param comment Comment to attach
     */
    void attachComment(CommentEntity comment);
}
