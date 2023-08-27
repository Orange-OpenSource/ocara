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

package com.orange.ocara.mobile.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.orange.ocara.mobile.R
import com.orange.ocara.utils.enums.Answer
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


fun setFocusColorForTextField(editText: TextView, activity: Activity) {
    editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        ViewCompat.setBackgroundTintList(editText, if (hasFocus) ColorStateList.valueOf(activity.getColor(R.color.orange))
        else ColorStateList.valueOf(Color.DKGRAY))
    }
}

fun setFocusBackgroundForTextField(editText: TextView, context: Context) {
    editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            ViewCompat.setBackground(editText, ResourcesCompat.getDrawable(context.resources, R.drawable.edit_text_bg_focus, null))
        }
        else{
            ViewCompat.setBackground(editText, ResourcesCompat.getDrawable(context.resources, R.drawable.edit_text_background, null))
        }
    }
}

fun showProgressDialog(context: Context, msg: String): ProgressDialog {
    Timber.d("ActivityMessage=showing progress dialog")
    val mDownloadRuleSetProgressDialog = ProgressDialog(context)
    mDownloadRuleSetProgressDialog.setCanceledOnTouchOutside(false)
    mDownloadRuleSetProgressDialog.setCancelable(false)
    mDownloadRuleSetProgressDialog.setMessage(msg)
    mDownloadRuleSetProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
    mDownloadRuleSetProgressDialog.isIndeterminate = true
    mDownloadRuleSetProgressDialog.show()
    return mDownloadRuleSetProgressDialog
}


fun loadImageIntoView(iconName: String, imageView: ImageView) {
    val path: String = imageView.context.externalCacheDir.toString() + File.separator + iconName
    val icon = File(path)
    Picasso.with(imageView.context)
            .load(icon)
            .placeholder(AppCompatResources.getDrawable(imageView.context, R.drawable.placeholder))
            .into(imageView)
}

fun loadImageIntoView(iconName: String, imageView: ImageView, placeHolder: Int) {
    val path: String = imageView.context.externalCacheDir.toString() + File.separator + iconName
    val icon = File(path)
    Picasso.with(imageView.context)
            .load(icon)
            .placeholder(AppCompatResources.getDrawable(imageView.context, placeHolder))
            .into(imageView)
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat.getDateInstance()
    return format.format(date)
}

fun convertLongToDateTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
    return format.format(date)
}

fun convertLongToDateTimeAuditList  (time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("MM/dd/yyyy HH:mm")
    return format.format(date)
}

fun getIconFromAnswer(answer: Answer): Int {
    return when (answer) {
        Answer.OK -> R.drawable.ok_answer
        Answer.DOUBT -> R.drawable.doubt_answer
        Answer.NOK -> R.drawable.no_answer
        else -> R.drawable.na_answer
    }
}

@SuppressLint("ClickableViewAccessibility")
fun setOnClickListenerForDrawableInTextField(editText: TextView, drawablePosition: DrawablePosition, action: () -> Unit) {


    editText.setOnTouchListener(View.OnTouchListener { view, event ->

        val drawableWidth = editText.compoundDrawables[drawablePosition.ordinal].bounds.width()


        if (event.action == MotionEvent.ACTION_UP) {

            val condition = when (drawablePosition) {
                DrawablePosition.DRAWABLE_RIGHT -> event.x >= (editText.width - drawableWidth)
                DrawablePosition.DRAWABLE_LEFT -> event.x <= (editText.width - drawableWidth)
                DrawablePosition.DRAWABLE_TOP -> event.x >= (editText.width - drawableWidth)
                DrawablePosition.DRAWABLE_BOTTOM -> event.x >= (editText.width - drawableWidth)
            }
            if (condition) {

                action()
            }

            return@OnTouchListener true
        }
        return@OnTouchListener false
    })
}

enum class DrawablePosition {
    DRAWABLE_LEFT,
    DRAWABLE_TOP,
    DRAWABLE_RIGHT,
    DRAWABLE_BOTTOM
}