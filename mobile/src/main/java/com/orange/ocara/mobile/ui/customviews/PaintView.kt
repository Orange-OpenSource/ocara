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

package com.orange.ocara.mobile.ui.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.orange.ocara.domain.models.SiteModel
import com.orange.ocara.utils.FileUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class PaintView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var mX = 0f
    private var mY = 0f
    private var mPath: Path? = null
    private val mPaint: Paint = Paint()
    private val paths: ArrayList<FingerPath> = ArrayList()
    private var currentColor = 0
    private var bgColor = DEFAULT_BG_COLOR
    private var strokeWidth = 0
    private var mBitmap: Bitmap? = null
    private var emptyBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    private var emptyCanvas: Canvas? = null
    private val mBitmapPaint: Paint = Paint(Paint.DITHER_FLAG)

    private lateinit var onDrawListen: () -> Unit
    private lateinit var onStartDrawListen: () -> Unit
    private lateinit var onEndDrawListen: () -> Unit

    constructor(context: Context?) : this(context, null) {}

    fun init(metrics: DisplayMetrics) {
        val height = metrics.heightPixels
        val width = metrics.widthPixels
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        emptyBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap!!)
        emptyCanvas = Canvas(emptyBitmap!!)
        currentColor = DEFAULT_COLOR
        strokeWidth = BRUSH_SIZE
    }

    fun clear() {
        bgColor = DEFAULT_BG_COLOR
        paths.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        mCanvas?.drawColor(bgColor)
        for (fp in paths) {
            mPaint.color = fp.color
            mPaint.strokeWidth = fp.strokeWidth.toFloat()
            mPaint.maskFilter = null
            mCanvas?.drawPath(fp.path, mPaint)
        }
        canvas.drawBitmap(mBitmap!!, 0.0f, 0.0f, mBitmapPaint)
        if (paths.isEmpty()) {
            emptyCanvas?.drawColor(bgColor)
            emptyCanvas?.drawBitmap(emptyBitmap!!, 0.0f, 0.0f, mBitmapPaint)
        }

        canvas.restore()
        onDrawListen()
    }

    private fun touchStart(x: Float, y: Float) {
        onStartDrawListen()
        mPath = Path()
        val fp = FingerPath(currentColor, strokeWidth, mPath!!)
        paths.add(fp)
        mPath?.reset()
        mPath?.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath?.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touchUp() {
        onEndDrawListen()
        mPath?.lineTo(mX, mY)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    companion object {
        var BRUSH_SIZE = 10
        val DEFAULT_COLOR: Int = Color.BLACK
        val DEFAULT_BG_COLOR: Int = Color.WHITE
        private const val TOUCH_TOLERANCE = 4f
    }

    fun isBlank(): Boolean {
        return mBitmap!!.sameAs(emptyBitmap)
    }

    fun setOnDraw(onDrawListen: () -> Unit) {
        this.onDrawListen = onDrawListen
    }


    fun setOnStartDraw(onStartDrawListen: () -> Unit) {
        this.onStartDrawListen = onStartDrawListen
    }

    fun setOnEndDraw(onEndDrawListen: () -> Unit) {
        this.onEndDrawListen = onEndDrawListen
    }
    fun saveImage(auditId: Int) {
        val exportFile = File(FileUtils.getAppPath(context) + "/sign" + auditId + ".png")
        exportFile.createNewFile()
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(exportFile.absoluteFile)
            mBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, out)
            Toast.makeText(
                context,
                "File Saved to " + exportFile.absoluteFile,
                Toast.LENGTH_LONG
            )
                .show()
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    init {
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = DEFAULT_COLOR
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.xfermode = null
        mPaint.alpha = 0xff
    }
}

internal class FingerPath(
    var color: Int,
    var strokeWidth: Int,
    var path: Path
)