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

package com.orange.ocara.ui.fragment;

import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.LoadRulesTask;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.model.RuleGroupModel;
import com.orange.ocara.business.model.RuleModel;
import com.orange.ocara.business.service.EquipmentService;
import com.orange.ocara.business.service.impl.EquipmentServiceImpl;
import com.orange.ocara.data.net.model.Equipment;
import com.orange.ocara.ui.BrowseRulesUiConfig;
import com.orange.ocara.ui.adapter.RulesExpandableListAdapter;
import com.orange.ocara.ui.adapter.RulesetEquipmentsAdapter;
import com.orange.ocara.ui.contract.BrowseRulesContract;
import com.orange.ocara.ui.contract.ListEquipmentsContract;
import com.orange.ocara.ui.presenter.ListEquipmentsPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import timber.log.Timber;

import static com.orange.ocara.tools.ListUtils.emptyList;

/**
 * {@link BaseFragment} dedicated to the display of equipments
 * and their related rules, according to a given ruleset
 */
@EFragment(R.layout.fragment_object_list_by_rules)
public class BrowseEquipmentsFragment extends BaseFragment implements ListEquipmentsContract.ListEquipmentsView, BrowseRulesContract.BrowseRulesView {

    @ViewById(R.id.objects_listview)
    ListView equipmentsListView;

    @ViewById(R.id.text_no_question)
    TextView noRuleTextView;

    @ViewById(R.id.rules_listview)
    ExpandableListView rulesListView;

    @Bean
    RulesetEquipmentsAdapter equipmentsAdapter;

    private RulesExpandableListAdapter rulesAdapter;

    private BrowseRulesContract.BrowseRulesUserActionsListener browseRulesListener;

    private ListEquipmentsContract.ListEquipmentsUserActionsListener listEquipmentsListener;

    @Bean(EquipmentServiceImpl.class)
    EquipmentService equipmentService;

    @Bean(BrowseRulesUiConfig.class)
    BrowseRulesUiConfig browseRulesUiConfig;

    @FragmentArg
    String defaultEquipmentReference;

    @FragmentArg
    Long rulesetId;

    /**
     * Initializing the listeners, aka the presenters
     */
    @AfterViews
    void initData() {
        rulesAdapter = new RulesExpandableListAdapter(this.getContext());

        listEquipmentsListener = new ListEquipmentsPresenter(equipmentService, this);

        browseRulesListener = browseRulesUiConfig.browseRulesActionsListener();
    }

    @AfterViews
    void initView() {

        Timber.d("Message=Initializing the view;");

        // initializing the view that displays equipments
        equipmentsListView.setOnItemClickListener(equipmentClickListener());
        equipmentsListView.setAdapter(equipmentsAdapter);

        // initializing the view that has an expandable list of rules
        rulesListView.setAdapter(rulesAdapter);
        rulesListView.setEmptyView(noRuleTextView);

        // initializing the content of the view
        listEquipmentsListener.loadAllEquipmentsByRulesetId(rulesetId);
    }

    @Override
    public void showEquipments(List<Equipment> equipments) {

        Timber.d("Message=Showing a list of equipments;DefaultObjectRef=%s;ObjectsCount=%d;", defaultEquipmentReference, equipments.size());

        // Updating the adapter
        equipmentsAdapter.update(equipments);

        // Selecting the current item
        int currentPosition = equipmentsAdapter.getPositionByReference(defaultEquipmentReference);

        if (currentPosition < 0) {
            currentPosition = 0;
        }

        equipmentsListView.setSelection(currentPosition);
        equipmentsListView.setItemChecked(currentPosition, true);

        // Refreshing the subsequent questions
        Equipment equipment = equipmentsAdapter.getItem(currentPosition);
        Timber.d(
                "Message=Retrieving questions for selected equipment;ObjectDescriptionRef=%s;ObjectDescriptionName=%s",
                equipment.getReference(), equipment.getName());

        loadRules(equipment.getId());
    }

    @Override
    public void showRules(List<RuleGroupModel> groups, List<RuleModel> rules) {
        Timber.d("Message=Showing a list of questions;ObjectRef=%s;QuestionsCount=%d;", defaultEquipmentReference, groups.size());

        // Replacing the content of the adapter with the elements of the selected ObjectDescription
        rulesAdapter.update(groups, rules);

        // Then, collapsing all the expanded items
        for (int i = 0; i < rulesAdapter.getGroupCount(); i++) {
            rulesListView.collapseGroup(i);
        }
    }

    @Override
    public void showNoRules() {
        Timber.d("Message=Showing no rules;ObjectRef=%s;", defaultEquipmentReference);
        rulesAdapter.reset();
    }

    @Override
    public void showNoEquipments() {
        Timber.d("Message=Showing no equipment;ObjectRef=%s;", defaultEquipmentReference);
        equipmentsAdapter.update(emptyList());
    }

    /**
     * retrieves a callback dedicated to the selection of an item in a list of equipments
     *
     * @return an instance of {@link AdapterView.OnItemClickListener}
     */
    private AdapterView.OnItemClickListener equipmentClickListener() {

        return (parentView, clickedView, positionOfTheClickedView, positionOfTheItem) -> {
            Timber.d("Message=Clicked on an item;Position=%d;Index=%d", positionOfTheClickedView, positionOfTheItem);

            Equipment equipment = (Equipment) parentView.getItemAtPosition(positionOfTheClickedView);
            loadRules(equipment.getId());
        };
    }

    /**
     * triggers the loading of the content related to the given identifier
     *
     * @param equipmentId an identifier for an Equipment (aka ObjectDescription)
     */
    private void loadRules(long equipmentId) {
        browseRulesListener.loadAllRulesByEquipmentId(
                BrowseEquipmentsFragment.this.rulesetId,
                equipmentId,
                new UseCase.UseCaseCallback<LoadRulesTask.LoadRulesResponse>() {
                    @Override
                    public void onComplete(LoadRulesTask.LoadRulesResponse response) {

                        showRules(response.getGroups(), response.getRules());
                    }

                    @Override
                    public void onError(ErrorBundle errors) {

                        // do nothing yet
                    }
                });
    }

}
