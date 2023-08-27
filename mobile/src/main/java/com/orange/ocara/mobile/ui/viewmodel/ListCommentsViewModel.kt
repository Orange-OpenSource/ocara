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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orange.ocara.domain.interactors.DeleteMultipleComment
import com.orange.ocara.domain.interactors.InsertNewComment
import com.orange.ocara.domain.interactors.LoadAuditObjectComments
import com.orange.ocara.domain.models.CommentForCommentListModel
import com.orange.ocara.domain.models.CommentModel
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.utils.enums.CommentType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListCommentsViewModel @Inject constructor(
    private val insertNewComment: InsertNewComment,
    private val loadAuditObjectComments: LoadAuditObjectComments,
    private val deleteMultipleComment: DeleteMultipleComment
) : ViewModel() {

    private val _audioCommentsLiveData = MutableLiveData<List<CommentForCommentListModel>>()
    val audioCommentsLiveData: LiveData<List<CommentForCommentListModel>> = _audioCommentsLiveData
    private val _textCommentsLiveData = MutableLiveData<List<CommentForCommentListModel>>()
    val textCommentsLiveData: LiveData<List<CommentForCommentListModel>> = _textCommentsLiveData
    private val _photoCommentsLiveData = MutableLiveData<List<CommentForCommentListModel>>()
    val photoCommentsLiveData: LiveData<List<CommentForCommentListModel>> = _photoCommentsLiveData

    fun getAuditEquipmentComments(auditEquipmentId: Int) {
        loadAuditObjectComments.execute(auditEquipmentId.toLong())
            .map {
                castCommentToCommentForCommentList(it)
            }.subscribeAndObserve {
                Timber.d("nnnn s ${it.size}")
                val separatedComments = getSeparatedComments(it)
                Timber.d("nnnn audio ${separatedComments.audioComments.size}")
                _audioCommentsLiveData.postValue(separatedComments.audioComments)
                _textCommentsLiveData.postValue(separatedComments.textComments)
                _photoCommentsLiveData.postValue(separatedComments.photoComments)
            }
    }

    private fun getSeparatedComments(comments: List<CommentForCommentListModel>): CommentsSeparatedToTheirTypes {
        val audioComments = ArrayList<CommentForCommentListModel>()
        val textComments = ArrayList<CommentForCommentListModel>()
        val photoComments = ArrayList<CommentForCommentListModel>()
        // bad comments are photo or audio comments with attachments and they shouldn't exist
        val badComments = ArrayList<CommentForCommentListModel>()
        for (comment in comments) {
            when (comment.type) {
                CommentType.TEXT -> textComments.add(comment)
                CommentType.AUDIO -> {
                    if (comment.attachment.isNotEmpty())
                        audioComments.add(comment)
                    else
                        badComments.add(comment)
                }
                CommentType.FILE, CommentType.PHOTO -> {
                    if (comment.attachment.isNotEmpty()) {
                        photoComments.add(comment)
                    } else {
                        badComments.add(comment)
                    }
                }
                else -> {
                }
            }
        }
        deleteBadComments(badComments)
        return CommentsSeparatedToTheirTypes(
            audioComments = audioComments,
            textComments = textComments,
            photoComments = photoComments
        )
    }

    data class CommentsSeparatedToTheirTypes(
        val audioComments: List<CommentForCommentListModel>,
        val textComments: List<CommentForCommentListModel>,
        val photoComments: List<CommentForCommentListModel>
    )

    private fun castCommentToCommentForCommentList(comments: List<CommentModel>): List<CommentForCommentListModel> {
        val commentsForList = ArrayList<CommentForCommentListModel>()
        comments.forEach { comment ->
            commentsForList.add(CommentForCommentListModel(comment))
        }
        return commentsForList
    }

    fun insertComment(commentModel: CommentModel): Single<Int> {
        return insertNewComment.executeAndReturnId(commentModel)
    }

    // this is used to delete audio and photo comments that has no attachments
    // note : this case should never
    private fun deleteBadComments(comments: List<CommentForCommentListModel>) {
        deleteMultipleComment.execute(comments)
            .subscribeAndObserve {
                Timber.e("bad comments existed and they were deleted")
            }
    }

    fun getNumberOfSelectedComments() : Int {
        val selectedComments = ArrayList<CommentForCommentListModel>()
        selectedComments.addAll(getSelectedCommentsFromLiveData(audioCommentsLiveData))
        selectedComments.addAll(getSelectedCommentsFromLiveData(textCommentsLiveData))
        selectedComments.addAll(getSelectedCommentsFromLiveData(photoCommentsLiveData))
        return selectedComments.size

    }

    fun deleteSelectedComments(): Completable {
        val selectedComments = ArrayList<CommentForCommentListModel>()
        selectedComments.addAll(getSelectedCommentsFromLiveData(audioCommentsLiveData))
        selectedComments.addAll(getSelectedCommentsFromLiveData(textCommentsLiveData))
        selectedComments.addAll(getSelectedCommentsFromLiveData(photoCommentsLiveData))
        return deleteComments(selectedComments)
    }

    private fun getSelectedCommentsFromLiveData(commentsLiveData: LiveData<List<CommentForCommentListModel>>): List<CommentForCommentListModel> {
        val result = ArrayList<CommentForCommentListModel>()
        if (commentsLiveData.value != null) {
            for (comment in commentsLiveData.value!!) {
                if (comment.isSelected) result.add(comment)
            }
        }
        return result
    }

    private fun deleteComments(comments: List<CommentForCommentListModel>): Completable {
        return deleteMultipleComment.execute(comments)
    }
}