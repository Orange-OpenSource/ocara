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

import com.orange.ocara.domain.repositories.RulesetRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class DeleteRuleset {
    RulesetRepository repository;

    @Inject
    public DeleteRuleset(RulesetRepository repository) {
        this.repository = repository;
    }
    public Completable execute(int rulesetId){
        return repository.deleteRuleset(rulesetId);
    }
}
