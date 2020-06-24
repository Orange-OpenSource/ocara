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
import com.orange.ocara.tools.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import timber.log.Timber;

/**
 * Implementation of {@link FileStorage}
 */
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorage {

    private final Context context;

    private final StorageType storageType;

    @Override
    public List<String> list(String subDirectory) {

        return Arrays.asList(directory(subDirectory).list());
    }

    @Override
    public File read(String filename, String subDirectory) {
        File directory = directory(subDirectory);
        return new File(directory, filename);
    }

    @Override
    public boolean isEmpty(String subDirectory) {
        return directory(subDirectory).list().length == 0;
    }

    @Override
    public void write(InputStream inputStream, String targetFilename, String targetSubDirectory) {

        try {
            File directory = directory(targetSubDirectory);
            if (directory.exists() || directory.mkdirs()) {
                File target = new File(directory, targetFilename);
                Timber.i("Trying to copy InputStream to file;TargetAbsolutePath=%s;TargetFileName=%s", target.getAbsolutePath(), targetFilename);

                FileUtils.copyInputStreamToFile(inputStream, target);
            } else {
                Timber.w("InputStream %s could not be copied to %s", targetFilename, targetSubDirectory);
            }
        } catch (IOException e) {

            Timber.e(e, "Failed writing file %s to %s", targetFilename, targetSubDirectory);
            throw ConnectorException.from(e);
        }
    }

    @Override
    public boolean fileExists(String filename, String subDirectory) {
        File directory = directory(subDirectory);
        File file = new File(directory, filename);
        return directory.exists() && file.exists() && file.isFile();
    }

    private File directory(String subDirectory) {
        return storageType.storageDirectory(context, subDirectory);
    }
}
