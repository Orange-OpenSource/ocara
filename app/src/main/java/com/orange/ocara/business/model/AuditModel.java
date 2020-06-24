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

package com.orange.ocara.business.model;

import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.net.model.Identifiable;

/**
 * description of an audit
 */
public interface AuditModel extends Identifiable {

    String getName();

    AuditEntity.Level getLevel();

    int getVersion();

    String getAuthorName();
}