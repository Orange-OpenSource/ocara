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

package com.orange.ocara.mobile.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.orange.ocara.domain.workers.DemoData
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import timber.log.Timber

@HiltWorker
class DemoWorker @AssistedInject constructor(
        @Assisted context: Context,
        @Assisted params: WorkerParameters,
      private val demoData: DemoData) : CoroutineWorker(context, params) {

    companion object {
        const val Progress = "Progress"
        private const val TAG = "DemoWorker"
    }


    override suspend fun doWork(): Result {

        Timber.d("doWork Called !!")
        val firstUpdate = workDataOf(Progress to 0)
        val lastUpdate = workDataOf(Progress to 100)

        // setProgressAsync(firstUpdate)
        // setProgress(firstUpdate)

        delay(1000)

       // val data: Data = workDataOf("data" to "doWork: is Called + OK")
        val dataBuilder  = Data.Builder()
        dataBuilder.putString("data" , "doWork: is Called + OK")
        dataBuilder.putString("time" , demoData.getData())
        val data = dataBuilder.build()

       // setProgressAsync(lastUpdate)
        //setProgress(lastUpdate)
       // return Result.success(data)

        return Result.success(data)
    }


}