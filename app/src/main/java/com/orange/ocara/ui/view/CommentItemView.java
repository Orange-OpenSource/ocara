/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;

import lombok.Setter;

@EViewGroup(resName = "comment_item")
public class CommentItemView extends RelativeLayout {

    @ViewById(resName = "comment_type")
    ImageView commentTypeImageView;

    @ViewById(resName = "comment_content")
    TextView contentTextView;

    @ViewById(resName = "comment_date")
    TextView dateTextView;

    @ViewById(resName = "action_remove")
    View deleteCommentView;

    @ViewById(resName = "comment_audio_player_container")
    ViewGroup audioPlayerContainer;

    @Setter
    private View.OnClickListener deleteListener;

    /**
     * Constructor.
     *
     * @param context Context
     */
    public CommentItemView(Context context) {
        this(context, null);
    }

    /**
     * Constructor.
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public CommentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        audioPlayerContainer.setVisibility(GONE);
    }

    public void bind(CommentEntity comment) {
        final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

        contentTextView.setText(comment.getContent());
        dateTextView.setText(dateFormat.format(comment.getDate()));
        deleteCommentView.setVisibility(View.VISIBLE);
        deleteCommentView.setOnClickListener(deleteListener);

        switch (comment.getType()) {
            case TEXT:
                commentTypeImageView.setImageResource(R.drawable.ic_text);
                break;

            case AUDIO:
                audioPlayerContainer.setVisibility(VISIBLE);
                commentTypeImageView.setImageResource(R.drawable.ic_microphone);
                break;

            case FILE:
            case PHOTO:
                Picasso
                        .with(getContext())
                        .load(comment.getAttachment())
                        .placeholder(R.drawable.ic_camera)
                        .resizeDimen(R.dimen.commentTypeIconSize, R.dimen.commentTypeIconSize)
                        .centerInside()
                        .into(commentTypeImageView);
                break;
        }
    }

}
