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

import com.orange.ocara.domain.repositories.SiteRepository;
import com.orange.ocara.data.cache.JsonModels.Site;
import com.orange.ocara.utils.Utils;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.Completable;

public class SaveDefaultSite {
    private final Context context;
    private final SiteRepository repository;

    @Inject
    public SaveDefaultSite(@ApplicationContext Context context,SiteRepository repository) {
        this.context = context;
        this.repository = repository;
    }

    public Completable execute() {
        String jsonFileString = Utils.getSite(context);

        Gson gson = new Gson();
        Type siteType = new TypeToken<List<Site>>() {
        }.getType();

        List<Site> siteLst = gson.fromJson(jsonFileString, siteType);
        Site site = siteLst.get(0);
        com.orange.ocara.data.cache.database.Tables.Site siteEntity = com.orange.ocara.data.cache.database.Tables.Site.builder()
                .addressCity(site.getAddr().getCity())
                .addressCode(Integer.parseInt(site.getAddr().getCp()))
                .addressStreet(site.getAddr().getStreet())
                .name(site.getName())
                .noImmo(site.getNOIMMO())
                .build();

        return repository.insert(siteEntity);

    }
}
