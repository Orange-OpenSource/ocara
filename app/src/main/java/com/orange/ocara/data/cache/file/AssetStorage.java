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

package com.orange.ocara.data.cache.file;

import android.content.Context;

import com.orange.ocara.data.common.ConnectorException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import timber.log.Timber;

import static java.util.Objects.requireNonNull;

/**
 * implementation of {@link FileStorage} for assets
 */
@RequiredArgsConstructor
public class AssetStorage implements FileStorage {

    private final Context context;

    @Override
    public List<String> list(String subDirectory) {
        try {
            return Arrays.asList(requireNonNull(context.getAssets().list(subDirectory)));
        } catch (IOException e) {
            Timber.e(e, "Could not list elements from %s", subDirectory);
            throw ConnectorException.from(e);
        }
    }

    @Override
    public File read(String filename, String subDirectory) {
        return fileExists(subDirectory, filename) ? new File("file:///android_asset/" + subDirectory + File.separator + filename) : null;
    }

    @Override
    public boolean isEmpty(String subDirectory) {
        return list(subDirectory).isEmpty();
    }

    @Override
    public boolean fileExists(String filename, String subDirectory) {

        return list(subDirectory).contains(filename);
    }

    @Override
    public void write(InputStream inputStream, String targetFilename, String targetSubDirectory) {

        throw new ConnectorException("Invalid operation;Writing in assets is not allowed");
    }
}
