package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.data.cache.database.NonTables.RuleWithIllustrationsCount;
import com.orange.ocara.data.cache.database.Tables.Question;
import com.orange.ocara.data.cache.database.Tables.Rule;
import com.orange.ocara.data.cache.database.crossRef.QuestionsRulesCrossRef;
import com.orange.ocara.data.cache.database.crossRef.RuleWithIllustrations;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RuleDAOTest extends DAOTest {

    @Test
    public void getRuleWithImpactValueForRule() {
        // todo after refactoring the query and adding ruleset reference in rule_profile_type table
    }
    private void preData(){
        ruleDAO.insert(Rule.RuleBuilder.aRule().withReference("r1").build()).blockingAwait();
        ruleDAO.insert(Rule.RuleBuilder.aRule().withReference("r2").build()).blockingAwait();
        ruleDAO.insert(Rule.RuleBuilder.aRule().withReference("r3").build()).blockingAwait();
        ruleDAO.insert(Rule.RuleBuilder.aRule().withReference("r4").build()).blockingAwait();

        insertQuestionRule("q1","r1","rs1",1);
        insertQuestionRule("q1","r2","rs1",1);
        insertQuestionRule("q1","r3","rs1",1);

        insertQuestionRule("q2","r1","rs1",1);
        insertQuestionRule("q2","r4","rs1",1);

        insertQuestionRule("q3","r2","rs1",1);
        insertQuestionRule("q3","r3","rs2",1);
        insertQuestionRule("q3","r4","rs1",2);

        insertRuleWithIllustration("r1","i1","rs1",1);
        insertRuleWithIllustration("r1","i2","rs1",1);
        insertRuleWithIllustration("r1","i3","rs2",1);

        insertRuleWithIllustration("r2","i1","rs1",1);
        insertRuleWithIllustration("r2","i3","rs1",2);

        insertRuleWithIllustration("r3","i1","rs1",1);
        insertRuleWithIllustration("r3","i2","rs1",2);
        insertRuleWithIllustration("r3","i3","rs2",2);
    }
    private void testingLogicForBothTests(List<RuleWithIllustrationsCount> list,List<RuleWithIllustrationsCount> list2
            ,List<RuleWithIllustrationsCount> list3){
        // q1 -> r1(2),r2(1),r3(1)
        // q2 -> r1(2),r4(0)
        // q3 -> r2(1)
        Assert.assertArrayEquals(new int[]{list.size(),list2.size(),list3.size()},new int[]{3,2,1});

        HashMap<String,Integer> ruleToIllustrationCount=new HashMap<>();
        ruleToIllustrationCount.put("r1",2);
        ruleToIllustrationCount.put("r2",1);
        ruleToIllustrationCount.put("r3",1);
        ruleToIllustrationCount.put("r4",0);

        int q1r1=0,q1r2=0,q1r3=0,q2r1=0,q2r4=0,q3r2=0;
        for(RuleWithIllustrationsCount item:list){
            if(check(item,"r1","q1")){
                q1r1++;
            }else if(check(item,"r2","q1")){
                q1r2++;
            }else if(check(item,"r3","q1")){
                q1r3++;
            }
            Assert.assertEquals(item.getIllustrations(),
                    ruleToIllustrationCount.get(item.getRule().getReference()).intValue());
        }
        for(RuleWithIllustrationsCount item:list2){
            if(check(item,"r1","q2")){
                q2r1++;
            }else if(check(item,"r4","q2")){
                q2r4++;
            }
            Assert.assertEquals(item.getIllustrations(),
                    ruleToIllustrationCount.get(item.getRule().getReference()).intValue());
        }
        for(RuleWithIllustrationsCount item:list3){
            if(check(item,"r2","q3")){
                q3r2++;
            }
            Assert.assertEquals(item.getIllustrations(),
                    ruleToIllustrationCount.get(item.getRule().getReference()).intValue());
        }
        Assert.assertArrayEquals(new int[]{1,1,1, 1,1,1},new int[]{q1r1,q1r2,q1r3,q2r1,q2r4,q3r2});
    }
    @Test
    public void getRulesWithQuestionAndRuleset() {
        preData();
        List<RuleWithIllustrationsCount> list=ruleDAO.getRulesWithQuestionAndRuleset("q1","rs1",1).blockingGet();
        List<RuleWithIllustrationsCount> list2=ruleDAO.getRulesWithQuestionAndRuleset("q2","rs1",1).blockingGet();
        List<RuleWithIllustrationsCount> list3=ruleDAO.getRulesWithQuestionAndRuleset("q3","rs1",1).blockingGet();
        for(RuleWithIllustrationsCount item:list){
            item.setQuestionRef("q1");
        }
        for(RuleWithIllustrationsCount item:list2){
            item.setQuestionRef("q2");
        }
        for(RuleWithIllustrationsCount item:list3){
            item.setQuestionRef("q3");
        }
        testingLogicForBothTests(list,list2,list3);
    }
    private boolean check(RuleWithIllustrationsCount item,String r,String q){
        return item.getQuestionRef().equals(q) && item.getRule().getReference().equals(r);
    }
    private void insertRuleWithIllustration(String r,String i,String rs,int ver){
        ruleWithIllustrationDAO.insert(new RuleWithIllustrations.RuleWithIllustrationBuilder()
                .setVersion(ver)
                .setRulesetRef(rs)
                .setIllustRef(i)
                .setRuleRef(r)
                .createRuleWithIllustrations()).blockingAwait();
    }
    private void insertQuestionRule(String q,String r,String rs,int ver){
        questionRulesDAO.insert(QuestionsRulesCrossRef.QuestionsRulesCrossRefBuilder
                .aQuestionsRulesCrossRef()
                .withQuestionRef(q)
                .withRuleRef(r)
                .withRulesetRef(rs)
                .withRulesetVersion(ver)
                .build()).blockingAwait();
    }
    @Test
    public void testGetRulesWithQuestionAndRuleset() {
        preData();
        List<String> questionRefs=new ArrayList<>();
        questionRefs.add("q1");
        questionRefs.add("q2");
        questionRefs.add("q3");
        List<RuleWithIllustrationsCount> list=ruleDAO.getRulesWithQuestionAndRuleset(questionRefs,"rs1",1).blockingGet();

        List<RuleWithIllustrationsCount> list1=new ArrayList<>();
        List<RuleWithIllustrationsCount> list2=new ArrayList<>();
        List<RuleWithIllustrationsCount> list3=new ArrayList<>();

        for(RuleWithIllustrationsCount item:list){
            if(item.getQuestionRef().equals("q1")){
                list1.add(item);
            }else if(item.getQuestionRef().equals("q2")){
                list2.add(item);
            }else if(item.getQuestionRef().equals("q3")){
                list3.add(item);
            }
        }
        testingLogicForBothTests(list1,list2,list3);
    }
}