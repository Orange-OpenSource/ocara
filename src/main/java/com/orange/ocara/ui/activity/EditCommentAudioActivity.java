/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.orange.ocara.ui.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.orange.ocara.ui.dialog.AudioRecorderDialog;
import com.orange.ocara.ui.fragment.AudioPlayerFragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

@EActivity(resName="activity_edit_comment_audio")
public class EditCommentAudioActivity extends EditCommentActivity implements AudioRecorderDialog.AudioRecorderDialogListener{


    private static final int RECORD_AUDIO_REQUEST_CODE = 2;


    /**
     * The file recorded
     */
    private File tempAttachment;

    /**
     * the file to play (from recorder or attachment)
     */
    private String audioFilename;

    @ViewById(resName="editCommentLayout")
    LinearLayout editCommentLayout;

    @ViewById(resName="validate_comment")
    Button validateComment;

    @ViewById(resName="start_playing_recording")
    Button startPlayingRecordingButton;


    @FragmentById(resName="comment_audio_player")
    AudioPlayerFragment audioPlayerFragment;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        editCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(EditCommentAudioActivity.this);
            }
        });

    }

    @Click(resName="start_playing_recording")
    void startPlayingRecording() {
            launchRecorderCustom();
    }

    @Click(resName="validate_comment")
    void onValidateComment() {

        if (!hasAttachment() && tempAttachment != null) {

            // copy recorded attachment in correct audit folder

            String attachmentFileName = null;
            try {

                String shortFilename = makeTimestampedFilename("REC") + ".amr";
                attachmentFileName = copyAsAttachment(tempAttachment, shortFilename);
                attachmentFileName = "file:" + attachmentFileName;
                tempAttachment.delete();

            } catch (IOException e) {
                Timber.e(e, "Cannot store attachment %s to dir %s", tempAttachment, attachmentDirectory);
            }
            comment.setAttachment(attachmentFileName);
        }
        // terminate
        super.onValidateComment();
    }

    @Override
    protected void createAttachment() {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(audioPlayerFragment);
        transaction.commit();
        validateComment.setEnabled(false);

        // new :
        launchRecorder();

    }


    @Override
    protected void handleAttachment() {

        if (comment.getAttachment() != null) {
            // override recordFilename value with those from comment
            audioFilename = comment.getAttachment();
        }
        Uri audioAttachment = Uri.parse(audioFilename);
        audioPlayerFragment.setUri(audioAttachment);
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.show(audioPlayerFragment);
        transaction.commit();
        validateComment.setEnabled(true);

    }







    @OnActivityResult(RECORD_AUDIO_REQUEST_CODE)
    void onAudioRecorded(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            String sourcePath = getRealPathFromURI(intent.getData());
            tempAttachment = new File(sourcePath);
            audioFilename = sourcePath;
            handleAttachment();
        } else {
            finish();
        }
    }







    /**
     * from:
     * http://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore
     */
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void launchRecorder() {

        final Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, RECORD_AUDIO_REQUEST_CODE);
        }else{
            startPlayingRecordingButton.setVisibility(View.VISIBLE);
        }
    }


    private void launchRecorderCustom() {
        try {
            tempAttachment = createAudioFile();
            FragmentManager fm = getSupportFragmentManager();
            startPlayingRecordingButton.setVisibility(View.GONE);
            AudioRecorderDialog audioRecorderDialog = AudioRecorderDialog.newInstance(tempAttachment.getAbsolutePath());
            audioRecorderDialog.show(fm, "fragment_edit_name");
        } catch (IOException e) {
            Timber.e(e, "Cannot create audio attachment ");
        }
    }

    @Override
    public void onRecordDialogDismiss(AudioRecorderDialog recordDialog) {
        audioFilename = tempAttachment.getAbsolutePath();
        handleAttachment();

    }

    private File createAudioFile() throws IOException {
        // Create an image file name
        String imageFileName = makeTimestampedFilename("REC");
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".amr",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    private boolean hasAttachment() {
        return comment.getAttachment() != null;
    }
}
