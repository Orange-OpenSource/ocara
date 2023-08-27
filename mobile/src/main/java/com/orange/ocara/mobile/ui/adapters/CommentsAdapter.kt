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

package com.orange.ocara.mobile.ui.adapters

import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.domain.models.CommentForCommentListModel
import com.orange.ocara.domain.models.CommentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.managers.PlayAudioManager
import com.orange.ocara.mobile.ui.managers.PlayAudioSuperManager
import com.orange.ocara.utils.ImageUtils
import com.orange.ocara.utils.enums.CommentType

class CommentsAdapter(
        private val context: Context,
        private val comments: ArrayList<CommentForCommentListModel>,
        private val audioSuperManager: PlayAudioSuperManager,
        private val onClickListener: ((comment: CommentModel) -> Unit)?) :
        ItemRVAdapter<CommentForCommentListModel, CommentsAdapter.ViewHolder>(comments) {
    private var editMode: Boolean = false

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textCommentLayout: ConstraintLayout = view.findViewById(R.id.textCommentLayout)
        val textComment: TextView = view.findViewById(R.id.textComment)
        val imageCommentLayout: ConstraintLayout = view.findViewById(R.id.imageCommentLayout)
        val audioCommentWithNoAudioLayout: ConstraintLayout = view.findViewById(R.id.audioCommentWithNoAudioLayout)
        val imageComment: ImageView = view.findViewById(R.id.imageComment)
        val audioCommentLayout: ConstraintLayout = view.findViewById(R.id.audioCommentLayout)
        val audioStat: ImageView = view.findViewById(R.id.audioStatImage)
        val seekBar: SeekBar = view.findViewById(R.id.seekBar)
        val timer: TextView = view.findViewById(R.id.timer)
        val checkBox: CheckBox = view.findViewById(R.id.checkbox)
    }

    override fun getLayout() = R.layout.comments_list_item

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.view.setOnClickListener {
            onClickListener?.invoke(comment)
        }
        addCheckboxLogic(comment, holder)
        when (comment.type) {
            CommentType.AUDIO -> audioComment(holder, comment)
            CommentType.TEXT -> textComment(holder, comment)
            CommentType.FILE, CommentType.PHOTO -> photoComment(holder, comment)
            else -> photoComment(holder, comment)
        }
    }

    private fun addCheckboxLogic(comment: CommentForCommentListModel, holder: ViewHolder) {
        if (!editMode) {
            holder.checkBox.visibility = View.GONE
        }else{
            holder.checkBox.visibility = View.VISIBLE
        }
        if (comment.isSelected) {
            holder.checkBox.isChecked = true
        }
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            comment.isSelected = isChecked
        }
    }

    fun setEditMode(x: Boolean) {
        editMode = x
        notifyDataSetChanged()
    }

    private fun audioComment(holder: ViewHolder, comment: CommentModel) {
        if (comment.attachment.isEmpty()) {
            audioCommentButNoAudio(holder)
        } else {
            audioCommentWithAudio(holder, comment)
        }
    }

    private fun setAllViewsAsGone(holder: ViewHolder) {
        holder.audioCommentLayout.visibility = View.GONE
        holder.audioCommentWithNoAudioLayout.visibility = View.GONE
        holder.imageCommentLayout.visibility = View.GONE
        holder.textCommentLayout.visibility = View.GONE
    }

    private fun audioCommentButNoAudio(holder: ViewHolder) {
        setAllViewsAsGone(holder)
        holder.audioCommentWithNoAudioLayout.visibility = View.VISIBLE
    }

    private fun audioCommentWithAudio(holder: ViewHolder, comment: CommentModel) {
        setAllViewsAsGone(holder)
        holder.audioCommentLayout.visibility = View.VISIBLE
        PlayAudioManager.builder()
                .audioSuperManager(audioSuperManager)
                .context(context)
                .fileName(comment.attachment)
                .seekBar(holder.seekBar)
                .playButton(holder.audioStat)
                .timer(holder.timer)
                .build()
    }

    private fun textComment(holder: ViewHolder, comment: CommentModel) {
        setAllViewsAsGone(holder)
        holder.textCommentLayout.visibility = View.VISIBLE
        holder.textComment.text = comment.content
    }

    private fun photoComment(holder: ViewHolder, comment: CommentModel) {
        setAllViewsAsGone(holder)
        holder.imageCommentLayout.visibility = View.VISIBLE
        ImageUtils.setImageWithFile(holder.imageComment, comment.attachment)
    }

    override fun getViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }
}