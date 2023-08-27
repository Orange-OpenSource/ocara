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


package com.orange.ocara.domain.interactors;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.orange.ocara.domain.models.RulesetModel;
import com.orange.ocara.domain.repositories.RulesetRepository;
import com.orange.ocara.data.network.models.RulesetWs;
import com.orange.ocara.utils.Utils;

import java.lang.reflect.Type;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SaveDefaultRuleset {
    private final Context context;
    private final RulesetRepository repository;
    @Inject
    public SaveDefaultRuleset(@ApplicationContext Context context,RulesetRepository repository){
        this.context=context;
        this.repository=repository;
    }
    public Completable execute(){
        String json= Utils.getRuleset(context);
        Gson gson = new Gson();
        Type rulesetType = new TypeToken<RulesetWs>() {
        }.getType();
        RulesetWs rulesetWs = gson.fromJson(json, rulesetType);
        rulesetWs.setIsDemo(true);
        repository.saveDefaultRuleset(RulesetModel.builder()
                .reference(rulesetWs.getReference())
                .version(""+rulesetWs.getVersion()).isDemo(true)
                .build());
        repository.saveIcons(rulesetWs);
        return repository.saveInRoom(rulesetWs);
    }
}
