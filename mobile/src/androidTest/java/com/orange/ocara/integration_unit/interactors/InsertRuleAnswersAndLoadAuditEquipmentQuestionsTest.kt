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

package com.orange.ocara.integration_unit.interactors

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.orange.ocara.data.cache.database.OcaraDB
import com.orange.ocara.data.cache.database.Tables.*
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments
import com.orange.ocara.data.cache.database.crossRef.EquipmentRulesetVersion
import com.orange.ocara.data.cache.database.crossRef.QuestionsEquipmentsCrossRef
import com.orange.ocara.data.cache.database.crossRef.QuestionsRulesCrossRef
import com.orange.ocara.domain.interactors.InsertRuleAnswersWithAuditEquipmentId
import com.orange.ocara.domain.interactors.LoadAuditEquipmentByIdForNoviceMode
import com.orange.ocara.domain.interactors.LoadAuditEquipmentQuestions
import com.orange.ocara.domain.models.QuestionAnswerModel
import com.orange.ocara.domain.models.QuestionRuleAnswer
import com.orange.ocara.domain.models.RuleAnswerModel
import com.orange.ocara.domain.repositories.AuditEquipmentRepository
import com.orange.ocara.domain.repositories.RuleAnswerRepository
import com.orange.ocara.utils.enums.Answer
import com.orange.ocara.utils.enums.AuditLevel
import com.orange.ocara.utils.enums.RuleSetStat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InsertRuleAnswersAndLoadAuditEquipmentQuestionsTest {
    lateinit var ocaraDB: OcaraDB
    lateinit var ruleAnswerRepository: RuleAnswerRepository
    lateinit var auditEquipmentRepository: AuditEquipmentRepository
    lateinit var loadAuditEquipmentQuestions: LoadAuditEquipmentQuestions
    lateinit var loadAuditEquipmentByIdForNoviceMode: LoadAuditEquipmentByIdForNoviceMode
    lateinit var insertRuleAnswersWithAuditEquipmentId: InsertRuleAnswersWithAuditEquipmentId

    @Before
    fun setup() {
        ocaraDB = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                OcaraDB::class.java
        ).allowMainThreadQueries().build()
        ruleAnswerRepository = RuleAnswerRepository(ocaraDB)
        auditEquipmentRepository = AuditEquipmentRepository(ocaraDB)
        loadAuditEquipmentByIdForNoviceMode = LoadAuditEquipmentByIdForNoviceMode(auditEquipmentRepository)
        loadAuditEquipmentQuestions = LoadAuditEquipmentQuestions(loadAuditEquipmentByIdForNoviceMode, ruleAnswerRepository)
        insertRuleAnswersWithAuditEquipmentId = InsertRuleAnswersWithAuditEquipmentId(ruleAnswerRepository)
    }

    @Test
    fun test1() {
        val auditEquipmentIds = insertData()

        var questionAnswers = loadAuditEquipmentQuestions.execute(auditEquipmentIds[0]).blockingGet()
        questionAnswers = sortQuestionsBasedOnTheirReference(questionAnswers)
        Assert.assertEquals("NODN",getQuestionAnswerPattern(questionAnswers[0]))
        Assert.assertEquals("NON",getQuestionAnswerPattern(questionAnswers[1]))

        var questionAnswers2 = loadAuditEquipmentQuestions.execute(auditEquipmentIds[1]).blockingGet()
        questionAnswers2 = sortQuestionsBasedOnTheirReference(questionAnswers2)
        Assert.assertEquals("XXX",getQuestionAnswerPattern(questionAnswers2[0]))
        Assert.assertEquals("DDX",getQuestionAnswerPattern(questionAnswers2[1]))
    }
    private fun insertRulesAnswers(auditEquipmentIds: List<Int>) {
        val list = ArrayList<QuestionRuleAnswer>()
        list.add(getRuleAnswer(auditEquipmentIds[0], Answer.OK, "r1", "q1"))
        list.add(getRuleAnswer(auditEquipmentIds[0], Answer.DOUBT, "r2", "q1"))
        list.add(getRuleAnswer(auditEquipmentIds[0], Answer.NOK, "r3", "q1"))
        list.add(getRuleAnswer(auditEquipmentIds[0], Answer.OK, "r3", "q2"))
        list.add(getRuleAnswer(auditEquipmentIds[0], Answer.NOK, "r4", "q2"))
        list.add(getRuleAnswer(auditEquipmentIds[1], Answer.NO_ANSWER, "r6", "q3"))
        list.add(getRuleAnswer(auditEquipmentIds[1], Answer.DOUBT, "r7", "q4"))
        insertRuleAnswersWithAuditEquipmentId.execute(list).blockingAwait()
    }

    private fun getRuleAnswer(auditEqId: Int, answer: Answer, ruleRef: String, questionRef: String) =
            QuestionRuleAnswer.QuestionRuleAnswerBuilder()
                    .setAnswer(answer)
                    .setRuleReference(ruleRef)
                    .setAuditEquipmentId(auditEqId)
                    .setQuestionReference(questionRef)
                    .build()

    // this returns a pattern where the first character represents the first question and the others are the rules sorted by their reference
    private fun getQuestionAnswerPattern(questionAnswer: QuestionAnswerModel): String {
        var result = ""
        result += getAnswerChar(questionAnswer.answer)
        val sortedRuleAnswers = sortRulesBasedOnTheirReference(questionAnswer.ruleAnswers)
        for (rule in sortedRuleAnswers)
            result += getAnswerChar(rule.answer)
        return result
    }

    private fun sortQuestionsBasedOnTheirReference(questionAnswers: List<QuestionAnswerModel>): List<QuestionAnswerModel> {
        return questionAnswers.sortedBy {
            it.question.ref
        }
    }
    private fun sortRulesBasedOnTheirReference(ruleAnswers: List<RuleAnswerModel>): List<RuleAnswerModel> {
        return ruleAnswers.sortedBy {
            it.rule.ref
        }
    }

    // OK = O , Doubt = D , NOK = N , NO_Answer = X
    private fun getAnswerChar(answer: Answer): Char {
        when (answer) {
            Answer.OK -> return 'O'
            Answer.NOK -> return 'N'
            Answer.DOUBT -> return 'D'
            Answer.NO_ANSWER -> return 'X'
            // this is a bad case so we just return something different from all of them to fail the test
            else -> return 'A'
        }
    }

    private fun insertData(): List<Int> {
        insertRuleset()
        insertEquipments()
        insertEquipmentRulesetRelation()
        insertQuestions()
        insertQuestionsEquipmentRelation()
        insertRules()
        insertRulesQuestionsRelation()
        val auditId = insertAudit()
        val auditEquipmentIds = insertAuditEquipments(auditId)
        insertRulesAnswers(auditEquipmentIds)
        return auditEquipmentIds
    }



    private fun insertRulesQuestionsRelation() {
        var questionNumber = 1
        for (ruleNumber in 1..8) {
            val questionRule = QuestionsRulesCrossRef("q$questionNumber", "r$ruleNumber", "r1", 1)
            ocaraDB.questionRulesDAO().insert(questionRule).blockingAwait()
            // this increases question number every steps for rule number
            // so q1 will have r1 ,r2
            // and q2 will have r3 ,r4
            // and so on
            if (ruleNumber % 2 == 0) {
                questionNumber++
            }
        }
        // this is to test special case where one rule is added to multiple questions
        ocaraDB.questionRulesDAO().insert(QuestionsRulesCrossRef("q1", "r3", "r1", 1)).blockingAwait()
    }

    private fun insertRules() {
        for (i in 1..8)
            ocaraDB.ruleDao().insert(Rule("r$i", "", "", "")).blockingAwait()
    }

    private fun insertQuestionsEquipmentRelation() {
        ocaraDB.questionEquipmentDAO().insert(QuestionsEquipmentsCrossRef("q1", "e1", "r1", 1)).blockingAwait()
        ocaraDB.questionEquipmentDAO().insert(QuestionsEquipmentsCrossRef("q2", "e1", "r1", 1)).blockingAwait()
        ocaraDB.questionEquipmentDAO().insert(QuestionsEquipmentsCrossRef("q3", "e2", "r1", 1)).blockingAwait()
        ocaraDB.questionEquipmentDAO().insert(QuestionsEquipmentsCrossRef("q4", "e2", "r1", 1)).blockingAwait()
    }

    private fun insertQuestions() {
        for (i in 1..4)
            ocaraDB.questionDao().insert(Question("q$i", "", "", "", "")).blockingAwait()
    }

    private fun insertAudit(): Long {
        return ocaraDB.auditDao().insert(getAudit()).blockingGet()
    }

    private fun insertAuditEquipments(auditId: Long): List<Int> {
        val auditEquipmentIds = ArrayList<Int>()
        auditEquipmentIds.add(ocaraDB.auditObjectDao().insert(AuditEquipments("e1", "e1", auditId.toInt())).blockingGet().toInt())
        auditEquipmentIds.add(ocaraDB.auditObjectDao().insert(AuditEquipments("e2", "e2", auditId.toInt())).blockingGet().toInt())
        return auditEquipmentIds
    }

    private fun insertEquipmentRulesetRelation() {
        val equipmentsRulesetVersion = ArrayList<EquipmentRulesetVersion>()
        equipmentsRulesetVersion.add(EquipmentRulesetVersion.Builder()
                .setObjRef("e1")
                .setRulesetRef("r1")
                .setVersion(1)
                .createEquipmentRulesetVersion())
        equipmentsRulesetVersion.add(EquipmentRulesetVersion.Builder()
                .setObjRef("e2")
                .setRulesetRef("r1")
                .setVersion(1)
                .createEquipmentRulesetVersion())
        ocaraDB.equipmentRulesetVersionDAO().insert(equipmentsRulesetVersion).blockingAwait()
    }

    private fun insertEquipments() {
        ocaraDB.equipmentDao().insert(Equipment.EquipmentBuilder
                .anEquipment()
                .withReference("e1")
                .build()).blockingAwait()
        ocaraDB.equipmentDao().insert(Equipment.EquipmentBuilder
                .anEquipment()
                .withReference("e2")
                .build()).blockingAwait()
    }

    private fun insertRuleset() {
        ocaraDB.rulesetDAO().insert(RulesetDetails.Builder()
                .setReference("r1")
                .setVersion(1)
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .build()).blockingAwait()
    }

    private fun getAudit(): Audit {
        val audit = Audit()
        audit.name = "a1"
        audit.rulesetRef = "r1"
        audit.rulesetVer = 1
        audit.level = AuditLevel.BEGINNER
        return audit
    }

    @After
    fun finish() {
        ocaraDB.close()
    }
}