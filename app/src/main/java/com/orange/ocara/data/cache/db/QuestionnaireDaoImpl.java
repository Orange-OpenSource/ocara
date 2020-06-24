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

import com.activeandroid.query.Select;
import com.orange.ocara.data.net.model.QuestionnaireEntity;
import com.orange.ocara.data.net.model.RulesetById;
import com.orange.ocara.data.net.model.WithReference;

/**
 * Default implementation of {@link QuestionnaireDao}
 */
public class QuestionnaireDaoImpl implements QuestionnaireDao {

    @Override
    public QuestionnaireEntity findOne(Long questionnaireId) {
        return new Select()
                .from(QuestionnaireEntity.class)
                .where( QuestionnaireEntity.ID + "=?", questionnaireId)
                .executeSingle();
    }

    @Override
    public QuestionnaireEntity findOne(RulesetById ruleset, WithReference objectRef) {
        return new Select()
                .from(QuestionnaireEntity.class)
                .where( QuestionnaireEntity.RULSETDETAILS + "=?", ruleset.getId())
                .and(QuestionnaireEntity.OBJECT_DESCRIPTION + "=?", objectRef)
                .executeSingle();
    }

    @Override
    public boolean exists(RulesetById ruleset, WithReference objectRef) {
        return new Select()
                .from(QuestionnaireEntity.class)
                .where( QuestionnaireEntity.RULSETDETAILS + "=?", ruleset.getId())
                .and(QuestionnaireEntity.OBJECT_DESCRIPTION + "=?", objectRef)
                .exists();
    }
}
