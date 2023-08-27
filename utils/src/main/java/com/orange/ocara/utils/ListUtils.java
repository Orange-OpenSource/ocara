/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility methods in order to instantiate {@link java.util.List}s
 *
 * @see <https://github.com/google/guava/blob/master/guava/src/com/google/common/collect/Lists.java>
 */
public class ListUtils {

    private ListUtils() {}

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    public static <T> ArrayList<T> newArrayList(Iterable<? extends T> elements) {
        if (elements == null) {
            return null;
        }
        ArrayList<T> list = newArrayList();
        for (T e : elements) {
            list.add(e);
        }
        return list;
    }

    public static <T> ArrayList<T> newArrayList(T... elements) {
        if (elements == null) {
            return null;
        }
        ArrayList<T> list = newArrayList();
        Collections.addAll(list, elements);
        return list;
    }

    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }
}
