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
import com.orange.ocara.data.cache.database.Tables.Comment
import com.orange.ocara.data.cache.database.Tables.Equipment
import com.orange.ocara.data.cache.database.Tables.RulesetDetails
import com.orange.ocara.data.cache.database.crossRef.AuditEquipmentSubEquipment
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments
import com.orange.ocara.data.cache.database.crossRef.EquipmentRulesetVersion
import com.orange.ocara.data.cache.database.crossRef.EquipmentSubEquipmentCrossRef
import com.orange.ocara.domain.interactors.GetAuditEquipmentWithCharacteristics
import com.orange.ocara.domain.repositories.AuditEquipmentRepository
import com.orange.ocara.utils.enums.AuditLevel
import com.orange.ocara.utils.enums.CommentType
import com.orange.ocara.utils.enums.RuleSetStat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GetAuditEquipmentWithCharacteristics {
    lateinit var ocaraDB: OcaraDB
    lateinit var repository: AuditEquipmentRepository
    lateinit var getAuditEquipmentWithCharacteristics: GetAuditEquipmentWithCharacteristics

    @Before
    fun setup() {
        ocaraDB = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                OcaraDB::class.java
        ).allowMainThreadQueries().build()
        repository = AuditEquipmentRepository(ocaraDB)
        getAuditEquipmentWithCharacteristics = GetAuditEquipmentWithCharacteristics(repository)
    }

    @After
    fun finish() {
        ocaraDB.close()
    }

    @Test
    fun getAuditEquipmentWithCharacteristicsTest() {
        ocaraDB.rulesetDAO().insert(RulesetDetails.Builder()
                .setReference("r1")
                .setVersion(1)
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .build()).blockingAwait()

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

        val equipmentsRulesetVersion = ArrayList<EquipmentRulesetVersion>()
        equipmentsRulesetVersion.add(EquipmentRulesetVersion.Builder()
                .setObjRef("e1")
                .setRulesetRef("r1")
                .setVersion(1)
                .createEquipmentRulesetVersion())

        ocaraDB.equipmentRulesetVersionDAO().insert(equipmentsRulesetVersion).blockingAwait()
        ocaraDB.equipmentSubEquipmentDAO().insert(EquipmentSubEquipmentCrossRef.EquipmentSubEquipmentCrossRefBuilder
                .anEquipmentSubEquipmentCrossRef()
                .withChildRef("e2")
                .withParentRef("e1")
                .withRulesetRef("r1")
                .withVersion(1)
                .build()).blockingAwait()
        ocaraDB.equipmentSubEquipmentDAO().insert(EquipmentSubEquipmentCrossRef.EquipmentSubEquipmentCrossRefBuilder
                .anEquipmentSubEquipmentCrossRef()
                .withChildRef("e3")
                .withParentRef("e1")
                .withRulesetRef("r1")
                .withVersion(1)
                .build()).blockingAwait()

        val audit = Audit()
        audit.name = "a1"
        audit.rulesetRef = "r1"
        audit.rulesetVer = 1
        audit.level = AuditLevel.BEGINNER
        val auditId = ocaraDB.auditDao().insert(audit).blockingGet()

        val audEqId = ocaraDB.auditObjectDao().insert(AuditEquipments("e1", "e1", auditId.toInt())).blockingGet()
        val lst = ArrayList<AuditEquipmentSubEquipment>()
        lst.add(AuditEquipmentSubEquipment(auditId.toInt(), audEqId.toInt(), "e2"))
        ocaraDB.auditEquipmentSubEquipmentDAO().insert(lst).blockingAwait()
        val cmnt1 = Comment(CommentType.TEXT, "date", "cm1", "cm1")
        cmnt1.audit_equipment_id = audEqId.toInt()
        val cmnt2 = Comment(CommentType.TEXT, "date", "cm2", "cm1")
        val cmLst = ArrayList<Comment>()
        cmLst.add(cmnt1)
        cmLst.add(cmnt2)
        cmnt2.audit_equipment_id = audEqId.toInt()
        ocaraDB.commentDao().insert(cmLst).blockingAwait()
        val auditEq = getAuditEquipmentWithCharacteristics.execute(audEqId.toInt()).blockingGet()
        Assert.assertEquals(auditEq.numberOfComments,2)
        Assert.assertEquals(auditEq.equipment.reference,"e1")
        Assert.assertEquals(auditEq.children.size,1)
        Assert.assertEquals(auditEq.children[0].equipment.reference,"e2")
        Assert.assertEquals(auditEq.equipment.children.size,2)
        Assert.assertEquals(auditEq.equipment.children[0].reference,"e2")
        Assert.assertEquals(auditEq.equipment.children[1].reference,"e3")
    }
}