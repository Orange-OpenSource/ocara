/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.modelStatic;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(exclude = {"questions"})
@EqualsAndHashCode(exclude = {"questions"})
public class ObjectDescription {
    private String name;
    private String description;
    private String definition;
    private List<ObjectDescription> children  = new ArrayList<ObjectDescription>();
    private List<Illustration> illustrations = new ArrayList<>();

    private URI icon;

    private List<Question> questions = new ArrayList<Question>();




    public Collection<Question> computeAllQuestions() {
        Map<String, Question> result = new LinkedHashMap<String, Question>();
        for(Question q : questions) {
            q.computeAllQuestions(result);
        }
        return result.values();
    }

}
