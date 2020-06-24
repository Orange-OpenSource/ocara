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

package com.orange.ocara.data.cache.model;

import com.orange.ocara.data.net.model.QuestionEntity;

/** a response line related to a {@link QuestionEntity} */
public interface RulesGroupResponse extends WithResponse {

    QuestionEntity getQuestion();
}
