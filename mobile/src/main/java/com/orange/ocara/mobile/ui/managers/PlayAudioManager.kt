/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara.mobile.ui.managers

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.orange.ocara.mobile.R
import com.orange.ocara.utils.FileUtils
import com.orange.ocara.utils.TimeUtils
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.math.abs

class PlayAudioManager private constructor(builder: Builder) {
    lateinit var mediaPlayer: MediaPlayer
    lateinit var context: Context
    lateinit var playButton: ImageView
    lateinit var timerTv:TextView
    lateinit var seekBar: SeekBar
    lateinit var fileName: String
    // audio super manager will be null when we play the audio in create comment fragment
    // because there will be only one audio
    private var audioSuperManager: PlayAudioSuperManager? = null
    private var timer: Job? = null

    init {
        setAttributes(builder)
        prepareAudioFile()
        prepareSeekBar()
        addClickListenerForPlayButton()
    }

    private fun setAttributes(builder: Builder) {
        this.context = builder.context
        this.seekBar = builder.seekBar
        this.playButton = builder.playButton
        this.fileName = builder.fileName
        this.timerTv = builder.timer
        this.audioSuperManager = builder.audioSuperManager
        this.audioSuperManager?.addAudioManager(this)
    }

    private fun prepareSeekBar() {
        updateAudioBasedOnSeekBar()
    }

    private fun updateSeekBarBasedOnAudio() {
        // it's okay to use global scope because we are canceling the job when fragment.onPause() is called
        timer = GlobalScope.launch {
            while (true) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    timerTv.text = TimeUtils.getTime(mediaPlayer.currentPosition.toLong())
                    seekBar.progress = mediaPlayer.currentPosition
                }
            }
        }
    }

    private fun updateAudioBasedOnSeekBar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val curProgForMedia = mediaPlayer.currentPosition
                if(checkIfChangeIsBig(progress, curProgForMedia)) {
                    timerTv.text = TimeUtils.getTime(progress.toLong())
                    mediaPlayer.seekTo(progress)
                }
            }

            private fun checkIfChangeIsBig(progress: Int, curProgForMedia: Int) = abs(progress - curProgForMedia) > 1000

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    private fun prepareAudioFile() {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(FileUtils.getAppPath(context) + "/" + fileName)
        mediaPlayer.prepare()
        mediaPlayer.setOnCompletionListener {
            audioStopped()
            timerTv.text = TimeUtils.getTime(mediaPlayer.duration.toLong())

        }
        seekBar.max = mediaPlayer.duration
        timerTv.text = TimeUtils.getTime(mediaPlayer.duration.toLong())
    }

    private fun addClickListenerForPlayButton() {
        playButton.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                playAudio()
            } else {
                pauseAudio()
            }
        }
    }

    fun pauseAudio() {
        if (mediaPlayer.isPlaying) {
            playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            mediaPlayer.pause()
            timer?.cancel()
        }
    }

    fun stopAudio() {
        mediaPlayer.stop()
        audioStopped()
    }

    private fun audioStopped() {
        playButton.setContentDescription(context.getString(R.string.content_desc_audio_state_play))
        playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        seekBar.progress = 0
        timer?.cancel()
    }

    fun playAudio() {
        this.audioSuperManager?.playAudio(this)
        playButton.setContentDescription(context.getString(R.string.content_desc_audio_state_stop))
        playButton.setImageResource(R.drawable.ic_baseline_pause_24)
        mediaPlayer.start()
        updateSeekBarBasedOnAudio()
    }


    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        lateinit var context: Context
        lateinit var playButton: ImageView
        lateinit var seekBar: SeekBar
        lateinit var timer: TextView
        lateinit var fileName: String
        var audioSuperManager: PlayAudioSuperManager? = null

        fun timer(timer: TextView): Builder {
            this.timer = timer
            return this
        }

        fun context(context: Context): Builder {
            this.context = context
            return this
        }

        fun playButton(playButton: ImageView): Builder {
            this.playButton = playButton
            return this
        }

        fun seekBar(seekBar: SeekBar): Builder {
            this.seekBar = seekBar
            return this
        }

        fun fileName(fileName: String): Builder {
            this.fileName = fileName
            return this
        }

        fun audioSuperManager(audioSuperManager: PlayAudioSuperManager): Builder {
            this.audioSuperManager = audioSuperManager
            return this
        }

        fun build(): PlayAudioManager {
            return PlayAudioManager(this)
        }
    }
}