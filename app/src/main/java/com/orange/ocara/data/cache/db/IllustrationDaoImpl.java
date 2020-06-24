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
import com.orange.ocara.data.net.model.IllustrationEntity;
import com.orange.ocara.data.net.model.RuleEntity;

import java.util.Collections;
import java.util.List;

/**
 * default implementation of {@link IllustrationDao}
 */
public class IllustrationDaoImpl implements IllustrationDao {

    private final static String WHERE_BY_ID_CLAUSE = IllustrationEntity.ID + " = ?";

    private final static String WHERE_BY_REF_CLAUSE = IllustrationEntity.REFERENCE + " = ?";

    private final static String WHERE_BY_RULESET_ID_CLAUSE = IllustrationEntity.DETAIL + " = ?";

    @Override
    public IllustrationEntity findOne(Long id) {
        return new Select()
                .from(IllustrationEntity.class)
                .where(WHERE_BY_ID_CLAUSE, id)
                .executeSingle();
    }

    @Override
    public IllustrationEntity findOne(Long rulesetId, String illustrationReference) {
        return new Select()
                .from(IllustrationEntity.class)
                .where(WHERE_BY_REF_CLAUSE, illustrationReference)
                .and(WHERE_BY_RULESET_ID_CLAUSE, rulesetId)
                .executeSingle();
    }

    @Override
    public boolean exists(Long id) {
        return new Select()
                .from(IllustrationEntity.class)
                .where(WHERE_BY_ID_CLAUSE, id)
                .exists();
    }

    @Override
    public boolean exists(Long rulesetId, String illustrationReference) {
        return new Select()
                .from(IllustrationEntity.class)
                .where(WHERE_BY_REF_CLAUSE, illustrationReference)
                .and(WHERE_BY_RULESET_ID_CLAUSE, rulesetId)
                .exists();
    }

    @Override
    public List<IllustrationEntity> findAllByRulesetId(Long rulesetId) {
        return new Select()
                .from(IllustrationEntity.class)
                .where(WHERE_BY_RULESET_ID_CLAUSE, rulesetId)
                .execute();
    }

    @Override
    public List<IllustrationEntity> findAllByRuleId(Long ruleId) {

        RuleEntity rule = new Select()
                .from(RuleEntity.class)
                .where(RuleEntity.ID + " = ?", ruleId)
                .executeSingle();

        List<IllustrationEntity> result;
        if (rule.getIllustration() == null || rule.getIllustration().isEmpty()) {
            result = Collections.emptyList();
        } else {
            List<String> refs = rule.getIllustration();
            int length = refs.size();
            Character[] placeholders = new Character[length];
            for (int i = 0; i < length; i++) {
                placeholders[i] = '?';
            }
            result = new Select()
                    .from(IllustrationEntity.class)
                    .where(IllustrationEntity.REFERENCE + " IN (" + TextUtils.join(",", placeholders) + ")", refs.toArray())
                    .and(WHERE_BY_RULESET_ID_CLAUSE, rule.getRuleSeDetail().getId())
                    .execute();
        }

        return result;
    }
}
