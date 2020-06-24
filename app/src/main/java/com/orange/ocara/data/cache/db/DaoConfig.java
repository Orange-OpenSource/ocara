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

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/** Configuration for the data/cache layer */
@EBean(scope = EBean.Scope.Singleton)
public class DaoConfig {

    private RulesetDao rulesetDao;

    private TermsDao termsDao;

    private SiteDao siteDao;

    private EquipmentDao objectDescriptionDao;

    private ProfileTypeDao profileTypeDao;

    @RootContext
    void setContext(Context rootContext) {
        rulesetDao = new RulesetDaoImpl(rootContext);
        termsDao = new TermsDaoImpl(rootContext);
        siteDao = new SiteDaoImpl();
    }

    public RulesetDao rulesetDao() {
        return rulesetDao;
    }

    public TermsDao termsDao() {
        return termsDao;
    }

    public SiteDao locationDao() {
        return siteDao;
    }

    public EquipmentDao objectDescriptionDao() {
        return objectDescriptionDao;
    }

    public ProfileTypeDao profileTypeDao() {
        return profileTypeDao;
    }
}
