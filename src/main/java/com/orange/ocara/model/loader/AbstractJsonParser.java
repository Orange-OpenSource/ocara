/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.loader;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

public abstract class AbstractJsonParser {


    public void load(InputStream jsonStream) {
        try {
            doLoad(jsonStream);
        } catch (IOException e) {
            throw new JsonParserException(e);
        }
    }

    protected abstract void doLoad(InputStream jsonStream) throws IOException;


    protected <T> T readValue(InputStream jsonStream, Class<T> valueType) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        com.fasterxml.jackson.core.JsonParser jp = jsonFactory.createParser(jsonStream);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jp, valueType);
    }


    protected Collection extractCollection(Map content, String fieldName) {
        return extractCollection(content, fieldName, true);
    }

    protected Collection extractCollection(Map content, String fieldName, boolean required) {
        Object result = extractField(content, fieldName, required);
        if (!(result instanceof Collection)) {
            throw new JsonBadStructureException(fieldName);
        }
        return (Collection) result;
    }



    protected Map extractMap(Map content, String fieldName) {
        return extractMap(content, fieldName, true);
    }


    protected Map extractMap(Map content, String fieldName, boolean required) {
        Object result = extractField(content, fieldName, required);
        if (!(result instanceof Map)) {
            throw new JsonBadStructureException(fieldName);
        }
        return (Map) result;
    }



    protected String extractString(Map content, String fieldName) {
        return extractString(content, fieldName, true);
    }

    protected String extractString(Map content, String fieldName, boolean required) {
        Object result = extractField(content, fieldName, required);
        if (required && !(result instanceof String)) {
            throw new JsonBadStructureException(fieldName);
        }
        return (String) result;
    }

    protected int extractInt(Map content, String fieldName) {
        return extractInt(content, fieldName, true);
    }

    protected Integer extractInt(Map content, String fieldName, boolean required) {
        Object field = extractField(content, fieldName, required);
        try {
            int result = Integer.parseInt(field.toString());
            return result;
        } catch (NumberFormatException e) {
            if (required) {
                throw new JsonBadStructureException(fieldName);
            }
        }
        return null;
    }


    protected Object extractField(Map content, String fieldName, boolean required) {
        Object result = content.get(fieldName);
        if (result == null && required) {
            throw new JsonMissingFieldException(fieldName);
        }
        return result;
    }
}
