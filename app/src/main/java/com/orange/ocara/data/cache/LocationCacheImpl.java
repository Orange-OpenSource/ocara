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
package com.orange.ocara.data.cache;

import com.orange.ocara.business.model.SiteModel;
import com.orange.ocara.data.cache.db.DemoSiteDao;
import com.orange.ocara.data.cache.db.SiteDao;
import com.orange.ocara.data.source.LocationSource;

import java.util.List;

import timber.log.Timber;

/**
 * default implementation of {@link com.orange.ocara.data.source.LocationSource.LocationCache}
 */
public class LocationCacheImpl implements LocationSource.LocationCache {

    private final SiteDao siteDao;

    private final DemoSiteDao locationsReader;

    LocationCacheImpl(SiteDao siteDao, DemoSiteDao locationsReader) {
        this.siteDao = siteDao;
        this.locationsReader = locationsReader;
    }

    @Override
    public boolean exists(String noImmo) {
        return siteDao.exists(noImmo);
    }

    @Override
    public SiteModel save(SiteModel location) {
        return siteDao.save(location);
    }

    @Override
    public Iterable<SiteModel> save(Iterable<SiteModel> items) {
        return siteDao.save(items);
    }

    @Override
    public int count() {
        return siteDao.count();
    }

    @Override
    public void init() {

        List<SiteModel> sites = locationsReader.findAll();
        Timber.i("CacheMessage=Reading available demo sites;NewSitesCount=%d;CurrentCacheSize=%d", sites.size(), siteDao.count());

        for (SiteModel site : sites) {
            if (!siteDao.exists(site)) {
                siteDao.save(site);
            }
        }
    }
}
