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

import android.app.ProgressDialog
import android.content.Context
import android.media.MediaPlayer
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.showProgressDialog
import com.orange.ocara.utils.FileUtils

/*
    this manager is responsible for playing the audio comment and telling the SpeechRecognitionManager to start listening to it and
    getting the text from it and sending that text back to the fragment
    it takes as arguments the context and speechToTextCallback to send to the SpeechRecognitionManager , and fileName to play
    the audio file from it
 */
class PlayAudioForRecognitionManager(private val context: Context, private val fileName: String, speechToTextCallback: SpeechRecognitionManager.SpeechToTextCallback) {
    private val speechRecognitionManager = SpeechRecognitionManager(context, speechToTextCallback)
    private val mediaPlayer = MediaPlayer()
    private lateinit var progressDialog: ProgressDialog
    init {
        prepareAudioFile()
    }

    private fun prepareAudioFile() {
        mediaPlayer.setDataSource(FileUtils.getAppPath(context) + "/" + fileName)
        mediaPlayer.prepare()
        mediaPlayer.setOnCompletionListener {
            speechRecognitionManager.stopListening()
            progressDialog.dismiss()
        }
    }

    fun startRecognition() {
        speechRecognitionManager.startListening()
        mediaPlayer.start()
        progressDialog = showProgressDialog(context, context.getString(R.string.dont_make_noise))
    }

}