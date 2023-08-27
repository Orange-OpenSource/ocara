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
import com.orange.ocara.domain.interactors.DeleteSingleComment
import com.orange.ocara.domain.interactors.LoadSingleCommentById
import com.orange.ocara.domain.interactors.UpdateCommentContent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@HiltViewModel
class TextCommentViewModel @Inject constructor(private val deleteSingleComment: DeleteSingleComment,
                                               private val updateCommentContent: UpdateCommentContent,
                                               private val loadSingleCommentById: LoadSingleCommentById)
    : ViewModel() {

    fun deleteComment(id: Int): Completable {
        return deleteSingleComment.execute(id.toLong())
    }

    fun getCommentContent(id: Int): Single<String> {
        return loadSingleCommentById.execute(id.toLong()).map {
            it.content
        }
    }

    fun changeContent(id: Int, content: String): Completable {
        return updateCommentContent.execute(id, content)
    }
}