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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.dialogs.GenericConfirmationDialog
import com.orange.ocara.mobile.ui.helpers.CameraHelper
import com.orange.ocara.utils.FileUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class TakeCameraImageManager(
    private val fragment: Fragment,
    private val launcher: ActivityResultLauncher<Intent>,
    private val cameraHelper: CameraHelper
) {
    private val permission = Manifest.permission.CAMERA
    private val PERMISSION_CODE = 100
    private lateinit var imagePath : String

    private val PERMISSION_GRANTED = 1
    private val PERMISSION_DENIED = 0
    private val PERMISSION_RATIONAL = -1
//    private lateinit var imageUri : Uri
    private val permissionLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        this::onPermissionGranted
    )

    //    { isGranted ->
//        if (isGranted) {
//            // Do if the permission is granted
//            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
//            launcher.launch(intent)
//        }
//        else {
//            // Do otherwise
//        }
//    }
    init {
//        if (!checkPermissions()) {
//            requestPermissions(fragment.requireActivity(), arrayOf(permission), PERMISSION_CODE)
//        }

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
            title = fragment.getString(R.string.add_image_comment),
            msg = fragment.getString(R.string.take_image_permission),
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
//            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
//            launcher.launch(intent)
            dispatchTakePictureIntent()
        } else {
            // Do otherwise
            openAppSettingsDialog()
        }
    }

    //
    fun takeImage() {
        when (checkPermissions()) {
            PERMISSION_DENIED -> permissionLauncher.launch(permission)
            PERMISSION_GRANTED -> dispatchTakePictureIntent()
        }
//        if (!checkPermissions()) {
//            permissionLauncher.launch(permission)
//        } else {
////            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
////            launcher.launch(intent)
//            dispatchTakePictureIntent()
//        }
    }

    fun imageTaken() {
//        val photo = data.extras!!.get("data") as Bitmap
//        cameraHelper.imageSaved(saveImage(photo))

        val sd: File = Environment.getExternalStorageDirectory()
        val image: File = File(imagePath)
        val bmOptions = BitmapFactory.Options()
        var bitmap = BitmapFactory.decodeFile(image.absolutePath, bmOptions)
        bitmap = rotateBitmap(bitmap , 90f)

        try {
            FileOutputStream(imagePath).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        cameraHelper.imageSaved(imagePath)
    }

    private fun saveImage(bitmap: Bitmap): String {
        val imagePath = getImageFile()
        val stream = FileOutputStream(File(imagePath))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.close()
        return imagePath
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imagePath = getImageFile()
        // Create the File where the photo should go
        var photoFile: File? = File(imagePath)
        // Continue only if the File was successfully created
        if (photoFile != null) {
            var imageUri : Uri = FileProvider.getUriForFile(
                fragment.requireContext(),
                fragment.requireContext().getPackageName() + ".fileprovider",
                photoFile
            )

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            launcher.launch(takePictureIntent)
        }

    }


    private fun getImageFile(): String {
        val now = System.currentTimeMillis()
        val fileName = "image_$now.jpeg"
//        val fileName = "image_$now"
        return FileUtils.getAppPath(fragment.requireContext()) + "/" + fileName
    }


    fun rotateBitmap(bitmapOrg: Bitmap, degree: Float): Bitmap {
        Matrix().let { matrix ->
            matrix.postRotate(degree)
            val scaledBitmap =
                    Bitmap.createScaledBitmap(bitmapOrg, bitmapOrg.width, bitmapOrg.height, true)
            return Bitmap.createBitmap(
                    scaledBitmap,
                    0,
                    0,
                    scaledBitmap.width,
                    scaledBitmap.height,
                    matrix,
                    true
            )
        }

    }
}