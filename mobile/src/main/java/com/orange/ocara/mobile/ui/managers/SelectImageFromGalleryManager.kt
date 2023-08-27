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
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.dialogs.GenericConfirmationDialog
import com.orange.ocara.mobile.ui.helpers.GalleryHelper
import com.orange.ocara.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SelectImageFromGalleryManager(
    private val fragment: Fragment,
    private val galleryHelper: GalleryHelper,
    private val launcher: ActivityResultLauncher<Intent>
) {
    private val context = galleryHelper.getActivity()?.baseContext
    private val permission = Manifest.permission.READ_EXTERNAL_STORAGE
    private val PERMISSION_CODE = 22
    private val permissionLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        this::onPermissionGranted
    )
    private val PERMISSION_GRANTED = 1
    private val PERMISSION_DENIED = 0
    private val PERMISSION_RATIONAL = -1


    init {
//        checkPermissions()
//        if (!checkPermissions()){
//            ActivityCompat.requestPermissions(fragment.requireActivity(), arrayOf(permission), PERMISSION_CODE)
//        }
    }

    fun selectImage() {
        when (checkPermissions()) {
            PERMISSION_DENIED -> permissionLauncher.launch(permission)
            PERMISSION_GRANTED -> dispatchChooseImageAction()
        }
//        if (checkPermissions() == PERMISSION_DENIED) {
//            permissionLauncher.launch(permission)
//        } else {
//            dispatchChooseImageAction()
////            val intent = Intent()
////            intent.type = "image/*"
////            intent.action = Intent.ACTION_GET_CONTENT
////            launcher.launch(intent)
//        }
    }

    fun createCommentWithImage(uri: Uri) {
        val path = FileUtils.getPath(context, uri)
        val input = File(path)
        val outputFileName = getImageFile()
        val output = File(outputFileName)
        launchCoroutineToCopyImage(input, output, outputFileName)
    }

    private fun launchCoroutineToCopyImage(input: File, output: File, outputFileName: String) {
        galleryHelper.getLifecycleScope().launch(Dispatchers.IO) {
            input.copyTo(output)
            withContext(Dispatchers.Main) {
                galleryHelper.onImageSelectedFromGallery(outputFileName)
            }
        }
    }

    private fun getImageFile(): String {
        val now = System.currentTimeMillis()
        val fileName = "image_$now.jpeg"
        return FileUtils.getAppPath(context) + "/" + fileName
    }

    private fun checkPermissions(): Int {
//        return (ActivityCompat.checkSelfPermission(
//            fragment.requireContext(),
//            permission
//        ) == PackageManager.PERMISSION_GRANTED)

        if ((ActivityCompat.checkSelfPermission(
                fragment.requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    fragment.requireActivity(),
                    permission
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
            title = fragment.getString(R.string.add_photo_comment),
            msg = fragment.getString(R.string.add_photo_permission),
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

    private fun onPermissionGranted(isGranted: Boolean) {
        if (isGranted) {
            // Do if the permission is granted
            dispatchChooseImageAction()
        } else {
            openAppSettingsDialog()
            // Do otherwise
        }
    }

    private fun dispatchChooseImageAction() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        launcher.launch(intent)
    }
}