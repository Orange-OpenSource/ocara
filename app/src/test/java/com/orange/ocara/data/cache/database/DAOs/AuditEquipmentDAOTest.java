package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.domain.models.QuestionAnswerModel;
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentWithAuditAndEquipment;
import com.orange.ocara.data.cache.database.NonTables.EquipmentNameAndIcon;
import com.orange.ocara.data.cache.database.NonTables.PairOfOldAndNewAuditEquipmentId;
import com.orange.ocara.data.cache.database.Tables.Audit;
import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.Tables.RuleAnswer;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipmentSubEquipment;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments;
import com.orange.ocara.utils.enums.Answer;
import com.orange.ocara.utils.enums.AuditLevel;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuditEquipmentDAOTest extends DAOTest {
    // test deleting all the equipments of some audit
    @Test
    public void deleteAllAuditObjects() {
        List<AuditEquipments> auditEquipments = new ArrayList<>();
        auditEquipments.add(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setName("eq1")
                .setObjectRef("obj1")
                .createAuditEquipments());
        auditEquipments.add(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(2)
                .setName("eq2")
                .setObjectRef("obj2")
                .createAuditEquipments());
        auditEquipments.add(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setName("eq3")
                .setObjectRef("obj3")
                .createAuditEquipments());
        auditEquipments.add(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(2)
                .setName("eq4")
                .setObjectRef("obj4")
                .createAuditEquipments());

        auditEquipmentDAO.insert(auditEquipments).blockingGet();
        auditEquipmentDAO.deleteAllAuditObjects(1).blockingAwait();
        List<AuditEquipments> auditEquipments1 = auditEquipmentDAO.getAuditEquipments(1).blockingGet();
        List<AuditEquipments> auditEquipments2 = auditEquipmentDAO.getAuditEquipments(2).blockingGet();
        Assert.assertEquals(0, auditEquipments1.size());
        Assert.assertEquals(2, auditEquipments2.size());
        Assert.assertEquals("eq2", auditEquipments2.get(0).getName());
        Assert.assertEquals("eq4", auditEquipments2.get(1).getName());
    }

    @Test
    public void deleteAuditObject() {
        int auditEq1 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setName("eq1")
                .setObjectRef("obj1")
                .createAuditEquipments()).blockingGet().intValue();
        int auditEq2 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setName("eq2")
                .setObjectRef("obj2")
                .createAuditEquipments()).blockingGet().intValue();

        auditEquipmentDAO.deleteAuditObject(1, auditEq1).blockingAwait();
        List<AuditEquipments> auditEquipments1 = auditEquipmentDAO.getAuditEquipments(1).blockingGet();
        Assert.assertEquals(1, auditEquipments1.size());
        Assert.assertEquals("eq2", auditEquipments1.get(0).getName());
    }

    @Test
    public void updateAuditObjectName() {
        int auditEq1 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setName("eq1")
                .setObjectRef("obj1")
                .createAuditEquipments()).blockingGet().intValue();
        int auditEq2 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setName("eq2")
                .setObjectRef("obj2")
                .createAuditEquipments()).blockingGet().intValue();

        auditEquipmentDAO.updateAuditObjectName(auditEq1, "eq1Upd").blockingAwait();
        List<AuditEquipments> auditEquipments = auditEquipmentDAO.getAuditEquipments(1).blockingGet();
        Assert.assertEquals(2, auditEquipments.size());
        Assert.assertEquals("eq1Upd", auditEquipments.get(0).getName());
        Assert.assertEquals("eq2", auditEquipments.get(1).getName());
    }

    @Test
    public void loadAuditEquipments() {
        Audit audit1 = new Audit.Builder()
                .setLevel(AuditLevel.BEGINNER)
                .setName("audit1")
                .build();
        Audit audit2 = new Audit.Builder()
                .setLevel(AuditLevel.BEGINNER)
                .setName("audit2")
                .build();
        int audit1Id = auditDAO.insert(audit1).blockingGet().intValue();
        int audit2Id = auditDAO.insert(audit2).blockingGet().intValue();
        int auditEq1 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(audit1Id)
                .setName("eq1")
                .setObjectRef("obj1")
                .createAuditEquipments()).blockingGet().intValue();
        int auditEq2 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(audit1Id)
                .setName("eq2")
                .setObjectRef("obj2")
                .createAuditEquipments()).blockingGet().intValue();
        int auditEq3 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(audit2Id)
                .setName("eq3")
                .setObjectRef("obj3")
                .createAuditEquipments()).blockingGet().intValue();

        List<RuleAnswer> ruleAnswers = new ArrayList<>();
        // eq1 = no
        ruleAnswers.add(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r1")
                .setQuestionRef("q1")
                .setAnswer(Answer.DOUBT)
                .setAuditEquipmentId(auditEq1)
                .createRuleAnswer());
        ruleAnswers.add(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r2")
                .setQuestionRef("q2")
                .setAnswer(Answer.NO_ANSWER)
                .setAuditEquipmentId(auditEq1)
                .createRuleAnswer());
        ruleAnswers.add(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r3")
                .setQuestionRef("q3")
                .setAnswer(Answer.NOK)
                .setAuditEquipmentId(auditEq1)
                .createRuleAnswer());

        // eq2 = DOUBT
        ruleAnswers.add(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r1")
                .setQuestionRef("q1")
                .setAnswer(Answer.DOUBT)
                .setAuditEquipmentId(auditEq2)
                .createRuleAnswer());
        ruleAnswers.add(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r2")
                .setQuestionRef("q2")
                .setAnswer(Answer.NO_ANSWER)
                .setAuditEquipmentId(auditEq2)
                .createRuleAnswer());
        ruleAnswers.add(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r3")
                .setQuestionRef("q3")
                .setAnswer(Answer.OK)
                .setAuditEquipmentId(auditEq2)
                .createRuleAnswer());

        // eq3 = OK
        ruleAnswers.add(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r1")
                .setQuestionRef("q1")
                .setAnswer(Answer.NO_ANSWER)
                .setAuditEquipmentId(auditEq3)
                .createRuleAnswer());
        ruleAnswers.add(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r2")
                .setQuestionRef("q2")
                .setAnswer(Answer.OK)
                .setAuditEquipmentId(auditEq3)
                .createRuleAnswer());
        ruleAnswers.add(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r3")
                .setQuestionRef("q3")
                .setAnswer(Answer.NO_ANSWER)
                .setAuditEquipmentId(auditEq3)
                .createRuleAnswer());

        ruleAnswerDAO.insert(ruleAnswers).blockingAwait();

        List<AuditEquipmentWithAuditAndEquipment> auditEquipments1 = auditEquipmentDAO.loadAuditEquipments((long) audit1Id).blockingGet();
        List<AuditEquipmentWithAuditAndEquipment> auditEquipments2 = auditEquipmentDAO.loadAuditEquipments((long) audit2Id).blockingGet();
        AuditEquipmentWithAuditAndEquipment auditAndEquipment1 = auditEquipmentDAO.loadAuditEquipmentById(auditEq1).blockingGet();

        Assert.assertEquals(2, auditEquipments1.size());
        Assert.assertEquals(Answer.NOK, auditEquipments1.get(0).getAnswer());
        Assert.assertEquals("audit1", auditEquipments1.get(0).getAudit().getName());
        Assert.assertEquals("eq1", auditEquipments1.get(0).getName());
        Assert.assertEquals(Answer.NOK, auditAndEquipment1.getAnswer());
        Assert.assertEquals("audit1", auditAndEquipment1.getAudit().getName());
        Assert.assertEquals("eq1", auditAndEquipment1.getName());

        Assert.assertEquals(Answer.DOUBT, auditEquipments1.get(1).getAnswer());
        Assert.assertEquals("audit1", auditEquipments1.get(1).getAudit().getName());
        Assert.assertEquals("eq2", auditEquipments1.get(1).getName());

        Assert.assertEquals(1, auditEquipments2.size());
        Assert.assertEquals(Answer.OK, auditEquipments2.get(0).getAnswer());
        Assert.assertEquals("audit2", auditEquipments2.get(0).getAudit().getName());
        Assert.assertEquals("eq3", auditEquipments2.get(0).getName());
    }

    @Test
    public void getSubEquipments() {
        int auditEq1 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setName("eq1")
                .setObjectRef("obj1")
                .createAuditEquipments()).blockingGet().intValue();
        int auditEq2 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setName("eq2")
                .setObjectRef("obj2")
                .createAuditEquipments()).blockingGet().intValue();

        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("r1")
                .withName("name1")
                .build()).blockingAwait();
        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("r2")
                .withName("name2")
                .build()).blockingAwait();
        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("r3")
                .withName("name3")
                .build()).blockingAwait();
        List<AuditEquipmentSubEquipment> auditEquipmentSubEquipments = new ArrayList<>();
        auditEquipmentSubEquipments.add(new AuditEquipmentSubEquipment.AuditEquipmentSubEquipmentBuilder()
                .setAuditEquipmentId(auditEq1)
                .setChildRef("r1")
                .createAuditEquipmentSubEquipment());

        auditEquipmentSubEquipments.add(new AuditEquipmentSubEquipment.AuditEquipmentSubEquipmentBuilder()
                .setAuditEquipmentId(auditEq1)
                .setChildRef("r2")
                .createAuditEquipmentSubEquipment());

        auditEquipmentSubEquipments.add(new AuditEquipmentSubEquipment.AuditEquipmentSubEquipmentBuilder()
                .setAuditEquipmentId(auditEq2)
                .setChildRef("r3")
                .createAuditEquipmentSubEquipment());

        auditEquipmentSubEquipmentDAO.insert(auditEquipmentSubEquipments).blockingAwait();

        List<Equipment> subEq1 = auditEquipmentDAO.getSubEquipments(auditEq1).blockingGet();
        List<Equipment> subEq2 = auditEquipmentDAO.getSubEquipments(auditEq2).blockingGet();
        Assert.assertEquals(2, subEq1.size());
        Assert.assertEquals(1, subEq2.size());
        Assert.assertEquals("r1", subEq1.get(0).getReference());
        Assert.assertEquals("name1", subEq1.get(0).getName());
        Assert.assertEquals("r2", subEq1.get(1).getReference());
        Assert.assertEquals("name2", subEq1.get(1).getName());
        Assert.assertEquals("r3", subEq2.get(0).getReference());
        Assert.assertEquals("name3", subEq2.get(0).getName());
    }

    @Test
    public void getAuditEquipments() {
        List<AuditEquipments> auditEquipments = new ArrayList<>();
        auditEquipments.add(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setName("eq1")
                .setObjectRef("obj1")
                .createAuditEquipments());
        auditEquipments.add(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(2)
                .setName("eq2")
                .setObjectRef("obj2")
                .createAuditEquipments());
        auditEquipments.add(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setName("eq3")
                .setObjectRef("obj3")
                .createAuditEquipments());
        auditEquipments.add(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(2)
                .setName("eq4")
                .setObjectRef("obj4")
                .createAuditEquipments());

        auditEquipmentDAO.insert(auditEquipments).blockingGet();
        List<AuditEquipments> auditEquipments1 = auditEquipmentDAO.getAuditEquipments(1).blockingGet();
        List<AuditEquipments> auditEquipments2 = auditEquipmentDAO.getAuditEquipments(2).blockingGet();
        Assert.assertEquals(2, auditEquipments1.size());
        Assert.assertEquals(2, auditEquipments2.size());
        Assert.assertEquals("eq2", auditEquipments2.get(0).getName());
        Assert.assertEquals("eq4", auditEquipments2.get(1).getName());
        Assert.assertEquals("eq1", auditEquipments1.get(0).getName());
        Assert.assertEquals("eq3", auditEquipments1.get(1).getName());
    }

    @Test
    public void mapOldAuditEquipmentIdToNewAuditEquipmentId() {
        int ae1 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setObjectRef("r1")
                .setAudit_id(1)
                .createAuditEquipments()).blockingGet().intValue();
        int ae2 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setObjectRef("r2")
                .setAudit_id(1)
                .createAuditEquipments()).blockingGet().intValue();
        int ae5 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setObjectRef("r3")
                .setAudit_id(1)
                .createAuditEquipments()).blockingGet().intValue();
        int ae3 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setObjectRef("r1")
                .setAudit_id(2)
                .createAuditEquipments()).blockingGet().intValue();
        int ae4 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setObjectRef("r2")
                .setAudit_id(2)
                .createAuditEquipments()).blockingGet().intValue();
        HashMap<Integer, Integer> expected = new HashMap<>();
        expected.put(ae1, ae3);
        expected.put(ae2, ae4);
        List<PairOfOldAndNewAuditEquipmentId> lst = auditEquipmentDAO.mapOldAuditEquipmentIdToNewAuditEquipmentId(1, 2).blockingGet();
        Assert.assertEquals(2, lst.size());
        for (PairOfOldAndNewAuditEquipmentId p : lst) {
            Assert.assertEquals(expected.get(p.getOldAuditEquipmentId()).intValue(), p.getNewAuditEquipmentId());
        }
    }

    @Test
    public void getAuditEquipmentNameAndIcon() {
        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withName("eq1")
                .withIcon("ic1")
                .withReference("r1")
                .build()).blockingAwait();
        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withName("eq2")
                .withIcon("ic2")
                .withReference("r2")
                .build()).blockingAwait();
        int ae1 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setObjectRef("r1")
                .setAudit_id(1)
                .createAuditEquipments()).blockingGet().intValue();
        int ae2 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setObjectRef("r1")
                .setAudit_id(2)
                .createAuditEquipments()).blockingGet().intValue();
        int ae3 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setObjectRef("r2")
                .setAudit_id(1)
                .createAuditEquipments()).blockingGet().intValue();
        int ae4 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setObjectRef("r2")
                .setAudit_id(2)
                .createAuditEquipments()).blockingGet().intValue();
        EquipmentNameAndIcon equipment1 = auditEquipmentDAO.getAuditEquipmentNameAndIcon(ae1).blockingGet();
        EquipmentNameAndIcon equipment2 = auditEquipmentDAO.getAuditEquipmentNameAndIcon(ae2).blockingGet();
        EquipmentNameAndIcon equipment3 = auditEquipmentDAO.getAuditEquipmentNameAndIcon(ae3).blockingGet();
        EquipmentNameAndIcon equipment4 = auditEquipmentDAO.getAuditEquipmentNameAndIcon(ae4).blockingGet();
        Assert.assertEquals(equipment1.getEquipment_name(), "eq1");
        Assert.assertEquals(equipment2.getEquipment_name(), "eq1");
        Assert.assertEquals(equipment3.getEquipment_name(), "eq2");
        Assert.assertEquals(equipment4.getEquipment_name(), "eq2");
    }
}
