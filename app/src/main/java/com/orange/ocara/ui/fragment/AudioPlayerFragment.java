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

package com.orange.ocara.ui.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;

import lombok.Getter;
import timber.log.Timber;

@EFragment(resName = "fragment_audio_player")
public class AudioPlayerFragment extends BaseFragment implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {


    private static final int SHOW_PROGRESS = 2;
    @ViewById(resName = "play_pause_button")
    ImageButton playPauseButton;
    @ViewById(resName = "total_time")
    TextView totalTime;
    @ViewById(resName = "current_time")
    TextView currentTime;
    @ViewById(resName = "audio_player_progress")
    ProgressBar progressBar;
    MediaPlayer mediaPlayer;
    @Getter
    private Uri uri;
    private boolean draggingProgressBar = false;
    private Handler handler = new MessageHandler(this);
    private StringBuilder formatBuilder;
    private Formatter formatter;
    private View.OnClickListener playPauseListener = new View.OnClickListener() {
        public void onClick(View v) {
            doPlayPause();
        }
    };
    // There are two scenarios that can trigger the seekbar listener to trigger:
    //
    // The first is the user using the touchpad to adjust the posititon of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed by
    // a number of onProgressChanged notifications, concluded by onStopTrackingTouch.
    // We're setting the field "draggingProgressBar" to true for the duration of the dragging
    // session to avoid jumps in the position in case of ongoing playback.
    //
    // The second scenario involves the user operating the scroll ball, in this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch notifications,
    // we will simply apply the updated position without suspending regular updates.
    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {

            draggingProgressBar = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            handler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (mediaPlayer == null) {
                return;
            }

            if (!fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }


            long duration = mediaPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mediaPlayer.seekTo((int) newposition);
            updateCurrentTime((int) newposition);
        }


        public void onStopTrackingTouch(SeekBar bar) {
            draggingProgressBar = false;
            updateProgress();
            updatePausePlay();

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            handler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };




    @Override
    public void onResume() {
        super.onResume();
        initPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    void initPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

        playPauseButton.setOnClickListener(playPauseListener);

        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());

        if (progressBar instanceof SeekBar) {
            SeekBar seeker = (SeekBar) progressBar;
            seeker.setOnSeekBarChangeListener(mSeekListener);
        }
        progressBar.setMax(1000);

        reset();
    }

    public void setUri(Uri uri) {
        this.uri = uri;
        if (mediaPlayer != null) {
            reset();
        }
    }

    public void reset() {
        try {
            mediaPlayer.reset();
            if (uri != null) {
                mediaPlayer.setDataSource(getActivity(), uri);
                mediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
            Timber.e(e, "Could not open file " + uri + " for playback.");
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Timber.v("Media player prepared %s", uri);
        updatePausePlay();
        updateProgress();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        reset();
    }

    private void doPlayPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
            // cause the progress bar to be updated
            handler.sendEmptyMessage(SHOW_PROGRESS);
        }
        updatePausePlay();
    }

    public void updatePausePlay() {
        if (mediaPlayer.isPlaying()) {
            playPauseButton.setImageResource(com.orange.ocara.R.drawable.ic_media_pause);
        } else {
            playPauseButton.setImageResource(com.orange.ocara.R.drawable.ic_media_play);
        }
    }

    private int updateProgress() {
        int position = mediaPlayer.getCurrentPosition();
        int duration = mediaPlayer.getDuration();
        if (progressBar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                progressBar.setProgress((int) pos);
            }
        }

        if (totalTime != null)
            totalTime.setText(stringForTime(duration));

        updateCurrentTime(position);

        return position;
    }

    private void updateCurrentTime(int position) {
        if (currentTime != null)
            currentTime.setText(stringForTime(position));
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        formatBuilder.setLength(0);
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private static class MessageHandler extends Handler {

        private final WeakReference<AudioPlayerFragment> mAudioPlayerFragmentWeakReference;

        MessageHandler(AudioPlayerFragment mediaPlayer) {
            super();
            mAudioPlayerFragmentWeakReference = new WeakReference<>(mediaPlayer);
        }

        @Override
        public void handleMessage(Message msg) {
            AudioPlayerFragment audioPlayerFragment = mAudioPlayerFragmentWeakReference.get();
            if (audioPlayerFragment != null) {
                MediaPlayer mediaPlayer = audioPlayerFragment.mediaPlayer;
                if (mediaPlayer == null) {
                    return;
                }

                int pos;
                if (msg.what == SHOW_PROGRESS) {
                    pos = audioPlayerFragment.updateProgress();
                    if (!audioPlayerFragment.draggingProgressBar && mediaPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }

                }
            }
        }
    }

}
