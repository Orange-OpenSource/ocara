/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.modelStatic.Illustration;
import com.orange.ocara.tools.injection.DaggerHelper;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

@EViewGroup(resName="illustration_comment_item")
public class IllustrationCommentItemView extends RelativeLayout {

    @ViewById(resName="illustrations")
    ImageView illustrationImage;
    @ViewById(resName="comment_content")
    TextView comment;
    @ViewById(resName="title_of_Illustration")
    TextView title;



    @Inject
    Picasso picasso;

    /**
     * Constructor.
     *
     * @param context Context
     */
    public IllustrationCommentItemView(Context context) {
        this(context, null);
    }

    /**
     * Constructor.
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public IllustrationCommentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        DaggerHelper.inject(this, context);
    }


    public void bind(Illustration illustration) {
        illustrationImage.setImageURI(Uri.parse(String.valueOf(illustration.getImage())));
        title.setText(illustration.getTitle());
        comment.setText(illustration.getComment());
        }
    }



