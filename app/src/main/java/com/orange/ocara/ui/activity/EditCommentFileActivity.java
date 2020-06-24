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

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.orange.ocara.R;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

@EActivity(R.layout.activity_edit_comment_file)
public class EditCommentFileActivity extends EditCommentActivity {

    protected static final int REQUEST_PICK_SINGLE_FILE = 1;

    @ViewById(R.id.editCommentLayout)
    LinearLayout editCommentLayout;

    @ViewById(R.id.photo)
    ImageView imageView;

    private File tempAttachment;

    private String pictureFilename;

    @Click(R.id.change_image)
    void onChangeImage() {
        createAttachment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        editCommentLayout.setOnClickListener(view -> hideSoftKeyboard(EditCommentFileActivity.this));

    }

    @Override
    void onValidateComment() {
        if (comment.getAttachment() == null && tempAttachment != null) {
            try {
                pictureFilename = copyAsAttachment(tempAttachment);
                pictureFilename = "file:" + pictureFilename;

                // Save a file: path for use with ACTION_VIEW intents
                comment.setAttachment(pictureFilename);
            } catch (IOException e) {
                Timber.e(e, "Cannot store attachment %s to dir %s", tempAttachment, attachmentDirectory);
            }
        }

        // terminate
        super.onValidateComment();
    }

    @Override
    protected void createAttachment() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PICK_SINGLE_FILE);
    }

    @OnActivityResult(REQUEST_PICK_SINGLE_FILE)
    void onImageSelected(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if(data != null) {
                Uri selectedImageUri = data.getData();

                // Get the path from the Uri
                final String path = getPathFromURI(selectedImageUri);
                if (path != null) {
                    tempAttachment = new File(path);
                    selectedImageUri = Uri.fromFile(tempAttachment);

                    try {
                        pictureFilename = copyAsAttachment(tempAttachment);
                        pictureFilename = "file:" + pictureFilename;

                        // Save a file: path for use with ACTION_VIEW intents
                        comment.setAttachment(pictureFilename);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // Set the image in ImageView
                imageView.setImageURI(selectedImageUri);

                Timber.v("Message=On choosing image result;Path=%s;", pictureFilename);
                handleAttachment();
            }
        } else {
            Timber.e("Message=Failed on choosing image;ResultCode=%d;", resultCode);
            finish();
        }
    }

    @Override
    protected void handleAttachment() {
        imageView.setVisibility(View.VISIBLE);

        if (comment.getAttachment() != null) {
            // override pictureFilename value with those from comment
            pictureFilename = comment.getAttachment();
        }

        Uri uri = Uri.parse(pictureFilename);
        Timber.v("Message=Trying to load image;Icon=%s", pictureFilename);
        Picasso
                .with(this)
                .load(uri)
                .placeholder(R.color.black50)
                .fit()
                .into(imageView);

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        sendBroadcast(mediaScanIntent);
    }

    String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
