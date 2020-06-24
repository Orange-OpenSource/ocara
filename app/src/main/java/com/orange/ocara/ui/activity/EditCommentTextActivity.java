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

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.orange.ocara.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_edit_comment_text)
public class EditCommentTextActivity extends EditCommentActivity {

    @ViewById(R.id.editCommentLayout)
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
