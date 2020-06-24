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

import androidx.annotation.NonNull;

import com.orange.ocara.business.model.SiteModel;

import java.util.List;

/**
 * description of an offline storage that brings one and only bunch of {@link SiteModel}s
 */
public interface DemoSiteDao {

    boolean isEmpty();

    int count();

    @NonNull
    List<SiteModel> findAll();
}
