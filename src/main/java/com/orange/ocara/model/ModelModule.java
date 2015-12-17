/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import com.orange.ocara.model.export.AuditExportService_;

import dagger.Module;
import dagger.Provides;

@Module(
        complete = false,
        library = true,
        injects = {AuditExportService_.class}
)
public final class ModelModule {

    @Provides
    ModelManager providesModelManager(ModelManagerImpl modelManager) {
        return modelManager;
    }

    @Provides
    RuleSetLoader providesRuleSetLoader(RuleSetLoaderImpl ruleSetLoader) {
        return ruleSetLoader;
    }

    @Provides
    SitesLoader providesSitesLoader(SitesLoaderImpl sitesLoader) {
        return sitesLoader;
    }


}
