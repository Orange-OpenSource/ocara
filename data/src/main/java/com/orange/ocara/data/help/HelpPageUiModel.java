/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL-2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.data.help;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * a model that brings the index of a page related to a view
 *
 */
public class HelpPageUiModel {

    private final Map<String, Integer> summary = summary();

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
            Timber.i("Message=Found key;Key=%s;ReturnedValue=%d", key, defaultValue);
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
        content.put("com.orange.ocara.ui.activity.BrowseRulesetsActivity_", 12);
        content.put("com.orange.ocara.ui.activity.UpdateAuditActivity_", 13);
        content.put("com.orange.ocara.ui.activity.ListAuditActivity_", 14);
        content.put("com.orange.ocara.ui.activity.SetupAuditPathActivity_", 18);
        content.put("com.orange.ocara.ui.activity.AuditObjectsActivity_", 18);
        content.put("com.orange.ocara.ui.activity.DetailsActivity_", 22);
        content.put("com.orange.ocara.ui.activity.ShowEquipmentInfoActivity_", 22);
        content.put("com.orange.ocara.ui.activity.AuditObjectsExpertModeActivity_", 24);
        content.put("com.orange.ocara.ui.activity.BrowseIllustrationsActivity_", 27);
        content.put("com.orange.ocara.ui.activity.BrowseExplanationsActivity_", 27);
        content.put("com.orange.ocara.ui.activity.ListAuditCommentActivity_", 29);
        content.put("com.orange.ocara.ui.activity.ListAuditObjectCommentActivity_", 29);
        content.put("com.orange.ocara.ui.activity.EditCommentActivity_", 29);
        content.put("com.orange.ocara.ui.activity.EditCommentTextActivity_", 32);
        content.put("com.orange.ocara.ui.activity.EditCommentAudioActivity_", 33);
        content.put("com.orange.ocara.ui.activity.EditCommentPhotoActivity_", 35);
        content.put("com.orange.ocara.ui.activity.AuditObjectsNoviceModeActivity_", 35);
        content.put("com.orange.ocara.ui.activity.ResultAuditActivity_", 39);
        content.put("com.orange.ocara.ui.activity.SettingsActivity_", 47);
        content.put("com.orange.ocara.ui.activity.HelpDisplayFragment_", 49);


        content.put("com.orange.ocara.ui.fragment.CreateAuditFragment", 9);
        content.put("com.orange.ocara.ui.fragment.CreateSiteFragment_", 11);
        content.put("com.orange.ocara.ui.fragment.BrowseRulesetsFragment", 12);
        content.put("com.orange.ocara.ui.fragment.EditAuditFragment_", 13);
        content.put("com.orange.ocara.ui.fragment.ListAuditFragment_", 14);
        content.put("com.orange.ocara.ui.fragment.SetupAuditPathFragment_", 18);
        content.put("com.orange.ocara.ui.fragment.AuditObjectsFragment_", 18);
        content.put("com.orange.ocara.ui.fragment.AuditObjectDetailsFragment_", 22);
        content.put("com.orange.ocara.ui.fragment.ShowEquipmentInfoFragment_", 22);
        content.put("com.orange.ocara.ui.fragment.AuditObjectsExpertFragment_", 24);
        content.put("com.orange.ocara.ui.fragment.BrowseExplanationsFragment_", 27);
        content.put("com.orange.ocara.ui.fragment.BrowseIllustrationsFragment_", 27);
        content.put("com.orange.ocara.ui.fragment.EditCommentFragment_", 29);
        content.put("com.orange.ocara.ui.fragment.ListAuditCommentFragment_", 29);
        content.put("com.orange.ocara.ui.fragment.ListAuditObjectCommentFragment_", 29);
        content.put("com.orange.ocara.ui.fragment.EditCommentTextFragment_", 32);
        content.put("com.orange.ocara.ui.fragment.EditCommentAudioFragment_", 33);
        content.put("com.orange.ocara.ui.fragment.EditCommentPhotoFragment_", 35);
        content.put("com.orange.ocara.ui.fragment.AuditObjectsNoviceFragment_", 35);
//        content.put("com.orange.ocara.ui.fragment._", 39);
        content.put("com.orange.ocara.ui.fragment.SettingsFragment_", 47);
        content.put("com.orange.ocara.ui.fragment.HelpFragment_", 49);


        content.put("com.orange.ocara.mobile.ui.fragment.CreateAuditFragment", 9);
        content.put("com.orange.ocara.mobile.ui.fragment.AddSiteFragment", 11);
        content.put("com.orange.ocara.mobile.ui.fragment.ViewReferentialFragment", 12);
        content.put("com.orange.ocara.mobile.ui.fragment.RulesetEquipmentsQuestionsFragment", 12);

        content.put("com.orange.ocara.mobile.ui.fragment.EditAuditFragment", 13);
        content.put("com.orange.ocara.mobile.ui.fragment.ListAuditsFragment", 14);
        content.put("com.orange.ocara.mobile.ui.fragment.CurrentRouteFragment", 18);
        content.put("com.orange.ocara.mobile.ui.fragment.AddEquipmentFragment", 18);
        content.put("com.orange.ocara.mobile.ui.fragment.AuditEquipmentDetailsFragment", 22);
        content.put("com.orange.ocara.mobile.ui.fragment.AuditEquipmentWithCharacteristicsFragment", 22);
        content.put("com.orange.ocara.mobile.ui.fragment.QuestionsFragment.E", 24);
        content.put("com.orange.ocara.mobile.ui.fragment.QuestionsFragment.N", 25);
        content.put("com.orange.ocara.mobile.ui.fragment.IllustrationsFragment", 28);
        content.put("com.orange.ocara.mobile.ui.fragment.ListCommentsFragment", 29);
        content.put("com.orange.ocara.mobile.ui.fragment.TextCommentFragment", 30);
        content.put("com.orange.ocara.mobile.ui.fragment.AudioCommentFragment", 30);
        content.put("com.orange.ocara.mobile.ui.fragment.PhotoCommentFragment", 30);
        content.put("com.orange.ocara.mobile.ui.fragment.TextCommentFragment", 32);
        content.put("com.orange.ocara.mobile.ui.fragment.AudioCommentFragment", 33);
        content.put("com.orange.ocara.mobile.ui.fragment.PhotoCommentFragment", 35);
        content.put("com.orange.ocara.mobile.ui.fragment.PreferencesFragment", 47);
        content.put("com.orange.ocara.mobile.ui.fragment.ReportFragment", 39);
        content.put("com.orange.ocara.mobile.ui.fragment.ReportViewRouteFragment", 39);

        content.put("com.orange.ocara.mobile.ui.fragment.ReportSummaryFragment", 39);
        content.put("com.orange.ocara.mobile.ui.fragment.ReportChartFragment", 39);

        //todo

        content.put("com.orange.ocara.mobile.ui.fragment.HelpFragment_", 49);

        return content;

    }
}
