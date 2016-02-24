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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data

@ToString(exclude = {"categories", "objectDescriptions", "questionsById"})
@EqualsAndHashCode(exclude = {"categories", "objectDescriptions", "questionsById"})
public class RuleSet {

    private String type;
    private String version;
    private String language;
    private String id;

    private List<Category> categories = new ArrayList<Category>();
    private List<ObjectDescription> objectDescriptions = new ArrayList<ObjectDescription>();
    private Map<String, Question> questionsById = new LinkedHashMap<String, Question>();
    private Map<String, Handicap> handicapsById = new LinkedHashMap<String, Handicap>();


    public ObjectDescription getObjectDescription(String objectDescriptionId) {
        // TODO better search for object description implementation
        for(ObjectDescription objectDescription : objectDescriptions) {
            if (objectDescription.getName().equals(objectDescriptionId)) {
                return objectDescription;
            }
        }
        return null;
    }

    public Question getQuestionById(String questionId) {
        return questionsById.get(questionId);
    }


    public Handicap getHandicapById(String handicapId) {
        return handicapsById.get(handicapId);
    }

}