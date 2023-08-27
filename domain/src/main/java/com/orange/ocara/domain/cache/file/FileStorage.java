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

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * description of a repository dedicated to load an image
 */
public interface FileStorage {

    /**
     * @param subDirectory a relative path
     * @return the names of the files in the given path
     */
    List<String> list(String subDirectory);

    /**
     * @param filename a {@link String}
     * @param subDirectory a relative path
     * @return an {@link InputStream} that matches the given argument
     */
    File read(String filename, String subDirectory);

    /**
     * @return true if the storage has no files. false, otherwise
     * @param subDirectory
     */
    boolean isEmpty(String subDirectory);

    /**
     *
     * @param filename the name of the file
     * @param subDirectory a relative path
     * @return true if the file exists in given path. False, otherwise.
     */
    boolean fileExists(String filename, String subDirectory);

    /**
     *
     * @param inputStream an {@link InputStream}
     * @param targetFilename the name of the file
     * @param targetSubDirectory a relative path
     */
    void write(InputStream inputStream, String targetFilename, String targetSubDirectory);
}
