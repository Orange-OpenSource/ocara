package com.orange.ocara.data.cache.database.DAOs;
/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentForReport;
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentNameAndIcon;
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentWithAuditAndEquipment;
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentWithNumberOfCommentsAndOrder;
import com.orange.ocara.data.cache.database.NonTables.EquipmentNameAndIcon;
import com.orange.ocara.data.cache.database.NonTables.PairOfOldAndNewAuditEquipmentId;
import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;

@Dao
public abstract class AuditEquipmentUpdateOrderDAO {
    @Query("update AuditEquipments set `order` = :order where audit_object_id = :id")
    abstract int updateOrder(int id, int order);

    @Transaction
     void updateOrdersInternal(List<Integer> eqIDS, List<Integer> eqOrders) {
        // Anything inside this method runs in a single transaction.
        if (eqIDS.isEmpty())
            return;
        for (int i = 0; i < eqIDS.size(); i++) {
            updateOrder(eqIDS.get(i), eqOrders.get(i));
        }
    }

    public Completable updateOrders(List<Integer> eqIDS, List<Integer> eqOrders) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                updateOrdersInternal(eqIDS, eqOrders);
                emitter.onComplete();

            }
        });
//            updateOrders(eqIDS, eqOrders)
//        )
    }

}
