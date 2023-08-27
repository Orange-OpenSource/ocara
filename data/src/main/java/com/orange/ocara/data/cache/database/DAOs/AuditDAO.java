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
import androidx.room.Update;

import com.orange.ocara.data.cache.database.NonTables.AuditInfoForReport;
import com.orange.ocara.data.cache.database.NonTables.AuditWithRulesetAndSiteAndAuditor;
import com.orange.ocara.data.cache.database.NonTables.AuditWithRulesetAndSiteAndComments;
import com.orange.ocara.data.cache.database.NonTables.AuditWithRulesetWithSite;
import com.orange.ocara.data.cache.database.NonTables.AuditWithSite;
import com.orange.ocara.data.cache.database.NonTables.RulesetRefAndVersion;
import com.orange.ocara.data.cache.database.Tables.Audit;
import com.orange.ocara.utils.enums.AuditLevel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface AuditDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(Audit audit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<Audit> audit);

    @Query("select ruleset_details.rulesetId from audit left join ruleset_details on " +
            "audit.audit_ruleset = ruleset_details.reference and audit.rulesetVer = ruleset_details.ruleset_version " +
            "where audit.audit_id = :auditId")
    Single<Integer> getRulesetIdForAudit(int auditId);

    @Query("select audit.audit_name as name,sites.name as siteName,audit.audit_date as date,audit.level as level," +
            "audit.audit_status as audit_status, ruleset_details.type as rulesetType,auditor.firstName as auditorName ,auditor.lastName as auditorLastName" +
            ", sites.name as siteName from audit left join sites on auditSiteId=siteId " +
            "left join ruleset_details on audit_ruleset=ruleset_details.reference left join " +
            "auditor on audit.auditorId=auditor.id where audit.audit_id=:id")
    Single<AuditInfoForReport> getAuditInfoForReport(int id);

    @Query("select audit_ruleset as reference ,rulesetVer as version from audit where audit_id=:auditId")
    Single<RulesetRefAndVersion> getRulesetInfo(int auditId);

    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId where audit_name LIKE :pattern ORDER BY audit_name DESC , audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedByNameDESC(String pattern);

    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId where audit_name LIKE :pattern ORDER BY audit_name ASC , audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedByNameASC(String pattern);


    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId where audit_name LIKE :pattern ORDER BY audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedByDateDESC(String pattern);

    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId where audit_name LIKE :pattern ORDER BY audit_status DESC , audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedByStatusDESC(String pattern);


    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId where audit_name LIKE :pattern ORDER BY sites.name DESC , audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedBySiteDESC(String pattern);

    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId where audit_name LIKE :pattern ORDER BY audit_date ASC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedByDateASC(String pattern);

    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId where audit_name LIKE :pattern ORDER BY audit_status ASC , audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedByStatusASC(String pattern);


    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId where audit_name LIKE :pattern ORDER BY sites.name ASC , audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedBySiteASC(String pattern);


    //-----------------------------------------
    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId ORDER BY audit_name DESC , audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedByNameDESC();

    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId ORDER BY audit_name ASC , audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedByNameASC();


    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId ORDER BY audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedByDateDESC();

    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId ORDER BY audit_status DESC , audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedByStatusDESC();


    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId ORDER BY sites.name DESC , audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedBySiteDESC();

    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId ORDER BY audit_date ASC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedByDateASC();

    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId ORDER BY audit_status ASC , audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedByStatusASC();


    @Query("select distinct * from audit left join sites on audit.auditSiteId = sites.siteId ORDER BY sites.name ASC , audit_date DESC")
    Flowable<List<AuditWithSite>> getAuditsWithSiteOrderedBySiteASC();


//    @Query("select distinct * from audit where audit_name LIKE :pattern ORDER BY audit_status , audit_date DESC")
//    Flowable<List<Audit>> getAudits(String pattern);

    @Query("select * from audit left join ruleset_details " +
            "on audit.audit_ruleset=ruleset_details.reference " +
            "left join sites on audit.auditSiteId=sites.siteId " +
            "left join comment on audit.audit_id=comment.audit_id " +
            "where audit.audit_id=:id")
    Single<AuditWithRulesetAndSiteAndComments> getAudit(Long id);

    @Query("select * from audit left join ruleset_details " +
            "on audit.audit_ruleset=ruleset_details.reference " +
            "left join sites on audit.auditSiteId=sites.siteId " +
            "left join auditor on audit.auditorId=auditor.id " +
            "where audit.audit_id=:id")
    Single<AuditWithRulesetAndSiteAndAuditor> getAuditWithRulesetAndSiteAndAuditor(Long id);

    @Query("select * from audit left join ruleset_details " +
            "on audit.audit_ruleset=ruleset_details.reference " +
            "left join sites on audit.auditSiteId=sites.siteId " +
            "where audit_name like '%' || :filter || '%' order by audit_date")
    Single<List<AuditWithRulesetAndSiteAndComments>> getAuditsWithRulesetAndSiteSortedByDateDESC(String filter);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable update(Audit audit);

    @Query("UPDATE audit set audit_name =:auditName,userName =:auditor, level =:level where audit_id =:id ")
    Completable update(String auditName, String auditor, AuditLevel level, int id);

    @Query("UPDATE audit set audit_name =:auditName, auditSiteId = :siteId,auditorId =:auditorId, level =:level where audit_id =:id ")
    Completable updateV2(String auditName, int siteId, int auditorId, AuditLevel level, int id);

    @Query("select distinct(userName) from audit")
    Single<List<String>> getAuthors();


    @Query("select * from audit where audit_name=:name and audit_version=:version and auditSiteId=:siteId")
    Single<Audit> getAudit(String name, int version, int siteId);

    @Query("delete from audit where audit_id=:auditId")
    Completable deleteAudit(long auditId);

    @Query("UPDATE audit SET audit_status = 0 WHERE audit_id = :auditId")
    Completable setAuditToLocked(long auditId);

    @Query("select level from audit left join auditequipments on " +
            "audit.audit_id=auditequipments.audit_id where auditequipments.audit_object_id=:auditEqId")
    Single<AuditLevel> getAuditLevel(int auditEqId);

    @Query("select level," +
            " audit_id AS auditId, " +
            " ruleSetStat As rulesetStat," +
            " rulesetVer AS rulesetVresion," +
            "audit_name AS auditName," +
            "userName AS auditor," +
            "ruleset_details.reference AS rulesetRef," +
            " ruleset_details.type AS rulesetType ," +
            "audit.auditSiteId AS siteId," +
            " sites.name AS siteName," +
            " sites.noImmo AS siteNoImmo  " +
            " FROM audit" +
            "  inner join ruleset_details on reference = audit_ruleset" +
            "  inner join sites on audit.auditSiteId = sites.siteId" +
            "  WHERE audit.audit_id =:id ")
    Single<AuditWithRulesetWithSite> getAuditWithRulesetWithSite(Long id);

    @Query("select * from audit order by audit_id DESC LIMIT 3")
    Single<List<Audit>> getTheLastThreeAudits();

    @Query("select count(audit.audit_id) from audit")
    Single<Integer> getTheNumberOfAudits();
}
