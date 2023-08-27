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


package com.orange.ocara.domain.repositories;

import com.orange.ocara.domain.models.SiteModel;
import com.orange.ocara.data.cache.database.DAOs.SiteDAO;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.Site;
import com.orange.ocara.data.oldEntities.SiteEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class SiteRepository {
    SiteDAO siteDAO;
    @Inject
    public SiteRepository(OcaraDB ocaraDB){
        siteDAO=ocaraDB.siteDAO();
    }

    public Single<List<SiteModel>> filterSites(String pattern){
        return siteDAO.getSites(pattern).map(sites -> {
            List<SiteModel> siteModels=new ArrayList<>();
            for(Site site:sites){
                siteModels.add(new SiteModel(site));
            }
            return siteModels;
        });
    }

    public Single<SiteModel> getSiteById(int id){
        return siteDAO.getSiteById(id).map(site -> new SiteModel(site));
    }

    public Single<List<SiteModel>> getAllSites(){
        return siteDAO.getAllSites().map(sites -> {
            List<SiteModel> siteModels=new ArrayList<>();
            for(Site site:sites){
                siteModels.add(new SiteModel(site));
            }
            return siteModels;
        });
    }
    public Single<List<SiteModel>> getAllSitesSortedDesc(){
        return siteDAO.getAllSitesSortedDesc().map(sites -> {
            List<SiteModel> siteModels=new ArrayList<>();
            for(Site site:sites){
                siteModels.add(new SiteModel(site));
            }
            return siteModels;
        });
    }

    public Completable insert(List<SiteEntity> siteEntities){
        List<Site> sites=new ArrayList<>();
        for(SiteEntity site:siteEntities){
            sites.add(mapToEntity(new SiteModel(site)));
        }
        return siteDAO.insert(sites);
    }

    public Observable<Long> insert(SiteModel siteModel){
        Site site = mapToEntity(siteModel);
        Timber.d("site name = "+siteModel.getName());
        return siteDAO.getSiteByNoImmoOrName(siteModel.getNoImmo(),siteModel.getName())
                .toObservable()
                .concatMap((Function<List<Site>, ObservableSource<Long>>) sites -> {
                    if(sites.isEmpty()){
                        return siteDAO.insert(site).toObservable();
                    }else{
                        Timber.d("in here");
                        return Observable.just(-1l);
                    }
                });
    }


    public Completable update(SiteModel siteModel){
        Site site = mapToEntity(siteModel);
        return siteDAO.updateSite(site);
    }

    public Completable insert(Site site){
        return Completable.fromSingle(siteDAO.insert(site));
    }
    private Site mapToEntity(SiteModel siteModel) {
        Site site=Site.builder().addressCity(siteModel.getAddressCity())
                .addressCode(siteModel.getAddressCode())
                .addressStreet(siteModel.getAddressStreet())
                .name(siteModel.getName())
                .noImmo(siteModel.getNoImmo())
                .build();
        if(siteModel.getId()!=0){
            site.setId(siteModel.getId());
        }
        return site;
    }

    public Single<Integer> getNumberOfAudits(int id) {
        return siteDAO.getNumberOfRelatedAudits(id);
    }

    public Completable deleteSite(int id) {
        return siteDAO.delete(id) ;
    }
}
