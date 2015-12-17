/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.loader;

public class JsonBadStructureException extends JsonParserException {
    public JsonBadStructureException(String fieldName) {
        super("Bad file format, unexpected structure for field : " + fieldName);
    }
}
