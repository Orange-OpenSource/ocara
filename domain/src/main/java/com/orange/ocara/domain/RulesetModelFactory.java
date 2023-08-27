//
// Software Name: OCARA
//
// SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
// SPDX-License-Identifier: MPL v2.0
//
// This software is distributed under the Mozilla Public License v. 2.0,
// the text of which is available at http://mozilla.org/MPL/2.0/ or
// see the "license.txt" file for more details.


package com.orange.ocara.domain;

import com.orange.ocara.domain.models.RulesetModel;
import com.orange.ocara.data.network.models.RulesetLightWs;

/**
 * default implementation of some interface
 */
public class RulesetModelFactory {

    public static RulesetModel makeRulesetModel(RulesetLightWs in) {

        return RulesetModel
                .builder()
                .author(in.getAuthor())
                .comment(in.getComment())
                .date(in.getDate())
                .reference(in.getReference())
                .ruleCategoryName(in.getRuleCategoryName())
                .stat(in.getStat())
                .type(in.getType())
                .version(in.getVersion())
                .id(null)
                .build();
    }
}
