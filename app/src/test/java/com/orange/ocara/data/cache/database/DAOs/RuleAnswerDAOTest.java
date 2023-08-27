package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.domain.models.QuestionAnswerModel;
import com.orange.ocara.data.cache.database.NonTables.QuestionWithRuleAnswer;
import com.orange.ocara.data.cache.database.Tables.Audit;
import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.Tables.Question;
import com.orange.ocara.data.cache.database.Tables.Rule;
import com.orange.ocara.data.cache.database.Tables.RuleAnswer;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments;
import com.orange.ocara.utils.enums.Answer;
import com.orange.ocara.utils.enums.AuditLevel;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RuleAnswerDAOTest extends DAOTest {

    @Test
    public void getRulesAnswers() {
        int ae1 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setObjectRef("e1")
                .createAuditEquipments()).blockingGet().intValue();
        int ae2 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setObjectRef("e2")
                .createAuditEquipments()).blockingGet().intValue();

        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e1")
                .withName("eq1")
                .build()).blockingAwait();
        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e2")
                .withName("eq2")
                .build()).blockingAwait();
        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e3")
                .withName("eq3")
                .build()).blockingAwait();

        ruleDAO.insert(Rule.RuleBuilder.aRule()
                .withReference("r1")
                .build()).blockingAwait();
        ruleDAO.insert(Rule.RuleBuilder.aRule()
                .withReference("r2")
                .build()).blockingAwait();
        ruleDAO.insert(Rule.RuleBuilder.aRule()
                .withReference("r3")
                .build()).blockingAwait();
        ruleDAO.insert(Rule.RuleBuilder.aRule()
                .withReference("r4")
                .build()).blockingAwait();

        questionDAO.insert(Question.QuestionBuilder.aQuestion()
                .withReference("q1")
                .build()).blockingAwait();
        questionDAO.insert(Question.QuestionBuilder.aQuestion()
                .withReference("q2")
                .build()).blockingAwait();
        questionDAO.insert(Question.QuestionBuilder.aQuestion()
                .withReference("q3")
                .build()).blockingAwait();

        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r1")
                .setQuestionRef("q1")
                .setAnswer(Answer.OK)
                .setAuditEquipmentId(ae1)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r2")
                .setQuestionRef("q1")
                .setAnswer(Answer.OK)
                .setAuditEquipmentId(ae1)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r3")
                .setQuestionRef("q2")
                .setAnswer(Answer.DOUBT)
                .setAuditEquipmentId(ae1)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r4")
                .setQuestionRef("q3")
                .setAnswer(Answer.NOK)
                .setAuditEquipmentId(ae1)
                .createRuleAnswer()).blockingAwait();

        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r1")
                .setQuestionRef("q2")
                .setAnswer(Answer.OK)
                .setAuditEquipmentId(ae2)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r2")
                .setQuestionRef("q3")
                .setAnswer(Answer.DOUBT)
                .setAuditEquipmentId(ae2)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r3")
                .setQuestionRef("q2")
                .setAnswer(Answer.DOUBT)
                .setAuditEquipmentId(ae2)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r4")
                .setQuestionRef("q2")
                .setAnswer(Answer.NOK)
                .setAuditEquipmentId(ae2)
                .createRuleAnswer()).blockingAwait();

        List<QuestionWithRuleAnswer> list1 = ruleAnswerDAO.getRulesAnswers(ae1).blockingGet();
        List<QuestionWithRuleAnswer> list2 = ruleAnswerDAO.getRulesAnswers(ae2).blockingGet();
        // r1 q1 ok
        // r2 q1 ok
        // r3 q2 doubt
        // r4 q3 nok

        int arr[] = new int[]{0, 0, 0, 0};
        for (QuestionWithRuleAnswer item : list1) {
            Assert.assertEquals(item.getObjectReference(), "e1");
            if (check(item, "r1", "q1", Answer.OK)) {
                arr[0]++;
            } else if (check(item, "r2", "q1", Answer.OK)) {
                arr[1]++;
            } else if (check(item, "r3", "q2", Answer.DOUBT)) {
                arr[2]++;
            } else if (check(item, "r4", "q3",Answer.NOK)) {
                arr[3]++;
            }
        }
        Assert.assertArrayEquals(new int[]{1, 1, 1, 1}, arr);
        arr = new int[]{0, 0, 0, 0};
        // r1 q2 ok
        // r2 q3 doubt
        // r3 q2 doubt
        // r4 q2 nok
        for (QuestionWithRuleAnswer item : list2) {
            Assert.assertEquals(item.getObjectReference(), "e2");
            if (check(item, "r1", "q2",Answer.OK)) {
                arr[0]++;
            } else if (check(item, "r2", "q3",Answer.DOUBT)) {
                arr[1]++;
            } else if (check(item, "r3", "q2",Answer.DOUBT)) {
                arr[2]++;
            } else if (check(item, "r4", "q2",Answer.NOK)) {
                arr[3]++;
            }
        }
        Assert.assertArrayEquals(new int[]{1, 1, 1, 1}, arr);
    }

    boolean check(QuestionWithRuleAnswer item, String r, String q, Answer answer) {
        return item.getQuestion().getReference().equals(q) &&
                item.getRule().getReference().equals(r) && item.getAnswer().equals(answer);
    }

    @Test
    public void deleteRuleAnswersOfAudit() {
        int audit1=auditDAO.insert(Audit.Builder()
                .setLevel(AuditLevel.BEGINNER)
                .build()).blockingGet().intValue();

        int audit2=auditDAO.insert(Audit.Builder()
                .setLevel(AuditLevel.BEGINNER)
                .build()).blockingGet().intValue();

        int ae1 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(audit1)
                .setObjectRef("e")
                .createAuditEquipments()).blockingGet().intValue();
        int ae2 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(audit1)
                .setObjectRef("e")
                .createAuditEquipments()).blockingGet().intValue();

        int ae3 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(audit2)
                .setObjectRef("e")
                .createAuditEquipments()).blockingGet().intValue();

        equipmentDAO.insert(Equipment.EquipmentBuilder
                .anEquipment()
                .withReference("e")
                .build()).blockingAwait();
        ruleDAO.insert(Rule.RuleBuilder
                .aRule()
                .withReference("r")
                .build()).blockingAwait();
        ruleDAO.insert(Rule.RuleBuilder
                .aRule()
                .withReference("r2")
                .build()).blockingAwait();
        ruleDAO.insert(Rule.RuleBuilder
                .aRule()
                .withReference("r3")
                .build()).blockingAwait();
        questionDAO.insert(Question.QuestionBuilder
                .aQuestion()
                .withReference("q")
                .build()).blockingAwait();

        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setAuditEquipmentId(ae1)
                .setQuestionRef("q")
                .setRuleRef("r")
                .setAnswer(Answer.OK)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setAuditEquipmentId(ae1)
                .setQuestionRef("q")
                .setRuleRef("r2")
                .setAnswer(Answer.OK)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setAuditEquipmentId(ae2)
                .setQuestionRef("q")
                .setAnswer(Answer.OK)
                .setRuleRef("r3")
                .createRuleAnswer()).blockingAwait();

        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setAuditEquipmentId(ae3)
                .setAnswer(Answer.OK)
                .setQuestionRef("q")
                .setRuleRef("r")
                .createRuleAnswer()).blockingAwait();

        ruleAnswerDAO.deleteRuleAnswersOfAudit(audit1).blockingAwait();
        List<QuestionWithRuleAnswer> list = ruleAnswerDAO.getRulesAnswers(ae1).blockingGet();
        List<QuestionWithRuleAnswer> list2 = ruleAnswerDAO.getRulesAnswers(ae2).blockingGet();
        List<QuestionWithRuleAnswer> list3 = ruleAnswerDAO.getRulesAnswers(ae3).blockingGet();

        Assert.assertEquals(list.size(), 0);
        Assert.assertEquals(list2.size(), 0);
        Assert.assertEquals(list3.size(), 1);
    }

    @Test
    public void deleteRuleAnswersOfAuditEquipment() {
        int ae1 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setObjectRef("e")
                .createAuditEquipments()).blockingGet().intValue();
        int ae2 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setObjectRef("e")
                .createAuditEquipments()).blockingGet().intValue();

        int ae3 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(2)
                .setObjectRef("e")
                .createAuditEquipments()).blockingGet().intValue();

        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setAuditEquipmentId(ae1)
                .setQuestionRef("q")
                .setAnswer(Answer.OK)
                .setRuleRef("r")
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setAuditEquipmentId(ae2)
                .setQuestionRef("q")
                .setAnswer(Answer.OK)
                .setRuleRef("r2")
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setAuditEquipmentId(ae2)
                .setQuestionRef("q")
                .setAnswer(Answer.OK)
                .setRuleRef("r3")
                .createRuleAnswer()).blockingAwait();

        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setAuditEquipmentId(ae3)
                .setQuestionRef("q")
                .setAnswer(Answer.OK)
                .setRuleRef("r")
                .createRuleAnswer()).blockingAwait();

        equipmentDAO.insert(Equipment.EquipmentBuilder
                .anEquipment()
                .withReference("e")
                .build()).blockingAwait();
        ruleDAO.insert(Rule.RuleBuilder
                .aRule()
                .withReference("r")
                .build()).blockingAwait();
        ruleDAO.insert(Rule.RuleBuilder
                .aRule()
                .withReference("r2")
                .build()).blockingAwait();
        ruleDAO.insert(Rule.RuleBuilder
                .aRule()
                .withReference("r3")
                .build()).blockingAwait();
        questionDAO.insert(Question.QuestionBuilder
                .aQuestion()
                .withReference("q")
                .build()).blockingAwait();

        ruleAnswerDAO.deleteRuleAnswersOfAuditEquipment(ae1).blockingAwait();
        List<QuestionWithRuleAnswer> list = ruleAnswerDAO.getRulesAnswers(ae1).blockingGet();
        List<QuestionWithRuleAnswer> list2 = ruleAnswerDAO.getRulesAnswers(ae2).blockingGet();
        List<QuestionWithRuleAnswer> list3 = ruleAnswerDAO.getRulesAnswers(ae3).blockingGet();

        Assert.assertEquals(list.size(), 0);
        Assert.assertEquals(list2.size(), 2);
        Assert.assertEquals(list3.size(), 1);
    }
}