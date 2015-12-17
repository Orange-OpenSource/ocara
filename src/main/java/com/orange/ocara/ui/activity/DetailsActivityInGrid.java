/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orange.ocara.model.ModelManager;
import com.orange.ocara.modelStatic.Illustration;
import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.ui.view.IllustrationCommentItemView;
import com.orange.ocara.ui.view.IllustrationCommentItemView_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@EActivity(resName="activity_details_in_grid")
@OptionsMenu(resName="object_details")
/*package*/ class DetailsActivityInGrid extends BaseActivityManagingAudit  {

    @Extra("objectDescriptionId")
    String objectDescriptionId;
    ObjectDescription objectDescription;


    @ViewById(resName="scrollview_container")
    ScrollView scrollContainer;



    @ViewById(resName="title")
    TextView customTitle;
    @ViewById(resName="subtitle")
    TextView customSubtitle;
    @ViewById(resName="object_definitions_layout")
    ViewGroup ObjectDefinitionLayout;
    @ViewById(resName="text_definition")
    TextView textDefinition;
      @ViewById(resName="title_characteristic")
    TextView titleCharacteristic;
    @ViewById(resName="illustrations")
    ImageView illustration;
    @ViewById(resName="text_characteristic")
    TextView textCharacteristic;
    @ViewById(resName="object_characteristic_layout")
    ViewGroup ObjectCharacteristicLayout;
    @ViewById(resName="object_illustrations_layout")
    ViewGroup ObjectIllustrationsLayout;

    @ViewById(resName="illustration_comments_section")
    ViewGroup illustrationsCommentsView;



    @Inject
    ModelManager modelManager;



    private void showIllustration(Uri icon, String name, List<Illustration> illustrationList, Illustration illustration) {
        int selectedIndex =0;
        String[] titles = new String[illustrationList.size()];
        String[] comments = new String[illustrationList.size()];
        String[] images = new String[illustrationList.size()];

        for (int i = 0; i < illustrationList.size(); i++) {
            if (illustrationList.get(i).equals(illustration)) {
                selectedIndex = i;
            }

            if (illustrationList.get(i).getTitle() != null) {
                titles[i] = illustrationList.get(i).getTitle();
            }
            else {
                titles[i] = getString(com.orange.ocara.R.string.illustration_activity_auditobject_title, i + 1, name);
            }
            comments[i] = illustrationList.get(i).getComment();

            if (!illustrationList.get(i).getImage().toString().isEmpty()) {
               images[i] = illustrationList.get(i).getImage().toString();
            }

        }

        IllustrationsActivity_.intent(DetailsActivityInGrid.this)
                .selectedIndex(selectedIndex)
                .icon(icon)
                .titles(titles)
                .comments(comments)
                .images(images)
                .start();

    }

    private final AdapterView.OnItemClickListener ObjectIllustrationCommentListViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<Illustration> illustrations = objectDescription.getIllustrations();
            Illustration illustration =(Illustration) parent.getAdapter().getItem(position);
            final String objectDescriptionName = objectDescription.getName();
            final Uri objectDescriptionIcon = Uri.parse(objectDescription.getIcon().toString());
            onIllustrationCommentClicked(objectDescriptionIcon, objectDescriptionName, illustrations, illustration, view);
        }
    };

    private void onIllustrationCommentClicked(Uri icon, String name, List<Illustration> illustrations, Illustration illustration, View commentView) {
        showIllustration(icon, name, illustrations, illustration);
    }


    @AfterViews
    void hideLogo() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayUseLogoEnabled(false);
    }


    @Override
    void auditRefreshed() {
        super.auditRefreshed();


        objectDescription = this.audit.getRuleSet().getObjectDescription(objectDescriptionId);

        updateObjectDefinitions(objectDescription);
        updateObjectCharacteristics(objectDescription);
        updateObjectIllustrations(objectDescription);
        updateActionBar();
    }



    @OptionsItem(resName="action_rules")
        void showRuleSet(){
            ListRulesActivity_.intent(this)
                    .ruleSetId(audit.getRuleSetId())
                    .objectDescriptionId(objectDescriptionId).start();
    }

    private void updateObjectDefinitions(ObjectDescription objectDescription) {
        String definitions = objectDescription.getDefinition();

        ObjectDefinitionLayout.setVisibility(GONE);
            if(!definitions.isEmpty()){
        ObjectDefinitionLayout.setVisibility(VISIBLE);
        textDefinition.setText(definitions);
            }
    }

    @Override
    public void setTitle(int id) {
        setTitle(getString(id));
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle("");
        customTitle.setText(title);
    }

    private void updateTitle() {
        setTitle(objectDescription.getDescription());
    }

    @Override
    void setUpToolbar() {
        super.setUpToolbar();

        final View responseButtonBar = LayoutInflater.from(this).inflate(com.orange.ocara.R.layout.audit_object_toolbar, null);
        responseButtonBar.findViewById(com.orange.ocara.R.id.response_ok_button).setVisibility(View.GONE);

        responseButtonBar.findViewById(com.orange.ocara.R.id.response_nok_button).setVisibility(View.GONE);

        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Gravity.LEFT);
        responseButtonBar.setLayoutParams(lp);

        toolbar.addView(responseButtonBar);
    }

     private void updateObjectCharacteristics(ObjectDescription objectDescription) {
        List<ObjectDescription> characteristics = objectDescription.getChildren();
        String characteristicsNames = "";
        ObjectCharacteristicLayout.setVisibility(GONE);
        for (ObjectDescription characteristic : characteristics){
            if(!characteristicsNames.isEmpty()){
                 characteristicsNames += " // ";
            }
            ObjectCharacteristicLayout.setVisibility(VISIBLE);
            characteristicsNames  = characteristicsNames  + characteristic.getDescription();
        }
         textCharacteristic.setText(characteristicsNames);
    }


    private void updateObjectIllustrations(ObjectDescription objectDescription) {
       final List<Illustration> illustrations = objectDescription.getIllustrations();
        if (illustrations.isEmpty()) {
            ObjectIllustrationsLayout.setVisibility(GONE);
        }

        illustrationsCommentsView.removeAllViews();

        final String ObjectName = objectDescription.getName();
        final Uri ObjectIcon = Uri.parse(objectDescription.getIcon().toString());

        for(final Illustration illustration : illustrations) {
            final IllustrationCommentItemView illustrationCommentsView = IllustrationCommentItemView_.build(this);
            illustrationCommentsView.bind(illustration);
           illustrationsCommentsView.addView(illustrationCommentsView);
            illustrationCommentsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ObjectIllustrationCommentListViewClickListener != null) {
                        onIllustrationCommentClicked(ObjectIcon, ObjectName, illustrations, illustration, illustrationCommentsView);
                    }
                }
            });
        }
    }


    private void updateActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setDisplayUseLogoEnabled(true);
        updateTitle();
        updateLogo(Uri.parse(objectDescription.getIcon().toString()));
    }


}
