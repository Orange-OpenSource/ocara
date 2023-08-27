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


package com.orange.ocara.domain.interactors;

import com.orange.ocara.data.cache.database.Tables.Comment;
import com.orange.ocara.domain.models.CommentModel;
import com.orange.ocara.domain.repositories.CommentRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;


public class InsertNewComment {
    CommentRepository commentRepository;

    @Inject
    public InsertNewComment(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    public Completable execute(CommentModel commentModel){
        return commentRepository.insertNewComment(commentModel).ignoreElement();
    }
    public Single<Integer> executeAndReturnId(CommentModel commentModel){
        return commentRepository.insertNewComment(commentModel);
    }
}