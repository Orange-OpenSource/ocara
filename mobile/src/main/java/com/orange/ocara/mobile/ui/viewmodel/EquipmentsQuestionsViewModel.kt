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

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.orange.ocara.domain.interactors.*
import com.orange.ocara.domain.models.QuestionAnswerModel
import com.orange.ocara.domain.models.QuestionModel
import com.orange.ocara.domain.models.QuestionRuleAnswer
import com.orange.ocara.domain.models.RuleAnswerModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.extensionFunctions.addTo
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.utils.enums.AuditLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class EquipmentsQuestionsViewModel @Inject constructor(
        private val loadQuestionsTask: LoadQuestionsAndRulesForEquipment
) : ViewModel() {
    var context: Context? = null
//    private var equipmentId: Int = -1
    private val disposables = CompositeDisposable()
    private lateinit var questions: List<QuestionModel>
    private val displayedQuestions = MutableLiveData<List<QuestionModel>>()



    fun addObserverForDisplayedQuestions(observerData: QuestionsViewModel.ObserverData<List<QuestionModel>>) {
        displayedQuestions.observe(observerData.owner, observerData.observer)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
    public fun loadQuestions(rulesetRef : String , objRef : String , rulesetVer : String) {
        loadQuestionsTask.execute(rulesetRef , objRef , rulesetVer).subscribeAndObserve {
            questions = it
            displayedQuestions.postValue(questions)
        }.addTo(disposables)
    }
    data class ObserverData<T>(val owner: LifecycleOwner, val observer: Observer<T>)
}