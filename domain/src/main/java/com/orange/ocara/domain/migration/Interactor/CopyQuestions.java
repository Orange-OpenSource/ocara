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


package com.orange.ocara.domain.migration.Interactor;

import com.orange.ocara.domain.repositories.QuestionRepository;
import com.orange.ocara.data.cache.database.Tables.Question;
import com.orange.ocara.data.oldEntities.QuestionEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyQuestions extends Interactor<QuestionEntity> {
    QuestionRepository repository;

    @Inject
    public CopyQuestions(QuestionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable execute() {
        List<QuestionEntity> questionEntities = getAll(QuestionEntity.class);
        List<Question> questions = new ArrayList<>();
        for (QuestionEntity questionEntity : questionEntities) {
            questions.add(Question.QuestionBuilder.aQuestion()
                    .withDate(questionEntity.date)
                    .withLabel(questionEntity.label)
                    .withReference(questionEntity.reference)
                    .withState(questionEntity.state)
                    .withSubject(questionEntity.subject==null?null:questionEntity.subject.name)
                    .build());
        }
        return repository.insertQuestions(questions);
    }
}
