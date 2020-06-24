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
package com.orange.ocara.business.model;

import com.orange.ocara.data.net.model.QuestionEntity;

/**
 * a model for rules'groups, (aka Questions)
 */
public class RuleGroupModel {

    private final long id;

    private final String label;

    private final int index;

    public RuleGroupModel(QuestionEntity q, int i) {
        this.id = q.getId();
        this.label = q.getLabel();
        index = i;
    }

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public int getIndex() {
        return index;
    }
}
