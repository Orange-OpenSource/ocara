/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.modelStatic;

import java.util.LinkedHashMap;
import java.util.Map;

public class HandicapAccessibilities {

    protected Map<String, Accessibility> values = new LinkedHashMap<String, Accessibility>();



    public Accessibility getHandicapAccessibility(String idHandicap) {
        Accessibility result = values.get(idHandicap);
        if (result == null) {
            result = Accessibility.NO_IMPACT;
        }
        return result;
    }

    public void setHandicapAccessibility(String idHandicap, Accessibility value) {
        values.put(idHandicap, value);
    }

    public boolean hasAccessibility(Accessibility accessibility) {

        for(String handicapId : values.keySet()) {
            Accessibility handicapAccessibility = values.get(handicapId);
            if (handicapAccessibility.equals(accessibility)) {
                return true;
            }
        }
        return false;
    }


}
