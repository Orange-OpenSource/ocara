/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RefreshStrategy {

    public enum DependencyDepth {

        AUDIT,
        AUDIT_OBJECT,
        QUESTION_ANSWER,
        RULE_ANSWER;
    }

    DependencyDepth depth;
    boolean commentsNeeded;
}
