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

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.orange.ocara.domain.interactors.DeleteSingleComment
import com.orange.ocara.domain.interactors.GetCommentAttachment
import com.orange.ocara.domain.interactors.UpdateCommentAttachment
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@HiltViewModel
class PhotoCommentViewModel @Inject constructor(
        private val getCommentAttachment: GetCommentAttachment,
        private val updateCommentAttachment: UpdateCommentAttachment,
        private val deleteComment: DeleteSingleComment) : ViewModel() {

    private lateinit var photoAttachment : String

    fun getPhotoAttachment():  String {
        return photoAttachment
    }

    fun setPhotoAttachment(attachment: String) {
        this.photoAttachment = attachment
    }
    fun getAttachment(commentId: Int): Single<String> {
        return getCommentAttachment.execute(commentId)
    }

    fun updateAttachment(commentId: Int, attachment: String): Completable {
        return updateCommentAttachment.execute(commentId, attachment)
    }

    fun deleteComment(commentId: Int) :Completable{
        return deleteComment.execute(commentId.toLong())
    }
}