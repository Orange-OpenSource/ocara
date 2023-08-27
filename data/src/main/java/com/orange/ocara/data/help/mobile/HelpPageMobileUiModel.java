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

package com.orange.ocara.data.help.mobile;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * a model that brings the index of a page related to a view
 *
 */
public class HelpPageMobileUiModel {

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


        content.put("com.orange.ocara.mobile.ui.fragment.CreateAuditFragment", 8);

        content.put("com.orange.ocara.mobile.ui.fragment.AddSiteFragment", 11);

        content.put("com.orange.ocara.mobile.ui.fragment.ViewReferentialFragment", 12);
        content.put("com.orange.ocara.mobile.ui.fragment.RulesetEquipmentsQuestionsFragment", 12);

        content.put("com.orange.ocara.mobile.ui.fragment.EditAuditFragment", 14);

        content.put("com.orange.ocara.mobile.ui.fragment.ListAuditsFragment", 15);

        content.put("com.orange.ocara.mobile.ui.fragment.CurrentRouteFragment", 20);
        content.put("com.orange.ocara.mobile.ui.fragment.AddEquipmentFragment", 20);

        content.put("com.orange.ocara.mobile.ui.fragment.AuditEquipmentDetailsFragment", 22);
        content.put("com.orange.ocara.mobile.ui.fragment.AuditEquipmentWithCharacteristicsFragment", 21);

        content.put("com.orange.ocara.mobile.ui.fragment.QuestionsFragment.E", 26);

        content.put("com.orange.ocara.mobile.ui.fragment.QuestionsFragment.N", 28);

        content.put("com.orange.ocara.mobile.ui.fragment.IllustrationsFragment", 31);

        content.put("com.orange.ocara.mobile.ui.fragment.ListCommentsFragment", 34);

        content.put("com.orange.ocara.mobile.ui.fragment.TextCommentFragment", 38);

        content.put("com.orange.ocara.mobile.ui.fragment.AudioCommentFragment", 40);
        content.put("com.orange.ocara.mobile.ui.fragment.RecordCommentFragment", 40);

        content.put("com.orange.ocara.mobile.ui.fragment.PhotoCommentFragment", 42);

        //todo
        content.put("com.orange.ocara.mobile.ui.fragment.PreferencesFragment", 47);


        content.put("com.orange.ocara.mobile.ui.fragment.ReportFragment", 46);
        content.put("com.orange.ocara.mobile.ui.fragment.ReportViewRouteFragment", 49);
        content.put("com.orange.ocara.mobile.ui.fragment.ReportChartFragment", 48);

        //todo

        content.put("com.orange.ocara.mobile.ui.fragment.HelpFragment", 56);

        return content;

    }
}
