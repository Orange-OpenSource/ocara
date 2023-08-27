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

package com.orange.ocara.mobile.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.EmptyResultSetException
import com.orange.ocara.domain.interactors.CheckDefaultRulesetIsInserted
import com.orange.ocara.domain.interactors.GetNumberOfAudits
import com.orange.ocara.domain.interactors.GetTheLastThreeAuditsInserted
import com.orange.ocara.domain.interactors.SaveDefaultRuleset
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val saveDefaultRuleset: SaveDefaultRuleset,
    private val checkDefaultRulesetIsInserted: CheckDefaultRulesetIsInserted,
    private val getNumberOfAudits: GetNumberOfAudits
) : ViewModel() {

    var disposables = CompositeDisposable()

    fun saveDefaultRuleset() {
        checkDefaultRulesetIsInserted.execute()
            .subscribeAndObserve({
                if (it == 0) {
                    insertDefaultRuleset()
                } else {
                    Timber.d("default ruleset already inserted")
                }
            }, {
                if (it is EmptyResultSetException) {
                    // if the error is of type EmptyResultSetException
                    // then it means that default ruleset isn't saved
                    insertDefaultRuleset()
                }
            })

    }

    fun getNumberOfAudits(): Single<Int> {
        return getNumberOfAudits.execute();
    }

    private fun insertDefaultRuleset() {
        saveDefaultRuleset.execute()
            .subscribeAndObserve {
                Timber.d("default ruleset saved")
            }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}