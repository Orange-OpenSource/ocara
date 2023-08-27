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
import com.orange.ocara.domain.interactors.LoadAuditLevel
import com.orange.ocara.domain.repositories.AuditRepository
import com.orange.ocara.utils.enums.AuditLevel
import com.orange.ocara.utils.enums.RuleSetStat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoadAuditLevelTest {
    lateinit var ocaraDB: OcaraDB
    lateinit var repository: AuditRepository
    lateinit var loadAuditLevel: LoadAuditLevel

    @Before
    fun setup() {
        ocaraDB = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                OcaraDB::class.java
        ).allowMainThreadQueries().build()
        repository = AuditRepository(null, ocaraDB)
        loadAuditLevel = LoadAuditLevel(repository)
    }

    @Test
    fun test() {
        insertRuleset()
        insertEquipments()
        insertEquipmentRulesetRelation()
        val auditId = insertAudit(AuditLevel.BEGINNER,1)
        val auditId2 = insertAudit(AuditLevel.EXPERT,2)
        val auditEquipmentId = insertAuditEquipments(auditId,"e1")
        val auditEquipmentId2 = insertAuditEquipments(auditId,"e2")
        val auditEquipmentId3 = insertAuditEquipments(auditId2,"e3")
        val level = loadAuditLevel.execute(auditEquipmentId).blockingGet()
        Assert.assertEquals(AuditLevel.BEGINNER,level)
        val level2 = loadAuditLevel.execute(auditEquipmentId2).blockingGet()
        Assert.assertEquals(AuditLevel.BEGINNER,level2)
        val level3 = loadAuditLevel.execute(auditEquipmentId3).blockingGet()
        Assert.assertEquals(AuditLevel.EXPERT,level3)
    }

    private fun insertAudit(level:AuditLevel,version:Int): Long {
        return ocaraDB.auditDao().insert(getAudit(level,version)).blockingGet()
    }

    private fun insertAuditEquipments(auditId: Long,objectRef:String): Int {
        return ocaraDB.auditObjectDao().insert(AuditEquipments(objectRef, "e1", auditId.toInt())).blockingGet().toInt()
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
                .setVersion(2)
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
        ocaraDB.equipmentDao().insert(Equipment.EquipmentBuilder
                .anEquipment()
                .withReference("e3")
                .build()).blockingAwait()
    }

    private fun getAudit(level:AuditLevel,rulesetVer:Int): Audit {
        val audit = Audit()
        audit.name = "a1"
        audit.rulesetRef = "r1"
        audit.rulesetVer = rulesetVer
        audit.level = level
        return audit
    }

    private fun insertRuleset() {
        ocaraDB.rulesetDAO().insert(RulesetDetails.Builder()
                .setReference("r1")
                .setVersion(1)
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .build()).blockingAwait()
        ocaraDB.rulesetDAO().insert(RulesetDetails.Builder()
                .setReference("r1")
                .setVersion(2)
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .build()).blockingAwait()
    }

    @After
    fun finish() {
        ocaraDB.close()
    }
}