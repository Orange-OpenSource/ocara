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


package com.orange.ocara.domain.migration;

import com.orange.ocara.domain.migration.Interactor.CopyAuditEquipmentAndSubEquipment;
import com.orange.ocara.domain.migration.Interactor.CopyAudits;
import com.orange.ocara.domain.migration.Interactor.CopyCategoryEquipmentRelation;
import com.orange.ocara.domain.migration.Interactor.CopyComments;
import com.orange.ocara.domain.migration.Interactor.CopyEquipmentAndQuestionRelation;
import com.orange.ocara.domain.migration.Interactor.CopyEquipmentSubEquipmentRelation;
import com.orange.ocara.domain.migration.Interactor.CopyEquipments;
import com.orange.ocara.domain.migration.Interactor.CopyIllustrations;
import com.orange.ocara.domain.migration.Interactor.CopyIllustrationsRulesRelation;
import com.orange.ocara.domain.migration.Interactor.CopyImpactValue;
import com.orange.ocara.domain.migration.Interactor.CopyProfileType;
import com.orange.ocara.domain.migration.Interactor.CopyQuestions;
import com.orange.ocara.domain.migration.Interactor.CopyQuestionsAndRulesRelation;
import com.orange.ocara.domain.migration.Interactor.CopyRuleAnswers;
import com.orange.ocara.domain.migration.Interactor.CopyRuleProfileImpactRelation;
import com.orange.ocara.domain.migration.Interactor.CopyRules;
import com.orange.ocara.domain.migration.Interactor.CopyRulesetsAndCategories;
import com.orange.ocara.domain.migration.Interactor.CopySites;
import com.orange.ocara.domain.migration.Interactor.Interactor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import timber.log.Timber;

public class RunMigration {
    List<Interactor<?>> interactors = new ArrayList<>();

    @Inject
    public RunMigration(CopyComments copyComments, CopyIllustrationsRulesRelation copyIllustrationsRulesRelation, CopyEquipmentAndQuestionRelation copyEquipmentAndQuestionRelation, CopyRuleAnswers copyRuleAnswers, CopyAuditEquipmentAndSubEquipment copyAuditEquipmentAndSubEquipment, CopyEquipmentSubEquipmentRelation copyEquipmentSubEquipmentRelation, CopyRuleProfileImpactRelation copyRuleProfileImpactRelation, CopyQuestionsAndRulesRelation copyQuestionsAndRulesRelation, CopyIllustrations copyIllustrations, CopyProfileType copyProfileType, CopyImpactValue copyImpactValue, CopyRules copyRules, CopyCategoryEquipmentRelation copyCategoryEquipmentRelation, CopyQuestions copyQuestions, CopyEquipments copyEquipments, CopyRulesetsAndCategories copyRulesetsAndCategories, CopyAudits copyAudits, CopySites copySites) {
        interactors.add(copyAudits);
        interactors.add(copyComments);
        interactors.add(copyRuleProfileImpactRelation);
        interactors.add(copyIllustrationsRulesRelation);
        interactors.add(copyRuleAnswers);
        interactors.add(copyEquipmentAndQuestionRelation);
        interactors.add(copyAuditEquipmentAndSubEquipment);
        interactors.add(copyEquipmentSubEquipmentRelation);
        interactors.add(copyQuestionsAndRulesRelation);
        interactors.add(copyIllustrations);
        interactors.add(copyProfileType);
        interactors.add(copyRules);
        interactors.add(copyImpactValue);
        interactors.add(copyCategoryEquipmentRelation);
        interactors.add(copyQuestions);
        interactors.add(copyEquipments);
        interactors.add(copySites);
        interactors.add(copyRulesetsAndCategories);
    }

    public Completable execute() {
        Timber.d("migration start");
        Completable completable = interactors.get(0).execute();
        for (int i = 1; i < interactors.size(); i++) {
            completable = completable.concatWith(interactors.get(i).execute());
        }
        return completable;
    }
}
