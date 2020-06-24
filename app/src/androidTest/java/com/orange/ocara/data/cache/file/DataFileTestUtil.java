/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.data.cache.file;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.orange.ocara.BuildConfig;

import java.util.Arrays;
import java.util.Collection;

/** Toolbox for Data/Cache/File tests */
class DataFileTestUtil {

    private DataFileTestUtil() {
    }

    /**
     *
     * @return an array of elements with 3 fields
     * - a subpath of assets/
     * - a count of items in the path
     * - the name of a file sample in the path
     */
    static Collection<Object[]> assets() {

        if (BuildConfig.FLAVOR_mode == "opensource") {
            return Arrays.asList(new Object[][]{
                    {"ruleset", 1, "ruleset_demo.json"},
                    {"sites", 1, "sites_open-source.json"},
                    {"images", 185, "0cdcb0bb-6bb5-468b-b50b-aa3cf0145a56.jpg"}
            });
        } else if (BuildConfig.FLAVOR_mode == "orangewithoutsmtk") {
            return Arrays.asList(new Object[][]{
                    {"ruleset", 1, "ruleset_demo.json"},
                    {"sites", 1, "sites_orange.json"},
                    {"images", 185, "0cdcb0bb-6bb5-468b-b50b-aa3cf0145a56.jpg"}
            });
        } else {
            throw new RuntimeException("Unexpected BuildConfig.FLAVOR_mode : " + BuildConfig.FLAVOR_mode);
        }
    }

    static Context instrumentationContext() {
        return InstrumentationRegistry.getInstrumentation().getContext();
    }

    static Collection<Object[]> assetImages() {

        return Arrays.asList(new Object[][]{
                {"ruleset", 55, "0cdcb0bb-6bb5-468b-b50b-aa3cf0145a56.jpg"},
                {"objects", 17, "3bd4d8e5-f6c1-43a6-999f-f4aff7d11a29.jpg"},
                {"profile-types", 9, "2ee93348-01e1-48e5-a316-b3860bb4c4d0.jpg"}
        });
    }
}
