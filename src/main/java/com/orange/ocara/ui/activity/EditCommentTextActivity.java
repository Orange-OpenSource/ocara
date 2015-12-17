/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.orange.ocara.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(resName="activity_edit_comment_text")
public class EditCommentTextActivity extends EditCommentActivity {

    @ViewById(resName="editCommentLayout")
    LinearLayout editCommentLayout;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        editCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(EditCommentTextActivity.this);
            }
        });

    }


    @Override
    protected void createAttachment() {
        // no attachment for text comment
    }


    @Override
    protected void handleAttachment() {
        // no attachment for text comment
    }

}
