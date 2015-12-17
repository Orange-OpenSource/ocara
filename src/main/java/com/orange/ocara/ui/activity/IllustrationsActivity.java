/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orange.ocara.ui.view.CustomViewFlipper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

import timber.log.Timber;

@EActivity(resName="illustrations")
public class IllustrationsActivity extends BaseActivity {

    @ViewById(resName="previous_button")
    ImageView leftButton;
    @ViewById(resName="next_button")
    ImageView rightButton;
    @ViewById(resName="view_flipper")
    CustomViewFlipper viewFlipper;

    @Extra ("selectedIndex")
    int selectedIndex;

    @Extra ("icon")
    Uri icon;

    @Extra("titles")
    String[] titles;

    @Extra("images")
    String[] images;

    @Extra("comments")
    String[] comments;

    TextView commentPicture;
    ImageView photo;



    @AfterViews
    protected void setUpViewFlipper() {

        updateArrows(selectedIndex);
        int nbIllustrations = titles.length;
        for (int i= 0; i < nbIllustrations; i++) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
            View view = inflater.inflate(com.orange.ocara.R.layout.illustration, null);
            viewFlipper.addView(view);
            commentPicture = (TextView) view.findViewById(com.orange.ocara.R.id.comment_picture);
            photo = (ImageView) view.findViewById(com.orange.ocara.R.id.photo);


            if (!StringUtils.isEmpty(comments[i])) {
                commentPicture.setText(comments[i]);
            }


            if (!StringUtils.isEmpty(images[i])) {
                picasso.load(images[i]).placeholder(com.orange.ocara.R.color.black50).fit().into(photo);
            }

        }

        viewFlipper.setDisplayedChild(selectedIndex);


        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showPrevious();
                Timber.v("index " + viewFlipper.indexOfChild(viewFlipper.getCurrentView()));
                int index = viewFlipper.indexOfChild(viewFlipper.getCurrentView());
                updateTitle(index);
                updateArrows(index);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
                Timber.v("index " + viewFlipper.indexOfChild(viewFlipper.getCurrentView()));
                int index = viewFlipper.indexOfChild(viewFlipper.getCurrentView());
                updateTitle(index);
                updateArrows(index);
            }
        });


    }

    @AfterViews
    protected void setUpTitle() {
        updateTitle(selectedIndex);
        if (icon != null) {
            updateLogo(icon);
        }
    }

    private void updateTitle(int index) {
        if (!StringUtils.isEmpty(titles[index])) {
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


    private static void updateButton(View view, boolean enabled) {
        view.setEnabled(enabled);
        view.setClickable(enabled);
    }

}


