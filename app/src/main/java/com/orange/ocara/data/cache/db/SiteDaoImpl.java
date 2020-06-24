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

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.orange.ocara.business.model.SiteModel;
import com.orange.ocara.data.cache.model.SiteEntity;

import timber.log.Timber;

/**
 * default implementation of {@link SiteDao}
 */
public class SiteDaoImpl implements SiteDao {

    @Override
    public int count() {
        return new Select().from(SiteEntity.class).count();
    }

    @Override
    public boolean exists(String noImmo) {
        return noImmo != null
                && new Select()
                .from(SiteEntity.class)
                .where(SiteEntity.COLUMN_NOIMMO + " LIKE ?", noImmo)
                .exists();
    }

    @Override
    public boolean exists(SiteModel location) {

        return location != null && exists(location.getNoImmo());
    }

    @Override
    public SiteModel save(SiteModel input) {
        SiteEntity entity = null;

        if (input != null) {
            ActiveAndroid.beginTransaction();

            try {
                entity = SiteEntity.from(input);
                if (entity != null) {
                    entity.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } catch (Exception ex) {
                Timber.e("CacheMessage=Error encountered while trying to save a demo site;SiteInfo=%s;", input.getCode());
            } finally {
                ActiveAndroid.endTransaction();
            }
        }
        return entity;
    }

    @Override
    public Iterable<SiteModel> save(Iterable<SiteModel> items) {

        SiteEntity entity;

        ActiveAndroid.beginTransaction();
        try {
            for (SiteModel model : items) {
                entity = SiteEntity.from(model);
                Timber.d("CacheMessage=Trying to save a demo site;SiteInfo=%s", entity.toString());
                entity.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception ex) {
            Timber.e(ex, "CacheMessage=Error encountered while trying to save a demo site;");
        } finally {
            ActiveAndroid.endTransaction();
        }

        return items;
    }
}
