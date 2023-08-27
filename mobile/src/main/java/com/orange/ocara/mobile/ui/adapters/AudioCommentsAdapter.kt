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
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.domain.models.CommentForCommentListModel
import com.orange.ocara.domain.models.CommentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.managers.PlayAudioManager
import com.orange.ocara.mobile.ui.managers.PlayAudioSuperManager

class AudioCommentsAdapter(
        comments: ArrayList<CommentForCommentListModel>,
        private val context: Context,
        private val audioSuperManager: PlayAudioSuperManager,
        onClickListener: (CommentModel) -> Unit
) : ParentCommentAdapter<AudioCommentsAdapter.ViewHolder>(comments,onClickListener) {

    class ViewHolder(val view: View) : ParentCommentAdapter.ViewHolder(view) {
        val audioStat: ImageView = view.findViewById(R.id.audioStatImage)
        val seekBar: SeekBar = view.findViewById(R.id.seekBar)
        val timer: TextView = view.findViewById(R.id.timer)
        val commentNumberTv: TextView = view.findViewById(R.id.commentNumberTv)
    }

    override fun getViewHolder(view: View) = ViewHolder(view)

    override fun getLayout() = R.layout.audio_comments_list_item

    private fun setupAudioManager(holder: ViewHolder, comment: CommentModel) {
        PlayAudioManager.builder()
                .audioSuperManager(audioSuperManager)
                .context(context)
                .fileName(comment.attachment)
                .seekBar(holder.seekBar)
                .playButton(holder.audioStat)
                .timer(holder.timer)
                .build()
    }

    override fun bindCommentView(holder: ViewHolder, position:Int) {
        val comment = data[position]
        setupAudioManager(holder, comment)
        holder.commentNumberTv.text = context.getString(R.string.comment_label_in_audio_list, position + 1)
    }
}