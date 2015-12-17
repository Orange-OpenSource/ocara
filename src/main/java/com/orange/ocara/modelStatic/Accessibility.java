/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.modelStatic;

public enum Accessibility {
    NO_IMPACT,
    ANNOYING,
    BLOCKING;

    public static Accessibility max(Accessibility oneValue, Accessibility anotherValue) {
        if (oneValue.compareTo(anotherValue) < 0) {
            return anotherValue;
        }
        return oneValue;
    }
}
