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

package com.orange.ocara.ui.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.orange.ocara.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.lucasr.twowayview.TwoWayView;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;

@EViewGroup(R.layout.audit_objects)
public class AuditObjectsView extends LinearLayout implements AdapterView.OnItemClickListener {

    @ViewById(R.id.previous_button)
    ImageView leftButton;
    @ViewById(R.id.next_button)
    ImageView rightButton;

    @ViewById(R.id.path)
    @Getter
    TwoWayView auditPath;

    @Setter(AccessLevel.PUBLIC)
    private AdapterView.OnItemClickListener itemClickListener = null;

    public AuditObjectsView(Context context) {
        this(context, null);
    }

    public AuditObjectsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(HORIZONTAL);
    }

    /**
     * To update a button.
     *
     * @param view    Button view to update
     * @param enabled true if enabled, false otherwise
     */
    private static void updateButton(View view, boolean enabled) {
        view.setEnabled(enabled);
        view.setClickable(enabled);
    }

    @AfterViews
    void initAuditPath() {
        auditPath.setOnItemClickListener(this);
        auditPath.setOrientation(TwoWayView.Orientation.HORIZONTAL);
        auditPath.setChoiceMode(TwoWayView.ChoiceMode.SINGLE);

    }

    @Click(resName = "previous_button")
    void leftButtonWasClicked() {
        final int selectedItemPosition = getSelectedItem() - 1;
        if (selectedItemPosition >= 0) {
            selectItemWithoutClick(selectedItemPosition);
        }
    }

    @Click(resName = "next_button")
    void rightButtonWasClicked() {
        final int selectedItemPosition = getSelectedItem() + 1;
        if (selectedItemPosition < auditPath.getAdapter().getCount()) {
            selectItemWithoutClick(selectedItemPosition);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        Timber.v("onItemClick %d", position);
        auditPath.post(new Runnable() {
            @Override
            public void run() {
                auditPath.smoothScrollToPosition(position);
            }
        });

        updateArrows(position);

        if (itemClickListener != null) {
            itemClickListener.onItemClick(parent, view, position, id);
        }
    }

    public int getSelectedItem() {
        Timber.v("getSelectedItem %d", auditPath.getCheckedItemPosition());
        return auditPath.getCheckedItemPosition();
    }

    public void selectItem(final int position) {
        Timber.v("selectItem %d", position);

        auditPath.performItemClick(
                auditPath.getChildAt(position),
                position,
                auditPath.getAdapter().getItemId(position));


    }

    public void selectItemWithoutClick(final int position) {
        auditPath.setItemChecked(position, true);
        auditPath.post(new Runnable() {

            @Override
            public void run() {
                auditPath.smoothScrollToPosition(position);
            }
        });

        updateArrows(position);
    }

    /**
     * Set the adapter that manage the content of this gallery.
     *
     * @param adapter gallery adapter
     */
    public void setAdapter(ListAdapter adapter) {

        if (adapter == null) {
            return;
        }

        auditPath.setAdapter(adapter);
        auditPath.setItemChecked(0, true);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateArrows(0);
            }
        });
    }

    /**
     * Update arrow states.
     *
     * @param selectedIndex selected index
     */
    private void updateArrows(int selectedIndex) {
        Timber.v("selectedIndex = %d", selectedIndex);

        updateLeftArrow(selectedIndex > 0);
        updateRightArrow(selectedIndex < auditPath.getAdapter().getCount() - 1);
    }

    private void updateLeftArrow(boolean enabled) {
        Timber.v("left arrow enabled = %b", enabled);

        updateButton(leftButton, enabled);
    }

    private void updateRightArrow(boolean enabled) {
        Timber.v("right arrow enabled = %b", enabled);

        updateButton(rightButton, enabled);
    }

}
