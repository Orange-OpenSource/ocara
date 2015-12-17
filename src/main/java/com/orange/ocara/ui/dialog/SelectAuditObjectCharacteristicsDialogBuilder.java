/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.orange.ocara.model.AuditObject;
import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.modelStatic.Response;
import com.orange.ocara.ui.adapter.ItemListAdapter;
import com.orange.ocara.ui.view.AuditObjectCharacteristicsItemView;
import com.orange.ocara.ui.view.AuditObjectCharacteristicsItemView_;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class SelectAuditObjectCharacteristicsDialogBuilder extends OcaraDialogBuilder  {


    private ObjectDescription objectDescription;
    private AuditObject auditObject;
    private View objectCharacteristicsView;
    private ListView objectDescriptionChildrenList;

    public SelectAuditObjectCharacteristicsDialogBuilder(Context context, ObjectDescription objectDescription) {
        super(context);
        this.objectDescription = objectDescription;

        setupObjectCharacteristics();
    }

    public SelectAuditObjectCharacteristicsDialogBuilder(Context context, AuditObject auditObject) {
        super(context);
        this.objectDescription = auditObject.getObjectDescription();
        this.auditObject = auditObject;

        setupObjectCharacteristics();
    }


    public void setupObjectCharacteristics() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        objectCharacteristicsView = inflater.inflate(com.orange.ocara.R.layout.dialog_select_audit_object_characteristics, null);
        setView(objectCharacteristicsView);

        objectDescriptionChildrenList = (ListView) objectCharacteristicsView.findViewById(com.orange.ocara.R.id.object_description_children);

        objectDescriptionChildrenList.setAdapter(objectDescriptionChildrenAdapter);

        List<ObjectDescriptionSelector> selectors = createObjectSelectors();
        objectDescriptionChildrenAdapter.update(selectors);
    }



    public List<ObjectDescription> getSelectedCharacteristics() {
        // detect which objects are selected

        List<ObjectDescription> selections = new ArrayList<>();

        for(int i = 0; i < objectDescriptionChildrenAdapter.getCount(); i++) {
            ObjectDescriptionSelector selector = objectDescriptionChildrenAdapter.getItem(i);
            if (selector.isSelected()) {
                selections.add(selector.getObject());
            }
        }
        return selections;
    }


    /**
     * ObjectDescription Selector adapter.
     */

    @Data
    public static class ObjectDescriptionSelector {
        ObjectDescription object;
        boolean selected = false;
    }


    private final ItemListAdapter<ObjectDescriptionSelector> objectDescriptionChildrenAdapter = new ObjectDescriptionSelectorItemListAdapter();

    public class ObjectDescriptionSelectorItemListAdapter extends ItemListAdapter<ObjectDescriptionSelector> {

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            AuditObjectCharacteristicsItemView auditObjectCharacteristicsItemView;

            if (convertView == null) {
                auditObjectCharacteristicsItemView = AuditObjectCharacteristicsItemView_.build(getContext());
            } else {
                auditObjectCharacteristicsItemView = (AuditObjectCharacteristicsItemView) convertView;
            }

            ObjectDescriptionSelector child = getItem(position);
            auditObjectCharacteristicsItemView.setCharacteristic(child);
            return auditObjectCharacteristicsItemView;
        }
    };



    private List<ObjectDescriptionSelector> createObjectSelectors() {
        List<ObjectDescriptionSelector> selectors = new ArrayList<>();
        for(ObjectDescription object : objectDescription.getChildren()) {
            ObjectDescriptionSelector selector = new ObjectDescriptionSelector();
            selector.object = object;
            selector.selected = isCharacteristicSelected(object);
            selectors.add(selector);
        }
        return selectors;
    }



    private boolean isCharacteristicSelected(ObjectDescription object) {
        return auditObject != null
                && auditObject.hasCharacteristic(object.getName());
    }



    public static void showCharacteriscticsAuditObject(final Activity activity,final AuditObject auditObject, final UpdateAuditObjectCharacteristicsListener callback) {
        final SelectAuditObjectCharacteristicsDialogBuilder selectCharacteristicsBuilder = new SelectAuditObjectCharacteristicsDialogBuilder(activity, auditObject);
        final Response response = auditObject.getResponse();
        AlertDialog showCharacteristicsDialog = selectCharacteristicsBuilder
                .setTitle(com.orange.ocara.R.string.audit_object_characteristics_title)
                .setPositiveButton(com.orange.ocara.R.string.audit_object_characteristics_validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<AuditObject> characteristics = auditObject.getChildren(); // existing characteristics
                        List<ObjectDescription> selections = selectCharacteristicsBuilder.getSelectedCharacteristics(); // new selected characteristics

                        // build a list of new characteristics to create and associate to audit object
                        List<ObjectDescription> characteristicsToCreate = new ArrayList<ObjectDescription>();
                        for (ObjectDescription selectedObject : selections) {
                            if (!auditObject.hasCharacteristic(selectedObject.getName())) {
                                characteristicsToCreate.add(selectedObject);
                            }
                        }
                        // build a list of no more selected characteristics to remove from audit object
                        List<AuditObject> characteristicsToRemove = new ArrayList<AuditObject>();
                        for (AuditObject caracteristic : characteristics) {
                            ObjectDescription objectDescription = caracteristic.getObjectDescription();
                            if (!selections.contains(objectDescription)) {
                                characteristicsToRemove.add(caracteristic);
                            }
                        }
                        callback.onUpdateAuditObjectChildren(auditObject, characteristicsToCreate, characteristicsToRemove);


                        if (!response.equals(Response.NoAnswer) && !characteristicsToCreate.isEmpty()) {

                            SnackbarManager.show(
                                    Snackbar.with(activity)
                                            .text(com.orange.ocara.R.string.info_retest)
                                            .type(SnackbarType.MULTI_LINE)
                                            .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                                            .backgroundDrawable(com.orange.ocara.R.color.white)
                                            .textColor(com.orange.ocara.R.color.black)
                                            .actionColorResource(com.orange.ocara.R.color.accent)
                                            .actionLabel(com.orange.ocara.R.string.action_retest)
                                            .actionListener(new ActionClickListener() {
                                                @Override
                                                public void onActionClicked(Snackbar snackbar) {
                                                    callback.onLaunchAuditObjectsTestRequested(auditObject);
                                                }
                                            }),
                                    (ViewGroup)activity.findViewById(android.R.id.content),
                                    true
                            );
                        }
                    }
                })
                .setNegativeButton(com.orange.ocara.R.string.action_back, null)
                .create();

        showCharacteristicsDialog.show();
    }


    public interface UpdateAuditObjectCharacteristicsListener {

        void onUpdateAuditObjectChildren(AuditObject auditObject, List<ObjectDescription> characteristicsToCreate, List<AuditObject> characteristicsToRemove) ;

        void onLaunchAuditObjectsTestRequested(AuditObject auditObject);
    }

}