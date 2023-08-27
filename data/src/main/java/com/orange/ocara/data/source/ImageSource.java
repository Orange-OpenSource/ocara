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

import android.net.Uri;

import java.io.File;
import java.io.InputStream;

/**
 * contract between a data store that handles and the repositories that handle them locally and remotely
 */
public interface ImageSource {

    /**
     * description of the repository that deals with local database
     */
    interface ImageCache {

        /**
         * @param filename a {@link String}
         * @return an {@link File} that matches the given argument, or null, if it does not exist
         */
        File get(String filename);

        /**
         * @param filename a {@link String}
         * @return
         */
        Uri getAsUri(String filename);

        /**
         * @return true if there are no stored images
         */
        boolean isEmpty();

        /**
         * @param filename a {@link String}
         * @return true if a file's name matches the input string. false, otherwise.
         */
        boolean fileExists(String filename);

        /**
         * @param is       an {@link InputStream}
         * @param filename the name of the target file
         */
        void write(InputStream is, String filename);

        /**
         * @param file     an {@link File}
         * @param filename the name of the target file
         */
        void write(File file, String filename);

        /**
         * @param filename the name of the file
         */
        void createNewFile(String filename, String subDirectory);
    }

    /**
     * description of the repository managed through webservices
     */
    interface ImageRemote {

        InputStream get(String filename);
    }

    /**
     * description of a repository that manages image files between both local and remote repositories
     */
    interface ImageDataStore {

        /**
         * check if a illustration exists
         *
         * @param filename an identifier for illustrations
         * @return true if the repository contains at least one illustration that matches the argument
         */
        boolean exists(String filename);

        /**
         * retrieves a file
         *
         * @param filename the name of the file
         * @return an {@link InputStream} if exits. Otherwise, null.
         */
        File get(String filename);
    }
}
