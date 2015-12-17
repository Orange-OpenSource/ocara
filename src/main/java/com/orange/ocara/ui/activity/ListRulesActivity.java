/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.orange.ocara.R;
import com.orange.ocara.model.ModelManager;
import com.orange.ocara.modelStatic.RuleSet;
import com.orange.ocara.ui.fragment.ObjectsByRuleSetFragment;
import com.orange.ocara.ui.fragment.ObjectsByRuleSetFragment_;
import com.orange.ocara.ui.view.PagerSlidingTabStrip;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

@EActivity(resName="activity_list_rules")
public class ListRulesActivity extends BaseActivity {
    @Inject
    ModelManager modelManager;

    @Extra("ruleSetId")
    String ruleSetId;

    @Extra("objectDescriptionId")
    String objectDescriptionId;

    @ViewById(resName="pager")
    ViewPager viewPager;
    @ViewById(resName="tabs")
    PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.list_rules_title);
    }

    @AfterViews
    void setUpViewPager() {
        RuleSetPagerAdapter ruleSetPagerAdapter = new RuleSetPagerAdapter(getSupportFragmentManager(), modelManager.getAllRuleSet());

        viewPager.setAdapter(ruleSetPagerAdapter);
        tabs.setViewPager(viewPager);

        int position = ruleSetPagerAdapter.getItemPositionById(ruleSetId);
        viewPager.setCurrentItem(position);
    }

    /**
     * Categories pager adapter.
     */
    private class RuleSetPagerAdapter extends FragmentPagerAdapter {
        private final List<RuleSet> ruleSets = new ArrayList<RuleSet>();

        RuleSetPagerAdapter(FragmentManager fm, Collection<RuleSet> ruleSets) {
            super(fm);

            this.ruleSets.addAll(ruleSets);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return ruleSets.size();
        }

        public int getItemPositionById(String ruleSetId) {
            if (ruleSetId != null) {
                for (int i = 0; i < ruleSets.size(); i++) {
                    if (ruleSets.get(i).getId().equals(ruleSetId)) {
                        return i;
                    }
                }
            }

            return 0;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {

            ObjectsByRuleSetFragment fragment = ObjectsByRuleSetFragment_.builder().build();
            fragment.setRuleSet(ruleSets.get(position));
            fragment.setObjectDescriptionId(objectDescriptionId);

            return fragment;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return ruleSets.get(position).getType();
        }
    }


}


