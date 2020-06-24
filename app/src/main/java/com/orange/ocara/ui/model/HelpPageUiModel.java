/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL-2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.ui.model;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * a model that brings the index of a page related to a view
 *
 */
public class HelpPageUiModel {

    private Map<String, Integer> summary = summary();

    /**
     *
     * @param key the name of a view
     * @param defaultValue a default value in case no value matches the given key
     * @return the number of the page that matches the given input {@link String}
     */
    public int getOrDefault(String key, int defaultValue) {
        int index;
        if (summary.containsKey(key)) {
            index = summary.get(key);
            Timber.i("Message=Found key;Key=%s;ReturnedValue=%d", key, index);
        } else {
            index = defaultValue;
            Timber.i("Message=Unknown key, default value is returned;Key=%s;DefaultValue=%d", key, defaultValue);
        }
        return index;
    }

    /**
     * factory
     *
     * @return a {@link Map} between an activity and the related page in the help file
     */
    private static Map<String, Integer> summary() {
        Map<String, Integer> content = new HashMap<>();

        content.put("com.orange.ocara.ui.activity.CreateAuditActivity_", 9);
        content.put("com.orange.ocara.ui.activity.CreateSiteActivity_", 11);
        content.put("com.orange.ocara.ui.activity.BrowseRulesetsActivity_", 14);
        content.put("com.orange.ocara.ui.activity.UpdateAuditActivity_", 16);
        content.put("com.orange.ocara.ui.activity.ListAuditActivity_", 17);
        content.put("com.orange.ocara.ui.activity.SetupAuditPathActivity_", 21);
        content.put("com.orange.ocara.ui.activity.AuditObjectsActivity_", 28);
        content.put("com.orange.ocara.ui.activity.DetailsActivity_", 25);
        content.put("com.orange.ocara.ui.activity.ShowEquipmentInfoActivity_", 25);
        content.put("com.orange.ocara.ui.activity.AuditObjectsExpertModeActivity_", 27);
        content.put("com.orange.ocara.ui.activity.BrowseIllustrationsActivity_", 30);
        content.put("com.orange.ocara.ui.activity.BrowseExplanationsActivity_", 30);
        content.put("com.orange.ocara.ui.activity.ListAuditCommentActivity_", 29);
        content.put("com.orange.ocara.ui.activity.ListAuditObjectCommentActivity_", 29);
        content.put("com.orange.ocara.ui.activity.EditCommentActivity_", 32);
        content.put("com.orange.ocara.ui.activity.EditCommentTextActivity_", 35);
        content.put("com.orange.ocara.ui.activity.EditCommentAudioActivity_", 36);
        content.put("com.orange.ocara.ui.activity.EditCommentFileActivity_", 40);
        content.put("com.orange.ocara.ui.activity.EditCommentPhotoActivity_", 38);
        content.put("com.orange.ocara.ui.activity.AuditObjectsNoviceModeActivity_", 28);
        content.put("com.orange.ocara.ui.activity.ResultAuditActivity_", 42);
        content.put("com.orange.ocara.ui.activity.RulesetInfoActivity_", 12);
        content.put("com.orange.ocara.ui.activity.SettingsActivity_", 50);
        content.put("com.orange.ocara.ui.activity.HelpDisplayFragment_", 52);
        content.put("com.orange.ocara.ui.activity.AboutActivity_", 52);

        return content;

    }
}
