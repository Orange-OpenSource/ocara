/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.modelStatic;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Rule {
    private String id;
    private String description;
    private HandicapAccessibilities handicapAccessibilities;
    private List<Metadata> metadata = new ArrayList<Metadata>();
    private List<Illustration> illustrations = new ArrayList<>();


    public boolean hasAccessibility(Accessibility accessibility) {
        return handicapAccessibilities.hasAccessibility(accessibility);
    }
}
