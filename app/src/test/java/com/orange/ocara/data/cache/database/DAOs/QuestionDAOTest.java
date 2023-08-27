package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.data.cache.database.NonTables.QuestionWithRuleAnswer;
import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.Tables.Question;
import com.orange.ocara.data.cache.database.Tables.Rule;
import com.orange.ocara.data.cache.database.Tables.RulesetDetails;
import com.orange.ocara.data.cache.database.crossRef.QuestionsEquipmentsCrossRef;
import com.orange.ocara.data.cache.database.crossRef.QuestionsRulesCrossRef;
import com.orange.ocara.utils.enums.RuleSetStat;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QuestionDAOTest extends DAOTest {

    @Test
    public void getQuestionsWithRules() {
        List<Question> questions = new ArrayList<>();
        questions.add(Question.QuestionBuilder.aQuestion()
                .withReference("q1")
                .build());
        questions.add(Question.QuestionBuilder.aQuestion()
                .withReference("q2")
                .build());
        questions.add(Question.QuestionBuilder.aQuestion()
                .withReference("q3")
                .build());
        questions.add(Question.QuestionBuilder.aQuestion()
                .withReference("q4")
                .build());
        questions.add(Question.QuestionBuilder.aQuestion()
                .withReference("q5")
                .build());
        questionDAO.insert(questions).blockingAwait();

        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.RuleBuilder.aRule()
                .withReference("r1")
                .build()
        );
        rules.add(Rule.RuleBuilder.aRule()
                .withReference("r2")
                .build()
        );
        rules.add(Rule.RuleBuilder.aRule()
                .withReference("r3")
                .build()
        );
        rules.add(Rule.RuleBuilder.aRule()
                .withReference("r4")
                .build()
        );
        rules.add(Rule.RuleBuilder.aRule()
                .withReference("r5")
                .build()
        );
        ruleDAO.insert(rules).blockingAwait();

        rulesetDAO.insert(new RulesetDetails.Builder()
                .setReference("r1")
                .setVersion(1)
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .build()).blockingAwait();

        questionRulesDAO.insert(new QuestionsRulesCrossRef("q1", "r1", "r1", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q1", "r4", "r1", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q1", "r2", "r1", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q1", "r3", "r1", 1)).blockingAwait();

        questionRulesDAO.insert(new QuestionsRulesCrossRef("q1", "r4", "r1", 2)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q1", "r5", "r2", 1)).blockingAwait();

        questionRulesDAO.insert(new QuestionsRulesCrossRef("q2", "r1", "r1", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q2", "r2", "r1", 1)).blockingAwait();

        questionRulesDAO.insert(new QuestionsRulesCrossRef("q2", "r3", "r2", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q2", "r4", "r1", 2)).blockingAwait();

        questionRulesDAO.insert(new QuestionsRulesCrossRef("q3", "r1", "r3", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q3", "r2", "r3", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q3", "r1", "r1", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q3", "r2", "r1", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q3", "r3", "r2", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q3", "r4", "r1", 2)).blockingAwait();

        questionRulesDAO.insert(new QuestionsRulesCrossRef("q4", "r5", "r1", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q4", "r2", "r1", 1)).blockingAwait();

        questionRulesDAO.insert(new QuestionsRulesCrossRef("q4", "r3", "r2", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q4", "r4", "r1", 2)).blockingAwait();

        questionRulesDAO.insert(new QuestionsRulesCrossRef("q5", "r1", "r1", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q5", "r2", "r1", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q5", "r3", "r1", 1)).blockingAwait();
        questionRulesDAO.insert(new QuestionsRulesCrossRef("q5", "r4", "r1", 1)).blockingAwait();

        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e1")
                .build()).blockingAwait();

        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e2")
                .build()).blockingAwait();

        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q1", "e1", "r1", 1)).blockingAwait();
        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q2", "e1", "r1", 1)).blockingAwait();
        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q2", "e1", "r1", 2)).blockingAwait();
        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q3", "e1", "r1", 2)).blockingAwait();

        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q3", "e2", "r1", 1)).blockingAwait();
        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q4", "e2", "r1", 1)).blockingAwait();
        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q1", "e1", "r1", 2)).blockingAwait();
        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q5", "e1", "r1", 2)).blockingAwait();

        List<String> objRefs = new ArrayList<>();
        objRefs.add("e1");
        objRefs.add("e2");

        List<QuestionWithRuleAnswer> list = questionDAO.getQuestionsWithRules("r1", objRefs, 1).blockingGet();
        /*
            for (r1,1):
                e1 q1 (r1,r2,r3,r4)
                e1 q2 (r1,r2)
                e2 q3 (r1,r2)
                e2 q4 (r2,r5)
         */
        Assert.assertEquals(list.size(), 10);

        int[] e1q1 = new int[]{0, 0, 0, 0}, e1q2 = new int[]{0, 0}, e2q3 = new int[]{0, 0}, e2q4 = new int[]{0, 0};
        for (QuestionWithRuleAnswer questionWithRuleAnswer : list) {
            if (check(questionWithRuleAnswer, "e1", "q1", "r1")) {
                e1q1[0]++;
            } else if (check(questionWithRuleAnswer, "e1", "q1", "r2")) {
                e1q1[1]++;
            } else if (check(questionWithRuleAnswer, "e1", "q1", "r3")) {
                e1q1[2]++;
            } else if (check(questionWithRuleAnswer, "e1", "q1", "r4")) {
                e1q1[3]++;
            } else if (check(questionWithRuleAnswer, "e1", "q2", "r1")) {
                e1q2[0]++;
            } else if (check(questionWithRuleAnswer, "e1", "q2", "r2")) {
                e1q2[1]++;
            } else if (check(questionWithRuleAnswer, "e2", "q3", "r1")) {
                e2q3[0]++;
            } else if (check(questionWithRuleAnswer, "e2", "q3", "r2")) {
                e2q3[1]++;
            } else if (check(questionWithRuleAnswer, "e2", "q4", "r5")) {
                e2q4[0]++;
            } else if (check(questionWithRuleAnswer, "e2", "q4", "r2")) {
                e2q4[1]++;
            }
        }
        Assert.assertArrayEquals(e1q1, new int[]{1, 1, 1, 1});
        Assert.assertArrayEquals(e1q2, new int[]{1, 1});
        Assert.assertArrayEquals(e2q3, new int[]{1, 1});
        Assert.assertArrayEquals(e2q4, new int[]{1, 1});
    }

    boolean check(QuestionWithRuleAnswer rule, String e, String q, String r) {
        return rule.getQuestion().getReference().equals(q) && rule.getRule().getReference().equals(r)
                && rule.getObjectReference().equals(e);

    }

    @Test
    public void getQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(Question.QuestionBuilder.aQuestion()
                .withReference("q1")
                .build());
        questions.add(Question.QuestionBuilder.aQuestion()
                .withReference("q2")
                .build());
        questions.add(Question.QuestionBuilder.aQuestion()
                .withReference("q3")
                .build());
        questions.add(Question.QuestionBuilder.aQuestion()
                .withReference("q4")
                .build());
        questions.add(Question.QuestionBuilder.aQuestion()
                .withReference("q5")
                .build());
        questionDAO.insert(questions).blockingAwait();

        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e1")
                .build()).blockingAwait();

        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e2")
                .build()).blockingAwait();


        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q1", "e1", "r1", 1)).blockingAwait();
        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q2", "e1", "r1", 1)).blockingAwait();
        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q2", "e1", "r1", 2)).blockingAwait();
        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q3", "e1", "r1", 2)).blockingAwait();

        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q3", "e2", "r1", 1)).blockingAwait();
        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q4", "e2", "r1", 1)).blockingAwait();
        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q1", "e2", "r1", 2)).blockingAwait();
        questionEquipmentDAO.insert(new QuestionsEquipmentsCrossRef("q5", "e2", "r1", 2)).blockingAwait();

        List<Question> list1=questionDAO.getQuestions("r1","e1",1).blockingGet();
        Assert.assertEquals(list1.size(),2);
        int q1=0,q2=0;
        for(Question question:list1){
            if(question.getReference().equals("q1")){
                q1++;
            }else if(question.getReference().equals("q2")){
                q2++;
            }
        }
        Assert.assertArrayEquals(new int[]{1,1},new int[]{q1,q2});

        List<Question> list2=questionDAO.getQuestions("r1","e2",1).blockingGet();
        Assert.assertEquals(list2.size(),2);
        int q3=0,q4=0;
        for(Question question:list2){
            if(question.getReference().equals("q3")){
                q3++;
            }else if(question.getReference().equals("q4")){
                q4++;
            }
        }
        Assert.assertArrayEquals(new int[]{1,1},new int[]{q3,q4});

        List<Question> list3=questionDAO.getQuestions("r1","e1",2).blockingGet();
        Assert.assertEquals(list3.size(),2);
        q2=0;q3=0;
        for(Question question:list3){
            if(question.getReference().equals("q2")){
                q2++;
            }else if(question.getReference().equals("q3")){
                q3++;
            }
        }
        Assert.assertArrayEquals(new int[]{1,1},new int[]{q2,q3});

        List<Question> list4=questionDAO.getQuestions("r1","e2",2).blockingGet();
        Assert.assertEquals(list4.size(),2);
        int q5=0;q1=0;
        for(Question question:list4){
            if(question.getReference().equals("q1")){
                q1++;
            }else if(question.getReference().equals("q5")){
                q5++;
            }
        }
        Assert.assertArrayEquals(new int[]{1,1},new int[]{q1,q5});
    }
}