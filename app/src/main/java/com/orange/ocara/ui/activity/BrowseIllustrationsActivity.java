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

package com.orange.ocara.ui.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.ui.view.CustomViewFlipper;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * Activity that displays a carousel for illustrations
 *
 * TPODO deprecated. should be replaced with {@link BrowseExplanationsActivity}
 */
@EActivity(R.layout.activity_illustrations)
public class BrowseIllustrationsActivity extends BaseActivity {

    @ViewById(R.id.previous_button)
    ImageView leftButton;

    @ViewById(R.id.next_button)
    ImageView rightButton;

    @ViewById(R.id.view_flipper)
    CustomViewFlipper viewFlipper;

    @Extra
    int selectedIndex;

    @Extra
    String iconName;

    @Extra
    String[] titles;

    @Extra
    String[] images;

    @Extra
    String[] comments;

    TextView commentPicture;

    ImageView photo;

    private static void updateButton(View view, boolean enabled) {
        view.setEnabled(enabled);
        view.setClickable(enabled);
    }

    @AfterViews
    protected void setUpViewFlipper() {

        updateArrows(selectedIndex);
        int nbIllustrations = titles.length;
        for (int i = 0; i < nbIllustrations; i++) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
            View view = inflater.inflate(R.layout.illustration, null);
            viewFlipper.addView(view);
            commentPicture = view.findViewById(R.id.comment_picture);
            photo = view.findViewById(R.id.photo);

            if (!TextUtils.isEmpty(comments[i])) {
                commentPicture.setText(comments[i]);
            }

            if (!TextUtils.isEmpty(images[i])) {
                String path = images[i];
                final Pattern compile = Pattern.compile("file*");
                if (!compile.matcher(path).find()) {
                    path = getExternalCacheDir() + File.separator + images[i];
                    File fileTmp = new File(path);
                    Timber.v("Message=Trying to load image from file;Path=%s", fileTmp);
                    Picasso.with(this).load(fileTmp).placeholder(R.color.dark_grey).into(photo);
                } else {
                    Timber.v("Message=Trying to load image from path;Path=%s", path);
                    Picasso.with(this).load(path).placeholder(R.color.dark_grey).into(photo);
                }
            }
        }

        viewFlipper.setDisplayedChild(selectedIndex);

        leftButton.setOnClickListener(v -> {
            viewFlipper.showPrevious();
            Timber.v("index " + viewFlipper.indexOfChild(viewFlipper.getCurrentView()));
            int index = viewFlipper.indexOfChild(viewFlipper.getCurrentView());
            updateTitle(index);
            updateArrows(index);
        });

        rightButton.setOnClickListener(v -> {
            viewFlipper.showNext();
            Timber.v("index " + viewFlipper.indexOfChild(viewFlipper.getCurrentView()));
            int index = viewFlipper.indexOfChild(viewFlipper.getCurrentView());
            updateTitle(index);
            updateArrows(index);
        });
    }

    @AfterViews
    protected void setUpTitle() {
        updateTitle(selectedIndex);
        if (iconName != null) {
            updateLogo(iconName);
        }
    }

    private void updateTitle(int index) {
        if (!TextUtils.isEmpty(titles[index])) {
            setTitle(titles[index]);
        }
    }

    private void updateArrows(int selectedIndex) {
        Timber.v("selectedIndex = %d", selectedIndex);
        int nbIllustrations = titles.length;

        updateLeftArrow(selectedIndex > 0);
        updateRightArrow(selectedIndex < nbIllustrations - 1);
    }

    private void updateLeftArrow(boolean enabled) {
        Timber.v("left arrow enabled = %b", enabled);

        updateButton(leftButton, enabled);
    }

    private void updateRightArrow(boolean enabled) {
        Timber.v("right arrow enabled = %b", enabled);

        updateButton(rightButton, enabled);
    }

}


