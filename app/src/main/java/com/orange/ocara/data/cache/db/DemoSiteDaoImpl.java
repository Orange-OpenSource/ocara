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

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.orange.ocara.R;
import com.orange.ocara.business.model.SiteModel;
import com.orange.ocara.data.common.ConnectorException;
import com.orange.ocara.tools.ListUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

/**
 * default implementation of {@link DemoSiteDao}
 */
public class DemoSiteDaoImpl implements DemoSiteDao {

    private final Context context;

    private final int SITES_RESOURCE = R.raw.sites;

    public DemoSiteDaoImpl(Context context) {

        this.context = context;
    }

    @Override
    public boolean isEmpty() {
        try {
            return findAll().isEmpty();
        } catch (Exception e) {
            Timber.w(e);
            return true;
        }
    }

    @Override
    public int count() {
        try {
            return findAll().size();
        } catch (Exception e) {
            Timber.w(e);
            return 0;
        }
    }

    @Override
    public List<SiteModel> findAll() {

        try (InputStream is = context
                .getResources()
                .openRawResource(SITES_RESOURCE);
             Reader reader = new InputStreamReader(is)) {

            Response data = new GsonBuilder()
                    .create()
                    .fromJson(reader, Response.class);

            if (data == null || data.getCount() == 0) {
                Timber.i("CacheMessage=Retrieving no demo sites;");
                return Collections.emptyList();
            } else {
                Timber.i("CacheMessage=Retrieving some demo sites;SitesCount=%d", data.getCount());
                return Collections.unmodifiableList(data.getResults());
            }

        } catch (Exception e) {
            Timber.e(e, "CacheError=json parsing error");
            throw new ConnectorException("default locations reading", e);
        }
    }

    /**
     * a collection of sites
     *
     * @see res/raw/sites.json
     */
    public static class Response {

        @SerializedName("count")
        private Integer count;

        @SerializedName("results")
        private SiteItem[] results;

        public Response() {
        }

        public Integer getCount() {
            return count;
        }

        public List<SiteItem> getResults() {
            return results == null ? Collections.emptyList() : Arrays.asList(results);
        }
    }

    /**
     * description of a site in the {@link Response}
     * implements {@link SiteModel}
     */
    public class SiteItem implements SiteModel {

        @SerializedName("idUGI")
        private String idUgi;

        @SerializedName("labelUGI")
        private String label;

        @SerializedName("NOIMMO")
        private String noImmo;

        @SerializedName("name")
        private String name;

        @SerializedName("addr")
        private AddressItem addr;

        @SerializedName("type")
        private String type;

        @Override
        public Long getId() {
            return null;
        }

        public String getName() {
            return name;
        }

        public String getNoImmo() {
            return noImmo;
        }

        public String getUgi() {
            return idUgi;
        }

        public String getLabelUgi() {
            return label;
        }

        public String getStreet() {
            return addr.getStreet();
        }

        public Integer getCode() {
            return addr.getCp();
        }

        public String getCity() {
            return addr.getCity();
        }
    }

    /**
     * description of an address that is nested in a  {@link SiteItem}
     */
    public static class AddressItem {

        private String street;

        private Integer cp;

        private String city;

        public AddressItem() {
        }

        public String getStreet() {
            return street;
        }

        public Integer getCp() {
            return cp;
        }

        public String getCity() {
            return city;
        }
    }
}
