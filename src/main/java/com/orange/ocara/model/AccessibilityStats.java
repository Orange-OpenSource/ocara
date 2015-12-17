/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import com.orange.ocara.modelStatic.Accessibility;
import com.orange.ocara.modelStatic.Response;

public class AccessibilityStats {

    public enum Type {
        ACCESSIBLE {
            @Override
            boolean isValid(Response response, Accessibility accessibility) {
                return (Response.OK.equals(response) && !Accessibility.NO_IMPACT.equals(accessibility));
            }
        },
        ANNOYING {
            @Override
            boolean isValid(Response response, Accessibility accessibility) {
                return (Response.NOK.equals(response) && Accessibility.ANNOYING.equals(accessibility));
            }
        },
        BLOCKING {
            @Override
            boolean isValid(Response response, Accessibility accessibility) {
                return (Response.NOK.equals(response) && Accessibility.BLOCKING.equals(accessibility));
            }

        },
        DOUBT {
            @Override
            boolean isValid(Response response, Accessibility accessibility) {
                return (Response.DOUBT.equals(response) && !Accessibility.NO_IMPACT.equals(accessibility));
            }
        },
        NOANSWER {
            @Override
            boolean isValid(Response response, Accessibility accessibility) {
                return (Response.NoAnswer.equals(response));
            }
        };

        abstract boolean isValid(Response response, Accessibility accessibility);
    }

    private final int[] counters = new int[Type.values().length];

    public void compute(Response response, Accessibility accessibility) {

        for (int i = 0; i < counters.length; i++) {
            Type type = Type.values()[i];
            if (type.isValid(response, accessibility)) {
                counters[i]++;
            }
        }
    }

    public void clear() {
        for (int i = 0; i < counters.length; i++) {
            counters[i] = 0;
        }
    }

    public void plus(AccessibilityStats stats) {
        for (int i = 0; i < counters.length; i++) {
            counters[i] += stats.counters[i];
        }
    }

    public int getCounter(Type type) {
        return counters[type.ordinal()];
    }

}
