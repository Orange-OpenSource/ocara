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

package com.orange.ocara.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import com.orange.ocara.R;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.data.cache.model.ResponseModel;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.ui.adapter.ItemListAdapter;
import com.orange.ocara.ui.view.AuditObjectCharacteristicsItemView;
import com.orange.ocara.ui.view.AuditObjectCharacteristicsItemView_;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class SelectAuditObjectCharacteristicsDialogBuilder extends OcaraDialogBuilder {

    private final ItemListAdapter<ObjectDescriptionSelector> objectDescriptionChildrenAdapter = new ObjectDescriptionSelectorItemListAdapter();
    private EquipmentEntity objectDescription;
    private RuleSetService mRuleSetService;
    private AuditObjectEntity auditObject;
    private View objectCharacteristicsView;
    private ListView objectDescriptionChildrenList;
    private RulesetEntity mRuleSetDetail;

    public SelectAuditObjectCharacteristicsDialogBuilder(Context context, EquipmentEntity objectDescription, RuleSetService ruleSetService) {
        super(context);
        this.objectDescription = objectDescription;
        mRuleSetService = ruleSetService;
        mRuleSetDetail = objectDescription.getRuleSetDetail();
        setupObjectCharacteristics();
    }

    public SelectAuditObjectCharacteristicsDialogBuilder(Context context, AuditObjectEntity auditObject, RuleSetService ruleSetService) {
        super(context);
        mRuleSetService = ruleSetService;
        this.objectDescription = mRuleSetService.getObjectDescriptionFromRef(auditObject.getAudit().getRuleSetRef(), auditObject.getAudit().getRuleSetVersion(), auditObject.getObjectDescriptionId());
        this.auditObject = auditObject;
        mRuleSetDetail = mRuleSetService.getRuleSet(auditObject.getAudit().getRuleSetRef(), auditObject.getAudit().getRuleSetVersion(), false);
        setupObjectCharacteristics();
    }

    public static void showCharacteriscticsAuditObject(final Activity activity, final AuditObjectEntity auditObject, final RuleSetService ruleSetService, final UpdateAuditObjectCharacteristicsListener callback) {
        final SelectAuditObjectCharacteristicsDialogBuilder selectCharacteristicsBuilder = new SelectAuditObjectCharacteristicsDialogBuilder(activity, auditObject, ruleSetService);
        final ResponseModel response = auditObject.getResponse();
        AlertDialog showCharacteristicsDialog = selectCharacteristicsBuilder
                .setTitle(R.string.audit_object_characteristics_title)
                .setPositiveButton(R.string.audit_object_characteristics_validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<AuditObjectEntity> characteristics = auditObject.getChildren(); // existing characteristics
                        List<EquipmentEntity> selections = selectCharacteristicsBuilder.getSelectedCharacteristics(); // new selected characteristics
                        // build a list of new characteristics to create and associate to audit object
                        List<EquipmentEntity> characteristicsToCreate = new ArrayList<>();
                        for (EquipmentEntity selectedObject : selections) {
                            if (!auditObject.hasCharacteristic(selectedObject.getName())) {
                                characteristicsToCreate.add(selectedObject);
                            }
                        }
                        // build a list of no more selected characteristics to remove from audit object
                        List<AuditObjectEntity> characteristicsToRemove = new ArrayList<AuditObjectEntity>();
                        for (AuditObjectEntity caracteristic : characteristics) {
                            final AuditEntity audit = caracteristic.getAudit();
                            EquipmentEntity objectDescription = ruleSetService.getObjectDescriptionFromRef(audit.getRuleSetRef(), audit.getRuleSetVersion(), caracteristic.getObjectDescriptionId());
                            if (!selections.contains(objectDescription)) {
                                characteristicsToRemove.add(caracteristic);
                            }
                        }
                        callback.onUpdateAuditObjectChildren(auditObject, characteristicsToCreate, characteristicsToRemove);

                        if (!response.equals(ResponseModel.NO_ANSWER) && !characteristicsToCreate.isEmpty()) {

                            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setMessage(R.string.info_retest);
                            builder.setPositiveButton(R.string.action_retest, (dialog1, which1) -> callback.onLaunchAuditObjectsTestRequested(auditObject));
                            builder.setNegativeButton(R.string.action_cancel, (dialog12, which12) -> dialog12.dismiss());
                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                })
                .setNegativeButton(R.string.action_back, null)
                .create();

        showCharacteristicsDialog.show();
    }

    public void setupObjectCharacteristics() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        objectCharacteristicsView = inflater.inflate(com.orange.ocara.R.layout.dialog_select_audit_object_characteristics, null);
        setView(objectCharacteristicsView);

        objectDescriptionChildrenList = (ListView) objectCharacteristicsView.findViewById(R.id.object_description_children);

        objectDescriptionChildrenList.setAdapter(objectDescriptionChildrenAdapter);

        List<ObjectDescriptionSelector> selectors = createObjectSelectors();
        objectDescriptionChildrenAdapter.update(selectors);
    }

    public List<EquipmentEntity> getSelectedCharacteristics() {
        // detect which subObject are selected

        List<EquipmentEntity> selections = new ArrayList<>();
        for (int i = 0; i < objectDescriptionChildrenAdapter.getCount(); i++) {
            ObjectDescriptionSelector selector = objectDescriptionChildrenAdapter.getItem(i);
            if (selector.isSelected()) {
                selections.add(selector.getObject());
            }
        }
        return selections;
    }

    private List<ObjectDescriptionSelector> createObjectSelectors() {
        List<ObjectDescriptionSelector> selectors = new ArrayList<>();
        List<EquipmentEntity> objectDescriptions = new ArrayList<>();
        final Integer version = Integer.valueOf(mRuleSetDetail.getVersion());
        final String reference = mRuleSetDetail.getReference();
        for (String ref : objectDescription.getSubObject()) {
            final EquipmentEntity objectDescriptionFormRef = mRuleSetService.getObjectDescriptionFromRef(reference, version, ref);
            objectDescriptions.add(objectDescriptionFormRef);
        }

        for (EquipmentEntity object : objectDescriptions) {
            ObjectDescriptionSelector selector = new ObjectDescriptionSelector();
            selector.object = object;
            selector.selected = isCharacteristicSelected(object);
            selectors.add(selector);
        }
        return selectors;
    }

    private boolean isCharacteristicSelected(EquipmentEntity object) {
        return auditObject != null
                && auditObject.hasCharacteristic(object.getName());
    }

    public interface UpdateAuditObjectCharacteristicsListener {

        void onUpdateAuditObjectChildren(AuditObjectEntity auditObject, List<EquipmentEntity> characteristicsToCreate, List<AuditObjectEntity> characteristicsToRemove);

        void onLaunchAuditObjectsTestRequested(AuditObjectEntity auditObject);
    }

    /**
     * ObjectDescription Selector adapter.
     */

    @Data
    public static class ObjectDescriptionSelector {
        EquipmentEntity object;
        boolean selected = false;
    }

    public class ObjectDescriptionSelectorItemListAdapter extends ItemListAdapter<ObjectDescriptionSelector> {

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            AuditObjectCharacteristicsItemView auditObjectCharacteristicsItemView;

            if (convertView != null && convertView instanceof AuditObjectCharacteristicsItemView) {
                auditObjectCharacteristicsItemView = (AuditObjectCharacteristicsItemView) convertView;
            } else {
                auditObjectCharacteristicsItemView = AuditObjectCharacteristicsItemView_.build(getContext());
            }

            ObjectDescriptionSelector child = getItem(position);
            auditObjectCharacteristicsItemView.setCharacteristic(child);
            return auditObjectCharacteristicsItemView;
        }
    }
}