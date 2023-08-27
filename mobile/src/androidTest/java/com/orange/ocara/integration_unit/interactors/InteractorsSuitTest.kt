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

package com.orange.ocara.integration_unit.interactors

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        GetAuditEquipmentWithCharacteristics::class,
        GetAuditIdFromAuditEquipmentIdTest::class,
        GetNumberOfSubObjectsInEquipmentTest::class,
        InsertRuleAnswersAndLoadAuditEquipmentQuestionsTest::class,
        LoadAuditLevelTest::class
)
class InteractorsSuitTest