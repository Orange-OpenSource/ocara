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
import android.os.Environment;

import java.io.File;

public enum StorageType {

    /**
     * /data/data/com.my.package/data/files/
     */
    INTERNAL {
        @Override
        File rootDirectory(Context context) {
            return context.getFilesDir();
        }
    },

    /**
     * (Varies on manufacturer)
     * /mnt/sdcard/
     * /sdcard/
     * <p/>
     * etc...
     */
    EXTERNAL {
        @Override
        File rootDirectory(Context context) {
            File directory;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                directory = Environment.getExternalStorageDirectory();
            } else {
                directory = context.getFilesDir();
            }
            return directory;
        }
    },

    /**
     * /data/data/com.my.package/data/cache/
     */
    INTERNAL_CACHE {
        @Override
        File rootDirectory(Context context) {
            return context.getCacheDir();
        }
    },

    /**
     * (Varies on manufacturer)
     * /mnt/sdcard/Android/data/com.my.package/cache/
     * /sdcard/Android/data/com.my.package/cache/
     * <p/>
     * etc...
     */
    EXTERNAL_CACHE {
        @Override
        File rootDirectory(Context context) {
            File directory;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                directory = context.getExternalCacheDir();
            } else {
                directory = context.getFilesDir();
            }
            return directory;
        }
    };

    /**
     *
     * @param context a {@link Context}
     * @return a {@link File}
     */
    abstract File rootDirectory(Context context);

    /**
     *
     * @param context a {@link Context}
     * @param subDirectory a sub directory path
     * @return a {@link File}
     */
    File storageDirectory(Context context, String subDirectory) {
        return new File(rootDirectory(context), subDirectory);
    }
}
