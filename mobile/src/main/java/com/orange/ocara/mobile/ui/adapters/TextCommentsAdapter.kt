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

import android.view.View
import android.widget.TextView
import com.orange.ocara.domain.models.CommentForCommentListModel
import com.orange.ocara.mobile.R

class TextCommentsAdapter(comments: ArrayList<CommentForCommentListModel>, onClickListener: (CommentForCommentListModel) -> Unit)
    : ParentCommentAdapter<TextCommentsAdapter.ViewHolder>(comments, onClickListener) {

    class ViewHolder(view: View) : ParentCommentAdapter.ViewHolder(view) {
        val commentContentTV: TextView = view.findViewById(R.id.textCommentContentTv)
    }

    override fun bindCommentView(holder: ViewHolder, position: Int) {
        holder.commentContentTV.text = data[position].content
    }

    override fun getViewHolder(view: View) = ViewHolder(view)

    override fun getLayout() = R.layout.text_comments_list_item
}