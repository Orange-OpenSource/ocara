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
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import timber.log.Timber
import java.util.*

/*
    this manager is responsible for recognizing audio coming from the mic , converting it to text
    and sending back the text
 */
class SpeechRecognitionManager(context:Context,private val speechToTextCallback: SpeechToTextCallback) {
    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

    init {
        initSpeechRecognition()
    }

    private fun initSpeechRecognition() {
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
                Timber.d("nnnn onReadyForSpeech")
            }

            override fun onBeginningOfSpeech() {
                Timber.d("nnnn onBeginningOfSpeech")
            }

            override fun onRmsChanged(p0: Float) {
                Timber.d("nnnn onRmsChanged")
            }

            override fun onBufferReceived(p0: ByteArray?) {
                Timber.d("nnnn onBufferReceived")
            }

            override fun onEndOfSpeech() {
                Timber.d("nnnn onEndOfSpeech")
            }

            override fun onError(p0: Int) {
                Timber.d("nnnn onError $p0")
            }

            override fun onResults(results: Bundle?) {
                val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if(data == null){
                    // this means that the recognizer failed for some reason
                    speechToTextCallback.speechToTextResultReceived("")
                    return
                }
                Timber.d("nnnn onResults ${data[0]}")
                speechToTextCallback.speechToTextResultReceived(data[0])
            }

            override fun onPartialResults(p0: Bundle?) {
                val data = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                Timber.d("nnnn onPartialResults ${data!![0]}")
            }

            override fun onEvent(p0: Int, p1: Bundle?) {
                Timber.d("nnnn onEvent")
            }

        })
    }

    fun startListening(){
        speechRecognizer.startListening(speechRecognizerIntent)
    }
    fun stopListening(){
        speechRecognizer.stopListening()
    }
    interface SpeechToTextCallback{
        fun speechToTextResultReceived(result:String)
    }
}