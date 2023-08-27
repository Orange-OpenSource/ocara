package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.domain.models.QuestionAnswerModel;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.AnswerCount;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.CountImpactName;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.NoAnsWithProfileAndImpact;
import com.orange.ocara.data.cache.database.NonTables.scoreCalc.YesDoubtNoAnsNAAnsWithprofileAndAns;
import com.orange.ocara.data.cache.database.Tables.ImpactValue;
import com.orange.ocara.data.cache.database.Tables.ProfileType;
import com.orange.ocara.data.cache.database.Tables.Rule;
import com.orange.ocara.data.cache.database.Tables.RuleAnswer;
import com.orange.ocara.data.cache.database.crossRef.AuditEquipments;
import com.orange.ocara.data.cache.database.crossRef.RuleProfileTypeImpactCrossRef;
import com.orange.ocara.utils.enums.Answer;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ReportScoresDAOTest extends DAOTest {
    private void preDataForNo() {
        int ae1 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setObjectRef("e1")
                .createAuditEquipments()).blockingGet().intValue();

        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r1")
                .setQuestionRef("q1")
                .setAnswer(Answer.NOK)
                .setAuditEquipmentId(ae1)
                .createRuleAnswer()).blockingAwait();

        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r2")
                .setQuestionRef("q1")
                .setAnswer(Answer.NOK)
                .setAuditEquipmentId(ae1)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r3")
                .setQuestionRef("q1")
                .setAnswer(Answer.NOK)
                .setAuditEquipmentId(ae1)
                .createRuleAnswer()).blockingAwait();

        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r4")
                .setQuestionRef("q1")
                .setAnswer(Answer.OK)
                .setAuditEquipmentId(ae1)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r5")
                .setQuestionRef("q1")
                .setAnswer(Answer.DOUBT)
                .setAuditEquipmentId(ae1)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r6")
                .setQuestionRef("q1")
                .setAnswer(Answer.NO_ANSWER)
                .setAuditEquipmentId(ae1)
                .createRuleAnswer()).blockingAwait();

        profileTypeDAO.insert(ProfileType.ProfileTypeBuilder.aProfileType()
                .withReference("p1")
                .withName("profile1")
                .build()).blockingAwait();
        profileTypeDAO.insert(ProfileType.ProfileTypeBuilder.aProfileType()
                .withReference("p2")
                .withName("profile2")
                .build()).blockingAwait();
        profileTypeDAO.insert(ProfileType.ProfileTypeBuilder.aProfileType()
                .withReference("p3")
                .withName("profile3")
                .build()).blockingAwait();

        impactValueDAO.insert(ImpactValue.ImpactValueBuilder.anImpactValue()
                .withReference("1")
                .withName("i1")
                .build()).blockingAwait();
        impactValueDAO.insert(ImpactValue.ImpactValueBuilder.anImpactValue()
                .withReference("2")
                .withName("i2")
                .build()).blockingAwait();
        impactValueDAO.insert(ImpactValue.ImpactValueBuilder.anImpactValue()
                .withReference("3")
                .withName("i3")
                .build()).blockingAwait();

        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p1")
                .setRuleImpactRef("2")
                .setRuleRef("r1")
                .setRulesetRef("r1")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p1")
                .setRuleImpactRef("2")
                .setRuleRef("r2")
                .setRulesetRef("r1")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p1")
                .setRuleImpactRef("3")
                .setRuleRef("r1")
                .setRulesetRef("r1")

                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p1")
                .setRuleImpactRef("3")
                .setRuleRef("r3")
                .setRulesetRef("r1")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p1")
                .setRuleImpactRef("3")
                .setRuleRef("r5")
                .setRulesetRef("r1")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();


        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p2")
                .setRuleImpactRef("2")
                .setRuleRef("r2")
                .setRulesetRef("r1")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p2")
                .setRuleImpactRef("2")
                .setRuleRef("r3")
                .setRulesetRef("r1")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p2")
                .setRuleImpactRef("2")
                .setRuleRef("r4")
                .setRulesetRef("r1")

                .createRuleProfileTypeImpactCrossRef()).blockingAwait();

        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p2")
                .setRuleImpactRef("3")
                .setRuleRef("r2")
                .setRulesetRef("r1")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p2")
                .setRuleImpactRef("3")
                .setRuleRef("r5")
                .setRulesetRef("r1")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p2")
                .setRuleImpactRef("3")
                .setRuleRef("r4")
                .setRulesetRef("r1")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();

        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p1")
                .setRuleImpactRef("1")
                .setRuleRef("r3")
                .setRulesetRef("r1")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p1")
                .setRuleImpactRef("1")
                .setRuleRef("r5")
                .setRulesetRef("r1")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p3")
                .setRuleImpactRef("1")
                .setRuleRef("r5")
                .setRulesetRef("r1")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
    }

    private void preDataForYesAndDoubt() {
        int ae1 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setObjectRef("e1")
                .createAuditEquipments()).blockingGet().intValue();
        int ae2 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setObjectRef("e2")
                .createAuditEquipments()).blockingGet().intValue();
        int ae3 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(2)
                .setObjectRef("e1")
                .createAuditEquipments()).blockingGet().intValue();

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
                .setQuestionRef("q1")
                .setAnswer(Answer.DOUBT)
                .setAuditEquipmentId(ae2)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r4")
                .setQuestionRef("q1")
                .setAnswer(Answer.NOK)
                .setAuditEquipmentId(ae2)
                .createRuleAnswer()).blockingAwait();

        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r1")
                .setQuestionRef("q1")
                .setAnswer(Answer.OK)
                .setAuditEquipmentId(ae3)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r2")
                .setQuestionRef("q1")
                .setAnswer(Answer.DOUBT)
                .setAuditEquipmentId(ae3)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r3")
                .setQuestionRef("q1")
                .setAnswer(Answer.DOUBT)
                .setAuditEquipmentId(ae3)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r4")
                .setQuestionRef("q1")
                .setAnswer(Answer.NOK)
                .setAuditEquipmentId(ae3)
                .createRuleAnswer()).blockingAwait();

        profileTypeDAO.insert(ProfileType.ProfileTypeBuilder.aProfileType()
                .withReference("p1")
                .withName("profile1")
                .build()).blockingAwait();
        profileTypeDAO.insert(ProfileType.ProfileTypeBuilder.aProfileType()
                .withReference("p2")
                .withName("profile2")
                .build()).blockingAwait();
        profileTypeDAO.insert(ProfileType.ProfileTypeBuilder.aProfileType()
                .withReference("p3")
                .withName("profile3")
                .build()).blockingAwait();

        impactValueDAO.insert(ImpactValue.ImpactValueBuilder.anImpactValue()
                .withReference("1")
                .build()).blockingAwait();
        impactValueDAO.insert(ImpactValue.ImpactValueBuilder.anImpactValue()
                .withReference("2")
                .build()).blockingAwait();

        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p1")
                .setRuleImpactRef("2")
                .setRuleRef("r1")
                .setRulesetRef("r1")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p1")
                .setRuleImpactRef("2")
                .setRulesetRef("r1")

                .setRuleRef("r2")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p1")
                .setRuleImpactRef("1")
                .setRulesetRef("r1")

                .setRuleRef("r3")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p2")
                .setRuleImpactRef("2")
                .setRulesetRef("r1")

                .setRuleRef("r2")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p2")
                .setRuleImpactRef("2")
                .setRulesetRef("r1")

                .setRuleRef("r3")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p2")
                .setRuleImpactRef("2")
                .setRulesetRef("r1")

                .setRuleRef("r4")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();

        ruleProfileTypeImpactDAO.insert(new RuleProfileTypeImpactCrossRef.Builder()
                .setProfileRef("p3")
                .setRuleImpactRef("1")
                .setRulesetRef("r1")

                .setRuleRef("r4")
                .createRuleProfileTypeImpactCrossRef()).blockingAwait();
    }

    // why this query include join with impact value table
    @Test
    public void getNumberOfRulesAnsweredByYesOrDoubtOrNoAnsOrNAGrpByAnsAndProfileForAudit() {
        preDataForYesAndDoubt();
        List<YesDoubtNoAnsNAAnsWithprofileAndAns> list = reportScoresDAO.getNumberOfRulesAnsweredByYesOrDoubtOrNoAnsOrNAGrpByAnsAndProfileForAudit(1L).blockingGet();
        Assert.assertEquals(list.size(), 3);
        int p1_2_yes = 0, p2_1_ok = 0, p2_1_doubt = 0;
        for (YesDoubtNoAnsNAAnsWithprofileAndAns item : list) {
            if (check(item, "p1", 2, Answer.OK)) {
                p1_2_yes++;
                Assert.assertEquals(item.getProfileName(), "profile1");
            } else if (check(item, "p2", 1, Answer.OK)) {
                p2_1_ok++;
                Assert.assertEquals(item.getProfileName(), "profile2");
            } else if (check(item, "p2", 1, Answer.DOUBT)) {
                p2_1_doubt++;
                Assert.assertEquals(item.getProfileName(), "profile2");
            }
        }
        Assert.assertArrayEquals(new int[]{1, 1, 1}, new int[]{p1_2_yes, p2_1_doubt, p2_1_ok});
        list = reportScoresDAO.getNumberOfRulesAnsweredByYesOrDoubtOrNoAnsOrNAGrpByAnsAndProfileForAudit(2L).blockingGet();
        Assert.assertEquals(list.size(), 3);
        int p1_1_ok = 0, p1_1_d = 0, p2_2_d = 0;
        for (YesDoubtNoAnsNAAnsWithprofileAndAns item : list) {
            if (check(item, "p1", 1, Answer.OK)) {
                p1_1_ok++;
            } else if (check(item, "p1", 1, Answer.DOUBT)) {
                p1_1_d++;
            } else if (check(item, "p2", 2,Answer.DOUBT)) {
                p2_2_d++;
            }
        }
        Assert.assertArrayEquals(new int[]{1, 1, 1}, new int[]{p1_1_ok, p1_1_d, p2_2_d});
    }

    private boolean check(YesDoubtNoAnsNAAnsWithprofileAndAns item, String p, int count, Answer answer) {
        return item.getProfileRef().equals(p) && item.getAnswer().equals(answer)
                && item.getCount() == count;
    }

    @Test
    public void getNumberOfRulesAnsweredByNoGrpByImpactAndProfileForAudit() {
        preDataForNo();
        // p2 1 no 3i
        // p2 2 nos 2i
        // p1 2nos 3i
        // p1 2nos 2i
        List<NoAnsWithProfileAndImpact> list = reportScoresDAO.getNumberOfRulesAnsweredByNoGrpByImpactAndProfileForAudit(1L).blockingGet();
        Assert.assertEquals(list.size(), 4);
        int arr[] = new int[]{0, 0, 0, 0};
        for (NoAnsWithProfileAndImpact item : list) {
            Assert.assertEquals(item.getAnswer(), Answer.NOK);
            if (check2(item, "p2", "i3", 1)) {
                arr[0]++;
                Assert.assertEquals(item.getProfileName(), "profile2");
            } else if (check2(item, "p2", "i2", 2)) {
                arr[1]++;
                Assert.assertEquals(item.getProfileName(), "profile2");
            } else if (check2(item, "p1", "i3", 2)) {
                arr[2]++;
                Assert.assertEquals(item.getProfileName(), "profile1");
            } else if (check2(item, "p1", "i2", 2)) {
                arr[3]++;
                Assert.assertEquals(item.getProfileName(), "profile1");
            }
        }
        Assert.assertArrayEquals(arr, new int[]{1, 1, 1, 1});
    }

    private boolean check2(NoAnsWithProfileAndImpact item, String p, String i, int cnt) {
        return item.getCount() == cnt && item.getProfileRef().equals(p) && item.getImpactName().equals(i);
    }

    @Test
    public void getTotalNumRulesYesOrDoubtHasAnyImpact() {
        preDataForYesAndDoubt();
        List<AnswerCount> list = reportScoresDAO.getTotalNumRulesYesOrDoubtHasAnyImpact(1L).blockingGet();
        Assert.assertEquals(list.size(), 2);
        int yes_2 = 0, doubt_1 = 0;
        for (AnswerCount item : list) {
            if (item.getAnswer().equals(Answer.OK) && item.getCount() == 2) {
                yes_2++;
            } else if (item.getAnswer().equals(Answer.DOUBT) && item.getCount() == 1) {
                doubt_1++;
            }
        }
        Assert.assertArrayEquals(new int[]{1, 1}, new int[]{yes_2, doubt_1});

        list = reportScoresDAO.getTotalNumRulesYesOrDoubtHasAnyImpact(2L).blockingGet();
        Assert.assertEquals(list.size(), 2);
        int yes_1 = 0, doubt_2 = 0;
        for (AnswerCount item : list) {
            if (item.getAnswer().equals(Answer.OK) && item.getCount() == 1) {
                yes_1++;
            } else if (item.getAnswer().equals(Answer.DOUBT) && item.getCount() == 2) {
                doubt_2++;
            }
        }
        Assert.assertArrayEquals(new int[]{1, 1}, new int[]{yes_1, doubt_2});
    }

    @Test
    public void getTotalNumRulesNoHasAnyImpact() {
        preDataForNo();
        // 3 3i
        // 3 2i
        List<CountImpactName> list = reportScoresDAO.getTotalNumRulesNoHasAnyImpact(1L).blockingGet();
        Assert.assertEquals(list.size(), 2);
        int arr[] = new int[]{0, 0};
        for (CountImpactName item : list) {
            if (item.getCount() == 3 && item.getImpactName().equals("i3")) {
                arr[0]++;
            } else if (item.getCount() == 3 && item.getImpactName().equals("i2")) {
                arr[1]++;
            }
        }
        Assert.assertArrayEquals(arr, new int[]{1, 1});
    }

    @Test
    public void getAnomaliesAuditEquipment() {
        int ae1 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setObjectRef("e1")
                .createAuditEquipments()).blockingGet().intValue();
        int ae2 = auditEquipmentDAO.insert(new AuditEquipments.AuditEquipmentsBuilder()
                .setAudit_id(1)
                .setObjectRef("e2")
                .createAuditEquipments()).blockingGet().intValue();

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

        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r1")
                .setQuestionRef("q1")
                .setAnswer(Answer.OK)
                .setAuditEquipmentId(ae1)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r2")
                .setQuestionRef("q1")
                .setAnswer(Answer.NOK)
                .setAuditEquipmentId(ae1)
                .createRuleAnswer()).blockingAwait();

        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r3")
                .setQuestionRef("q1")
                .setAnswer(Answer.DOUBT)
                .setAuditEquipmentId(ae2)
                .createRuleAnswer()).blockingAwait();
        ruleAnswerDAO.insert(new RuleAnswer.RuleAnswerBuilder()
                .setRuleRef("r4")
                .setQuestionRef("q1")
                .setAnswer(Answer.DOUBT)
                .setAuditEquipmentId(ae2)
                .createRuleAnswer()).blockingAwait();

        List<Rule> ruleAnswers = reportScoresDAO.getAnomaliesAuditEquipment((long) ae1).blockingGet();
        List<Rule> ruleAnswers2 = reportScoresDAO.getAnomaliesAuditEquipment((long) ae2).blockingGet();
        Assert.assertEquals(ruleAnswers.size(), 1);
        Assert.assertEquals(ruleAnswers.get(0).getReference(), "r2");

        Assert.assertEquals(ruleAnswers2.size(), 2);
        int r3 = 0, r4 = 0;
        for (Rule rule : ruleAnswers2) {
            if (rule.getReference().equals("r3")) {
                r3++;
            } else if (rule.getReference().equals("r4")) {
                r4++;
            }
        }
        Assert.assertArrayEquals(new int[]{1, 1}, new int[]{r3, r4});
    }
}