/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */


package com.orange.ocara.domain.interactors;

import android.util.Pair;

import com.orange.ocara.domain.models.QuestionModel;
import com.orange.ocara.domain.models.RuleModel;
import com.orange.ocara.domain.models.RuleWithEquipmentModel;
import com.orange.ocara.domain.repositories.QuestionRepository;
import com.orange.ocara.domain.repositories.RuleRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class LoadRulesWithEquipmentNamesForRuleSet {
    RuleRepository ruleRepository;

    @Inject
    public LoadRulesWithEquipmentNamesForRuleSet( RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public Single<List<RuleWithEquipmentModel>>  execute(String rulesetRef, String rulesetVersion) {

        return ruleRepository.findRulesWithEquipments(rulesetRef , rulesetVersion);
    }
}
