/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import android.content.Context;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(overrides = true, complete = false, library = true)
public class ModelModuleMock {

    @Provides
    @Singleton
    ModelManager providesModelManager(Context context) {
        return Mockito.mock(ModelManager.class);
    }
}
