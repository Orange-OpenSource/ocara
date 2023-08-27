package com.orange.ocara.domain.docexport.models;

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


import java.io.File;

public interface DocxExporter {

    /**
     * To compile the document to export.
     *
     * @param workingDirectory Working directory
     */
    void compile(File workingDirectory) throws DocxExporterException;
}
