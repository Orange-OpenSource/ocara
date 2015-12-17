/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.dialog;

import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.Button;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import timber.log.Timber;

@EFragment(resName="dialog_audio_recorder")
public class AudioRecorderDialog extends BaseDialogFragment  implements MediaRecorder.OnInfoListener {


    public static AudioRecorderDialog newInstance(String recordFilename) {
        AudioRecorderDialog dialog = new AudioRecorderDialog_();
        Bundle args = new Bundle();
        args.putString("recordFilename", recordFilename);
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE | DialogFragment.STYLE_NORMAL, 0);
        dialog.setArguments(args);
        return dialog;
    }

    public static interface AudioRecorderDialogListener {
        public void onRecordDialogDismiss(AudioRecorderDialog recordDialog);
    }

    private static final int SHOW_PROGRESS = 2;

    private String recordFilename;

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        releaseRecorder();
        FragmentActivity parent = getActivity();
        if (parent instanceof AudioRecorderDialogListener) {
            ((AudioRecorderDialogListener)parent).onRecordDialogDismiss(this);
        }
    }

    @ViewById(resName="record_pause_button")
    Button recordPauseButton;

    MediaRecorder mediaRecorder;

    boolean isRecording = false;


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseRecorder();
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        // set dialog width
        int width = getResources().getDimensionPixelSize(com.orange.ocara.R.dimen.audioPlayerDialogWidth);
        getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @AfterViews
    public void setArguments() {
        recordFilename = getArguments().getString("recordFilename");
        reset();
        doRecordPause(); // starts recording
    }

    @Click(resName="close_dialog")
    public void closeDialog() {
        getDialog().dismiss();
    }

    @Click(resName="record_pause_button")
    public void recordPauseButtonClicked() {
        doRecordPause();
    }


    public void reset() {
        try {

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mediaRecorder.setOnInfoListener(this);

            if (recordFilename != null) {
                mediaRecorder.setOutputFile(recordFilename);
                mediaRecorder.prepare();
            }
        } catch (IOException e) {
            Timber.e("Could not create file " + recordFilename + " for record.", e);
        }
    }



    protected void releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }


    private void doRecordPause() {
        isRecording = ! isRecording;
        if (isRecording) { // now recording
            mediaRecorder.start();
        } else { // pause
            mediaRecorder.release();
            this.dismiss();
        }
        updatePausePlay();
    }




    public void updatePausePlay() {
        if (isRecording) {
            recordPauseButton.setCompoundDrawablesWithIntrinsicBounds(com.orange.ocara.R.drawable.ic_media_stop, 0, 0, 0);
            recordPauseButton.setText(com.orange.ocara.R.string.audio_recorder_stop);
        } else {
            recordPauseButton.setCompoundDrawablesWithIntrinsicBounds(com.orange.ocara.R.drawable.ic_media_play, 0, 0, 0);
            recordPauseButton.setText(com.orange.ocara.R.string.audio_recorder_rec);
        }
    }



    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {

    }


}