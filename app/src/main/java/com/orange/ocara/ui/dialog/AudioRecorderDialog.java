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

package com.orange.ocara.ui.dialog;

import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.orange.ocara.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import timber.log.Timber;

@EFragment(R.layout.dialog_audio_recorder)
public class AudioRecorderDialog extends BaseDialogFragment implements MediaRecorder.OnInfoListener {


    @ViewById(resName = "record_pause_button")
    Button recordPauseButton;
    MediaRecorder mediaRecorder;
    boolean isRecording = false;
    private String recordFilename;

    public static AudioRecorderDialog newInstance(String recordFilename) {
        AudioRecorderDialog dialog = new AudioRecorderDialog_();
        Bundle args = new Bundle();
        args.putString("recordFilename", recordFilename);
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        releaseRecorder();
        FragmentActivity parent = getActivity();
        if (parent instanceof AudioRecorderDialogListener) {
            ((AudioRecorderDialogListener) parent).onRecordDialogDismiss(this);
        }
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

    @Click(resName = "close_dialog")
    public void closeDialog() {
        getDialog().dismiss();
    }

    @Click(resName = "record_pause_button")
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
            Timber.e(e, "Could not create file " + recordFilename + " for record.");
        }
    }

    protected void releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void doRecordPause() {
        isRecording = !isRecording;
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


    public interface AudioRecorderDialogListener {
        void onRecordDialogDismiss(AudioRecorderDialog recordDialog);
    }


}