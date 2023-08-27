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

import com.orange.ocara.data.cache.database.DAOs.QuestionRulesDAO;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.crossRef.QuestionsRulesCrossRef;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class QuestionRuleRepository {
    QuestionRulesDAO questionRulesDAO;
    @Inject
    public QuestionRuleRepository(OcaraDB ocaraDB){
        this.questionRulesDAO=ocaraDB.questionRulesDAO();
    }
    public Completable insert(List<QuestionsRulesCrossRef> lst){
        return questionRulesDAO.insert(lst);
    }
}
