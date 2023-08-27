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

package com.orange.ocara.mobile.ui.extensionFunctions

import com.orange.ocara.mobile.ui.testing.EspressoIdlingResource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

fun Completable.subscribeAndObserve(onComplete:()->Unit): DisposableCompletableObserver {
    // for testing
    EspressoIdlingResource.increment()

    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableCompletableObserver(){
                override fun onComplete() {
                    // for testing
                    EspressoIdlingResource.decrement()

                    onComplete()
                }

                override fun onError(e: Throwable) {
                    // for testing
                    EspressoIdlingResource.decrement()

                    Timber.d(e)
                }
            })
}