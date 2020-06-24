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

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;

import com.orange.ocara.R;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.business.service.impl.RuleSetServiceImpl;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.data.cache.model.ResponseModel;
import com.orange.ocara.data.net.model.EquipmentCategoryEntity;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.tools.StringUtils;
import com.orange.ocara.ui.adapter.AuditObjectsAdapter;
import com.orange.ocara.ui.adapter.CategoryPagerAdapter;
import com.orange.ocara.ui.dialog.NotificationDialogBuilder;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.orange.ocara.ui.dialog.SelectAuditObjectCharacteristicsDialogBuilder;
import com.orange.ocara.ui.fragment.BrowseEquipmentsByCategoryFragment;
import com.orange.ocara.ui.routing.DestinationController;
import com.orange.ocara.ui.routing.Navigation;
import com.orange.ocara.ui.tools.RefreshStrategy;
import com.orange.ocara.ui.view.AuditObjectsView;
import com.orange.ocara.ui.view.BadgeView;
import com.orange.ocara.ui.view.PagerSlidingTabStrip;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Activity dedicated to the configuration of an audit's content
 */
@EActivity(R.layout.activity_setup_audit_path)
@OptionsMenu(R.menu.audit_setup_path)
public class SetupAuditPathActivity extends BaseActivityManagingAudit
        implements BrowseEquipmentsByCategoryFragment.OnEquipmentSelectListener, SelectAuditObjectCharacteristicsDialogBuilder.UpdateAuditObjectCharacteristicsListener {

    private static final int MAX_ITEM_MARGIN = 15;

    private static final int BADGE_VERTICAL_MARGIN = 4;

    private static final int USER_WANTS_TO_AUDIT_OBJECT_NOW = 1;

    private static final int USER_WANTS_TO_AUDIT_OBJECT_LATER = 2;

    /**
     * Refresh strategy.
     */
    private static final RefreshStrategy STRATEGY = RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.AUDIT_OBJECT).commentsNeeded(true).build();

    @Bean(DestinationController.class)
    Navigation navController;

    @Extra
    boolean fromListAudit;

    @ViewById(R.id.pager)
    ViewPager viewPager;

    @ViewById(R.id.tabs)
    PagerSlidingTabStrip tabs;

    @ViewById(R.id.audited_objects)
    AuditObjectsView auditedObjects;

    @ViewById(R.id.buttonbar_left_button)
    Button leftButton;

    @ViewById(R.id.buttonbar_right_button)
    Button rightButton;

    @Bean(RuleSetServiceImpl.class)
    RuleSetService mRuleSetService;

    private int selectedIndex = -1;

    private AuditObjectsAdapter auditObjectsAdapter;

    private BadgeView badge;


    @Override
    RefreshStrategy getRefreshStrategy() {
        return STRATEGY;
    }

    @Override
    protected boolean isChildActivity() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (auditObjectsAdapter != null && audit != null) {
            auditRefreshed();
        }
    }

    @Override
    void auditRefreshed() {
        super.auditRefreshed();
        initTitle();
        setUpViewPager();
        setUpAuditedPath();
        auditObjectsAdapter.update(audit.getObjects());

        Timber.d("ActivityMessage=refreshing audit named %s", audit.getName());
        Timber.v("SetupAuditPathActivity selectedIndex %d", selectedIndex);

        // Refresh selected with last one
        if (selectedIndex < 0) {
            if (auditObjectsAdapter.isEmpty()) {
                selectedIndex = 0;
            } else {
                selectedIndex = auditObjectsAdapter.getCount() - 1;
            }
        }
        auditedObjects.selectItemWithoutClick(selectedIndex);

        updateButtonBar();
        badgeNotifierCommentsAudit();
    }

    // @AfterViews
    void initTitle() {
        setTitle(getString(R.string.setup_objects_title) + " - " + getString(R.string.audit_item_name_format, audit.getName(), audit.getVersion()));
    }

    //  @AfterViews
    void setUpViewPager() {
        RulesetEntity ruleSet = mRuleSetService.getRuleSet(audit.getRuleSetRef(), audit.getRuleSetVersion(), false);
        final List<EquipmentCategoryEntity> categories = mRuleSetService.getRulsetObjects(ruleSet);

        Timber.d("ActivityMessage=Retrieving %d categories related to the ruleset named %s (id=%d)", categories.size(), ruleSet.getType(), ruleSet.getId());

        final CategoryPagerAdapter categoriesAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), categories, audit);

        viewPager.setAdapter(categoriesAdapter);
        tabs.setViewPager(viewPager);
    }

    //  @AfterViews
    void setUpAuditedPath() {
        auditObjectsAdapter = new AuditObjectsAdapter(this);
        auditedObjects.setAdapter(auditObjectsAdapter);

        auditedObjects.setItemClickListener((parent, view, position, id) -> {
            selectedIndex = position;
            AuditObjectEntity auditObject = auditObjectsAdapter.getItem(position);
            launchAuditObjectsTest(!auditObject.getResponse().equals(ResponseModel.NO_ANSWER), auditObject);
        });

        registerForContextMenu(auditedObjects.getAuditPath());

        auditedObjects.getAuditPath().getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            float pathWitdh = auditedObjects.getAuditPath().getWidth();
            int auditObjectItemWidth = getResources().getInteger(R.integer.auditObjectItemWidth);
            final BigDecimal pathWitdhDb = BigDecimal.valueOf(pathWitdh);
            final BigDecimal auditObjectItemWidthBg = new BigDecimal(auditObjectItemWidth);
            final BigDecimal result = pathWitdhDb.divide(auditObjectItemWidthBg, BigDecimal.ROUND_HALF_UP);
            int numberAuditedObjects = result.intValue();
            float freeSpace = pathWitdh % auditObjectItemWidth;
            BigDecimal freeSpaceBg = BigDecimal.valueOf(freeSpace);
            BigDecimal numberAuditedObjectsLessOneBg = new BigDecimal(numberAuditedObjects - 1);
            BigDecimal result1 = freeSpaceBg.divide(numberAuditedObjectsLessOneBg, BigDecimal.ROUND_HALF_UP);
            int margin = result1.intValue();
            if (margin > MAX_ITEM_MARGIN) {
                margin = MAX_ITEM_MARGIN;
            }

            Timber.v("auditPath setUpAuditedPath " + pathWitdh + "auditObjectItemWitdh " + auditObjectItemWidth + " freeSpace " + freeSpace + " numberAuditedObjects " + numberAuditedObjects + " margin " + margin);

            auditedObjects.getAuditPath().setItemMargin(margin);
        });
    }

    @AfterViews
    void setUpButtonBar() {
        leftButton.setText(R.string.setup_objects_button_back);
        rightButton.setText(R.string.setup_objects_button_audit);
    }

    @Background
    void createAuditObject(AuditEntity audit, EquipmentEntity objectDescription, List<EquipmentEntity> children) {
        AuditObjectEntity auditObject = modelManager.createAuditObject(audit, objectDescription);
        for (EquipmentEntity child : children) {
            modelManager.createChildAuditObject(auditObject, child);
        }
        Timber.d("ActivityMessage=Created audit object named %s, related to the ruleset named %s", auditObject.getName(), auditObject.getRuleSet().getType());
        auditObject.refresh(getRefreshStrategy());
        auditObjectCreated(auditObject);
    }


    @Background
    @Override
    public void onUpdateAuditObjectChildren(final AuditObjectEntity parent, List<EquipmentEntity> childrenToCreate, List<AuditObjectEntity> childrenToRemove) {
        modelManager.updateAuditObjectChildren(parent, childrenToCreate, childrenToRemove);
        refreshAudit();
    }

    @Override
    public void onLaunchAuditObjectsTestRequested(final AuditObjectEntity parent) {
        launchAuditObjectsTest(true, parent);
    }


    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditObjectCreated(final AuditObjectEntity auditObject) {
        selectedIndex = auditObjectsAdapter.getCount();

        // findAll application preference to know if he wants to audit object now
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int applicationAuditObjectNowPreference = Integer.parseInt(sharedPreferences.getString(getString(com.orange.ocara.R.string.setting_when_test_entries_key), "0"));

        switch (applicationAuditObjectNowPreference) {
            case 0:
                final NotificationDialogBuilder dialogBuilder = new NotificationDialogBuilder(this);
                dialogBuilder
                        .setInfo(getString(R.string.do_you_want_to_test_audit_object_now))
                        .setOption(getString(R.string.remember_test_now_answer))
                        .setCancelable(false)
                        .setTitle(R.string.audit_object_now_title)
                        .setPositiveButton(R.string.test_now, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                askToAuditObjectNowDialogDismiss(true, dialogBuilder.getOptionValue());
                                refreshAudit();
                                launchAuditObjectsTest(false, auditObject);
                            }
                        })
                        .setNegativeButton(R.string.test_later, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                askToAuditObjectNowDialogDismiss(false, dialogBuilder.getOptionValue());
                                refreshAudit();
                            }
                        })
                        .create();

                dialogBuilder.show();
                break;
            case USER_WANTS_TO_AUDIT_OBJECT_NOW:
                refreshAudit();
                launchAuditObjectsTest(false, auditObject);
                break;
            case USER_WANTS_TO_AUDIT_OBJECT_LATER:
                refreshAudit();
                break;
            default:
                break;
        }
    }

    @Background
    void deleteAuditObject(AuditObjectEntity auditObject) {
        modelManager.deleteAuditObject(auditObject);
        selectedIndex = selectedIndex - 1;
        refreshAudit();
    }

    @Background
    void deleteAllAuditObjects() {
        modelManager.deleteAllAuditObjects(audit);
        selectedIndex = -1;
        refreshAudit();
    }

    @Click(R.id.buttonbar_left_button)
    void onLeftButtonClicked() {
        onBackPressed();
    }

    @Click(R.id.buttonbar_right_button)
    void onRightButtonClicked() {
        List<AuditObjectEntity> selectedObjectsList = new ArrayList<>();
        for (int i = 0; i < auditObjectsAdapter.getCount(); i++) {
            AuditObjectEntity auditObject = auditObjectsAdapter.getItem(i);

            if (!auditObject.isAudited()) {
                selectedObjectsList.add(auditObject);
            }
        }
        launchAuditObjectsTest(true, selectedObjectsList.toArray(new AuditObjectEntity[]{}));
    }

    @OptionsItem(R.id.delete_path)
    void onDeletePath() {
        askToDeleteAllAuditObjects();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final AuditObjectEntity auditObject = auditObjectsAdapter.getItem(info.position);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.orange.ocara.R.menu.audit_objects_context_menu, menu);
        final EquipmentEntity objectDescription = mRuleSetService.getObjectDescriptionFromRef(auditObject.getAudit().getRuleSetRef(), auditObject.getAudit().getRuleSetVersion(), auditObject.getObjectDescriptionId());

        if (objectDescription.getSubObject().isEmpty()) {
            menu.findItem(R.id.action_object_characteristics).setVisible(false);
        } else {
            menu.findItem(R.id.action_object_characteristics).setVisible(true);
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final AuditObjectEntity auditObject = auditObjectsAdapter.getItem(info.position);

        int i = item.getItemId();
        if (i == com.orange.ocara.R.id.action_object_update) {
            updateAuditObject(auditObject);
            return true;
        } else if (i == com.orange.ocara.R.id.action_object_comment) {
            commentAuditObject(auditObject);
            return true;
        } else if (i == com.orange.ocara.R.id.action_object_characteristics) {
            SelectAuditObjectCharacteristicsDialogBuilder.showCharacteriscticsAuditObject(this, auditObject, mRuleSetService, this);
            return true;
        } else if (i == com.orange.ocara.R.id.action_object_detail) {
            showDetailsObject(auditObject);
            return true;
        } else if (i == com.orange.ocara.R.id.action_object_remove) {
            askToDeleteAuditObject(auditObject);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }

    }

    private void showDetailsObject(AuditObjectEntity auditObject) {
        DetailsActivity_.intent(this)
                .auditId(auditId)
                .auditObjectId(auditObject.getId())
                .startForResult(0);
    }


    private void updateAuditObject(final AuditObjectEntity auditObject) {

        // Use an EditText view to findAll user input.
        final EditText input = new EditText(this);
        input.setText(auditObject.getName());

        AlertDialog updateDialog = new OcaraDialogBuilder(this)
                .setTitle(com.orange.ocara.R.string.audit_object_update_object_title) // title
                .setView(input)
                .setPositiveButton(R.string.audit_object_update_object_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();

                        if (StringUtils.isNotBlank(name)) {
                            updateAuditObjectName(auditObject, name);
                        }
                    }
                })
                .setNegativeButton(com.orange.ocara.R.string.action_cancel, null)
                .create();

        updateDialog.show();
    }

    @Background
    void updateAuditObjectName(AuditObjectEntity auditObject, String name) {
        auditObject.setName(name);
        auditObject.save();
        refreshAudit();
    }

    private void commentAuditObject(AuditObjectEntity auditObject) {
        String attachmentDirectory = modelManager.getAttachmentDirectory(auditId).getAbsolutePath();
        ListAuditObjectCommentActivity_.intent(this)
                .auditObjectId(auditObject.getId())
                .attachmentDirectory(attachmentDirectory)
                .startForResult(0);
    }

    private void askToDeleteAuditObject(final AuditObjectEntity auditObject) {
        AlertDialog confirmDialog = new OcaraDialogBuilder(this)
                .setTitle(com.orange.ocara.R.string.audit_object_remove_object_title) // title
                .setMessage(com.orange.ocara.R.string.audit_object_remove_object_message) // message
                .setPositiveButton(com.orange.ocara.R.string.audit_object_remove_object_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAuditObject(auditObject);
                    }
                })
                .setNegativeButton(com.orange.ocara.R.string.action_cancel, null)
                .setCancelable(true)
                .create();

        confirmDialog.show();
    }

    private void askToDeleteAllAuditObjects() {

        AlertDialog confirmDialog = new OcaraDialogBuilder(this)
                .setTitle(com.orange.ocara.R.string.audit_object_remove_all_objects_title) // title
                .setMessage(com.orange.ocara.R.string.audit_object_remove_all_objects_message) // message
                .setPositiveButton(com.orange.ocara.R.string.audit_object_remove_all_objects_button, (dialog, which) -> deleteAllAuditObjects())
                .setNegativeButton(com.orange.ocara.R.string.action_cancel, null)
                .setCancelable(true)
                .create();

        confirmDialog.show();
    }

    @OptionsItem(R.id.action_update)
    void onUpdateMenuItem() {
        navController.navigateToAuditEditView(this, auditId, 0);
    }

    @OptionsItem(R.id.action_report)
    void onReportMenuItem() {
        ResultAuditActivity_.intent(this).auditId(auditId).start();
    }

    @OptionsItem(R.id.action_comment)
    void onCommentMenuItem() {
        String attachmentDirectory = modelManager.getAttachmentDirectory(auditId).getAbsolutePath();
        ListAuditCommentActivity_
                .intent(this)
                .auditId(auditId)
                .attachmentDirectory(attachmentDirectory)
                .startForResult(0);

    }

    /**
     * creates and displays a {@link BadgeView}
     */
    @UiThread(propagation = UiThread.Propagation.REUSE)
    public void badgeNotifierCommentsAudit() {
        View target = findViewById(com.orange.ocara.R.id.action_comment);
        if (target == null) {
            return;
        }
        if (badge != null) {
            badge.hide();
        }
        badge = new BadgeView(this, target);
        List<CommentEntity> nbrComments = audit.getComments();
        if (!audit.getComments().isEmpty()) {
            badge.setText(String.valueOf(nbrComments.size()));
            badge.setBadgeMargin(0, BADGE_VERTICAL_MARGIN);
            badge.show();
        }
    }

    @Override
    @Background
    public void onEquipmentSelected(final EquipmentEntity equipment) {

        // ask user to select children subObject (also named characteristic equipments)
        List<EquipmentEntity> children = new ArrayList<>();
        for (String ref : equipment.getSubObject()) {
            final EquipmentEntity subEquipment = mRuleSetService.getObjectDescriptionFromRef(audit.getRuleSetRef(), audit.getRuleSetVersion(), ref);
            children.add(subEquipment);
        }

        if (children.isEmpty()) {
            createAuditObject(audit, equipment, children);
        } else {
            // dialog to select characteristics
            triggerAlert(equipment);
        }
    }

    @UiThread
    void triggerAlert(final EquipmentEntity equipment) {
        final SelectAuditObjectCharacteristicsDialogBuilder selectCharacteristicsBuilder = new SelectAuditObjectCharacteristicsDialogBuilder(this, equipment, mRuleSetService);
        selectCharacteristicsBuilder
                .setTitle(R.string.audit_object_characteristics_title)
                .setCancelable(false)
                .setPositiveButton(R.string.audit_object_characteristics_validate, (dialog, which) -> {
                    List<EquipmentEntity> selections = selectCharacteristicsBuilder.getSelectedCharacteristics();
                    createAuditObject(audit, equipment, selections);
                })
                .create().show();
    }


    @UiThread(propagation = UiThread.Propagation.REUSE)
    void updateButtonBar() {
        updateRightButton();
    }

    private void updateRightButton() {

        // If no subObject button should not appear
        boolean visible = !auditObjectsAdapter.isEmpty();
        rightButton.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);

        // Button should be enabled if one or more object are not tested yet
        boolean enabled = false;
        for (int i = 0; i < auditObjectsAdapter.getCount(); i++) {
            AuditObjectEntity auditObject = auditObjectsAdapter.getItem(i);

            if (!auditObject.isAudited()) {
                enabled = true;
                break;
            }
        }
        rightButton.setEnabled(enabled);
    }

    private void askToAuditObjectNowDialogDismiss(boolean userWantsToAuditObjectNow, boolean rememberUserChoice) {
        if (rememberUserChoice) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (userWantsToAuditObjectNow) {
                editor.putString(getString(R.string.setting_when_test_entries_key), "1");

            } else {
                editor.putString(getString(R.string.setting_when_test_entries_key), "2");
            }
            editor.apply();


        }
    }

    @Override
    public void onBackPressed() {
        if (fromListAudit) {
            ListAuditActivity_.intent(this).start();
        } else {
            CreateAuditActivity_.intent(this).start();
        }
        super.onBackPressed();
    }
}
