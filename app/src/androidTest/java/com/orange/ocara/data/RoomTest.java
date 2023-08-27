
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
package com.orange.ocara.data;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.orange.ocara.data.cache.room.DAOs.*;
import com.orange.ocara.data.cache.room.Tables.*;
import com.orange.ocara.data.cache.room.crossRef.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class RoomTest {
    private OcaraDB ocaraDB;
    private RulesetDAO rulesetDAO;
    private EquipmentDAO equipmentDao;
    private CategoryDAO categoryDao;
    private QuestionDAO questionDao;
    private RuleDAO ruleDao;
    private ImpactValueDAO impactValueDao;
    private ProfileTypeDAO profileTypeDao;
    private AuditDAO auditDao;
    private AuditEquipmentDAO auditEquipmentDao;
    private CommentDAO commentDao;

    @Before
    public void setup(){
        ocaraDB = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                OcaraDB.class
        ).allowMainThreadQueries().build();
        rulesetDAO = ocaraDB.rulesetDAO();
        equipmentDao = ocaraDB.equipmentDao();
        categoryDao = ocaraDB.categoryDao();
        questionDao = ocaraDB.questionDao();
        ruleDao = ocaraDB.ruleDao();
        impactValueDao = ocaraDB.impactValueDao();
        profileTypeDao = ocaraDB.profileTypeDao();
        auditDao = ocaraDB.auditDao();
        auditEquipmentDao = ocaraDB.auditObjectDao();
        commentDao = ocaraDB.commentDao();
    }


    @After
    public void finish(){
        ocaraDB.close();
    }

    /*@Test
    public void insertRuleset_retrieveIt(){
        final RulesetDetails ruleset=new RulesetDetails.Builder().setReference("r1").build();
        rulesetDAO.insert(ruleset).subscribe(() -> {
            insertRuleset_retrieveIt2();
        }, throwable -> {
            Assert.fail();
        });

    }

    private void insertRuleset_retrieveIt2() {
        rulesetDAO.getRuleset("r1").subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RulesetDetails>(){
                    @Override
                    public void onSuccess(RulesetDetails rulesetDetails) {
                        Assert.assertEquals(rulesetDetails.getReference(),"r1");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Assert.fail();
                    }
                });
    }

    @Test
    public void insertObjectAndCategories_checkRelationIsCorrect(){
        List<Completable> completables=new ArrayList<>();
        Equipment obj1 = new Equipment("obj1",null,null,null,null,null,null);
        completables.add(equipmentDao.insert(obj1));

        Equipment obj2 = new Equipment("obj2",null,null,null,null,null,null);
        completables.add(equipmentDao.insert(obj2));

        Equipment obj3 = new Equipment("obj3",null,null,null,null,null,null);
        completables.add(equipmentDao.insert(obj3));

        Equipment obj4 = new Equipment("obj4",null,null,null,null,null,null);
        completables.add(equipmentDao.insert(obj4));

        Equipment obj5 = new Equipment("obj5",null,null,null,null,null,null);
        completables.add(equipmentDao.insert(obj5));

        Equipment subobject1 = new Equipment("subobject1",null,null,null,null,"obj5",null);
        completables.add(equipmentDao.insert(subobject1));

        Equipment subobject2 = new Equipment("subobject2",null,null,null,null,"obj5",null);
        completables.add(equipmentDao.insert(subobject2));

        EquipmentCategory cat1 = new EquipmentCategory("cat1","RS1");
        completables.add(categoryDao.insert(cat1));

        EquipmentCategory cat2 = new EquipmentCategory("cat2","RS1");
        completables.add(categoryDao.insert(cat2));

        EquipmentCategory cat3 = new EquipmentCategory("cat3","RS2");
        completables.add(categoryDao.insert(cat3));

        completables.add(categoryDao.addObject(new EquipmentAndCategoryCrossRef("obj1",1)));
        completables.add(categoryDao.addObject(new EquipmentAndCategoryCrossRef("obj2",1)));
        completables.add(categoryDao.addObject(new EquipmentAndCategoryCrossRef("obj3",2)));
        completables.add(categoryDao.addObject(new EquipmentAndCategoryCrossRef("obj4",2)));
        completables.add(categoryDao.addObject(new EquipmentAndCategoryCrossRef("obj5",3)));

        Completable.concat(completables)
                .subscribe(()->{
                    insertObjectAndCategories_checkRelationIsCorrect2();
                },e->{
                    Assert.fail();
                });

    }

    private void insertObjectAndCategories_checkRelationIsCorrect2() {
        categoryDao.getCategories("RS1")
                .subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<CategoryWithEquipments>>(){
                    @Override
                    public void onSuccess(List<CategoryWithEquipments> list) {
                        Assert.assertEquals(list.size(),2);
                        Assert.assertEquals(list.get(0).getEquipmentCategory().getName(),"cat1");
                        Assert.assertEquals(list.get(1).getEquipmentCategory().getName(),"cat2");
                        Assert.assertEquals(list.get(0).getObjects().get(0).getEquipment().getReference(),"obj1");
                        Assert.assertEquals(list.get(0).getObjects().get(1).getEquipment().getReference(),"obj2");
                        Assert.assertEquals(list.get(1).getObjects().get(0).getEquipment().getReference(),"obj3");
                        Assert.assertEquals(list.get(1).getObjects().get(1).getEquipment().getReference(),"obj4");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Assert.fail();
                    }
                });

        categoryDao.getCategories("RS2")
                .subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<CategoryWithEquipments>>(){
                    @Override
                    public void onSuccess(List<CategoryWithEquipments> list) {
                        Assert.assertEquals(list.size(),1);
                        Assert.assertEquals(list.get(0).getEquipmentCategory().getName(),"cat3");
                        Assert.assertEquals(list.get(0).getObjects().get(0).getEquipment().getReference(),"obj5");
                        Assert.assertEquals(list.get(0).getObjects().get(0).getSubobject().get(0).getReference(),"subobject1");
                        Assert.assertEquals(list.get(0).getObjects().get(0).getSubobject().get(1).getReference(),"subobject2");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Assert.fail();
                    }
                });
    }

    @Test
    public void insertObjectsWithQuestions_checkRelation(){
        List<Completable> completables=new ArrayList<>();
        Equipment obj1 = new Equipment("obj1",null,null,null,null,null,null);
        Equipment obj2 = new Equipment("obj2",null,null,null,null,null,null);
        Equipment obj3 = new Equipment("obj3",null,null,null,null,null,null);
        Question q1 = new Question("Q1",null,null,null,null,null);
        Question q2 = new Question("Q2",null,null,null,null,null);
        Question q3 = new Question("Q3",null,null,null,null,null);
        Question q4 = new Question("Q4",null,null,null,null,null);
        Question q5 = new Question("Q5",null,null,null,null,null);
        Question q6 = new Question("Q6",null,null,null,null,null);

        completables.add(equipmentDao.insert(obj1));
        completables.add(equipmentDao.insert(obj2));
        completables.add(equipmentDao.insert(obj3));

        completables.add(questionDao.insert(q1));
        completables.add(questionDao.insert(q2));
        completables.add(questionDao.insert(q3));
        completables.add(questionDao.insert(q4));
        completables.add(questionDao.insert(q5));
        completables.add(questionDao.insert(q6));

        completables.add(equipmentDao.insert(new QuestionsEquipmentsCrossRef("Q1","obj1")));
        completables.add(equipmentDao.insert(new QuestionsEquipmentsCrossRef("Q2","obj1")));
        completables.add(equipmentDao.insert(new QuestionsEquipmentsCrossRef("Q3","obj1")));

        completables.add(equipmentDao.insert(new QuestionsEquipmentsCrossRef("Q2","obj2")));
        completables.add(equipmentDao.insert(new QuestionsEquipmentsCrossRef("Q4","obj2")));
        completables.add(equipmentDao.insert(new QuestionsEquipmentsCrossRef("Q5","obj2")));

        completables.add(equipmentDao.insert(new QuestionsEquipmentsCrossRef("Q4","obj3")));
        completables.add(equipmentDao.insert(new QuestionsEquipmentsCrossRef("Q2","obj3")));
        completables.add(equipmentDao.insert(new QuestionsEquipmentsCrossRef("Q6","obj3")));
        Completable.concat(completables)
                .subscribe(()->{
                    insertObjectsWithQuestions_checkRelation2();
                },e->{
                    Assert.fail();
                });

    }

    private void insertObjectsWithQuestions_checkRelation2() {
        List<String> objs = new ArrayList<>();
        objs.add("obj1");
        objs.add("obj2");

        equipmentDao.getQuestions(objs)
                .subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<EquipmentWithQuestions>>(){
                    @Override
                    public void onSuccess(List<EquipmentWithQuestions> equipmentWithQuestions) {
                        Assert.assertEquals(equipmentWithQuestions.size(),2);
                        Assert.assertEquals(equipmentWithQuestions.get(0).getEquipment().getReference(),"obj1");
                        Assert.assertEquals(equipmentWithQuestions.get(0).getQuestions().size(),3);
                        Assert.assertEquals(equipmentWithQuestions.get(0).getQuestions().get(0).getQuestion().getReference(),"Q1");
                        Assert.assertEquals(equipmentWithQuestions.get(0).getQuestions().get(1).getQuestion().getReference(),"Q2");
                        Assert.assertEquals(equipmentWithQuestions.get(0).getQuestions().get(2).getQuestion().getReference(),"Q3");

                        Assert.assertEquals(equipmentWithQuestions.get(1).getEquipment().getReference(),"obj2");
                        Assert.assertEquals(equipmentWithQuestions.get(1).getQuestions().get(0).getQuestion().getReference(),"Q2");
                        Assert.assertEquals(equipmentWithQuestions.get(1).getQuestions().get(1).getQuestion().getReference(),"Q4");
                        Assert.assertEquals(equipmentWithQuestions.get(1).getQuestions().get(2).getQuestion().getReference(),"Q5");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Assert.fail();
                    }
                });


        objs.clear();
        objs.add("obj3");
        equipmentDao.getQuestions(objs)
                .subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<EquipmentWithQuestions>>(){
                    @Override
                    public void onSuccess(List<EquipmentWithQuestions> equipmentWithQuestions) {
                        Assert.assertEquals(equipmentWithQuestions.size(),1);
                        Assert.assertEquals(equipmentWithQuestions.get(0).getEquipment().getReference(),"obj3");
                        Assert.assertEquals(equipmentWithQuestions.get(0).getQuestions().size(),3);
                        Assert.assertEquals(equipmentWithQuestions.get(0).getQuestions().get(0).getQuestion().getReference(),"Q4");
                        Assert.assertEquals(equipmentWithQuestions.get(0).getQuestions().get(1).getQuestion().getReference(),"Q2");
                        Assert.assertEquals(equipmentWithQuestions.get(0).getQuestions().get(2).getQuestion().getReference(),"Q6");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Assert.fail();
                    }
                });
    }

    @Test
    public void checkRelationFromObjectTillRules(){
        List<Completable> completables=new ArrayList<>();
        Equipment obj1=new Equipment("obj1",null,null,null,null,null,null);
        Question q1 = new Question("Q1",null,null,null,null,null);
        Rule r1 = new Rule("r1",null,null,null);
        completables.add(equipmentDao.insert(obj1));
        completables.add(questionDao.insert(q1));
        completables.add(ruleDao.insert(r1));
        completables.add(equipmentDao.insert(new QuestionsEquipmentsCrossRef("Q1","obj1")));
        completables.add(questionDao.insert(new QuestionsRulesCrossRef("Q1","r1")));
        Completable.concat(completables)
                .subscribe(()->{
                    checkRelationFromObjectTillRules2();
                },e->{
                    Assert.fail();
                });
    }

    private void checkRelationFromObjectTillRules2() {
        List<String> objs = new ArrayList<>();
        objs.add("obj1");
        equipmentDao.getQuestions(objs)
                .subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<EquipmentWithQuestions>>(){
                    @Override
                    public void onSuccess(List<EquipmentWithQuestions> list) {
                        Assert.assertEquals(list.size(),1);
                        Assert.assertEquals(list.get(0).getEquipment().getReference(),"obj1");
                        Assert.assertEquals(list.get(0).getQuestions().size(),1);
                        Assert.assertEquals(list.get(0).getQuestions().get(0).getQuestion().getReference(),"Q1");
                        Assert.assertEquals(list.get(0).getQuestions().get(0).getRules().size(),1);
                        Assert.assertEquals(list.get(0).getQuestions().get(0).getRules().get(0).getReference(),"r1");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Assert.fail();
                    }
                });
    }

    @Test
    public void insertRuleWithProfileTypeAndImpactValue_checkRelation(){
        List<Completable> completables=new ArrayList<>();
        Rule r1=new Rule("r1",null,null,null);
        Rule r2=new Rule("r2",null,null,null);
        ImpactValue iv1=new ImpactValue("iv1",null,false);
        ImpactValue iv2=new ImpactValue("iv2",null,false);
        ImpactValue iv3=new ImpactValue("iv3",null,false);
        ProfileType p1=new ProfileType(null,"p1",null);
        ProfileType p2=new ProfileType(null,"p2",null);
        ProfileType p3=new ProfileType(null,"p3",null);
        completables.add(ruleDao.insert(r1));
        completables.add(ruleDao.insert(r2));
        completables.add(impactValueDao.insert(iv1));
        completables.add(impactValueDao.insert(iv2));
        completables.add(impactValueDao.insert(iv3));
        completables.add(profileTypeDao.insert(p1));
        completables.add(profileTypeDao.insert(p2));
        completables.add(profileTypeDao.insert(p3));

        completables.add(ruleDao.insert(new RuleProfileTypeImpactCrossRef("r1","iv1","p1")));
        completables.add(ruleDao.insert(new RuleProfileTypeImpactCrossRef("r1","iv1","p2")));
        completables.add(ruleDao.insert(new RuleProfileTypeImpactCrossRef("r1","iv2","p3")));
        completables.add(ruleDao.insert(new RuleProfileTypeImpactCrossRef("r2","iv3","p1")));
        completables.add(ruleDao.insert(new RuleProfileTypeImpactCrossRef("r2","iv3","p2")));
        Completable.concat(completables)
                .subscribe(()->{
                    insertRuleWithProfileTypeAndImpactValue_checkRelation2();
                },e->{
                    Assert.fail();
                });
    }

    private void insertRuleWithProfileTypeAndImpactValue_checkRelation2() {
        ruleDao.getRuleWithImpactValue()
                .subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<RuleWithProfiletypeImpact>>(){
                    @Override
                    public void onSuccess(List<RuleWithProfiletypeImpact> list) {
                        Assert.assertEquals(list.size(),5);
                        Assert.assertEquals(list.get(0).getRule().getReference(),"r1");
                        Assert.assertEquals(list.get(1).getRule().getReference(),"r1");
                        Assert.assertEquals(list.get(2).getRule().getReference(),"r1");
                        Assert.assertEquals(list.get(3).getRule().getReference(),"r2");
                        Assert.assertEquals(list.get(4).getRule().getReference(),"r2");

                        Assert.assertEquals(list.get(0).getImpactValue().getReference(),"iv1");
                        Assert.assertEquals(list.get(1).getImpactValue().getReference(),"iv1");
                        Assert.assertEquals(list.get(2).getImpactValue().getReference(),"iv2");
                        Assert.assertEquals(list.get(3).getImpactValue().getReference(),"iv3");
                        Assert.assertEquals(list.get(4).getImpactValue().getReference(),"iv3");

                        Assert.assertEquals(list.get(0).getProfileType().getReference(),"p1");
                        Assert.assertEquals(list.get(1).getProfileType().getReference(),"p2");
                        Assert.assertEquals(list.get(2).getProfileType().getReference(),"p3");
                        Assert.assertEquals(list.get(3).getProfileType().getReference(),"p1");
                        Assert.assertEquals(list.get(4).getProfileType().getReference(),"p2");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Assert.fail();
                    }
                });
    }

    @Test
    public void insertAuditWithCommentsAndObjects_checkRelation(){
        List<Completable> completables=new ArrayList<>();
        Audit a1=new Audit("a1",null,null,null,null,null,0,0,0);
        Audit a2=new Audit("a2",null,null,null,null,null,0,0,0);
        Equipment obj1 = new Equipment("obj1",null,null,null,null,null,null);
        Equipment obj2 = new Equipment("obj2",null,null,null,null,"obj1",null);

        Comment c1=new Comment(null,null,null,"c1");
        Comment c2=new Comment(null,null,null,"c2");
        Comment c3=new Comment(null,null,null,"c3");
        Comment c4=new Comment(null,null,null,"c4");
        c1.setAudit_id(1);
        c2.setAudit_id(1);
        c3.setAudit_id(2);
        c4.setAudit_id(2);

        AuditEquipments ao1=new AuditEquipments("obj1", AuditEquipments.Status.GREEN);
        ao1.setAudit_id(1);
        AuditEquipments ao2=new AuditEquipments("obj1", AuditEquipments.Status.YELLOW);
        ao2.setAudit_id(2);
        AuditEquipments ao3=new AuditEquipments("obj2", AuditEquipments.Status.RED);
        ao3.setParentId(1);

        completables.add(auditDao.insert(a1));
        completables.add(equipmentDao.insert(obj1));
        completables.add(equipmentDao.insert(obj2));
        completables.add(auditDao.insert(a2));
        completables.add(commentDao.insert(c1));
        completables.add(commentDao.insert(c2));
        completables.add(commentDao.insert(c3));
        completables.add(commentDao.insert(c4));
        completables.add(auditObjectDao.insert(ao1));
        completables.add(auditObjectDao.insert(ao2));
        completables.add(auditObjectDao.insert(ao3));

        Completable.concat(completables)
                .subscribe(()->{
                    insertAuditWithCommentsAndObjects_checkRelation2();
                },e->{
                    Assert.fail();
                });
    }

    private void insertAuditWithCommentsAndObjects_checkRelation2() {
        auditDao.getAudits()
                .subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<AuditWithCommentAndObjects>>(){
                    @Override
                    public void onSuccess(List<AuditWithCommentAndObjects> list) {
                        Assert.assertEquals(list.size(),2);
                        Assert.assertEquals(list.get(0).audit.getName(),"a1");
                        Assert.assertEquals(list.get(0).comments.size(),2);
                        Assert.assertEquals(list.get(0).comments.get(0).getContent(),"c1");
                        Assert.assertEquals(list.get(0).comments.get(1).getContent(),"c2");
                        Assert.assertEquals(list.get(0).auditObjects.size(),1);
                        Assert.assertEquals(list.get(0).auditObjects.get(0).auditEquipments.getStatus(), AuditEquipments.Status.GREEN);
                        Assert.assertEquals(list.get(0).auditObjects.get(0).equipment.getReference(), "obj1");
                        Assert.assertEquals(list.get(0).auditObjects.get(0).subobjects.size(), 1);
                        Assert.assertEquals(list.get(0).auditObjects.get(0).subobjects.get(0).auditEquipments.getStatus(), AuditEquipments.Status.RED);
                        Assert.assertEquals(list.get(0).auditObjects.get(0).subobjects.get(0).equipment.getReference(), "obj2");

                        Assert.assertEquals(list.get(1).audit.getName(),"a2");
                        Assert.assertEquals(list.get(1).comments.size(),2);
                        Assert.assertEquals(list.get(1).comments.get(0).getContent(),"c3");
                        Assert.assertEquals(list.get(1).comments.get(1).getContent(),"c4");
                        Assert.assertEquals(list.get(1).auditObjects.size(),1);
                        Assert.assertEquals(list.get(1).auditObjects.get(0).equipment.getReference(), "obj1");
                        Assert.assertEquals(list.get(1).auditObjects.get(0).auditEquipments.getStatus(), AuditEquipments.Status.YELLOW);
                        Assert.assertEquals(list.get(1).auditObjects.get(0).subobjects.size(), 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Assert.fail();
                    }
                });
    }*/
    @Test
    public void insertRules_checkRulesQuestionRelation(){
        String ref="R1";
        int version=2;
        Rule rule=new Rule("r1","","","");
        Question question=new Question("q1","","","","");
        Completable completable=ocaraDB.ruleDao().insert(rule);
        completable=completable.concatWith(ocaraDB.questionDao().insert(question));
        completable=completable.concatWith(ocaraDB.questionRulesDAO().insert(new QuestionsRulesCrossRef("q1","r1",ref,version)));
        completable.subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        retrieve("r1","q1",ref,version);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
    public void retrieve(String rule,String quest,String ref,int version){
        ocaraDB.ruleDao().getRulesWithQuestionAndRuleset(quest,ref,version)
                .subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Rule>>() {
                    @Override
                    public void onSuccess(List<Rule> rules) {
                        Assert.assertEquals(rules.size(),1);
                        Assert.assertEquals(rules.get(0).getReference(),rule);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }) ;
    }
}
