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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/** Toolbox for common operations on collections */
public class CollectionUtils {

    private CollectionUtils() {
    }

    public static Set<String> intersection(Collection<String> first, Collection<String> second) {

        Set<String> set = new HashSet<>(first);
        set.retainAll(new HashSet<>(second));
        return set;
    }

    public static Set<String> disjunction(Collection<String> first, Collection<String> second) {

        Set<String> set = new HashSet<>(first);
        set.addAll(new HashSet<>(second));
        set.removeAll(intersection(first, second));
        return set;
    }
}
