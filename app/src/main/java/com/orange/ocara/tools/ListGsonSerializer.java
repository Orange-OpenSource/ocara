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
package com.orange.ocara.tools;

import com.activeandroid.serializer.TypeSerializer;
import com.google.gson.Gson;

import java.util.List;

/**
 * Helper for serializing/deserializing lists.
 *
 * Is used in AndroidManifest.xml
 */
public class ListGsonSerializer extends TypeSerializer {

    private final static Gson gson = new Gson();

    @Override
    public Class<?> getDeserializedType() {
        return List.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public String serialize(Object data) {
        if (null == data)
            return null;

        return gson.toJson(data);
    }

    @Override
    public List<String> deserialize(Object data) {
        if (null == data)
            return null;

        List<String> stringList = gson.fromJson(data.toString(), List.class);
        return stringList;
    }

}
