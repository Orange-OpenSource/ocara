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

package com.orange.ocara.utils.tools;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RefreshStrategy {

    DependencyDepth depth;
    boolean commentsNeeded;

    public enum DependencyDepth {

        AUDIT,
        AUDIT_OBJECT,
        QUESTION_ANSWER,
        RULE_ANSWER
    }

    public static RefreshStrategy auditObjectRefreshStrategy() {
        return RefreshStrategy
                .builder()
                .depth(RefreshStrategy.DependencyDepth.AUDIT_OBJECT)
                .commentsNeeded(true)
                .build();
    }

    public static RefreshStrategy ruleAnswerRefreshStrategy() {
        return RefreshStrategy
                .builder()
                .depth(RefreshStrategy.DependencyDepth.RULE_ANSWER)
                .commentsNeeded(true)
                .build();
    }

    public static RefreshStrategy questionAnswerRefreshStrategy() {
        return RefreshStrategy
                .builder()
                .depth(RefreshStrategy.DependencyDepth.QUESTION_ANSWER)
                .commentsNeeded(true)
                .build();
    }

}
