/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import android.content.Context;
import android.content.res.AssetManager;

import com.orange.ocara.model.loader.SitesJsonParser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
/* package. */ class SitesLoaderImpl implements SitesLoader {

    public static final String SITES_FOLDER = "sites";
    private final Context context;

    /**
     * Constructor.
     *
     * @param context
     */
    @Inject
    public SitesLoaderImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void initialize() {
    }

    @Override
    public Set<String> getUninstalledSitePackages() {
        Set<String> result = new HashSet<String>();

        final AssetManager assetManager = context.getAssets();

        try {
            String[] assetsList = assetManager.list(SITES_FOLDER);
            for (String asset : assetsList) {
                if (isJsonFile(asset)) {
                    Timber.i("site package : %s", asset);
                    File packagesDir = getInstalledSitePackageDir();
                    File target = new File(packagesDir, asset);
                    if (!target.exists()) {
                        result.add(asset);
                        Timber.i("site package is not installed %s", asset);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Site> installSitePackage(String packageName) {

        Timber.i("installing site package %s", packageName);
        File packagesDir = getInstalledSitePackageDir();
        File target = new File(packagesDir, packageName);

        SitesJsonParser parser = new SitesJsonParser();
        InputStream jsonStream = null;

        try {
            FileUtils.copyInputStreamToFile(context.getAssets().open(SITES_FOLDER + "/" + packageName), target);

            jsonStream = new FileInputStream(target);
            parser.load(jsonStream);

            return parser.getSites();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(jsonStream);
        }
    }

    private File getInstalledSitePackageDir() {
        return context.getDir(SITES_FOLDER, Context.MODE_PRIVATE);
    }

    @Override
    public void terminate() {
    }

    private boolean isJsonFile(String asset) {
        return FilenameUtils.isExtension(asset, "json");
    }

}
