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

package com.orange.ocara.data.cache.db;

import android.content.Context;

import com.orange.ocara.R;
import com.orange.ocara.business.model.OnboardingItemModel;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * default implementation of {@link OnboardingItemDao}
 */
@RequiredArgsConstructor
public class OnboardingItemInMemoryDaoImpl implements OnboardingItemDao {

    private final List<OnboardingItemModel> list;

    public OnboardingItemInMemoryDaoImpl(Context context) {
        list = Arrays.asList(
                new ScreenItemModel(context.getString(R.string.tutorial_step_1_title), context.getString(R.string.tutorial_step_1_description), R.drawable.intro01),
                new ScreenItemModel(context.getString(R.string.tutorial_step_2_title), context.getString(R.string.tutorial_step_2_description), R.drawable.intro02),
                new ScreenItemModel(context.getString(R.string.tutorial_step_3_title), context.getString(R.string.tutorial_step_3_description), R.drawable.intro03),
                new ScreenItemModel(context.getString(R.string.tutorial_step_4_title), context.getString(R.string.tutorial_step_4_description), R.drawable.intro04),
                new ScreenItemModel(context.getString(R.string.tutorial_step_5_title), context.getString(R.string.tutorial_step_5_description), R.drawable.intro05)
        );
    }

    @Override
    public List<OnboardingItemModel> findAll() {
        return list;
    }

    /**
     * basic implementation of {@link OnboardingItemModel}
     */
    public class ScreenItemModel implements OnboardingItemModel {

        private final String title;
        private final String description;
        private final int screenImg;

        ScreenItemModel(String title, String description, int screenImg) {
            this.title = title;
            this.description = description;
            this.screenImg = screenImg;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public int getScreenImg() {
            return screenImg;
        }

    }
}
