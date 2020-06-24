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

package com.orange.ocara.data;

import com.orange.ocara.business.model.ExplanationModel;
import com.orange.ocara.business.repository.ExplanationRepository;
import com.orange.ocara.data.net.model.Explanation;
import com.orange.ocara.data.source.IllustrationSource;
import com.orange.ocara.data.source.ImageSource;
import com.orange.ocara.tools.ListUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

/** default implementation of {@link ExplanationRepository} */
public class ExplanationDataRepository implements ExplanationRepository {

    private final IllustrationSource.IllustrationDataStore dataStore;

    private final ImageSource.ImageDataStore fileStore;

    ExplanationDataRepository(IllustrationSource.IllustrationDataStore dataStore, ImageSource.ImageDataStore fileStore) {
        this.dataStore = dataStore;
        this.fileStore = fileStore;
    }

    @Override
    public List<ExplanationModel> findAllByRuleId(Long ruleId) {
        List<Explanation> list = dataStore.findAllByRuleId(ruleId);

        ExplanationModelListBuilder builder = new ExplanationModelListBuilder();
        for (Explanation item : list) {
            if (item.hasIcon() && fileStore.exists(item.getIcon())) {
                builder.addExplanation(item, fileStore.get(item.getIcon()));
            } else {
                builder.addExplanation(item);
            }
        }

        return builder.build();
    }

    /** a builder for immutable {@link List}s of {@link ExplanationModel}s */
    private static final class ExplanationModelListBuilder {

        private List<ExplanationModel> data = ListUtils.newArrayList();

        ExplanationModelListBuilder addExplanation(Explanation e, File f) {
            data.add(new ExplanationModel(e, f));
            return this;
        }

        ExplanationModelListBuilder addExplanation(Explanation e) {
            data.add(new ExplanationModel(e, null));
            return this;
        }

        public List<ExplanationModel> build() {
            return data.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(data);
        }
    }
}
