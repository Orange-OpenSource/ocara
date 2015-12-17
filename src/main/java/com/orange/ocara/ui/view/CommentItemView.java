/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
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
import com.orange.ocara.model.Comment;
import com.orange.ocara.tools.injection.DaggerHelper;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;

import javax.inject.Inject;

@EViewGroup(resName="comment_item")
public class CommentItemView extends RelativeLayout {

    @ViewById(resName="comment_type")
    ImageView type;
    @ViewById(resName="comment_content")
    TextView content;
    @ViewById(resName="comment_date")
    TextView date;
    @ViewById(resName="action_remove")
    View deleteComment;



    @ViewById(resName="comment_audio_player_container")
    ViewGroup audioPlayerContainer;


    @Inject
    Picasso picasso;

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

        DaggerHelper.inject(this, context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        audioPlayerContainer.setVisibility(GONE);
    }

    public void bind(Comment comment) {
        final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

        content.setText(comment.getContent());
        date.setText(dateFormat.format(comment.getDate()));

        switch (comment.getType()) {
            case TEXT:
                type.setImageResource(R.drawable.ic_text);
                break;

            case AUDIO:
                audioPlayerContainer.setVisibility(VISIBLE);
                type.setImageResource(R.drawable.ic_microphone);
                break;

            case PHOTO:
                picasso.load(comment.getAttachment()).placeholder(R.drawable.ic_camera).resizeDimen(R.dimen.commentTypeIconSize, R.dimen.commentTypeIconSize).centerInside().into(type);
                break;

        }

    }

    public View getDeleteComment() {
        return deleteComment;
    }

}
