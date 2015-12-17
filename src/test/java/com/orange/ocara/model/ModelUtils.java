/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import com.activeandroid.Model;

import java.lang.reflect.Field;

public class ModelUtils {

    public static void setId(Model model, long id) throws NoSuchFieldException, IllegalAccessException {
        Field f = Model.class.getDeclaredField("mId");
        f.setAccessible(true);
        f.set(model, Long.valueOf(id)); // IllegalAccessException
    }

}
