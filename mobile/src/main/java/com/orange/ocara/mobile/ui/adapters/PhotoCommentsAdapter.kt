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
import android.widget.ImageView
import com.orange.ocara.domain.models.CommentForCommentListModel
import com.orange.ocara.domain.models.CommentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.utils.ImageUtils

class PhotoCommentsAdapter(comments: ArrayList<CommentForCommentListModel>, onClickListener: (CommentForCommentListModel) -> Unit)
    : ParentCommentAdapter<PhotoCommentsAdapter.ViewHolder>(comments, onClickListener) {

    class ViewHolder(view: View) : ParentCommentAdapter.ViewHolder(view) {
        val imageComment: ImageView = view.findViewById(R.id.imageComment)
    }

    override fun bindCommentView(holder: ViewHolder, position: Int) {
        ImageUtils.setImageWithFile(holder.imageComment, data[position].attachment)
    }

    override fun getViewHolder(view: View) = ViewHolder(view)

    override fun getLayout() = R.layout.photo_comments_list_item
}