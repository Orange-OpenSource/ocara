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

import java.io.InputStream;
import java.util.List;

/**
 * description of a reader for the images that are stored offline
 */
public interface ImageStorage {

    /**
     * @return the names of the files in the reader's path
     */
    List<String> list(String subdirectory);

    /**
     * @param filename a {@link String}
     * @param subdirectory name of the relative target subdirectory
     * @return an {@link InputStream} that matches the given argument
     */
    InputStream read(String filename, String subdirectory);

    /**
     * @param directory name of the relative target subdirectory
     * @return true if the reader has no files. false, otherwise
     */
    boolean isEmpty(String directory);

    /**
     *
     * @param filename a {@link String}
     * @param directory name of the relative target subdirectory
     * @return true if a file's name matches the input string. false, otherwise.
     */
    boolean fileExists(String filename, String directory);

    /**
     *
     * @param inputStream an {@link InputStream}
     * @param targetFilename the name of the file
     * @param targetSubDirectory a relative path
     */
    void write(InputStream inputStream, String targetFilename, String targetSubDirectory);
}
