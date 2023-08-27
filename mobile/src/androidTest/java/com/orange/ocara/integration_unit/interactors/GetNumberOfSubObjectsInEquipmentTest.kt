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
import com.orange.ocara.data.cache.database.Tables.Audit
import com.orange.ocara.data.cache.database.Tables.Equipment
import com.orange.ocara.data.cache.database.Tables.RulesetDetails
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments
import com.orange.ocara.data.cache.database.crossRef.EquipmentSubEquipmentCrossRef
import com.orange.ocara.domain.interactors.GetNumberOfSubObjectsInEquipment
import com.orange.ocara.domain.repositories.EquipmentRepository
import com.orange.ocara.utils.enums.AuditLevel
import com.orange.ocara.utils.enums.RuleSetStat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GetNumberOfSubObjectsInEquipmentTest {
    lateinit var ocaraDB: OcaraDB
    lateinit var repository: EquipmentRepository
    lateinit var getNumberOfSubObjectsInEquipment: GetNumberOfSubObjectsInEquipment

    @Before
    fun setup() {
        ocaraDB = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                OcaraDB::class.java
        ).allowMainThreadQueries().build()
        repository = EquipmentRepository(ocaraDB)
        getNumberOfSubObjectsInEquipment = GetNumberOfSubObjectsInEquipment(repository)
    }

    @After
    fun finish() {
        ocaraDB.close()
    }

    @Test
    fun getNumberOfSubObjectsInEquipmentTest(){
        ocaraDB.rulesetDAO().insert(RulesetDetails.Builder()
                .id(1)
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .setReference("r1")
                .setVersion(1)
                .build()).blockingAwait()
        ocaraDB.rulesetDAO().insert(RulesetDetails.Builder()
                .id(2)
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .setReference("r1")
                .setVersion(2)
                .build()).blockingAwait()
        ocaraDB.rulesetDAO().insert(RulesetDetails.Builder()
                .id(3)
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .setReference("r2")
                .setVersion(1)
                .build()).blockingAwait()
        ocaraDB.equipmentDao().insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e1")
                .build()).blockingAwait()
        ocaraDB.equipmentDao().insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e2")
                .build()).blockingAwait()
        ocaraDB.equipmentDao().insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e3")
                .build()).blockingAwait()
        ocaraDB.equipmentDao().insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e4")
                .build()).blockingAwait()
        ocaraDB.equipmentSubEquipmentDAO().insert(EquipmentSubEquipmentCrossRef("e1", "e2", "r1", 1)).blockingAwait()
        ocaraDB.equipmentSubEquipmentDAO().insert(EquipmentSubEquipmentCrossRef("e1", "e3", "r1", 1)).blockingAwait()
        ocaraDB.equipmentSubEquipmentDAO().insert(EquipmentSubEquipmentCrossRef("e2", "e4", "r2", 1)).blockingAwait()
        ocaraDB.equipmentSubEquipmentDAO().insert(EquipmentSubEquipmentCrossRef("e3", "e4", "r1", 2)).blockingAwait()
        ocaraDB.equipmentSubEquipmentDAO().insert(EquipmentSubEquipmentCrossRef("e3", "e2", "r1", 2)).blockingAwait()
        ocaraDB.equipmentSubEquipmentDAO().insert(EquipmentSubEquipmentCrossRef("e3", "e1", "r1", 2)).blockingAwait()

        val auditId1 = ocaraDB.auditDao().insert(getAudit("r1", 1)).blockingGet()
        val auditId2 = ocaraDB.auditDao().insert(getAudit("r2", 1)).blockingGet()
        val auditId3 = ocaraDB.auditDao().insert(getAudit("r1", 2)).blockingGet()

        val auditEqId1 = ocaraDB.auditObjectDao().insert(AuditEquipments("e1", "", auditId1.toInt())).blockingGet().toInt()
        val auditEqId2 = ocaraDB.auditObjectDao().insert(AuditEquipments("e2", "", auditId2.toInt())).blockingGet().toInt()
        val auditEqId3 = ocaraDB.auditObjectDao().insert(AuditEquipments("e3", "", auditId3.toInt())).blockingGet().toInt()

        Assert.assertEquals(getNumberOfSubObjectsInEquipment.execute(auditEqId1).blockingGet(), 2)
        Assert.assertEquals(getNumberOfSubObjectsInEquipment.execute(auditEqId2).blockingGet(), 1)
        Assert.assertEquals(getNumberOfSubObjectsInEquipment.execute(auditEqId3).blockingGet(), 3)

    }

    fun getAudit(rulesetRef: String, rulesetVer: Int): Audit {
        val audit = Audit()
        audit.level = AuditLevel.EXPERT
        audit.rulesetRef = rulesetRef
        audit.rulesetVer = rulesetVer
        return audit
    }
}