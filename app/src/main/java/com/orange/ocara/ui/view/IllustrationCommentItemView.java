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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.data.net.model.IllustrationEntity;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.io.File;

@EViewGroup(R.layout.illustration_comment_item)
public class IllustrationCommentItemView extends RelativeLayout {

    @ViewById(R.id.illustrations)
    ImageView illustrationImage;
    @ViewById(R.id.comment_content)
    TextView comment;
    @ViewById(R.id.title_of_Illustration)
    TextView title;

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
    }


    public void bind(IllustrationEntity illustration) {
        if (illustration != null) {
            final String path = getContext().getExternalCacheDir() + File.separator + illustration.getImage();
            File icon = new File(path);
            Picasso.with(getContext()).load(icon).placeholder(R.color.black50).into(illustrationImage);
            title.setText(illustration.getComment());
            comment.setText(illustration.getComment());
        }
    }
}



