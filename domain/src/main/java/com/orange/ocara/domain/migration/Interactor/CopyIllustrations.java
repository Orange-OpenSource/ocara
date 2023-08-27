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


package com.orange.ocara.domain.migration.Interactor;

import com.orange.ocara.domain.repositories.IllustrationsRepository;
import com.orange.ocara.data.cache.database.Tables.Illustration;
import com.orange.ocara.data.oldEntities.IllustrationEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CopyIllustrations extends Interactor<IllustrationEntity> {
    IllustrationsRepository repository;

    @Inject
    public CopyIllustrations(IllustrationsRepository repository) {
        this.repository = repository;
    }


    @Override
    public Completable execute() {
        List<IllustrationEntity> illustrationEntities = getAll(IllustrationEntity.class);
        List<Illustration> illustrations = new ArrayList<>();
        for (IllustrationEntity entity : illustrationEntities) {
            illustrations.add(Illustration.IllustrationBuilder.anIllustration()
                    .withComment(entity.comment)
                    .withDate(entity.date)
                    .withImage(entity.image)
                    .withReference(entity.reference)
                    .build());
        }
        return repository.insert(illustrations);
    }
}
