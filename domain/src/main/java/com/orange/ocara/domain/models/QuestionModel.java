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


package com.orange.ocara.domain.models;

import com.orange.ocara.data.cache.database.Tables.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionModel {
    private final String ref;
    private final String label;
    private int index;
    private AuditEquipmentModel auditEquipment;
    private final List<RuleModel> rules = new ArrayList<>();

    public QuestionModel(Question q, int i) {
        this(q);
        index = i;
    }

    public QuestionModel(Question q) {
        this.ref = q.getReference();
        this.label = q.getLabel();
    }

    public List<RuleModel> getRules() {
        return rules;
    }

    public void addRule(RuleModel rule) {
        rules.add(rule);
    }

    public AuditEquipmentModel getAuditEquipment() {
        return auditEquipment;
    }

    public void setAuditEquipment(AuditEquipmentModel auditEquipment) {
        this.auditEquipment = auditEquipment;
    }

    public String getLabel() {
        return label;
    }

    public String getRef() {
        return ref;
    }

    public int getIndex() {
        return index;
    }

}
