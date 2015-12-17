/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.tools;

import android.content.res.AssetManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

public class AssetsHelper {

    /**
     * Copy the asset at the specified path to this app's data directory. If the
     * asset is a directory, its contents are also copied.
     */
    public static void copyAsset(AssetManager assetManager, String path, File targetFolder) throws IOException {
        copyAsset(assetManager, path, "", targetFolder);
    }


    /**
     * Copy the asset at the specified path to this app's data directory. If the
     * asset is a directory, its contents are also copied.
     */
    public static void copyAsset(AssetManager assetManager, String rootPath, String path, File targetFolder) throws IOException {

        String fullPath = StringUtils.isEmpty(path) ? rootPath : rootPath + File.separator + path;

        // If we have a directory, we make it and recurse. If a file, we copy its
        // contents.
        try {
            String[] contents = assetManager.list(fullPath);

            // The documentation suggests that list throws an IOException, but doesn't
            // say under what conditions. It'd be nice if it did so when the path was
            // to a file. That doesn't appear to be the case. If the returned array is
            // null or has 0 length, we assume the path is to a file. This means empty
            // directories will get turned into files.
            if (contents == null || contents.length == 0) {
                throw new IOException();
            }

            // Recurse on the contents.
            for (String entry : contents) {
                String newPath = StringUtils.isEmpty(path) ? entry : path + File.separator + entry;

                copyAsset(assetManager, rootPath, newPath, targetFolder);
            }
        } catch (IOException e) {

            File file = new File(targetFolder, path);

            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            FileUtils.copyInputStreamToFile(assetManager.open(fullPath), file);
        }
    }

}
