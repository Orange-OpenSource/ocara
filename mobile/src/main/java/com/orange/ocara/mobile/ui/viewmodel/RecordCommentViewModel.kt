/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.orange.ocara.domain.interactors.*
import com.orange.ocara.domain.models.CommentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@HiltViewModel
class RecordCommentViewModel @Inject constructor(
        private val loadComment: LoadSingleCommentById,
        private val updateCommentAttachment: UpdateCommentAttachment,
        private val deleteSingleComment: DeleteSingleComment,
        private val insertComment: InsertNewComment
) : ViewModel() {

    fun updateAttachment(id: Int, attach: String): Completable {
        return updateCommentAttachment.execute(id, attach)
    }

    fun insertComment(comment: CommentModel): Single<Int> {
        return insertComment.executeAndReturnId(comment)
    }

    fun deleteComment(commentId: Int): Completable {
        return deleteSingleComment.execute(commentId.toLong())
    }
}