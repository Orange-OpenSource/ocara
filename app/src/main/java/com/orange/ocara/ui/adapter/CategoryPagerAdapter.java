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

package com.orange.ocara.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.net.model.EquipmentCategoryEntity;
import com.orange.ocara.ui.fragment.BrowseEquipmentsByCategoryFragment_;

import java.util.ArrayList;
import java.util.List;

/**
 * Categories pager adapter.
 */
public class CategoryPagerAdapter extends FragmentPagerAdapter {


    private final List<EquipmentCategoryEntity> categories = new ArrayList<>();
    private AuditEntity mAudit;

    public CategoryPagerAdapter(FragmentManager fm, List<EquipmentCategoryEntity> categories,AuditEntity audit) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mAudit = audit;
        this.categories.addAll(categories);
    }


    // Returns total number of pages
    @Override
    public int getCount() {
        return categories.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {

        return BrowseEquipmentsByCategoryFragment_
                .builder()
                .auditId(mAudit.getId())
                .category(categories.get(position))
                .ruleSetReference(mAudit.getRuleSetRef())
                .ruleSetVersion(mAudit.getRuleSetVersion())
                .build();
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position) != null ? categories.get(position).getName() : "";
    }
}
