/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.modelStatic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(exclude = {"rulesById", "nextQuestionByResponseType", "conditions"})
@EqualsAndHashCode(exclude = {"rulesById", "nextQuestionByResponseType", "conditions"})
public class Question {
    private String questionId;
    private String subject;
    private String title;
    private Map<String, Rule> rulesById = new LinkedHashMap<String, Rule>();
    private Map<Response, Question> nextQuestionByResponseType = new LinkedHashMap<Response, Question>();
    private List<Condition> conditions = new ArrayList<Condition>();

    public void addRule(Rule rule) {
        rulesById.put(rule.getId(), rule);
    }

    public Rule getRuleById(String ruleId) {
        return rulesById.get(ruleId);
    }

    public Question getNextQuestion(Response response) {
        if ( response == null ) {
            return null;
        }
        return nextQuestionByResponseType.get(response);
    }

    public void computeAllQuestions(Map<String, Question> result) {
        result.put(questionId, this);
        for(Question q : nextQuestionByResponseType.values()) {
            q.computeAllQuestions(result);
        }
    }

}