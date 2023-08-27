package com.orange.ocara.data.cache.database.NonTables;
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
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.Tables.Question;
import com.orange.ocara.data.cache.database.crossRef.QuestionsEquipmentsCrossRef;

import java.util.List;

public class EquipmentWithQuestions {
    @Embedded
    private Equipment equipment;
    @Relation(
            entity = Question.class,
            parentColumn = "objectReference",
            entityColumn = "questionRef",
            associateBy = @Junction(QuestionsEquipmentsCrossRef.class)
    )
    private List<QuestionWithRuleAnswer> questions;

    public List<QuestionWithRuleAnswer> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionWithRuleAnswer> questions) {
        this.questions = questions;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }


}
