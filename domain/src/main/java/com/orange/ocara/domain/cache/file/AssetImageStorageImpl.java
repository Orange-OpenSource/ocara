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
package com.orange.ocara.domain.cache.file;

import android.content.Context;

import com.orange.ocara.utils.exceptions.ConnectorException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

/** implementation of {@link ImageStorage} that reads images stored in assets */
public class AssetImageStorageImpl implements ImageStorage {

    private final Context context;

    private final String rootDirectory;

    /**
     * instantiate
     *
     * @param context an Android {@link Context}
     * @param rootDirectory an assets subdirectory
     */
    public AssetImageStorageImpl(Context context, String rootDirectory) {
        this.context = context;
        this.rootDirectory = rootDirectory;
    }

    @Override
    public List<String> list(String directory) {

        List<String> assets;

        try {
            String[] list = context.getAssets().list(rootDirectory + File.separator + directory);
            assets = list == null ? Collections.emptyList() : Arrays.asList(list);
        } catch (IOException e) {
            Timber.w(e);
            assets = Collections.emptyList();
        }

        return assets;
    }

    @Override
    public InputStream read(String filename, String directory) {

        try {
            String path = rootDirectory + File.separator;
            if (!directory.isEmpty()){
                path = path + directory + File.separator;
            }
            path = path + filename;
            return context.getAssets().open(path);
        } catch (IOException e) {

            throw ConnectorException.from(e);
        }
    }

    @Override
    public boolean fileExists(String filename, String directory) {
        return list(directory).contains(filename);
    }

    @Override
    public boolean isEmpty(String directory) {
        return list(directory).isEmpty();
    }

    @Override
    public void write(InputStream inputStream, String targetFilename, String targetSubDirectory) {
        throw new ConnectorException("Writing in the assets is not allowed");
    }
}
