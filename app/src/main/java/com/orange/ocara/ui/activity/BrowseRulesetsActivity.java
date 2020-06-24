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

package com.orange.ocara.ui.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;

import com.orange.ocara.R;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.ui.ListRulesUiConfig;
import com.orange.ocara.ui.adapter.RulesetSpinnerAdapter;
import com.orange.ocara.ui.contract.ListRulesetsContract;
import com.orange.ocara.ui.fragment.BrowseEquipmentsFragment;
import com.orange.ocara.ui.fragment.BrowseEquipmentsFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import timber.log.Timber;

/**
 * Activity dedicated to the browsing of rulesets
 */
@EActivity(R.layout.activity_list_rules)
public class BrowseRulesetsActivity extends BaseActivity implements ListRulesetsContract.ListRulesView {

    private static final String RULESET = "RULESET";

    @Extra
    String rulesetReference;

    @Extra
    Integer ruleSetVersion;

    @Extra
    String defaultEquipmentReference;

    @ViewById(R.id.rule_spinner)
    AppCompatSpinner rulesetSpinner;

    @ViewById(R.id.rule_layout)
    FrameLayout equipmentFragment;

    @Bean
    RulesetSpinnerAdapter rulesetAdapter;

    private boolean init = false;

    @Bean(ListRulesUiConfig.class)
    ListRulesUiConfig uiConfig;

    ListRulesetsContract.ListRulesUserActionsListener actionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.list_rules_title);
    }

    @AfterViews
    void initView() {
        actionListener = uiConfig.actionsListener(this);

        actionListener.loadRulesets();
    }

    @Override
    public void showRulesets(List<RulesetEntity> list) {

        Timber.d("Message=Initializing the list of rulesets;RulesetsCount=%d", list.size());
        if (!list.isEmpty()) {
            rulesetAdapter.update(list);
            rulesetSpinner.setAdapter(rulesetAdapter);
            int position = 0;
            if (rulesetReference == null && ruleSetVersion == null) {
                position = rulesetSpinner.getSelectedItemPosition();
            } else if (ruleSetVersion != null) {
                init = true;
                RulesetEntity cursor;
                for (int i = 0; i < list.size(); i++) {
                    cursor = list.get(i);
                    if (cursor != null
                            && cursor.getReference().equals(rulesetReference)
                            && cursor.getVersion() != null
                            && Integer.parseInt(cursor.getVersion()) == ruleSetVersion) {
                        position = i;
                        break;
                    }
                }
            }
            rulesetSpinner.setSelection(position);
        }
    }

    @ItemSelect(R.id.rule_spinner)
    public void onRulesetSelected(boolean selected, int position) {
        Timber.d("Message=Selecting a ruleset;RulesetIndex=%d;IsInitialized=%b", position, init);
        updateFragment(rulesetAdapter.getItem(position));
    }

    /** triggers the display of the {@link BrowseEquipmentsFragment} */
    private void updateFragment(final @NonNull RulesetEntity ruleset) {

        Timber.d("Message=Updating fragment;RulesetId=%d;EquipmentRef=%s", ruleset.getId(), defaultEquipmentReference);

        BrowseEquipmentsFragment fragment = BrowseEquipmentsFragment_
                .builder()
                .rulesetId(ruleset.getId())
                .defaultEquipmentReference(defaultEquipmentReference)
                .build();

        fragment.setRetainInstance(true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(equipmentFragment.getId(), fragment, RULESET)
                .commit();
    }
}