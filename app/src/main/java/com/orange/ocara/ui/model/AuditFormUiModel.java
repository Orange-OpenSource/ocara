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
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.SiteModel;
import com.orange.ocara.business.model.UserModel;

/**
 * description of a form dedicated to audits
 */
public interface AuditFormUiModel {

    AuditModel getInitialAudit();

    AuditModel getActualAudit();

    SiteModel getActualSite();

    RulesetModel getActualRuleset();

    UserModel getActualAuthor();
}
