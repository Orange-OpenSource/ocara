/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.data.cache.db;

import android.text.TextUtils;

import com.activeandroid.query.Select;
import com.orange.ocara.data.net.model.ChainEntity;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.QuestionnaireEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.orange.ocara.tools.ListUtils.newArrayList;

/**
 * Default implementation for {@link QuestionDao}
 */
public class QuestionDaoImpl implements QuestionDao {

    @Override
    public QuestionEntity findOne(Long questionId) {
        return new Select()
                .from(QuestionEntity.class)
                .where(QuestionEntity.ID + " = ?", questionId)
                .executeSingle();
    }

    @Override
    public QuestionEntity findOne(Long rulesetId, String reference) {
        return new Select()
                .from(QuestionEntity.class)
                .where(QuestionEntity.REFERENCE + " = ?", reference)
                .and(EquipmentEntity.RULESSET_DETAILS + " = ?", rulesetId)
                .executeSingle();
    }

    @Override
    public boolean exists(Long rulesetId, String reference) {
        return new Select()
                .from(QuestionEntity.class)
                .where(QuestionEntity.REFERENCE + " = ?", reference)
                .and(EquipmentEntity.RULESSET_DETAILS + " = ?", rulesetId)
                .exists();
    }

    @Override
    public List<QuestionEntity> findAll(Long rulesetId, List<String> references) {

        List<QuestionEntity> output = newArrayList();

        for (String ref : references) {
            if (exists(rulesetId, ref)) {
                output.add(findOne(rulesetId, ref));
            }
        }
        return output;
    }

    @Override
    public List<QuestionEntity> findAll(Long rulesetId, Long equipmentId) {

        EquipmentEntity equipment = new Select()
                .from(EquipmentEntity.class)
                .where(EquipmentEntity.ID + "=?", equipmentId)
                .executeSingle();

        QuestionnaireEntity questionnaire = new Select()
                .from(QuestionnaireEntity.class)
                .where(QuestionnaireEntity.RULSETDETAILS + "= ?", rulesetId)
                .and(QuestionnaireEntity.OBJECT_DESCRIPTION + "= ?", equipment.getReference())
                .executeSingle();

        List<ChainEntity> chains = questionnaire.getChaineDb();
        List<QuestionEntity> questions = newArrayList();
        List<QuestionEntity> tmpQuestions;
        Character[] placeholders;
        int length;
        for (ChainEntity chain : chains) {
            List<String>  questionRefs = chain.getQuestionsRef();
            length = questionRefs.size();
            if (length > 0) {
                placeholders = new Character[length];
                for (int i = 0; i < length; i++) {
                    placeholders[i] = '?';
                }
                tmpQuestions = new Select()
                        .from(QuestionEntity.class)
                        .where(QuestionEntity.REFERENCE + " IN (" + TextUtils.join(",", placeholders) + ")", questionRefs.toArray())
                        .and(QuestionEntity.RULESET_DETAILS + "= ?", rulesetId)
                        .execute();
                Collections.sort(tmpQuestions, (o1, o2) -> Integer.compare(questionRefs.indexOf(o1.getReference()), questionRefs.indexOf(o2.getReference())));
                questions.addAll(tmpQuestions);
            }
        }

        return questions;
    }
}
