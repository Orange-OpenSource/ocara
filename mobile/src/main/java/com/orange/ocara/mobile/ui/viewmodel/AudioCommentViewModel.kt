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
class AudioCommentViewModel @Inject constructor(
    private val loadComment: LoadSingleCommentById,
    private val deleteCommentAttachment: DeleteCommentAttachment,
    private val updateCommentAttachment: UpdateCommentAttachment,
    private val insertComment: InsertNewComment,
    private val updateCommentContent: UpdateCommentContent,
    private val deleteSingleComment: DeleteSingleComment
) : ViewModel() {
    var comment: CommentModel? = null

    fun updateContent(id: Int, content: String): Completable {
        return updateCommentContent.execute(id, content)
    }

    fun getComment(id: Int): Single<CommentModel> {
        return loadComment.execute(id.toLong())
                .map {
                    comment = it
                    return@map comment
                }
    }

    fun deleteRecord(id: Int): Completable {
        return deleteCommentAttachment.execute(id)
    }

    fun deleteComment(id: Int): Completable {
        return deleteSingleComment.execute(id.toLong())
    }


    fun updateAttachment(id: Int, attach: String): Completable {
        return updateCommentAttachment.execute(id, attach)
    }

    fun insertComment(comment: CommentModel): Single<Int> {
        return insertComment.executeAndReturnId(comment)
    }

}