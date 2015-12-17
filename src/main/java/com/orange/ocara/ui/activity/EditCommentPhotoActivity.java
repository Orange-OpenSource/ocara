/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.orange.ocara.R;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import timber.log.Timber;

@EActivity(resName="activity_edit_comment_photo")
public class EditCommentPhotoActivity extends EditCommentActivity {

    protected static final int TAKE_PHOTO_REQUEST_CODE = 1;

    @Inject
    Picasso picasso;

    @ViewById(resName="editCommentLayout")
    LinearLayout editCommentLayout;

    @ViewById(resName="photo")
    ImageView commentPhoto;


    private File tempAttachment;
    private String pictureFilename;



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        editCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(EditCommentPhotoActivity.this);
            }
        });

    }

    @Override
    void onValidateComment() {
        if (comment.getAttachment() == null && tempAttachment != null)
            try {
                pictureFilename = copyAsAttachment(tempAttachment);
                pictureFilename = "file:"+ pictureFilename;
                // Save a file: path for use with ACTION_VIEW intents
                comment.setAttachment(pictureFilename);
            } catch (IOException e) {
                Timber.e(e, "Cannot store attachment %s to dir %s", tempAttachment, attachmentDirectory);
            }

        // terminate
        super.onValidateComment();
    }

    @Override
    protected void createAttachment() {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go

            try {
                tempAttachment = createImageFile();


                Timber.v("takePhoto path = %s", tempAttachment);

                // Continue only if the File was successfully created
                if (tempAttachment != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(tempAttachment));
                    startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
                }

            } catch (IOException ex) {
                Timber.e(ex, "Failed to take photo");
            }
        }
    }


    @Override
    protected void handleAttachment() {
        commentPhoto.setVisibility(View.VISIBLE);

        if (comment.getAttachment() != null) {
            // override pictureFilename value with those from comment
            pictureFilename = comment.getAttachment();
        }

        Uri uri = Uri.parse(pictureFilename);
        picasso.load(uri).placeholder(R.color.black50).fit().into(commentPhoto);

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        sendBroadcast(mediaScanIntent);
    }



    @OnActivityResult(TAKE_PHOTO_REQUEST_CODE)
    void onPictureTaken(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
             // Save a file: path for use with ACTION_VIEW intents
             pictureFilename = "file:" + tempAttachment.getAbsolutePath();
             handleAttachment();
        } else {
            finish();
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = makeTimestampedFilename("JPEG");
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }


}
