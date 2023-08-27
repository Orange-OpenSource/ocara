/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL-2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.data.help;

/**
 * a model that renders the location of the help file, in case it is stored in the assets
 *
 */
public class HelpAssetPathUiModel {

    private static final String HELP_FILE_ASSET_PATH = "Guide_de_formation_V18.0.0.pdf";

    /**
     *
     * @return the location of the help file stored in the assets of the app
     */
    public String get() {

        return HELP_FILE_ASSET_PATH;
    }
}
