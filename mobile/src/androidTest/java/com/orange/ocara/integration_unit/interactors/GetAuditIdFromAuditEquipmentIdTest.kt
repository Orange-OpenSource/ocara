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
import com.orange.ocara.data.cache.database.crossRef.EquipmentRulesetVersion
import com.orange.ocara.domain.interactors.GetAuditIdFromAuditEquipmentId
import com.orange.ocara.domain.repositories.AuditEquipmentRepository
import com.orange.ocara.utils.enums.AuditLevel
import com.orange.ocara.utils.enums.RuleSetStat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GetAuditIdFromAuditEquipmentIdTest {
    lateinit var ocaraDB: OcaraDB
    lateinit var repository: AuditEquipmentRepository
    lateinit var getAuditIdFromAuditEquipmentId: GetAuditIdFromAuditEquipmentId

    @Before
    fun setup() {
        ocaraDB = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                OcaraDB::class.java
        ).allowMainThreadQueries().build()
        repository = AuditEquipmentRepository(ocaraDB)
        getAuditIdFromAuditEquipmentId = GetAuditIdFromAuditEquipmentId(repository)
    }

    @After
    fun finish() {
        ocaraDB.close()
    }

    @Test
    fun test() {
        insertRuleset()
        insertEquipments()
        insertEquipmentRulesetRelation()
        val auditId1 = insertAudit()
        val auditId2 = insertAudit()
        val auditEquipmentId1 = insertAuditEquipments(auditId1,"e1")
        val auditEquipmentId2 = insertAuditEquipments(auditId1,"e2")
        val auditEquipmentId3 = insertAuditEquipments(auditId2,"e3")

        Assert.assertEquals(auditId1.toInt(),getAuditIdFromAuditEquipmentId.execute(auditEquipmentId1).blockingGet())
        Assert.assertEquals(auditId1.toInt(),getAuditIdFromAuditEquipmentId.execute(auditEquipmentId2).blockingGet())
        Assert.assertEquals(auditId2.toInt(),getAuditIdFromAuditEquipmentId.execute(auditEquipmentId3).blockingGet())
    }
    private fun insertAuditEquipments(auditId: Long,objectRef:String): Int {
        return ocaraDB.auditObjectDao().insert(AuditEquipments(objectRef, "e1", auditId.toInt())).blockingGet().toInt()
    }
    private fun insertRuleset() {
        ocaraDB.rulesetDAO().insert(RulesetDetails.Builder()
                .setReference("r1")
                .setVersion(1)
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .build()).blockingAwait()
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
        ocaraDB.equipmentDao().insert(Equipment.EquipmentBuilder
                .anEquipment()
                .withReference("e3")
                .build()).blockingAwait()
    }
    private fun insertAudit(): Long {
        return ocaraDB.auditDao().insert(getAudit()).blockingGet()
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
        equipmentsRulesetVersion.add(EquipmentRulesetVersion.Builder()
                .setObjRef("e3")
                .setRulesetRef("r1")
                .setVersion(1)
                .createEquipmentRulesetVersion())
        ocaraDB.equipmentRulesetVersionDAO().insert(equipmentsRulesetVersion).blockingAwait()
    }
    private fun getAudit(): Audit {
        val audit = Audit()
        audit.name = "a1"
        audit.rulesetRef = "r1"
        audit.rulesetVer = 1
        audit.level = AuditLevel.EXPERT
        return audit
    }

}