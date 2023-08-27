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
import com.orange.ocara.data.cache.database.OcaraDB
import com.orange.ocara.data.cache.database.Tables.Audit
import com.orange.ocara.data.cache.database.Tables.Comment
import com.orange.ocara.data.cache.database.Tables.RuleAnswer
import com.orange.ocara.data.cache.database.crossRef.AuditEquipmentSubEquipment
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments
import com.orange.ocara.domain.interactors.DeleteAuditEquipmentWithItsData
import com.orange.ocara.domain.interactors.GetNumberOfSubObjectsInEquipment
import com.orange.ocara.domain.repositories.AuditEquipmentRepository
import com.orange.ocara.utils.enums.Answer
import com.orange.ocara.utils.enums.AuditLevel
import com.orange.ocara.utils.enums.CommentType
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DeleteAuditEquipmentWithItsDataTest {
    lateinit var ocaraDB: OcaraDB
    lateinit var deleteAuditEquipmentWithItsData: DeleteAuditEquipmentWithItsData
    @Before
    fun init() {
        ocaraDB = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                OcaraDB::class.java
        ).allowMainThreadQueries().build()
        val repository = AuditEquipmentRepository(ocaraDB)
        deleteAuditEquipmentWithItsData = DeleteAuditEquipmentWithItsData(repository)
    }

    @Test
    fun test() {
        val auditId1 = ocaraDB.auditDao().insert(getAudit()).blockingGet().toInt()
        val auditEquipmentId1 = ocaraDB.auditObjectDao().insert(AuditEquipments("e1", "", auditId1)).blockingGet().toInt()

        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(auditEquipmentId1, Answer.NO_ANSWER, "r1", "")).blockingAwait()
        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(auditEquipmentId1, Answer.NO_ANSWER, "r2", "")).blockingAwait()
        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(auditEquipmentId1, Answer.NO_ANSWER, "r3", "")).blockingAwait()

        val auditSubObjectsInEquipment = ArrayList<AuditEquipmentSubEquipment>()
        auditSubObjectsInEquipment.add(AuditEquipmentSubEquipment(auditId1,auditEquipmentId1,""))
        auditSubObjectsInEquipment.add(AuditEquipmentSubEquipment(auditId1,auditEquipmentId1,""))
        ocaraDB.auditEquipmentSubEquipmentDAO().insert(auditSubObjectsInEquipment).blockingAwait()

        val comment = Comment(CommentType.TEXT, "", "", "")
        comment.audit_equipment_id = auditEquipmentId1
        ocaraDB.commentDao().insert(comment).blockingGet()
        ocaraDB.commentDao().insert(comment).blockingGet()

        val auditEquipmentSubEquipmentsBeforeDelete = ocaraDB.auditEquipmentSubEquipmentDAO().getSubEquipmentsOfAudit(auditEquipmentId1).blockingGet().size
        Assert.assertEquals(auditEquipmentSubEquipmentsBeforeDelete,2)
        val auditEquipmentsBeforeDelete = ocaraDB.auditObjectDao().getAuditEquipments(auditId1).blockingGet().size
        Assert.assertEquals(auditEquipmentsBeforeDelete, 1)
        val rulesCountBeforeDelete = ocaraDB.ruleAnswerDAO().getRuleAnswersList(auditEquipmentId1).blockingGet().size
        Assert.assertEquals(rulesCountBeforeDelete, 3)
        val commentsCountBeforeDelete = ocaraDB.commentDao().getAuditObjectComments(auditEquipmentId1.toLong()).blockingGet().size
        Assert.assertEquals(commentsCountBeforeDelete, 2)

        deleteAuditEquipmentWithItsData.execute(auditEquipmentId1).blockingAwait()

        val auditEquipmentSubEquipmentsAfterDelete = ocaraDB.auditEquipmentSubEquipmentDAO().getSubEquipmentsOfAudit(auditEquipmentId1).blockingGet().size
        Assert.assertEquals(auditEquipmentSubEquipmentsAfterDelete,0)
        val auditEquipmentsAfterDelete = ocaraDB.auditObjectDao().getAuditEquipments(auditId1).blockingGet().size
        Assert.assertEquals(auditEquipmentsAfterDelete, 0)
        val rulesCountAfterDelete = ocaraDB.ruleAnswerDAO().getRulesAnswers(auditEquipmentId1).blockingGet().size
        Assert.assertEquals(rulesCountAfterDelete, 0)
        val commentsCountAfterDelete = ocaraDB.commentDao().getAuditObjectComments(auditEquipmentId1.toLong()).blockingGet().size
        Assert.assertEquals(commentsCountAfterDelete, 0)
    }

    fun getAudit(): Audit {
        val audit = Audit()
        audit.level = AuditLevel.EXPERT
        return audit
    }

    @After
    fun finish() {
        ocaraDB.close()
    }
}