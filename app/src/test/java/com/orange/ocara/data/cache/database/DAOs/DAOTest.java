package com.orange.ocara.data.cache.database.DAOs;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.orange.ocara.data.cache.database.OcaraDB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {28})

public class DAOTest {
    OcaraDB ocaraDB;
    AuditorDAO auditorDAO;
    ReportScoresDAO reportScoresDAO;
    QuestionEquipmentDAO questionEquipmentDAO;
    EquipmentRulesetVersionDAO equipmentRulesetVersionDAO;
    AuditDAO auditDAO;
    SiteDAO siteDAO;
    ProfileTypeRulesetDao profileTypeRulesetDao;
    RulesetDAO rulesetDAO;
    CommentDAO commentDAO;
    QuestionDAO questionDAO;
    ProfileTypeDAO profileTypeDAO;
    AuditEquipmentDAO auditEquipmentDAO;
    RuleDAO ruleDAO;
    RuleWithIllustrationDAO ruleWithIllustrationDAO;
    EquipmentAndCategoryRelationDAO equipmentAndCategoryRelationDAO;
    CategoryDAO categoryDAO;
    ImpactValueRulesetDao impactValueRulesetDao;
    IllustrationDAO illustrationDAO;
    ImpactValueDAO impactValueDAO;
    RuleAnswerDAO ruleAnswerDAO;
    AuditEquipmentSubEquipmentDAO auditEquipmentSubEquipmentDAO;
    EquipmentDAO equipmentDAO;
    EquipmentSubEquipmentDAO equipmentSubEquipmentDAO;
    QuestionRulesDAO questionRulesDAO;
    RuleProfileTypeImpactDAO ruleProfileTypeImpactDAO;

    @Before
    public void setup() {
        ocaraDB = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                OcaraDB.class
        ).allowMainThreadQueries().build();
        auditDAO = ocaraDB.auditDao();
        reportScoresDAO = ocaraDB.reportScoresDAO();
        ruleProfileTypeImpactDAO = ocaraDB.ruleProfileTypeImpactDAO();
        impactValueRulesetDao = ocaraDB.impactValueRulesetDao();
        impactValueDAO = ocaraDB.impactValueDao();
        profileTypeDAO = ocaraDB.profileTypeDao();
        profileTypeRulesetDao = ocaraDB.profileTypeRulesetDao();
        ruleWithIllustrationDAO = ocaraDB.ruleWithIllustrationsDAO();
        equipmentRulesetVersionDAO = ocaraDB.equipmentRulesetVersionDAO();
        equipmentSubEquipmentDAO = ocaraDB.equipmentSubEquipmentDAO();
        siteDAO = ocaraDB.siteDAO();
        questionDAO = ocaraDB.questionDao();
        illustrationDAO = ocaraDB.illustrationDao();
        auditorDAO = ocaraDB.auditorDAO();
        categoryDAO = ocaraDB.categoryDao();
        ruleAnswerDAO = ocaraDB.ruleAnswerDAO();
        rulesetDAO = ocaraDB.rulesetDAO();
        commentDAO = ocaraDB.commentDao();
        equipmentAndCategoryRelationDAO = ocaraDB.equipmentAndCategoryRelationDAO();
        auditEquipmentDAO = ocaraDB.auditObjectDao();
        ruleDAO = ocaraDB.ruleDao();
        questionEquipmentDAO = ocaraDB.questionEquipmentDAO();
        questionRulesDAO = ocaraDB.questionRulesDAO();
        auditEquipmentSubEquipmentDAO = ocaraDB.auditEquipmentSubEquipmentDAO();
        equipmentDAO = ocaraDB.equipmentDao();
    }

    @After
    public void finish() {
        ocaraDB.close();
    }

    // don't delete this , or it will give error
    @Test
    public void demo() {
    }
}
