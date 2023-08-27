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

import com.orange.ocara.domain.models.AuditModel;
import com.orange.ocara.domain.repositories.AuditRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DeepCopyAudit {
    AuditRepository repository;
    @Inject
    public DeepCopyAudit(AuditRepository repository){
        this.repository=repository;
    }
    public Completable execute(int id , boolean copyAnswers){
        // wrapping the observer<List<long>> to completable
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                // we need to reload the auditmodel with its ruleset
                repository.getAuditWithSiteAndAuditorAndRulesetById((long)id)
                        .toObservable()
                        .concatMap(new Function<AuditModel, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(AuditModel auditWithRuleset) throws Exception {
                                return repository.copyAudit(auditWithRuleset.updateAuditVersion() , copyAnswers);
                            }
                        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Object>() {
                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        emitter.onComplete();
                    }
                });
            }
        });
    }


}
