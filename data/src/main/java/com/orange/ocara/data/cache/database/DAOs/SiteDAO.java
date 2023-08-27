package com.orange.ocara.data.cache.database.DAOs;
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
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.orange.ocara.data.cache.database.Tables.Site;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface SiteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(Site site);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<Site> site);

    @Query("delete from sites where siteId=:id")
    Completable delete(int id);

    @Query("select * from sites where name like '%' || :pattern || '%'")
    Single<List<Site>> getSites(String pattern);

    @Query("select * from sites where siteId=:id")
    Single<Site> getSiteById(int id);

    @Query("select * from sites")
    Single<List<Site>> getAllSites();

    @Query("select * from sites order by siteId DESC")
    Single<List<Site>> getAllSitesSortedDesc();

    @Query("select * from sites where noImmo=:noImmo or name=:name")
    Single<List<Site>> getSiteByNoImmoOrName(String noImmo, String name);

    @Update
    Completable updateSite(Site site);

    @Query("select count(audit_id)  from audit left join sites on auditSiteId = siteId  where siteId=:id")
    Single<Integer> getNumberOfRelatedAudits(int id);

}
