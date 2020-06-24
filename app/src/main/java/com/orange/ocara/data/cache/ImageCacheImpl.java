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

import android.net.Uri;

import com.orange.ocara.data.cache.file.FileStorage;
import com.orange.ocara.data.cache.file.ImageStorage;
import com.orange.ocara.data.source.ImageSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import timber.log.Timber;

/**
 * default implementation of {@link com.orange.ocara.data.source.ImageSource.ImageCache}
 *
 * Manages local storage for images
 */
public class ImageCacheImpl implements ImageSource.ImageCache {

    private final FileStorage fileStorage;

    private final ImageStorage imageReader;

    private static final String IMAGES_SUB_DIRECTORY = "";

    ImageCacheImpl(FileStorage fileStorage, ImageStorage imageReader) {
        this.fileStorage = fileStorage;
        this.imageReader = imageReader;
    }

    @Override
    public File get(String filename) {
        File file = null;
        if (fileStorage.fileExists(filename, IMAGES_SUB_DIRECTORY)) {
            file = fileStorage.read(filename, IMAGES_SUB_DIRECTORY);
        }
        return file;
    }

    @Override
    public Uri getAsUri(String filename) {
        File file = null;
        if (fileStorage.fileExists(filename, IMAGES_SUB_DIRECTORY)) {
            file = fileStorage.read(filename, IMAGES_SUB_DIRECTORY);
        }
        return file != null ? Uri.fromFile(file) : null;
    }

    @Override
    public boolean isEmpty() {
        return fileStorage.isEmpty(IMAGES_SUB_DIRECTORY);
    }

    @Override
    public boolean fileExists(String filename) {
        return fileStorage.fileExists(filename, IMAGES_SUB_DIRECTORY);
    }

    @Override
    public void write(InputStream is, String filename) {
        Timber.d("Message=Copying image;Filename=%s;target=%s", filename, IMAGES_SUB_DIRECTORY);
        fileStorage.write(is, filename, IMAGES_SUB_DIRECTORY);
    }

    @Override
    public void write(File file, String filename) {

        try {
            write(new FileInputStream(file), filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createNewFile(String filename, String subDirectory) {

        if(imageReader.fileExists(filename, subDirectory)) {
            fileStorage.write(imageReader.read(filename, subDirectory), filename, IMAGES_SUB_DIRECTORY);
        }
    }
}
