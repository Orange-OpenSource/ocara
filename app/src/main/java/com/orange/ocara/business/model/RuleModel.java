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

import com.orange.ocara.data.net.model.RuleEntity;

public class RuleModel {

    private final long id;

    private final String label;

    private final long groupId;

    private final boolean illustrated;

    private final int index;

    public RuleModel(RuleEntity rule, long questionId, int i) {
        this.id = rule.getId();
        this.label = rule.getLabel();
        this.groupId = questionId;
        this.illustrated = rule.withIllustrations();
        index = i;
    }

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public long getGroupId() {
        return groupId;
    }

    public int getIndex() {
        return index;
    }

    public boolean isIllustrated() {
        return illustrated;
    }
}
