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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.FileProvider;

import com.orange.ocara.BuildConfig;
import com.orange.ocara.R;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;

/**
 * a View that allows a user to take a photo and add a comment to it
 */
@EActivity(R.layout.activity_edit_comment_photo)
public class EditCommentPhotoActivity extends EditCommentActivity {

    protected static final int TAKE_PHOTO_REQUEST_CODE = 1;

    @ViewById(R.id.editCommentLayout)
    LinearLayout editCommentLayout;

    @ViewById(R.id.photo)
    ImageView imageView;

    private File tempAttachment;

    private String pictureFilename;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        editCommentLayout.setOnClickListener(view -> hideSoftKeyboard(EditCommentPhotoActivity.this));

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

    /**
     * Function that starts an {@link Intent} in order to take picture and store it
     *
     * Be careful : ACTION_IMAGE_CAPTURE implementations have a history of bugs. These are not
     * necessarily tied to Android OS version, so we have to distinguish between {@link VERSION_CODES}.
     *
     * @link https://commonsware.com/blog/2015/06/08/action-image-capture-fallacy.html
     */
    @Override
    protected void createAttachment() {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            try {
                tempAttachment = createImageFile();

                Timber.i("Message=Taking photo;TargetPath=%s", tempAttachment);

                // Continue only if the File was successfully created
                if (tempAttachment != null) {
                    /**
                     * Using FileProvider with a location on external storage may be an interesting
                     * approach to EXTRA_OUTPUT. Android N is starting to ban file: Uri values, so
                     * FileProvider is definitely a fine answer.
                     *
                     * However, there are many camera apps that will have problems with content: Uri
                     * values, because the developers of those camera apps did not expect such
                     * values. And there is ABSOLUTELY no way for an app to advertise what schemes
                     * it supports for an extra... Even the <intent-filter> (that can advertise
                     * supported schemes) does not work for extras.
                     *
                     * @link https://stackoverflow.com/questions/38291062/taking-photo-always-returns-result-canceled-0-in-onactivityresult
                     */
                    Uri uri;
                    if (VERSION.SDK_INT >= VERSION_CODES.N) {
                        Timber.i("Message=Configuring intent for version >= VERSION_CODES.N");
                        uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", tempAttachment);
                    } else {
                        Timber.i("Message=Configuring intent for version < VERSION_CODES.N");
                        uri = Uri.fromFile(tempAttachment);
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
                }

            } catch (IOException e) {
                Timber.e(e, "Failed to take photo", e);
            }
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

    /**
     * dispatching the result of the operation of taking a picture
     *
     * @param resultCode may equal {@link android.app.Activity#RESULT_OK} or {@link android.app.Activity#RESULT_CANCELED}
     * @param intent an {@link Intent}
     */
    @OnActivityResult(TAKE_PHOTO_REQUEST_CODE)
    void onPictureTaken(int resultCode, Intent intent) {

        if (resultCode == RESULT_OK) {
            // Save a file: path for use with ACTION_VIEW intents
            pictureFilename = "file:" + tempAttachment.getAbsolutePath();
            Timber.v("Message=On taking photo result;Path=%s;", pictureFilename);
            handleAttachment();
        } else {
            Timber.e("Message=Failed on taking photo;ResultCode=%d;", resultCode);
            finish();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = makeTimestampedFilename("JPEG");
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);


        File image = null;
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
            }
        } else {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        }

        return image;
    }
}
