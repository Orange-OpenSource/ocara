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

abstract class ParentCommentAdapter<VH : ParentCommentAdapter.ViewHolder>(comments: ArrayList<CommentForCommentListModel>
                                                                , private val onClickListener: (CommentForCommentListModel) -> Unit) :
        ItemRVAdapter<CommentForCommentListModel, VH>(comments) {
    private var editMode: Boolean = false

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkbox)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindCommentView(holder, position)
        val comment = data[position]
        holder.itemView.setOnClickListener {
            onClickListener(comment)
        }
        addCheckboxLogic(comment, holder)
    }

    abstract fun bindCommentView(holder: VH, position: Int)

    private fun addCheckboxLogic(comment: CommentForCommentListModel, holder: VH) {
        if (!editMode) {
            holder.checkBox.visibility = View.GONE
        } else {
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
}