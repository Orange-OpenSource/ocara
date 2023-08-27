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


package com.orange.ocara.domain.repositories;

import com.orange.ocara.domain.models.QuestionModel;
import com.orange.ocara.data.cache.database.DAOs.QuestionDAO;
import com.orange.ocara.data.cache.database.DAOs.QuestionEquipmentDAO;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.Question;
import com.orange.ocara.data.cache.database.crossRef.QuestionsEquipmentsCrossRef;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class QuestionRepository {
    QuestionDAO questionDAO;
    QuestionEquipmentDAO questionEquipmentDAO;
    @Inject
    public QuestionRepository(OcaraDB ocaraDB){
        questionDAO=ocaraDB.questionDao();
        questionEquipmentDAO = ocaraDB.questionEquipmentDAO();
    }
    public Single<List<QuestionModel>> findAll(String rulesetRef, String objRef, int version) {
        return questionDAO.getQuestions(rulesetRef, objRef, version).map(questions -> {
            List<QuestionModel> questionModels=new ArrayList<>();
            int i=0;
            for(Question question:questions){
                questionModels.add(new QuestionModel(question,i++));
            }
            return questionModels;
        });
    }
    public Completable insertQuestions(List<Question> questions){
        return questionDAO.insert(questions);
    }
    public Completable insertEquipmentQuestions(List<QuestionsEquipmentsCrossRef> list){
        return questionEquipmentDAO.insert(list);
    }
}
