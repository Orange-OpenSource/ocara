/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import java.util.List;

public interface Commentable {

    /**
     * To retrieve all Comment
     *
     * @return list of Comment
     */
    List<Comment> getComments();

    /**
     * To attach a new comment.
     *
     * @param comment Comment to attach
     */
    void attachComment(Comment comment);
}
