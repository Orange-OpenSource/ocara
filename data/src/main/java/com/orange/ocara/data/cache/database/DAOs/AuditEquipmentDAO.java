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

import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentForReport;
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentNameAndIcon;
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentWithAuditAndEquipment;
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentWithNumberOfCommentsAndOrder;
import com.orange.ocara.data.cache.database.NonTables.EquipmentNameAndIcon;
import com.orange.ocara.data.cache.database.NonTables.PairOfOldAndNewAuditEquipmentId;
import com.orange.ocara.data.cache.database.NonTables.RulesetRefAndVersion;
import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface AuditEquipmentDAO {
    @Insert
    Single<Long> insert(AuditEquipments auditEquipments);

    @Insert
    Single<List<Long>> insert(List<AuditEquipments> auditEquipments);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAndReturnCompletable(List<AuditEquipments> auditEquipments);

    @Query("delete from AuditEquipments where audit_id=:auditId")
    Completable deleteAllAuditObjects(int auditId);

    @Query("delete from AuditEquipments where audit_id=:auditId and audit_object_id=:objRef")
    Completable deleteAuditObject(int auditId, int objRef);

    @Query("delete from AuditEquipments where audit_object_id=:auditEqId")
    Completable deleteAuditObject(int auditEqId);


    @Query("delete from AuditEquipments where audit_object_id in (:auditEqId)")
    Completable deleteAuditObjects(List<Integer> auditEqId);


    @Query("update AuditEquipments set name=:name where audit_object_id=:auditEqId")
    Completable updateAuditObjectName(int auditEqId, String name);

    @Query("select AuditEquipments.audit_object_id as id , AuditEquipments.`order`" +
            ",AuditEquipments.name,objects.*,audit.*,max(rule_answers.answer) as answer " +
            "from AuditEquipments left join audit " +
            "on audit.audit_id=AuditEquipments.audit_id " +
            "left join objects on AuditEquipments.objectReference=objects.objectReference " +
            "left join rule_answers on rule_answers.auditEquipmentId=AuditEquipments.audit_object_id " +
            "where AuditEquipments.audit_id=:auditId group by AuditEquipments.audit_object_id order by  AuditEquipments.`order`")
    Single<List<AuditEquipmentWithAuditAndEquipment>> loadAuditEquipments(Long auditId);

    @Query("select name from auditequipments where audit_object_id = :auditEquipmentId")
    Single<String> getAuditObjectName(int auditEquipmentId);

    @Query("select count(AuditEquipments.audit_object_id) from AuditEquipments where audit_id = :auditId")
    Single<Integer> getNumberOfAuditEquipmentsForAudit(int auditId);

    @Query("select AuditEquipments.audit_id from AuditEquipments where AuditEquipments.audit_object_id " +
            "=:auditEquipmentId")
    Single<Integer> loadAuditId(int auditEquipmentId);

    // this query gets a list of equipments that are the children of
    // the given auditEquipmentId
    @Query("select objects.* from objects " +
            "left join audit_equipment_subequipment on objects.objectReference= audit_equipment_subequipment.childRef " +
            "where audit_equipment_subequipment.auditEquipmentId=:auditEquipmentId")
    Single<List<Equipment>> getSubEquipments(int auditEquipmentId);

    @Query("select audit.audit_ruleset as reference, audit.rulesetVer as version from AuditEquipments " +
            "left join audit on AuditEquipments.audit_id= audit.audit_id " +
            "where AuditEquipments.audit_object_id=:auditEquipmentId")
    Single<RulesetRefAndVersion> getRuleSetAndVersion(int auditEquipmentId);


    @Query("select AuditEquipments.audit_object_id as id,AuditEquipments.name, AuditEquipments.`order`" +
            ", objects.*, audit.*, max(rule_answers.answer) as answer " +
            "from AuditEquipments " +
            "left join audit on audit.audit_id=AuditEquipments.audit_id " +
            "left join objects on AuditEquipments.objectReference=objects.objectReference " +
            "left join rule_answers on rule_answers.auditEquipmentId=AuditEquipments.audit_object_id " +
            "where AuditEquipments.audit_object_id=:id")
    Single<AuditEquipmentWithAuditAndEquipment> loadAuditEquipmentById(int id);

    @Query("select * from AuditEquipments where audit_id = :auditId")
    Single<List<AuditEquipments>> getAuditEquipments(int auditId);

    // this query is used in copying the audit , as it takes the oldAuditId and newAuditId
    // and for all equipments that associated with both of them , it will return a pair of the old id
    // and the new id
    @Query("select AE.audit_object_id as oldAuditEquipmentId,AE2.audit_object_id as newAuditEquipmentId" +
            " from AuditEquipments as AE left join AuditEquipments as AE2 " +
            "on AE.objectReference=AE2.objectReference where AE.audit_id=:oldAuditId " +
            "and AE2.audit_id=:newAuditId")
    Single<List<PairOfOldAndNewAuditEquipmentId>> mapOldAuditEquipmentIdToNewAuditEquipmentId(int oldAuditId, int newAuditId);

    @Query("select icon , equipment_name from objects " +
            "inner join AuditEquipments on AuditEquipments.objectReference = objects.objectReference \n" +
            "where audit_object_id =:equipmentId")
    Single<EquipmentNameAndIcon> getAuditEquipmentNameAndIcon(int equipmentId);

    @Query("select objects.* ,AuditEquipments.audit_object_id as id , AuditEquipments.name , AuditEquipments.`order`, audit.* ,count(comment.comment_id) as cnt " +
            "from AuditEquipments left join objects on AuditEquipments.objectReference=" +
            "objects.objectReference left join comment on " +
            "AuditEquipments.audit_object_id=comment.audit_equipment_id " +
            "left join audit on AuditEquipments.audit_id=audit.audit_id " +
            "where AuditEquipments.audit_object_id=:auditEquipmentId " +
            "group by AuditEquipments.audit_object_id")
    Single<AuditEquipmentWithNumberOfCommentsAndOrder> getAuditEquipment(int auditEquipmentId);

    @Query("select objects.* ,AuditEquipments.audit_object_id as id , AuditEquipments.name , AuditEquipments.`order`" +
            ", audit.* ,count(comment.comment_id) as cnt , max(rule_answers.answer) as answer " +
            "from AuditEquipments left join objects on AuditEquipments.objectReference=" +
            "objects.objectReference left join comment on " +
            "AuditEquipments.audit_object_id=comment.audit_equipment_id " +
            "left join audit on AuditEquipments.audit_id=audit.audit_id left join rule_answers on " +
            "rule_answers.auditEquipmentId = AuditEquipments.audit_object_id " +
            "where AuditEquipments.audit_id=:auditId " +
            "group by AuditEquipments.audit_object_id ")
    Single<List<AuditEquipmentWithNumberOfCommentsAndOrder>> loadAuditEquipmentsWithOrderAndNumberOfComments(Long auditId);


    @Query("update AuditEquipments set `order` = :order where audit_object_id = :id")
    Completable updateOrder(int id, int order);

    @Query("select AuditEquipments.`order` from AuditEquipments where audit_object_id = :id")
    Single<Integer> getOrder(int id);

    @Query("select AuditEquipments.name,AuditEquipments.audit_object_id as id" +
            ",objects.icon,max(rule_answers.answer) as answer" +
            "  from AuditEquipments left join objects on " +
            "AuditEquipments.objectReference = objects.objectReference left join rule_answers" +
            " on rule_answers.auditEquipmentId = AuditEquipments.audit_object_id " +
            "where AuditEquipments.audit_id=:auditId group by AuditEquipments.audit_object_id " +
            "order by AuditEquipments.`order` asc")
    Single<List<AuditEquipmentForReport>> getAuditEquipmentsForReport(int auditId);

    @Query("select AuditEquipments.name,AuditEquipments.lastAnsweredQuestion,objects.icon from AuditEquipments left join objects on " +
            "AuditEquipments.objectReference = objects.objectReference where " +
            "AuditEquipments.audit_object_id = :equipmentId")
    Single<AuditEquipmentNameAndIcon> getAuditEquipmentInfo(int equipmentId);

    @Query("update AuditEquipments set `lastAnsweredQuestion` = :lastAnsweredQuestion where audit_object_id = :id")
    Completable updateLastAnsweredQuestion(int id, int lastAnsweredQuestion);

}
