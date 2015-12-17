/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import com.orange.ocara.modelStatic.RuleSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", resourceDir = "../../build/intermediates/res/debug", emulateSdk = 18)
public class ModelManagerImplTest {
    RuleSetLoader ruleSetLoader;
    SitesLoader sitesLoader;

    ModelManagerImpl modelManager;

    @Before
    public void setup() throws SQLException {
        modelManager = Mockito.spy(new ModelManagerImpl(Robolectric.application));

        ruleSetLoader = mock(RuleSetLoader.class);
        modelManager.ruleSetLoader = ruleSetLoader;

        sitesLoader = mock(SitesLoader.class);
        modelManager.sitesLoader = sitesLoader;
    }

    @Test
    public void loads_all_ruleSets() {
        // Given
        RuleSet ruleSet1 = createRuleSet("ruleSet1", "Audit de Parcours", "1.0.0");
        RuleSet ruleSet2 = createRuleSet("ruleSet2", "Code du travail", "1.0.0");
        when(ruleSetLoader.getInstalledRuleSetIds()).thenReturn(new HashSet<String>(Arrays.asList(new String[]{"ruleSet1", "ruleSet2"})));
        when(ruleSetLoader.loadInstalledRuleSet(eq("ruleSet1"))).thenReturn(ruleSet1);
        when(ruleSetLoader.loadInstalledRuleSet(eq("ruleSet2"))).thenReturn(ruleSet2);

        // When
        modelManager.initialize();

        // Then
        assertThat(modelManager.getAllRuleSet()).hasSize(2);
        assertThat(modelManager.getRuleSet("ruleSet1")).isSameAs(ruleSet1);
        assertThat(modelManager.getRuleSet("ruleSet2")).isSameAs(ruleSet2);
    }


    private RuleSet createRuleSet(String id, String type, String version) {
        RuleSet ruleSet;
        ruleSet = new RuleSet();
        ruleSet.setId(id);
        ruleSet.setType(type);
        ruleSet.setVersion(version);
        return ruleSet;
    }
}
