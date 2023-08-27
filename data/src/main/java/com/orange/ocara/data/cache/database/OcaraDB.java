package com.orange.ocara.data.cache.database;
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
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.orange.ocara.data.cache.database.DAOs.AuditDAO;
import com.orange.ocara.data.cache.database.DAOs.AuditEquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.AuditEquipmentSubEquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.AuditEquipmentUpdateOrderDAO;
import com.orange.ocara.data.cache.database.DAOs.AuditorDAO;
import com.orange.ocara.data.cache.database.DAOs.CategoryDAO;
import com.orange.ocara.data.cache.database.DAOs.CommentDAO;
import com.orange.ocara.data.cache.database.DAOs.DocReportProfilesDAO;
import com.orange.ocara.data.cache.database.DAOs.EquipmentAndCategoryRelationDAO;
import com.orange.ocara.data.cache.database.DAOs.EquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.EquipmentRulesetVersionDAO;
import com.orange.ocara.data.cache.database.DAOs.EquipmentSubEquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.IllustrationDAO;
import com.orange.ocara.data.cache.database.DAOs.ImpactValueDAO;
import com.orange.ocara.data.cache.database.DAOs.ImpactValueRulesetDao;
import com.orange.ocara.data.cache.database.DAOs.ObjectWithIllustrationDAO;
import com.orange.ocara.data.cache.database.DAOs.ProfileTypeDAO;
import com.orange.ocara.data.cache.database.DAOs.ProfileTypeRulesetDao;
import com.orange.ocara.data.cache.database.DAOs.QuestionDAO;
import com.orange.ocara.data.cache.database.DAOs.QuestionEquipmentDAO;
import com.orange.ocara.data.cache.database.DAOs.QuestionRulesDAO;
import com.orange.ocara.data.cache.database.DAOs.ReportScoresDAO;
import com.orange.ocara.data.cache.database.DAOs.RuleAnswerDAO;
import com.orange.ocara.data.cache.database.DAOs.RuleDAO;
import com.orange.ocara.data.cache.database.DAOs.RuleProfileTypeImpactDAO;
import com.orange.ocara.data.cache.database.DAOs.RuleWithIllustrationDAO;
import com.orange.ocara.data.cache.database.DAOs.RulesetDAO;
import com.orange.ocara.data.cache.database.DAOs.SiteDAO;
import com.orange.ocara.data.cache.database.NonTables.AnswerConverter;
import com.orange.ocara.data.cache.database.NonTables.AuditLevelConverter;
import com.orange.ocara.data.cache.database.NonTables.CommentTypeConverter;
import com.orange.ocara.data.cache.database.NonTables.StatusConverter;
import com.orange.ocara.data.cache.database.Tables.Audit;
import com.orange.ocara.data.cache.database.Tables.Auditor;
import com.orange.ocara.data.cache.database.Tables.Comment;
import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.Tables.EquipmentCategory;
import com.orange.ocara.data.cache.database.Tables.Illustration;
import com.orange.ocara.data.cache.database.Tables.ImpactValue;
import com.orange.ocara.data.cache.database.Tables.ProfileType;
import com.orange.ocara.data.cache.database.Tables.Question;
import com.orange.ocara.data.cache.database.Tables.Rule;
import com.orange.ocara.data.cache.database.Tables.RuleAnswer;
import com.orange.ocara.data.cache.database.Tables.RulesetDetails;
import com.orange.ocara.data.cache.database.Tables.Site;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipmentSubEquipment;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments;
import com.orange.ocara.data.cache.database.crossRef.EquipmentAndCategoryCrossRef;
import com.orange.ocara.data.cache.database.crossRef.EquipmentRulesetVersion;
import com.orange.ocara.data.cache.database.crossRef.EquipmentSubEquipmentCrossRef;
import com.orange.ocara.data.cache.database.crossRef.EquipmentWithIllustrations;
import com.orange.ocara.data.cache.database.crossRef.ImpactValueRulesetCrossref;
import com.orange.ocara.data.cache.database.crossRef.ProfileTypeRulesetCrossref;
import com.orange.ocara.data.cache.database.crossRef.QuestionsEquipmentsCrossRef;
import com.orange.ocara.data.cache.database.crossRef.QuestionsRulesCrossRef;
import com.orange.ocara.data.cache.database.crossRef.RuleProfileTypeImpactCrossRef;
import com.orange.ocara.data.cache.database.crossRef.RuleWithIllustrations;

@Database(entities = {
        Audit.class,
        EquipmentRulesetVersion.class,
        AuditEquipmentSubEquipment.class,
        Comment.class,
        Site.class,
        AuditEquipments.class,
        RuleWithIllustrations.class,
        RuleProfileTypeImpactCrossRef.class,
        EquipmentWithIllustrations.class,
        ProfileType.class,
        ProfileTypeRulesetCrossref.class,
        QuestionsRulesCrossRef.class,
        Rule.class,
        EquipmentSubEquipmentCrossRef.class,
        ImpactValue.class,
        ImpactValueRulesetCrossref.class,
        RulesetDetails.class,
        RuleAnswer.class,
        Question.class,
        EquipmentAndCategoryCrossRef.class,
        Equipment.class,
        EquipmentCategory.class,
        QuestionsEquipmentsCrossRef.class,
        Illustration.class,
        Auditor.class},
        version = 1)
@TypeConverters({AuditLevelConverter.class, AnswerConverter.class, StatusConverter.class, CommentTypeConverter.class})
public abstract class OcaraDB extends RoomDatabase {
    private static OcaraDB instance;

    public static synchronized OcaraDB getInstance(@NonNull Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, OcaraDB.class
                            , "ocara_DB")
                    .fallbackToDestructiveMigration()
                    .build();
        return instance;
    }

    public abstract RulesetDAO rulesetDAO();

    public abstract AuditDAO auditDao();

    public abstract AuditEquipmentDAO auditObjectDao();

    public abstract AuditEquipmentUpdateOrderDAO auditObjectUpdateOrderDao();

    public abstract EquipmentDAO equipmentDao();

    public abstract CategoryDAO categoryDao();

    public abstract CommentDAO commentDao();

    public abstract QuestionDAO questionDao();

    public abstract IllustrationDAO illustrationDao();

    public abstract RuleWithIllustrationDAO ruleWithIllustrationsDAO();

    public abstract RuleDAO ruleDao();

    public abstract ObjectWithIllustrationDAO objectWithIllustrationsDAO();

    public abstract QuestionRulesDAO questionRulesDAO();

    public abstract ProfileTypeDAO profileTypeDao();

    public abstract ProfileTypeRulesetDao profileTypeRulesetDao();

    public abstract EquipmentSubEquipmentDAO equipmentSubEquipmentDAO();

    public abstract QuestionEquipmentDAO questionEquipmentDAO();

    public abstract EquipmentRulesetVersionDAO equipmentRulesetVersionDAO();

    public abstract EquipmentAndCategoryRelationDAO equipmentAndCategoryRelationDAO();

    public abstract ImpactValueDAO impactValueDao();

    public abstract ImpactValueRulesetDao impactValueRulesetDao();

    public abstract SiteDAO siteDAO();

    public abstract RuleAnswerDAO ruleAnswerDAO();

    public abstract RuleProfileTypeImpactDAO ruleProfileTypeImpactDAO();

    public abstract AuditEquipmentSubEquipmentDAO auditEquipmentSubEquipmentDAO();

    public abstract AuditorDAO auditorDAO();

    public abstract ReportScoresDAO reportScoresDAO();

    public abstract DocReportProfilesDAO docReportProfilesDAO();

}
