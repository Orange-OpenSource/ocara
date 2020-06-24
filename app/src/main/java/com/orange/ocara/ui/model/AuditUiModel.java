/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.ui.model;

import com.orange.ocara.business.model.AuditModel;
import com.orange.ocara.data.cache.model.AuditEntity;

import lombok.Builder;
import lombok.Getter;

/**
 * Value Object. Basic implementation of {@link AuditModel}
 */
@Builder
@Getter
public class AuditUiModel implements AuditModel {

    /**
     * identifier
     */
    private final Long id;

    /**
     * label
     */
    private final String name;

    /**
     * type
     */
    private final AuditEntity.Level level;

    /**
     * a revision
     */
    private final int version;

    /**
     * who is the creator
     */
    private final String authorName;
}
