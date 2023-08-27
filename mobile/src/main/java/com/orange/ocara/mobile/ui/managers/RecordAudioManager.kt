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

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.SystemClock
import android.provider.Settings
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.dialogs.GenericConfirmationDialog
import com.orange.ocara.mobile.ui.helpers.OnAudioRecordFinished
import com.orange.ocara.utils.FileUtils


class RecordAudioManager(builder: Builder) {
    private val fragment = builder.fragment
    private val onAudioRecordFinished = builder.onAudioRecordFinished
    private val recordButton = builder.recordButton
    private val labelTV = builder.labelTV
    private val recordTimer = builder.recordTimer
    private val recordPermission = Manifest.permission.RECORD_AUDIO
    private val PERMISSION_CODE = 21
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false

    lateinit var fileName: String

    private val PERMISSION_GRANTED = 1
    private val PERMISSION_DENIED = 0
    private val PERMISSION_RATIONAL = -1
    private val permissionLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            this::onPermissionGranted
    )

    init {
        initClickListeners()

//        if (!checkPermissions()) {
//            ActivityCompat.requestPermissions(
//                fragment.requireActivity(),
//                arrayOf(recordPermission),
//                PERMISSION_CODE
//            )
//        }

    }


    private fun checkPermissions(): Int {
//        return (ActivityCompat.checkSelfPermission(
//            fragment.requireContext(),
//            permission
//        ) == PackageManager.PERMISSION_GRANTED)

        if ((ActivityCompat.checkSelfPermission(
                        fragment.requireContext(),
                        recordPermission
                ) != PackageManager.PERMISSION_GRANTED)
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            fragment.requireActivity(),
                            recordPermission
                    )
            ) {

                openAppSettingsDialog()
                return PERMISSION_RATIONAL
            }
            return PERMISSION_DENIED
        } else
            return PERMISSION_GRANTED
    }

    private fun openAppSettingsDialog() {
        val confirmDialog = GenericConfirmationDialog(
                title = fragment.getString(R.string.record_comment),
                msg = fragment.getString(R.string.record_comment_permission),
                confirmString = fragment.getString(R.string.confirm),
                cancelString = fragment.getString(R.string.cancel),
                onConfirmed = this::openAppSettings, {}
        )
        confirmDialog.show(fragment.childFragmentManager, "ConfirmLockDialog")
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri =
                Uri.fromParts("package", fragment.requireContext().getPackageName(), null)
        intent.data = uri
        fragment.startActivity(intent)
    }

    private fun initClickListeners() {
        recordButton.setOnClickListener {
            if (isRecording) {
                fragment.requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)
                stopRecording()
            } else {
                val orientation = fragment.requireActivity().getResources().getConfiguration().orientation
                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    fragment.requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                } else  {
                    fragment.requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
                }
                onStartRecording()
            }
        }
    }

    private fun onPermissionGranted(isGranted: Boolean) {
        if (isGranted) {
            // Do if the permission is granted
//            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
//            launcher.launch(intent)
            performRecord()
        } else {
            // Do otherwise
            openAppSettingsDialog()
        }
    }

    private fun onStartRecording() {
        when (checkPermissions()) {
            PERMISSION_DENIED -> permissionLauncher.launch(recordPermission)
            PERMISSION_GRANTED -> performRecord()
        }
//        if (!checkPermissions()) {
//            permissionLauncher.launch(recordPermission)
//        } else {
//            performRecord()
//        }
    }

    private fun performRecord() {
        recordButton.setContentDescription(fragment.getString(R.string.content_desc_record_stop))
        recordButton.setImageResource(R.drawable.recording)
        labelTV.text = fragment.getText(R.string.press_to_stop_recording)
        recordTimer.base = SystemClock.elapsedRealtime()
        recordTimer.start()
        startMediaRecorder()
        invertRecordingStat()

    }

    private fun stopRecording() {
        recordButton.setContentDescription(fragment.getString(R.string.content_desc_record_start))
        recordButton.setImageResource(R.drawable.mic2)
        labelTV.text = fragment.getText(R.string.press_to_start_recording)
        recordTimer.stop()
        stopMediaRecorder()
        onAudioRecordFinished.onAudioRecordFinished(fileName)
        invertRecordingStat()

    }

    private fun stopMediaRecorder() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
    }

    private fun startMediaRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setOutputFile(FileUtils.getAppPath(fragment.requireActivity().baseContext) + "/" + getRecordFile())
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder?.prepare()
        mediaRecorder?.start()
    }


    private fun getRecordFile(): String {
        val now = System.currentTimeMillis()
        fileName = "Recording_$now.3gp"
        return fileName
    }

    private fun invertRecordingStat() {
        isRecording = !isRecording
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        lateinit var fragment: Fragment
        lateinit var onAudioRecordFinished: OnAudioRecordFinished
        lateinit var recordButton: ImageView
        lateinit var recordTimer: Chronometer
        lateinit var labelTV: TextView

        fun fragment(fragment: Fragment): Builder {
            this.fragment = fragment
            return this
        }

        fun labelTV(labelTV: TextView): Builder {
            this.labelTV = labelTV
            return this
        }

        fun onAudioRecordFinished(onAudioRecordFinished: OnAudioRecordFinished): Builder {
            this.onAudioRecordFinished = onAudioRecordFinished
            return this
        }

        fun recordButton(recordButton: ImageView): Builder {
            this.recordButton = recordButton
            return this
        }

        fun recordTimer(recordTimer: Chronometer): Builder {
            this.recordTimer = recordTimer
            return this
        }

        fun build(): RecordAudioManager {
            return RecordAudioManager(this)
        }
    }
}