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
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

fun <T> Single<T>.subscribeAndObserve(onSuccess: Consumer<T>): Disposable {
    // for testing
    EspressoIdlingResource.increment()

    return this.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                // for testing
                EspressoIdlingResource.decrement()

                onSuccess.accept(it)
            }, {
                // for testing
                EspressoIdlingResource.decrement()

                Timber.d(it)
            })
}

fun <T> Single<T>.subscribeAndObserve(onSuccess: Consumer<T>, onError: Consumer<Throwable>? = null): Disposable {
    // for testing
    EspressoIdlingResource.increment()

    return this.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                // for testing
                EspressoIdlingResource.decrement()

                onSuccess.accept(it)
            }, {
                // for testing
                EspressoIdlingResource.decrement()

                Timber.d(it)

                onError?.accept(it)
            })

}

fun <T> Single<T>.subscribeAndObserveOnIo(onSuccess: Consumer<T>): Disposable {
    // for testing
    EspressoIdlingResource.increment()

    return this.observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe({
                // for testing
                EspressoIdlingResource.decrement()

                onSuccess.accept(it)
            }, {
                // for testing
                EspressoIdlingResource.decrement()

                Timber.e(it)

            })
}