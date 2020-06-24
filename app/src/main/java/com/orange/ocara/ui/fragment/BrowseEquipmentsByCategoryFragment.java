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

import android.content.Context;
import android.content.res.Configuration;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.orange.ocara.R;
import com.orange.ocara.data.net.model.EquipmentCategoryEntity;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.ui.activity.BrowseRulesetsActivity_;
import com.orange.ocara.ui.activity.ShowEquipmentInfoActivity_;
import com.orange.ocara.ui.adapter.CategoryObjectsAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

/**
 * Fragment dedicated to displaying a bunch of {@link EquipmentEntity}s
 */
@EFragment(R.layout.fragment_object_list)
public class BrowseEquipmentsByCategoryFragment extends BaseFragment {

    @ViewById(R.id.objects_gridview)
    GridView equipmentsView;

    @FragmentArg
    protected EquipmentCategoryEntity category;

    @FragmentArg
    protected String ruleSetReference;

    @FragmentArg
    protected Integer ruleSetVersion;

    @FragmentArg
    protected Long auditId;

    private CategoryObjectsAdapter categoryObjectsAdapter;
    private OnEquipmentSelectListener equipmentSelectListener = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnEquipmentSelectListener) {
            equipmentSelectListener = (OnEquipmentSelectListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        equipmentSelectListener = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // As Grid
        final int firstVisiblePosition = equipmentsView.getFirstVisiblePosition();
        equipmentsView.setNumColumns(getResources().getInteger(R.integer.object_columns_count));
        equipmentsView.setSelection(firstVisiblePosition);
    }

    @AfterViews
    void initView() {
        categoryObjectsAdapter = new CategoryObjectsAdapter(this.getContext());
        categoryObjectsAdapter.update(category.getObjects());
        equipmentsView.setAdapter(categoryObjectsAdapter);

        registerForContextMenu(equipmentsView);
    }

    @ItemClick(R.id.objects_gridview)
    void onEquipmentSelected(EquipmentEntity equipment) {
        if (equipmentSelectListener != null) {
            equipmentSelectListener.onEquipmentSelected(equipment);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.objects_context_menu, menu);


        // Workaround to force fragment menu item to call local onContextItemSelected not one from activity
        MenuItem.OnMenuItemClickListener clickListener = item -> {
            onContextItemSelected(item);
            return true;
        };

        for (int i = 0, n = menu.size(); i < n; i++) {
            menu.getItem(i).setOnMenuItemClickListener(clickListener);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (!getUserVisibleHint()) {
            return false;
        }

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final EquipmentEntity equipment = categoryObjectsAdapter.getItem(info.position);

        int i = item.getItemId();
        if (i == R.id.action_object_grid_detail) {
            showEquipment(equipment);
            return true;
        } else if (i == R.id.action_object_add) {
            onEquipmentSelected(equipment);
            return true;
        } else if (i == R.id.action_object_ruleset) {
            showRulesets(equipment.getReference());
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    private void showEquipment(EquipmentEntity equipment) {
        ShowEquipmentInfoActivity_
                .intent(this)
                .auditId(auditId)
                .objectDescriptionId(equipment.getReference())
                .start();

    }

    /** triggers the display of the browser for rulesets */
    private void showRulesets(final String reference) {
        BrowseRulesetsActivity_
                .intent(this)
                .rulesetReference(ruleSetReference)
                .ruleSetVersion(ruleSetVersion)
                .defaultEquipmentReference(reference)
                .start();
    }

    /**
     * Behaviour of a equipmentSelectListener related to the selection of an {@link EquipmentEntity}
     */
    public interface OnEquipmentSelectListener {
        /**
         * function to execute each time an {@link EquipmentEntity} is selected.
         *
         * @param item the selected element
         */
        void onEquipmentSelected(EquipmentEntity item);
    }
}
