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
package com.orange.ocara.data.source;



import java.io.File;

import static com.orange.ocara.data.source.ImageSource.ImageCache;
import static com.orange.ocara.data.source.ImageSource.ImageDataStore;
import static com.orange.ocara.data.source.ImageSource.ImageRemote;

/**
 * default implementation of {@link ImageDataStore}
 */
public class ImageDataStoreImpl implements ImageDataStore {

    private final ImageCache imageCache;

    private final ImageRemote imageRemote;

    /**
     * instantiates
     *
     * @param imageCache  a local repository for {@link IllustrationEntity}s
     * @param imageRemote a remote repository for {@link IllustrationEntity}s
     */
    public ImageDataStoreImpl(ImageCache imageCache, ImageRemote imageRemote) {
        this.imageCache = imageCache;
        this.imageRemote = imageRemote;
    }

    @Override
    public boolean exists(String filename) {
        return imageCache.fileExists(filename) || imageRemote.get(filename) != null;
    }

    @Override
    public File get(String filename) {
        if (!imageCache.fileExists(filename)) {
            imageCache.write(imageRemote.get(filename), filename);
        }
        return imageCache.get(filename);
    }
}
