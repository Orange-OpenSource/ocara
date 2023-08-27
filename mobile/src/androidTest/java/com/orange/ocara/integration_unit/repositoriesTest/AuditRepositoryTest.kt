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

package com.orange.ocara.integration_unit.interactors.repositoriesTest

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.orange.ocara.data.cache.database.DAOs.AuditEquipmentDAO
import com.orange.ocara.data.cache.database.OcaraDB
import com.orange.ocara.data.cache.database.Tables.Audit
import com.orange.ocara.data.cache.database.Tables.Equipment
import com.orange.ocara.domain.models.AuditEquipmentModel
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.domain.models.EquipmentModel
import com.orange.ocara.domain.repositories.AuditRepository
import com.orange.ocara.utils.enums.Answer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class AuditRepositoryTest {
    lateinit var ocaraDB: OcaraDB
    lateinit var repository: AuditRepository
    lateinit var auditEquipmentDAO: AuditEquipmentDAO

    @Before
    fun setup() {
        ocaraDB = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                OcaraDB::class.java
        ).allowMainThreadQueries().build()
        repository = AuditRepository(ApplicationProvider.getApplicationContext(), ocaraDB)
        auditEquipmentDAO = ocaraDB.auditObjectDao()
    }

    @Test
    fun insertAuditEquipmentWithCorrectOrderTest() {
        val auditEquipmentId1 = repository.insertAuditEquipmentAndSubEquipments(getAuditEquipment()).blockingGet()
        val auditEquipmentId2 = repository.insertAuditEquipmentAndSubEquipments(getAuditEquipment()).blockingGet()
        val auditEquipmentId3 = repository.insertAuditEquipmentAndSubEquipments(getAuditEquipment()).blockingGet()
        Assert.assertEquals(1,auditEquipmentDAO.getOrder(auditEquipmentId1!!.toInt()).blockingGet())
        Assert.assertEquals(2,auditEquipmentDAO.getOrder(auditEquipmentId2!!.toInt()).blockingGet())
        Assert.assertEquals(3,auditEquipmentDAO.getOrder(auditEquipmentId3!!.toInt()).blockingGet())
    }

    private fun getAuditEquipment(): AuditEquipmentModel {
        //return AuditEquipmentModel(getAudit(), getEquipment(), Answer.NO_ANSWER, "")
        return AuditEquipmentModel.Builder()
                .setAudit(getAudit())
                .setEquipment(getEquipment())
                .build()
    }

    private fun getAudit(): AuditModel {
        return AuditModel(Audit())
    }

    private fun getEquipment(): EquipmentModel {
        return EquipmentModel(Equipment("", "", "", "", "", ""))
    }

    @After
    fun finish() {
        ocaraDB.close()
    }
}