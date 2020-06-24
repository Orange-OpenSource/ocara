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

package com.orange.ocara.ui.intent.export.docx;

public class DocxExporterException extends Exception {

    public DocxExporterException() {
        super();
    }

    public DocxExporterException(String detailMessage) {
        super(detailMessage);
    }

    public DocxExporterException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
