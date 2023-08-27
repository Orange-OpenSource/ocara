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

import android.annotation.SuppressLint
import android.graphics.*
import android.view.MotionEvent
import android.widget.ImageView
import com.orange.ocara.mobile.ui.helpers.Editable
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ImageDrawerManager(private val imageView: ImageView,
                         private val imagePath: String,
                         private val editable: Editable) {

    var prvX = 0f
    var prvY = 0f
    var bitmapMaster: Bitmap? = null
    var canvasMaster: Canvas? = null
    var paintDraw: Paint? = null
    var stack = Stack<Bitmap>() // this is a stack to hold all previous states of an image to be able to undo changes

    init {
        setupForDrawing()
        setupImageBitmap()
    }

    private fun setupForDrawing() {
        setupPainter()
        setupImageDrawer()
    }

    private fun setupPainter() {
        paintDraw = Paint()
        paintDraw!!.style = Paint.Style.STROKE
        paintDraw!!.color = Color.WHITE
        paintDraw!!.strokeWidth = 10f
    }

    fun setPainterColor(color: Int) {
        paintDraw!!.color = color
    }

    fun getPainterColor(): Int {
        return paintDraw!!.color
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupImageDrawer() {
        imageView.setOnTouchListener { _, event ->
            if (editable.isEditMode()) {
                drawOnImage(event)
            }
            true
        }

    }

    fun save() {
        val stream = FileOutputStream(File(imagePath));
        bitmapMaster?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.close()
        stack.clear()
    }

    private fun drawOnImage(event: MotionEvent) {
        val action = event.action
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                actionStartDrawing(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                actionContinueDrawing(x, y)
            }
            MotionEvent.ACTION_UP -> {
                actionFinishDrawing(x, y)
            }
        }
    }

    private fun actionContinueDrawing(x: Int, y: Int) {
        drawOnProjectedBitMap(x.toFloat(), y.toFloat())
        prvX = x.toFloat()
        prvY = y.toFloat()
    }

    private fun actionStartDrawing(x: Int, y: Int) {
        addBitmapToStack()
        prvX = x.toFloat()
        prvY = y.toFloat()
        drawOnProjectedBitMap(x.toFloat(), y.toFloat())
    }

    private fun actionFinishDrawing(x: Int, y: Int) {
        drawOnProjectedBitMap(x.toFloat(), y.toFloat())
    }

    private fun drawOnProjectedBitMap(x: Float, y: Float) {
        canvasMaster?.drawLine(prvX, prvY, x, y, paintDraw!!)
        imageView.invalidate()
    }

    private fun addBitmapToStack() {
        stack.push(bitmapMaster?.copy(Bitmap.Config.ARGB_8888, true))
    }

    fun cancelAllDrawingMade() {
        setupImageBitmap()
        stack.clear()
    }

    private fun setupImageBitmap() {
        bitmapMaster = BitmapFactory.decodeFile(imagePath)
        // we scale the image to be of the same size as the view as this is essential for drawing
        bitmapMaster = Bitmap.createScaledBitmap(bitmapMaster!!, imageView.width, imageView.height, true)
        bitmapMaster = bitmapMaster?.copy(Bitmap.Config.ARGB_8888, true)
        canvasMaster = Canvas(bitmapMaster!!)
        canvasMaster?.drawBitmap(bitmapMaster!!, 0f, 0f, null)

        imageView.setImageBitmap(bitmapMaster)
        //  imageView.adjustViewBounds = true
    }

    fun onBack(): Boolean {
        if (!stack.empty()) {
            setLastBitmap()
            return true
        }
        return false
    }

    private fun setLastBitmap() {
        bitmapMaster = stack.pop()
        canvasMaster?.setBitmap(bitmapMaster)
        imageView.setImageBitmap(bitmapMaster)
    }
}