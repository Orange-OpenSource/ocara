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

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.orange.ocara.data.cache.database.NonTables.RulesetRefAndVersion
import com.orange.ocara.domain.interactors.*
import com.orange.ocara.domain.models.QuestionAnswerModel
import com.orange.ocara.domain.models.QuestionRuleAnswer
import com.orange.ocara.domain.models.RuleAnswerModel
import com.orange.ocara.mobile.ui.extensionFunctions.addTo
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.utils.enums.AuditLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val insertRuleAnswersWithAuditEquipmentId: InsertRuleAnswersWithAuditEquipmentId,
    private val updateAuditEquipmentLastAnsweredQuestion: UpdateAuditEquipmentLastAnsweredQuestion,
    private val loadAuditLevel: LoadAuditLevel,
    private val loadAuditEquipmentQuestions: LoadAuditEquipmentQuestions,
    private val getAuditIdFromAuditEquipmentId: GetAuditIdFromAuditEquipmentId,
    private val getRuleSetDataForEquipmentId: GetRuleSetDataForEquipmentId,
    private val getAuditEquipmentNameAndIcon: GetAuditEquipmentNameAndIcon
) : ViewModel() {
    companion object {
        const val VALIDATE_LABEL = 1
        const val NEXT_LABEL = 2

        const val SAVE_FAIL = -1
        const val SAVE_FOR_VALIDATE_SUCC_RES = 1
        const val SAVE_FOR_BACK_SUCC_RES = 2

    }

    private lateinit var mode: AuditLevel
    private var auditEquipmentId: Int = -1
    private val disposables = CompositeDisposable()
    private lateinit var questions: List<QuestionAnswerModel>
    private val displayedQuestions = MutableLiveData<List<QuestionAnswerModel>>()
    private var label = MutableLiveData<Int>()
    private var backBtnVis = MutableLiveData<Int>()
    private lateinit var equipmentName: String
    private lateinit var equipmentIconPath: String
    private var currentQuestionIndex: Int = -1
    private val answersSavedLiveData = MutableLiveData<Int>()
    private val auditIdLiveData = MutableLiveData<Int>()
    private val ruleSetLiveData = MutableLiveData<RulesetRefAndVersion>()

    private lateinit var ruleSetData: RulesetRefAndVersion

    fun getRulSetData() = ruleSetData

    fun getCurrentQuestionIndex(): Int = currentQuestionIndex

    fun getNumberOfQuestion(): Int = questions.size
    fun getMode(): AuditLevel = mode
    fun getEquipmentName(): String = equipmentName

    private fun setCurrentIndex(newValue: Int) {
        if (mode == AuditLevel.EXPERT) {
            displayAllQuestions()
        } else {
            currentQuestionIndex = newValue
            displayNewQuestion()
        }
        updateNavBtns()
    }

    private fun displayNewQuestion() {
        val currentQuestionInList = ArrayList<QuestionAnswerModel>()
        currentQuestionInList.add(questions[currentQuestionIndex])
        displayedQuestions.postValue(currentQuestionInList)
    }

    private fun displayAllQuestions() {
        displayedQuestions.postValue(questions)
    }


    private fun updateNavBtns() {
        updateNxtBtn()
        updatePreBtn()
    }

    private fun updatePreBtn() {
        if (checkIfThereIsPreQuestions())
            backBtnVis.postValue(View.VISIBLE)
        else
            backBtnVis.postValue(View.GONE)
    }

    private fun updateNxtBtn() {
        if (checkIfThereIsNoMoreQuestions())
            label.postValue(VALIDATE_LABEL)
        else
            label.postValue(NEXT_LABEL)
    }

    fun saveAnswers(saveforValidate: Boolean) {
        val questionRuleAnswers = getQuestionRuleAnswers()
        val idx = if (saveforValidate) 0 else currentQuestionIndex
        insertRuleAnswersWithAuditEquipmentId.execute(questionRuleAnswers)
            .concatWith(updateAuditEquipmentLastAnsweredQuestion.execute(auditEquipmentId, idx))
            .subscribeAndObserve {
                if (saveforValidate)
                    answersSavedLiveData.postValue(SAVE_FOR_VALIDATE_SUCC_RES)
                else
                    answersSavedLiveData.postValue(SAVE_FOR_BACK_SUCC_RES)
            }.addTo(disposables)
    }

    private fun getQuestionRuleAnswers(): List<QuestionRuleAnswer> {
        val result = ArrayList<QuestionRuleAnswer>()
        for (question in questions) {
            addQuestionRules(question, result)
        }
        return result
    }

    private fun addQuestionRules(
        question: QuestionAnswerModel,
        result: ArrayList<QuestionRuleAnswer>
    ) {
        for (rule in question.ruleAnswers) {
            result.add(getQuestionRuleAnswer(rule, question))
        }
    }

    private fun getQuestionRuleAnswer(rule: RuleAnswerModel, question: QuestionAnswerModel) =
        QuestionRuleAnswer.QuestionRuleAnswerBuilder()
            .setAuditEquipmentId(auditEquipmentId)
            .setAnswer(rule.answer)
            .setQuestionReference(question.question.ref)
            .setRuleReference(rule.rule.ref)
            .build()

    private fun loadQuestionLevel() {
        loadAuditLevel.execute(auditEquipmentId)
            .subscribeAndObserve {
                mode = it
                loadEquipmentInfo()
            }.addTo(disposables)
    }

    private fun loadEquipmentInfo() {
        getAuditEquipmentNameAndIcon.execute(auditEquipmentId)
            .subscribeAndObserve {
                equipmentName = it.name
                equipmentIconPath = it.icon
                loadQuestions(it.lastAnsweredQuestion)
            }.addTo(disposables)
    }

    private fun loadQuestions(lastAnsQ: Int) {
        loadAuditEquipmentQuestions.execute(auditEquipmentId)
            .subscribeAndObserve {
                questions = it
                setCurrentIndex(lastAnsQ)
            }.addTo(disposables)
    }


    /*
        this function returns true if it's novice mode and there are still other questions
        and returns false if it's expert mode (because there will always be no other questions)
        or if it's novice and and there are no other questions
     */
    fun getNextQuestion() {
        if (checkIfThereIsNoMoreQuestions()) {
            saveAnswers(saveforValidate = true)
            return
        }
        setCurrentIndex(currentQuestionIndex + 1)
    }

    fun getPreQuestion() {
        if (checkIfThereIsPreQuestions()) {
            setCurrentIndex(currentQuestionIndex - 1)
            return
        }
    }

    private fun checkIfThereIsNoMoreQuestions(): Boolean {
        return mode == AuditLevel.EXPERT || currentQuestionIndex >= questions.size - 1
    }

    private fun checkIfThereIsPreQuestions(): Boolean {
        return currentQuestionIndex > 0
    }

    fun addObserverForLabel(observerData: ObserverData<Int>) {
        label.observe(observerData.owner, observerData.observer)
    }

    fun addObserverForBackBtnVis(observerData: ObserverData<Int>) {
        backBtnVis.observe(observerData.owner, observerData.observer)
    }

    fun addObserverForAuditId(observerData: ObserverData<Int>) {
        auditIdLiveData.observe(observerData.owner, observerData.observer)
    }

    fun addObserverForRuleSetData(observerData: ObserverData<RulesetRefAndVersion>) {
        ruleSetLiveData.observe(observerData.owner, observerData.observer)
    }

    fun addObserverForDisplayedQuestions(observerData: ObserverData<List<QuestionAnswerModel>>) {
        displayedQuestions.removeObservers(observerData.owner)
        displayedQuestions.observe(observerData.owner, observerData.observer)
    }

    fun addObserverForAnswersSaved(observerData: ObserverData<Int>) {
        answersSavedLiveData.observe(observerData.owner, observerData.observer)
    }

    fun postValueInAnswersSaved(it: Int) {
        answersSavedLiveData.postValue(it)
    }

    fun postValueInAuditId(it: Int) {
        auditIdLiveData.postValue(it)
    }

    fun loadData(auditEqId: Int) {
        this.auditEquipmentId = auditEqId
        loadQuestionLevel()
    }

    fun loadAuditId() {
        getAuditIdFromAuditEquipmentId.execute(auditEquipmentId)
            .subscribeAndObserve {
                auditIdLiveData.postValue(it)
            }.addTo(disposables)
    }

    fun loadRuleSetData() {
        getRuleSetDataForEquipmentId.execute(auditEquipmentId)
            .subscribeAndObserve {
                ruleSetData = it
                ruleSetLiveData.postValue(it)
            }.addTo(disposables)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    data class ObserverData<T>(val owner: LifecycleOwner, val observer: Observer<T>)
}