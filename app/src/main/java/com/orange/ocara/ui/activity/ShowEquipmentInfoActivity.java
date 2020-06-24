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

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.orange.ocara.R;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.business.service.impl.RuleSetServiceImpl;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.data.net.model.IllustrationEntity;
import com.orange.ocara.ui.view.IllustrationCommentItemView;
import com.orange.ocara.ui.view.IllustrationCommentItemView_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.orange.ocara.R.id.illustrations;

/** displays information about an equipment */
@EActivity(R.layout.activity_details_in_grid)
@OptionsMenu(R.menu.object_details)
/*package*/ class ShowEquipmentInfoActivity extends BaseActivityManagingAudit {

    @Extra
    String objectDescriptionId;

    EquipmentEntity equipment;

    @ViewById(R.id.scrollview_container)
    ScrollView scrollContainer;

    @ViewById(R.id.title)
    TextView customTitle;

    @ViewById(R.id.subtitle)
    TextView customSubtitle;

    @ViewById(R.id.object_definitions_layout)
    ViewGroup ObjectDefinitionLayout;

    @ViewById(R.id.text_definition)
    TextView textDefinition;

    @ViewById(R.id.title_characteristic)
    TextView titleCharacteristic;

    @ViewById(illustrations)
    ImageView illustration;

    @ViewById(R.id.text_characteristic)
    TextView textCharacteristic;

    @ViewById(R.id.object_characteristic_layout)
    ViewGroup ObjectCharacteristicLayout;

    @ViewById(R.id.object_illustrations_layout)
    ViewGroup ObjectIllustrationsLayout;

    @ViewById(R.id.illustration_comments_section)
    ViewGroup illustrationsCommentsView;

    @Bean(RuleSetServiceImpl.class)
    RuleSetService mRuleSetService;

    private final AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<IllustrationEntity> illustrations = new ArrayList<>();
            for (String ref : equipment.getIllustration()) {
                illustrations.add(mRuleSetService.getIllutrationFormRef(equipment.getRuleSetDetail().getReference(), Integer.valueOf(equipment.getRuleSetDetail().getVersion()),ref));
            }

            IllustrationEntity illustration = (IllustrationEntity) parent.getAdapter().getItem(position);
            final String objectDescriptionName = equipment.getName();
            onIllustrationCommentClicked(equipment.getIcon(), objectDescriptionName, illustrations, illustration, view);
        }
    };

    /** TODO BrowseIllustrationsActivity is now deprecated. Should be replaced with ExplanationsCarouselActivity */
    private void showIllustration(String iconName, String name, List<IllustrationEntity> illustrationList, IllustrationEntity illustration) {
        int selectedIndex = 0;
        String[] titles = new String[illustrationList.size()];
        String[] comments = new String[illustrationList.size()];
        String[] images = new String[illustrationList.size()];

        for (int i = 0; i < illustrationList.size(); i++) {
            if (illustrationList.get(i).equals(illustration)) {
                selectedIndex = i;
            }

            if (illustrationList.get(i).getComment() != null) {
                titles[i] = illustrationList.get(i).getComment();
            } else {
                titles[i] = getString(com.orange.ocara.R.string.illustration_activity_auditobject_title, i + 1, name);
            }
            comments[i] = illustrationList.get(i).getComment();

            if (illustrationList.get(i) != null && illustrationList.get(i).getImage() != null && !illustrationList.get(i).getImage().isEmpty()) {
                images[i] = illustrationList.get(i).getImage();
            }
        }

        BrowseIllustrationsActivity_
                .intent(ShowEquipmentInfoActivity.this)
                .selectedIndex(selectedIndex)
                .iconName(iconName)
                .titles(titles)
                .comments(comments)
                .images(images)
                .start();

    }

    private void onIllustrationCommentClicked(String iconName, String name, List<IllustrationEntity> illustrations, IllustrationEntity illustration, View commentView) {
        showIllustration(iconName, name, illustrations, illustration);
    }


    @AfterViews
    void hideLogo() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayUseLogoEnabled(false);
    }

    @Override
    @Background
    void auditRefreshed() {
        super.auditRefreshed();
        if (audit == null) {
            audit = modelManager.getAudit(auditId);
        }

        equipment = mRuleSetService.getObjectDescriptionFromRef(audit.getRuleSetRef(), audit.getRuleSetVersion(), objectDescriptionId);
        updateObjectDefinitions(equipment);
        updateObjectCharacteristics(equipment);
        updateObjectIllustrations(equipment);
        updateActionBar();
    }


    @OptionsItem(R.id.action_rules)
    void showRuleSet() {
        if (audit != null) {
            BrowseRulesetsActivity_
                    .intent(this)
                    .rulesetReference(audit.getRuleSetRef())
                    .ruleSetVersion(audit.getRuleSetVersion())
                    .defaultEquipmentReference(objectDescriptionId)
                    .start();
        }
    }

    @UiThread
    void updateObjectDefinitions(EquipmentEntity objectDescription) {
        if (objectDescription != null) {
            String definitions = objectDescription.getDefinition();

            ObjectDefinitionLayout.setVisibility(GONE);
            if (!definitions.isEmpty()) {
                ObjectDefinitionLayout.setVisibility(VISIBLE);
                textDefinition.setText(definitions);
            }
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
        setTitle(equipment.getName());
    }

    @Override
    void setUpToolbar() {
        super.setUpToolbar();

        final View responseButtonBar = LayoutInflater.from(this).inflate(com.orange.ocara.R.layout.audit_object_toolbar, null);
        responseButtonBar.findViewById(com.orange.ocara.R.id.response_ok_button).setVisibility(View.GONE);
        responseButtonBar.findViewById(R.id.response_no_answer_button).setVisibility(GONE);
        responseButtonBar.findViewById(com.orange.ocara.R.id.response_nok_button).setVisibility(View.GONE);

        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Gravity.LEFT);
        responseButtonBar.setLayoutParams(lp);

        toolbar.addView(responseButtonBar);
    }

    @UiThread
    void updateObjectCharacteristics(EquipmentEntity objectDescription) {
        List<EquipmentEntity> characteristics = new ArrayList<>();
        if (objectDescription != null && objectDescription.getSubObject() != null) {
            for (String ref : objectDescription.getSubObject()) {
                characteristics.add(mRuleSetService.getObjectDescriptionFromRef(audit.getRuleSetRef(), audit.getRuleSetVersion(), ref));
            }
        }

        String characteristicsNames = "";
        ObjectCharacteristicLayout.setVisibility(GONE);
        for (EquipmentEntity characteristic : characteristics) {
            if (!characteristicsNames.isEmpty()) {
                characteristicsNames += " // ";
            }
            ObjectCharacteristicLayout.setVisibility(VISIBLE);
            characteristicsNames = characteristicsNames + characteristic.getName();
        }
        textCharacteristic.setText(characteristicsNames);
    }


    @UiThread
    void updateObjectIllustrations(EquipmentEntity objectDescription) {

        final List<IllustrationEntity> illustrations = new ArrayList<>();
        for (String ref : objectDescription.getIllustration()) {
            illustrations.add(mRuleSetService.getIllutrationFormRef(objectDescription.getRuleSetDetail().getReference(), Integer.valueOf(objectDescription.getRuleSetDetail().getVersion()),ref));
        }
        if (illustrations.isEmpty()) {
            ObjectIllustrationsLayout.setVisibility(GONE);
        }

        illustrationsCommentsView.removeAllViews();

        final String ObjectName = objectDescription.getName();
        final String ObjectIcon = objectDescription.getIcon();

        for (final IllustrationEntity illustration : illustrations) {
            final IllustrationCommentItemView illustrationCommentsView = IllustrationCommentItemView_.build(this);
            illustrationCommentsView.bind(illustration);
            illustrationsCommentsView.addView(illustrationCommentsView);
            illustrationCommentsView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    onIllustrationCommentClicked(ObjectIcon, ObjectName, illustrations, illustration, illustrationCommentsView);
                }
            });
        }
    }


    @UiThread
    void updateActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setDisplayUseLogoEnabled(true);
        updateTitle();
        updateLogo(equipment.getIcon());
    }


}
