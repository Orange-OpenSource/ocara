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

package com.orange.ocara.integration_unit.DAOs

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.orange.ocara.data.cache.database.OcaraDB
import com.orange.ocara.data.cache.database.Tables.Equipment
import com.orange.ocara.data.cache.database.Tables.RuleAnswer
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments
import com.orange.ocara.utils.enums.Answer
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AuditEquipmentDAOTest {
    lateinit var ocaraDB: OcaraDB

    @Before
    fun init() {
        ocaraDB = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                OcaraDB::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun getAuditEquipmentsForReport() {
        ocaraDB.equipmentDao().insert(Equipment("e1", "e1", "i1", "", "", "")).blockingAwait()
        ocaraDB.equipmentDao().insert(Equipment("e2", "e2", "i2", "", "", "")).blockingAwait()
        ocaraDB.equipmentDao().insert(Equipment("e3", "e3", "i3", "", "", "")).blockingAwait()
        val ae1 = ocaraDB.auditObjectDao().insert(AuditEquipments("e1", "e1", 1,2)).blockingGet()
        val ae2 = ocaraDB.auditObjectDao().insert(AuditEquipments("e2", "e2", 1,1)).blockingGet()
        val ae3 = ocaraDB.auditObjectDao().insert(AuditEquipments("e3", "e3", 2,1)).blockingGet()
        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(ae1.toInt(), Answer.NO_ANSWER, "r1", "")).blockingAwait()
        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(ae1.toInt(), Answer.NOK, "r2", "")).blockingAwait()
        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(ae1.toInt(), Answer.DOUBT, "r3", "")).blockingAwait()
        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(ae1.toInt(), Answer.OK, "r4", "")).blockingAwait()

        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(ae2.toInt(), Answer.NO_ANSWER, "r1", "")).blockingAwait()
        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(ae2.toInt(), Answer.DOUBT, "r2", "")).blockingAwait()
        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(ae2.toInt(), Answer.OK, "r3", "")).blockingAwait()
        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(ae2.toInt(), Answer.OK, "r4", "")).blockingAwait()

        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(ae3.toInt(), Answer.NO_ANSWER, "r1", "")).blockingAwait()
        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(ae3.toInt(), Answer.OK, "r2", "")).blockingAwait()
        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(ae3.toInt(), Answer.NO_ANSWER, "r3", "")).blockingAwait()
        ocaraDB.ruleAnswerDAO().insert(RuleAnswer(ae3.toInt(), Answer.OK, "r4", "")).blockingAwait()

        val audit1 = ocaraDB.auditObjectDao().getAuditEquipmentsForReport(1).blockingGet()
        Assert.assertEquals(audit1.size, 2)
        Assert.assertEquals(audit1[0].name,"e2")
        Assert.assertEquals(audit1[0].answer,Answer.DOUBT)
        Assert.assertEquals(audit1[1].name,"e1")
        Assert.assertEquals(audit1[1].answer,Answer.NOK)

        val audit2 = ocaraDB.auditObjectDao().getAuditEquipmentsForReport(2).blockingGet()
        Assert.assertEquals(audit2.size, 1)
        Assert.assertEquals(audit2[0].answer, Answer.OK)

    }
}