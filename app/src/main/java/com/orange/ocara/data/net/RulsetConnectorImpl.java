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

package com.orange.ocara.data.net;

import android.content.Context;

import androidx.annotation.NonNull;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.data.net.client.ApiClient;
import com.orange.ocara.data.net.model.DatabaseHelper;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.data.net.model.IllustrationEntity;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.RuleEntity;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.data.net.model.RulesetWs;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

import static timber.log.Timber.d;
import static timber.log.Timber.e;
import static timber.log.Timber.i;

/**
 * Default implementation of {@link RulsetConnector}
 */
@EBean
public class RulsetConnectorImpl implements RulsetConnector {

    @RootContext
    Context mContext;

    /**
     * default constructor.
     * Required by the annotation @EBean.
     *
     * @param context a {@link Context}
     */
    RulsetConnectorImpl(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RulesetEntity getRuleSetDetails(final String reference, final Integer version, final boolean download) {
        d("ref : " + reference);

        RulesetEntity load = new Select()
                .from(RulesetEntity.class)
                .where(RulesetEntity.REFERENCE + " = ?", reference)
                .and(RulesetEntity.VERSION + " = ?", String.valueOf(version))
                .executeSingle();

        if (load != null) {
            i("Message=Ruleset exists and its stats is set OFFLINE;RulesetReference=%s;RulesetVersion=%s", reference, version);
            load.setStat(RuleSetStat.OFFLINE);

            return load;
        } else {
            i("Message=Requesting for a ruleset;RulesetReference=%s;RulesetVersion=%s;RequireAddInDatabase=%b", reference, version, download);
            final Call<RulesetWs> call = ApiClient.getRuleSetApi().getRulsetDetail(reference, version);
            try {
                final RulesetEntity entity = RulesetEntity.toEntity(call.execute().body());
                if (entity != null) {
                    if (download) {
                        ActiveAndroid.beginTransaction();
                        entity.save();
                        DatabaseHelper.addQuestionnaireGroups(entity);
                        DatabaseHelper.addObjectDescription(entity, mContext);
                        DatabaseHelper.addQuestionToBase(entity);
                        DatabaseHelper.addQuestionaireToBase(entity);
                        DatabaseHelper.addRuleToBase(entity);
                        DatabaseHelper.addIllustrationToBase(entity, mContext);
                        DatabaseHelper.addImpactToBase(entity);
                        DatabaseHelper.addProfileType(entity, mContext);
                        ActiveAndroid.setTransactionSuccessful();
                        ActiveAndroid.endTransaction();

                        i("Message=Transaction is successful");
                    }
                    return entity;
                } else {
                    return new RulesetEntity();
                }
            } catch (IOException e) {
                e(e, "ErrorMessage=Error encountered while saving a RulesetDetail object;RulesetReference=%s;RulesetVersion=%s;RequireAddInDatabase=%b", reference, version, download);
            }
            return new RulesetEntity();
        }
    }


    @Override
    public QuestionEntity getQuestionFromRef(String ruleSetRef, Integer version, final String reference) {

        RulesetEntity load = new Select()
                .from(RulesetEntity.class)
                .where(RulesetEntity.REFERENCE + " = ?", ruleSetRef)
                .and(RulesetEntity.VERSION + " = ?", String.valueOf(version))
                .executeSingle();

        return new Select()
                .from(QuestionEntity.class)
                .where(QuestionEntity.REFERENCE + " = ?", reference)
                .and(EquipmentEntity.RULESSET_DETAILS + " = ?", load.getId())
                .executeSingle();
    }

    @Override
    public EquipmentEntity getObjectDescriptionFromRef(String ruleSetRef, Integer version, final String reference) {

        d("Message=Retrieving an object description;RulesetRef=%s;RulesetVersion=%d;ObjectDescriptionRef=%s", ruleSetRef, version, reference);

        RulesetEntity load = new Select()
                .from(RulesetEntity.class)
                .where(RulesetEntity.REFERENCE + " = ?", ruleSetRef)
                .and(RulesetEntity.VERSION + " = ?", String.valueOf(version))
                .executeSingle();

        return new Select()
                .from(EquipmentEntity.class)
                .where(EquipmentEntity.REFERENCE + " = ?", reference)
                .and(EquipmentEntity.RULESSET_DETAILS + " = ?", load.getId())
                .executeSingle();
    }

    @Override
    public RuleEntity getRule(final String ruleSetRef, final Integer version, final String ruleRef) {

        RulesetEntity load = new Select()
                .from(RulesetEntity.class)
                .where(RulesetEntity.REFERENCE + " = ?", ruleSetRef)
                .and(RulesetEntity.VERSION + " = ?", String.valueOf(version))
                .executeSingle();

        return new Select()
                .from(RuleEntity.class)
                .where(RuleEntity.REFERENCE + " = ?", ruleRef)
                .and(RuleEntity.RULESET_DETAILS + " = ?", load.getId())
                .executeSingle();
    }

    @Override
    public IllustrationEntity getIllutrationFromRef(final String ruleSetRef, final Integer version, final String ref) {

        RulesetEntity ruleset = new Select()
                .from(RulesetEntity.class)
                .where(RulesetEntity.REFERENCE + " = ?", ruleSetRef)
                .and(RulesetEntity.VERSION + " = ?", String.valueOf(version))
                .executeSingle();

        return new Select()
                .from(IllustrationEntity.class)
                .where(IllustrationEntity.REFERENCE + " = ?", ref)
                .and(IllustrationEntity.DETAIL + " = ?", ruleset.getId())
                .executeSingle();
    }

    @Override
    public List<RulesetEntity> getDownloadedRulesetDetails() {
        return new Select()
                .all()
                .from(RulesetEntity.class)
                .execute();
    }
}
