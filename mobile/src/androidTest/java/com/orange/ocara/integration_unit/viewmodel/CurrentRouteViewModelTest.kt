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

package com.orange.ocara.integration_unit.viewmodel

import com.orange.ocara.data.cache.database.Tables.Equipment
import com.orange.ocara.domain.interactors.DeleteAuditEquipmentWithItsData
import com.orange.ocara.domain.interactors.LoadAuditWithAuditEquipmentsAndComments
import com.orange.ocara.domain.interactors.UpdateAuditEquipmentOrder
import com.orange.ocara.domain.models.AuditEquipmentWithNumberOfCommentAndOrderModel
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.domain.models.EquipmentModel
import com.orange.ocara.mobile.ui.viewmodel.CurrentRouteViewModel
import com.orange.ocara.utils.enums.Answer
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.internal.operators.completable.CompletableCreate
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CurrentRouteViewModelTest {
    lateinit var viewModel: CurrentRouteViewModel

    @Before
    fun initialize() {
        viewModel = CurrentRouteViewModel(FakeLoadAuditEquipments(), FakeUpdateAuditEquipmentOrder(),FakeDelete())
        viewModel.getAudit(0).blockingGet()
    }

    @Test
    fun testSortingEquipments() {
        // their ordering with ids should be 2 4 3 1
        val sortedObjectsIds = arrayOf(2, 4, 3, 1)
        for (i in 0 until viewModel.audit!!.objects.size) {
            Assert.assertEquals(viewModel.audit!!.objects[i].id, sortedObjectsIds[i])
        }
        // we start from 1 because we check for each object against the one before it
        // and 0 object won't have any one before it

        for (i in 1 until viewModel.audit!!.objects.size) {
            val currentObject = viewModel.audit!!.objects[i] as AuditEquipmentWithNumberOfCommentAndOrderModel
            val previousObject = viewModel.audit!!.objects[i - 1] as AuditEquipmentWithNumberOfCommentAndOrderModel
            Assert.assertTrue(currentObject.order > previousObject.order)
        }
    }

    @Test
    fun testUpClickLogic() {
        var equipment = getObjectWithId(3)
        Assert.assertEquals(equipment?.order, 3)
        equipment = getObjectWithId(4)
        Assert.assertEquals(equipment?.order, 2)

        viewModel.onUpClick(0)
        testSortingEquipments() // test that up clicking the first element doesn't change anything

        viewModel.onUpClick(2) // object with order 3 (id = 3) should be replaced with object with order 2 (id = 4)

        equipment = getObjectWithId(3)
        Assert.assertEquals(equipment?.order, 2)
        equipment = getObjectWithId(4)
        Assert.assertEquals(equipment?.order, 3)


    }

    private fun getObjectWithId(id: Int): AuditEquipmentWithNumberOfCommentAndOrderModel? {
        for (equipment in viewModel.audit!!.objects) {
            if (equipment.id == id) return equipment as AuditEquipmentWithNumberOfCommentAndOrderModel
        }
        return null
    }

    @Test
    fun testDownClickLogic() {
        var equipment = getObjectWithId(3)
        Assert.assertEquals(equipment?.order, 3)
        equipment = getObjectWithId(4)
        Assert.assertEquals(equipment?.order, 2)

        viewModel.onDownClick(3)
        testSortingEquipments() // test that down clicking the last element doesn't change anything

        viewModel.onDownClick(2) // object with order 3 (id = 3) should be replaced with object with order 4 (id = 1)

        equipment = getObjectWithId(3)
        Assert.assertEquals(equipment?.order, 4)
        equipment = getObjectWithId(1)
        Assert.assertEquals(equipment?.order, 3)
    }
}

class FakeLoadAuditEquipments : LoadAuditWithAuditEquipmentsAndComments(null, null) {
    override fun execute(auditId: Int): Single<AuditModel> {
        return Single.create {
            it.onSuccess(getFakeAudit())
        }
    }

    private fun getFakeAudit(): AuditModel {
        val audit = AuditModel()
        val equipment = EquipmentModel(Equipment("", "", "", "", "", ""))
        audit.objects.add(getAuditEquipment(1,audit,equipment,4))
        audit.objects.add(getAuditEquipment(2,audit,equipment,1))
        audit.objects.add(getAuditEquipment(3,audit,equipment,3))
        audit.objects.add(getAuditEquipment(4,audit,equipment,2))
        return audit
    }

    private fun getAuditEquipment(id: Int, audit: AuditModel, equipment: EquipmentModel, order: Int): AuditEquipmentWithNumberOfCommentAndOrderModel {
        return AuditEquipmentWithNumberOfCommentAndOrderModel.Builder()
                .setId(id)
                .setAudit(audit)
                .setEquipment(equipment)
                .setAnswer(Answer.NO_ANSWER)
                .setNumberOfComments(0)
                .setOrder(order)
                .build()
    }
}

// it's just used so that viewmodel won't throw a null exception
class FakeUpdateAuditEquipmentOrder : UpdateAuditEquipmentOrder(null) {
    override fun execute(auditEquipmentId: Int, order: Int): Completable {
        return CompletableCreate {
            it.onComplete()
        }
    }
}
// it's just used so that viewmodel won't throw a null exception
class FakeDelete : DeleteAuditEquipmentWithItsData(null) {
    override fun execute(auditEquipmentId: Int): Completable {
        return CompletableCreate {
            it.onComplete()
        }
    }
}