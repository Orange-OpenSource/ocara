/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.orange.ocara.conf.OcaraConfiguration;
import com.orange.ocara.model.Audit;
import com.orange.ocara.model.AuditObject;
import com.orange.ocara.model.Comment;
import com.orange.ocara.model.RefreshStrategy;
import com.orange.ocara.modelStatic.Category;
import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.modelStatic.RuleSet;
import com.orange.ocara.ui.adapter.AuditObjectsAdapter;
import com.orange.ocara.ui.dialog.NotificationDialogBuilder;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.orange.ocara.ui.dialog.SelectAuditObjectCharacteristicsDialogBuilder;
import com.orange.ocara.ui.fragment.ObjectsByCategoryFragment;
import com.orange.ocara.ui.fragment.ObjectsByCategoryFragment_;
import com.orange.ocara.ui.view.AuditObjectsView;
import com.orange.ocara.ui.view.BadgeView;
import com.orange.ocara.ui.view.PagerSlidingTabStrip;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

@EActivity(resName="activity_setup_audit_path")
@OptionsMenu(resName="audit_setup_path")
public class SetupAuditPathActivity extends BaseActivityManagingAudit implements ObjectsByCategoryFragment.OnObjectSelectionListener, SelectAuditObjectCharacteristicsDialogBuilder.UpdateAuditObjectCharacteristicsListener {

    /**
     * Refresh strategy.
     */
    private static final RefreshStrategy STRATEGY = RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.AUDIT_OBJECT).commentsNeeded(true).build();


    @ViewById(resName="pager")
    ViewPager viewPager;
    @ViewById(resName="tabs")
    PagerSlidingTabStrip tabs;
    @ViewById(resName="audited_objects")
    AuditObjectsView auditedObjects;

    @ViewById(resName="buttonbar_left_button")
    Button leftButton;
    @ViewById(resName="buttonbar_right_button")
    Button rightButton;

    private int selectedIndex = -1;

    private CategoryPagerAdapter categoryPagerAdapter;
    private AuditObjectsAdapter auditObjectsAdapter;
    private BadgeView badge;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
             setTitle(com.orange.ocara.R.string.setup_objects_title);
    }

    @Override
    RefreshStrategy getRefreshStrategy() {
        return STRATEGY;
    }

    @Override
    protected boolean isChildActivity() {
        return true;
    }


    @Override
    void auditRefreshed() {
        super.auditRefreshed();
        auditObjectsAdapter.update(audit.getObjects());

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

    @AfterViews
    void setUpViewPager() {
        RuleSet ruleSet = modelManager.getRuleSet(audit.getRuleSetId());
        categoryPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), ruleSet.getCategories());

        viewPager.setAdapter(categoryPagerAdapter);
        tabs.setViewPager(viewPager);
    }

    @AfterViews
    void setUpAuditedPath() {
        auditObjectsAdapter = new AuditObjectsAdapter(this);
        auditedObjects.setAdapter(auditObjectsAdapter);

        auditedObjects.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex = position;

                AuditObject auditObject = auditObjectsAdapter.getItem(position);
                launchAuditObjectsTest(auditObject);
            }
        });

        registerForContextMenu(auditedObjects.getAuditPath());


        auditedObjects.getAuditPath().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float pathWitdh = auditedObjects.getAuditPath().getWidth();
                int auditObjectItemWidth = getResources().getInteger(com.orange.ocara.R.integer.auditObjectItemWidth);
                int numberAuditedObjects = (int) (pathWitdh / auditObjectItemWidth);
                float freeSpace = pathWitdh % auditObjectItemWidth;
                int margin = (int) (freeSpace / (numberAuditedObjects - 1));
                if (margin > 15) {
                    margin = 15;
                }

                Timber.v("auditPath setUpAuditedPath " + pathWitdh + "auditObjectItemWitdh " + auditObjectItemWidth + " freeSpace " + freeSpace + " numberAuditedObjects " + numberAuditedObjects + " margin " + margin);


                auditedObjects.getAuditPath().setItemMargin(margin);

            }
        });
    }

    @AfterViews
    void setUpButtonBar() {
        leftButton.setText(com.orange.ocara.R.string.setup_objects_button_back);
        rightButton.setText(com.orange.ocara.R.string.setup_objects_button_audit);
    }

    @Background
    void createAuditObject(Audit audit, ObjectDescription objectDescription, List<ObjectDescription> children) {
        AuditObject auditObject = modelManager.createAuditObject(audit, objectDescription);
        for (ObjectDescription child : children) {
            modelManager.createChildAuditObject(auditObject, child);
        }
        auditObject.refresh(getRefreshStrategy());
        auditObjectCreated(auditObject);
    }


    @Background
    @Override
    public void onUpdateAuditObjectChildren(final AuditObject parent, List<ObjectDescription> childrenToCreate, List<AuditObject> childrenToRemove) {
        modelManager.updateAuditObjectChildren(parent, childrenToCreate, childrenToRemove);
        refreshAudit();
    }

    @Override
    public void onLaunchAuditObjectsTestRequested(final AuditObject parent) {
        launchAuditObjectsTest(parent);
    }



    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditObjectCreated(final AuditObject auditObject) {
        selectedIndex = auditObjectsAdapter.getCount();

        // get application preference to know if he wants to audit object now

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int applicationAuditObjectNowPreference = Integer.parseInt(sharedPreferences.getString(getString(com.orange.ocara.R.string.setting_when_test_entries_key), "0"));


        switch (applicationAuditObjectNowPreference) {
            case 0:
                final NotificationDialogBuilder dialogBuilder = new NotificationDialogBuilder(this);
                dialogBuilder
                        .setInfo(getString(com.orange.ocara.R.string.do_you_want_to_test_audit_object_now))
                        .setOption(getString(com.orange.ocara.R.string.remember_test_now_answer))
                        .setCancelable(false)
                        .setTitle(com.orange.ocara.R.string.audit_object_now_title)
                        .setPositiveButton(com.orange.ocara.R.string.test_now, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                askToAuditObjectNowDialogDismiss(true, dialogBuilder.getOptionValue());
                                auditObject.refresh(getRefreshStrategy());
                                launchAuditObjectsTest(auditObject);
                            }
                        })
                        .setNegativeButton(com.orange.ocara.R.string.test_later, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                askToAuditObjectNowDialogDismiss(false, dialogBuilder.getOptionValue());
                                refreshAudit();
                            }
                        })
                        .create();

                dialogBuilder.show();
                break;

            case 1:
                launchAuditObjectsTest(auditObject);
                break;

            case 2:
                refreshAudit();
            default:
                break;
        }
    }

    @Background
    void deleteAuditObject(AuditObject auditObject) {
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

    @Click(resName="buttonbar_left_button")
    void onLeftButtonClicked() {
        onBackPressed();
    }

    @Click(resName="buttonbar_right_button")
    void onRightButtonClicked() {
        List<AuditObject> selectedObjectsList = new ArrayList<AuditObject>();
        for (int i = 0; i < auditObjectsAdapter.getCount(); i++) {
            AuditObject auditObject = auditObjectsAdapter.getItem(i);

            if (!auditObject.isAudited()) {
                selectedObjectsList.add(auditObject);
            }
        }
        launchAuditObjectsTest(selectedObjectsList.toArray(new AuditObject[]{}));
    }

    @OptionsItem(resName="delete_path")
    void onDeletePath() {
        askToDeleteAllAuditObjects();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean b = super.onCreateOptionsMenu(menu);
        this.menu = menu;
        new Handler().post(new Runnable() {
                               @java.lang.Override
                               public void run() {
                                   badgeNotifierCommentsAudit();
                               }
                           }
        );

        return b;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Handler handler = new Handler();


        handler.postDelayed(new Runnable() {
                                @java.lang.Override
                                public void run() {

                                    badgeNotifierCommentsAudit();
                                }
                            }, 200
        );
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final AuditObject auditObject = auditObjectsAdapter.getItem(info.position);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.orange.ocara.R.menu.audit_objects_context_menu, menu);


        if (auditObject.getObjectDescription().getChildren().isEmpty()) {
            menu.findItem(com.orange.ocara.R.id.action_object_characteristics).setVisible(false);
        } else {
            menu.findItem(com.orange.ocara.R.id.action_object_characteristics).setVisible(true);
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final AuditObject auditObject = auditObjectsAdapter.getItem(info.position);

        int i = item.getItemId();
        if (i == com.orange.ocara.R.id.action_object_update) {
            updateAuditObject(auditObject);
            return true;
        } else if (i == com.orange.ocara.R.id.action_object_comment) {
            commentAuditObject(auditObject);
            return true;
        } else if (i == com.orange.ocara.R.id.action_object_characteristics) {
            SelectAuditObjectCharacteristicsDialogBuilder.showCharacteriscticsAuditObject(this, auditObject, this);
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

    private void showDetailsObject(AuditObject auditObject) {
        DetailsActivity_.intent(this)
                .auditId(auditId)
                .auditObjectId(auditObject.getId())
                .startForResult(0);

    }


    private void updateAuditObject(final AuditObject auditObject) {

        // Use an EditText view to get user input.
        final EditText input = new EditText(this);
        input.setText(auditObject.getName());

        AlertDialog updateDialog = new OcaraDialogBuilder(this)
                .setTitle(com.orange.ocara.R.string.audit_object_update_object_title) // title
                .setView(input)
                .setPositiveButton(com.orange.ocara.R.string.audit_object_update_object_button, new DialogInterface.OnClickListener() {
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
    void updateAuditObjectName(AuditObject auditObject, String name) {
        auditObject.setName(name);
        auditObject.save();
        refreshAudit();
    }

    private void commentAuditObject(AuditObject auditObject) {
        String attachmentDirectory = modelManager.getAttachmentDirectory(auditId).getAbsolutePath();
        ListAuditObjectCommentActivity_.intent(this)
                .auditObjectId(auditObject.getId())
                .attachmentDirectory(attachmentDirectory)
                .startForResult(0);
    }

    private void askToDeleteAuditObject(final AuditObject auditObject) {

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
                .setPositiveButton(com.orange.ocara.R.string.audit_object_remove_all_objects_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllAuditObjects();
                    }
                })
                .setNegativeButton(com.orange.ocara.R.string.action_cancel, null)
                .setCancelable(true)
                .create();

        confirmDialog.show();
    }

    @OptionsItem(resName="action_update")
    void onUpdateMenuItem() {
        OcaraConfiguration.get().getUpdateAudit().updateAudit(this,auditId);
    }

    @OptionsItem(resName="action_report")
    void onReportMenuItem() {
        ResultAuditActivity_.intent(this).auditId(auditId).start();
    }

    @OptionsItem(resName="action_comment")
    void onCommentMenuItem() {
        String attachmentDirectory = modelManager.getAttachmentDirectory(auditId).getAbsolutePath();
             ListAuditCommentActivity_.intent(this)
                .auditId(auditId).attachmentDirectory(attachmentDirectory)
                .startForResult(0);

    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    public  void badgeNotifierCommentsAudit(){
        View target = findViewById(com.orange.ocara.R.id.action_comment);
        if(target ==null  ){
            return;
        }
        if (badge != null) {
            badge.hide();
        }
        badge = new BadgeView(this, target);
         List<Comment> nbrComments=audit.getComments();
        if (!audit.getComments().isEmpty()) {
           badge.setText(String.valueOf(nbrComments.size()));
            badge.setBadgeMargin(0, 4);
            badge.show();
        }
    }



    @Override
    public void onObjectSelected(final ObjectDescription objectDescription, Category category) {

        // ask user to select children objects


        List<ObjectDescription> children = objectDescription.getChildren();
        if (children == null || children.size() == 0) {
            List<ObjectDescription> empty = Collections.emptyList();
            createAuditObject(audit, objectDescription, empty);
        } else {
            // dialog to select characteristics

            final SelectAuditObjectCharacteristicsDialogBuilder selectCharacteristicsBuilder = new SelectAuditObjectCharacteristicsDialogBuilder(this, objectDescription);
            AlertDialog selectCharacteristicsDialog = selectCharacteristicsBuilder
                    .setTitle(com.orange.ocara.R.string.audit_object_characteristics_title)
                    .setCancelable(false)
                    .setPositiveButton(com.orange.ocara.R.string.audit_object_characteristics_validate, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<ObjectDescription> selections = selectCharacteristicsBuilder.getSelectedCharacteristics();
                            createAuditObject(audit, objectDescription, selections);
                        }
                    })
                    .create();

            selectCharacteristicsDialog.show();
        }
    }


    @UiThread(propagation = UiThread.Propagation.REUSE)
    void updateButtonBar() {
        updateRightButton();
    }

    private void updateRightButton() {

        // If no objects button should not appear
        boolean visible = !auditObjectsAdapter.isEmpty();
        rightButton.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);

        // Button should be enabled if one or more object are not tested yet
        boolean enabled = false;
        for (int i = 0; i < auditObjectsAdapter.getCount(); i++) {
            AuditObject auditObject = auditObjectsAdapter.getItem(i);

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
                editor.putString(getString(com.orange.ocara.R.string.setting_when_test_entries_key), "1");

            } else {
                editor.putString(getString(com.orange.ocara.R.string.setting_when_test_entries_key), "2");
            }
            editor.commit();


        }
    }

    /**
     * Categories pager adapter.
     */
    private class CategoryPagerAdapter extends FragmentPagerAdapter {
        private final List<Category> categories = new ArrayList<Category>();

        CategoryPagerAdapter(FragmentManager fm, List<Category> categories) {
            super(fm);

            this.categories.addAll(categories);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return categories.size();
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {

            ObjectsByCategoryFragment fragment = ObjectsByCategoryFragment_.builder().build();
            String ruleSetId = audit.getRuleSetId();
            Long auditId = audit.getId();
            fragment.setAuditId(auditId);
            fragment.setCategory(categories.get(position));
            fragment.setRuleSetId(ruleSetId);

            return fragment;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return categories.get(position).getName();
        }
    }


}
