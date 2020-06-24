package com.orange.ocara.data.source;

import com.orange.ocara.business.model.RulesetModel;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class RulesetDataStoreImplSaveDefaultRulesetTest {

    @Test
    public void shouldDelegateOperationToCache() {

        // given
        RulesetSource.RulesetRemote rulesetRemote = mock(RulesetSource.RulesetRemote.class);

        RulesetSource.RulesetCache rulesetCache = mock(RulesetSource.RulesetCache.class);

        ImageSource.ImageRemote imageRemote = mock(ImageSource.ImageRemote.class);

        ImageSource.ImageCache imageCache = mock(ImageSource.ImageCache.class);

        RulesetDataStoreImpl subject = new RulesetDataStoreImpl(rulesetCache, rulesetRemote, imageCache, imageRemote);


        RulesetModel input = mock(RulesetModel.class);

        // when
        subject.saveDefaultRuleset(input);

        // then
        verify(rulesetCache).saveDefaultRuleset(input);
        verifyNoMoreInteractions(rulesetCache, rulesetRemote);
    }
}