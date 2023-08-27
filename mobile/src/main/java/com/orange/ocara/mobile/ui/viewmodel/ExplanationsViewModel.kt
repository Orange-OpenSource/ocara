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
import com.orange.ocara.domain.interactors.LoadIllustrationsByRuleRef
import com.orange.ocara.domain.models.IllustrationModel
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExplanationsViewModel @Inject constructor(
    private val loadIllustrationsByRuleRef: LoadIllustrationsByRuleRef
) : ViewModel() {

    val isNextAvailable = MutableLiveData<Boolean>()
    val isPreAvailable = MutableLiveData<Boolean>()
    val currentIllustInd = MutableLiveData<Int>()
    lateinit var allIllust: List<IllustrationModel>

    fun loadIllustrations(ruleRef: String, ruleSetRef: String, ruleSetVer: Int) {
        loadIllustrationsByRuleRef.execute(ruleRef, ruleSetRef, ruleSetVer).subscribeAndObserve {
            allIllust = it
            initScreenData()
        }
    }

    private fun initScreenData() {
        currentIllustInd.postValue(0)
        isNextAvailable.postValue(allIllust.size > 1)
        isPreAvailable.postValue(false)
    }

    fun getNextIll() {
        if (currentIllustInd.value!! + 1 < allIllust.size) {
            isNextAvailable.postValue(currentIllustInd.value!! + 2 < allIllust.size)
            currentIllustInd.postValue(currentIllustInd.value!! + 1)
            isPreAvailable.postValue(true)
        }
    }

    fun getPreIll() {
        if (currentIllustInd.value!! > 0) {
            isPreAvailable.postValue(currentIllustInd.value!! >= 2)
            currentIllustInd.postValue(currentIllustInd.value!! - 1)
            isNextAvailable.postValue(true)
        }
    }
}