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

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.data.common.ConnectorException;
import com.orange.ocara.data.net.model.DatabaseHelper;
import com.orange.ocara.data.net.model.RulesetEntity;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RulesetDaoImpl implements RulesetDao {

    private final Context context;

    @Override
    public boolean exists(String reference) {
        return new Select()
                .from(RulesetEntity.class)
                .where(RulesetEntity.REFERENCE + " = ?", reference)
                .exists();
    }

    @Override
    public boolean exists(String reference, Integer version) {
        return new Select()
                .from(RulesetEntity.class)
                .where(RulesetEntity.REFERENCE + " = ?", reference)
                .and(RulesetEntity.VERSION + " = ?", version)
                .exists();
    }

    @Override
    public boolean exists(VersionableModel ruleset) {

        return exists(ruleset.getReference(), Integer.valueOf(ruleset.getVersion()));
    }

    @Override
    public RulesetEntity findLast(String reference) {
        RulesetEntity ruleset = new Select()
                .from(RulesetEntity.class)
                .where(RulesetEntity.REFERENCE + " = ?", reference)
                .orderBy("version DESC")
                .limit(0)
                .executeSingle();

        if (ruleset != null) {
            ruleset.setStat(RuleSetStat.OFFLINE);
        }

        return ruleset;

    }

    @Override
    public RulesetEntity findOne(String reference, Integer version) {
        RulesetEntity ruleset = new Select()
                .from(RulesetEntity.class)
                .where(RulesetEntity.REFERENCE + " = ?", reference)
                .and(RulesetEntity.VERSION + " = ?", version)
                .executeSingle();

        if (ruleset != null) {
            ruleset.setStat(RuleSetStat.OFFLINE);
        }

        return ruleset;
    }

    @Override
    public RulesetEntity findOne(Long rulesetId) {
        RulesetEntity ruleset = Model.load(RulesetEntity.class, rulesetId);

        if (ruleset != null) {
            ruleset.setStat(RuleSetStat.OFFLINE);
        }

        return ruleset;
    }

    @Override
    public List<RulesetEntity> findAll() {
        List<RulesetEntity> entities = new Select()
                .all()
                .from(RulesetEntity.class)
                .execute();

        for (RulesetEntity entity : entities) {
            entity.setStat(RuleSetStat.OFFLINE);
        }

        return entities;
    }

    @Override
    public RulesetEntity save(RulesetEntity entity) {

        try {
            ActiveAndroid.beginTransaction();
            entity.save();

            DatabaseHelper.addQuestionnaireGroups(entity);
            DatabaseHelper.addObjectDescription(entity);
            DatabaseHelper.addQuestionToBase(entity);
            DatabaseHelper.addQuestionaireToBase(entity);
            DatabaseHelper.addRuleToBase(entity);
            DatabaseHelper.addIllustration(entity);
            DatabaseHelper.addImpactToBase(entity);
            DatabaseHelper.addProfileType(entity);

            ActiveAndroid.setTransactionSuccessful();

            ActiveAndroid.endTransaction();

            entity.setStat(RuleSetStat.OFFLINE);

            return entity;
        } catch (Exception ex) {

            throw ConnectorException.from(ex);
        }
    }
}
