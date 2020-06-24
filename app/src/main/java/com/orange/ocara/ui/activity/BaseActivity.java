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

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.orange.ocara.BuildConfig;
import com.orange.ocara.R;
import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.CheckTermsOfUseTask;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.ui.TermsCheckingUiConfig;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.orange.ocara.ui.fragment.HelpDisplayFragment_;
import com.orange.ocara.ui.fragment.TermsOfUseAcceptanceFragment_;
import com.orange.ocara.ui.fragment.TermsOfUseReadingActivity_;
import com.orange.ocara.ui.routing.DestinationController;
import com.orange.ocara.ui.routing.Navigation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import lombok.RequiredArgsConstructor;
import timber.log.Timber;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.orange.ocara.business.interactor.CheckTermsOfUseTask.CheckTermsOfUseResponse;
import static com.orange.ocara.ui.contract.TermsOfUseDisplayContract.TermsOfUseCheckingUserActionsListener;
import static com.orange.ocara.ui.contract.TermsOfUseDisplayContract.TermsOfUseDisplayView;
import static timber.log.Timber.d;
import static timber.log.Timber.e;
import static timber.log.Timber.v;

@EActivity
abstract class BaseActivity extends AppCompatActivity implements TermsOfUseDisplayView {

    public static final int NEW_AUDIT = 0;
    public static final int ALL_AUDITS = 1;
    public static final int ALL_RULESETS = 2;
    public static final int HELP = 3;
    public static final int PERMISSION_REQUEST = 10;
    private static final int[] TITLE_RES_IDS = new int[]{R.string.navigation_item_new_audit, R.string.navigation_item_list_audit, R.string.navigation_item_show_ruleset, R.string.navigation_item_show_help,};
    private static final int[] ICON_RES_IDS = new int[]{R.drawable.ic_new, R.drawable.ic_list, R.drawable.ic_ruleset, R.drawable.ic_help};

    @Bean(DestinationController.class)
    Navigation navController;

    /**
     * Comment list adapter.
     */
    private final BaseAdapter navigationItemAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return TITLE_RES_IDS.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.navigation_item, parent, false);
            }

            TextView text = convertView.findViewById(R.id.item_text);

            text.setCompoundDrawablesWithIntrinsicBounds(ICON_RES_IDS[position], 0, 0, 0);
            text.setText(TITLE_RES_IDS[position]);

            return convertView;
        }
    };
    protected ActionBarDrawerToggle drawerToggle;
    protected Toolbar toolbar;
    protected ProgressBar progress;
    protected DrawerLayout drawerLayout;
    private final AdapterView.OnItemClickListener navigationItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case NEW_AUDIT:
                    onNewAuditItemClicked();
                    break;
                case ALL_AUDITS:
                    onAllAuditItemClicked();
                    break;
                case ALL_RULESETS:
                    onAllRuleSetItemClicked();
                    break;
                case HELP:
                    onShowGlobalHelpView();
                    break;
                default:
                    e("Unknown navigation item (position=%d)", position);
                    break;
            }
            drawerLayout.closeDrawers();
        }
    };
    protected ListView drawerList;

    protected FrameLayout contentLayout;


    // ------------------------------------------------------------------------------------------ //
    // ----------------------------------      LIFECYCLE        --------------------------------- //
    // ------------------------------------------------------------------------------------------ //
    @Extra("mustExitApplication")
    boolean mustExitApplication = false;
    private AlertDialog errorDialog = null;

    protected void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean b = super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);

        return b;
    }

    // ------------------------------------------------------------------------------------------ //
    // -------------------------------      NAVIGATION MENU       ------------------------------- //
    // ------------------------------------------------------------------------------------------ //

    @Override
    public void setContentView(int layoutResID) {
        checkPermissions();

        View fullLayout = getLayoutInflater().inflate(R.layout.activity_base, null);

        this.drawerLayout = fullLayout.findViewById(R.id.drawer_layout);
        this.contentLayout = drawerLayout.findViewById(R.id.content);

        this.drawerList = drawerLayout.findViewById(R.id.navigation_menu);
        setUpDrawerList();

        this.toolbar = fullLayout.findViewById(R.id.toolbar);
        setUpToolbar();

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_menu_open,
                R.string.navigation_menu_close
        );
        drawerLayout.setDrawerListener(drawerToggle);

        setSupportActionBar(toolbar);
        setUpActionBar();

        this.progress = toolbar.findViewById(R.id.progress);

        getLayoutInflater().inflate(layoutResID, contentLayout, true);
        super.setContentView(fullLayout);

    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
            }, PERMISSION_REQUEST);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            if (drawerToggle.isDrawerIndicatorEnabled()) {
                return drawerToggle.onOptionsItemSelected(item);
            } else {
                onBackPressed();
                return true;
            }
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * @return true if the activity is a child activity, false if it is a root. By default always
     * consider activity as child
     */
    protected boolean isChildActivity() {
        return true;
    }

    /**
     * To setup the drawer list.
     */
    void setUpDrawerList() {
        drawerList.setAdapter(navigationItemAdapter);
        drawerList.setOnItemClickListener(navigationItemClickListener);
    }

    /**
     * New audit item clicked.
     */
    protected void onNewAuditItemClicked() {
        navController.navigateToHome(this);
    }

    /**
     * All audits item clicked.
     */
    protected void onAllAuditItemClicked() {
        ListAuditActivity_
                .intent(this)
                .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .start();
    }

    /**
     * All RuleSet item clicked.
     */
    protected void onAllRuleSetItemClicked() {
        BrowseRulesetsActivity_
                .intent(this)
                .start();
    }

    /**
     * Show help item clicked.
     */
    protected void showHelp(String pdfName) {

        String locale = Locale.getDefault().getLanguage();
        String folder;
        File externalCacheDir = getExternalCacheDir();
        if (locale.equals("en")) {
            folder = "pdf/";
        } else {
            folder = "pdf-" + locale + "/";
        }
        v("ext " + externalCacheDir.getAbsolutePath() + " locale " + locale + " folder " + folder);

        try (InputStream in = getAssets().open(folder + pdfName)) {

            File file = new File(externalCacheDir, pdfName);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

            copyFile(in, out);
            in.close();
            out.flush();
            out.close();

            Uri uri;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && file.exists()) {
                Timber.i("Message=Viewing a pdf file;FileAbsolutePath=%s;IsFile=%b;IsReadable=%b", file.getAbsolutePath(), file.isFile(), file.canRead());
                uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
                intent.setDataAndType(uri, "application/pdf");

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                uri = Uri.parse("file://" + externalCacheDir + File.separator + pdfName);
                intent.setDataAndType(uri, "application/pdf");
                startActivity(intent);
            } else {
                Toast.makeText(this.getApplicationContext(), "The file does not exist... ", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e(e, "Error message");
        }
    }

    // ------------------------------------------------------------------------------------------ //
    // ---------------------------------      ACTION BAR       ---------------------------------- //
    // ------------------------------------------------------------------------------------------ //

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     * To setup the toolbar (old actionbar)
     */
    void setUpToolbar() {
    }

    /**
     * To setUp the action bar.
     */
    void setUpActionBar() {

        if (isChildActivity()) {
            // Set home as up
            drawerToggle.setDrawerIndicatorEnabled(false);
            drawerToggle.setHomeAsUpIndicator(getDrawerToggleDelegate().getThemeUpIndicator());
        }

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setLogo(R.drawable.ic_app);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeButtonEnabled(true);
    }

    /**
     * To update the action bar logo from an Uri.
     *
     * @param iconName logo name
     */
    void updateLogo(String iconName) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                getSupportActionBar().setLogo(new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                // no-op
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // no-op
            }
        };
        final int maxSize = getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_height_material);
        final String path = getExternalCacheDir() + File.separator + iconName;
        File icon = new File(path);
        Picasso.with(this).load(icon).placeholder(android.R.color.black).resize(maxSize, maxSize).into(target);
    }

    // ------------------------------------------------------------------------------------------ //
    // ---------------------------------      MENU ITEMS       ---------------------------------- //
    // ------------------------------------------------------------------------------------------ //
    @OptionsItem(R.id.action_about)
    void onAboutMenuItem() {
        AboutActivity_.intent(this).start();
    }

    /**
     * opens the help document of the app at the page related to the current view
     */
    @OptionsItem(R.id.action_help)
    void onHelpMenuItem() {

        final String activityName = this.getClass().getName();

        HelpDisplayFragment_
                .intent(this)
                .subject(activityName)
                .start();
    }

    /**
     * opens the help document of the app at the "Help" page
     */
    void onShowGlobalHelpView() {
        HelpDisplayFragment_
                .intent(BaseActivity.this)
                .start();
    }

    /**
     * Menu item for Terms-Of-Use
     */
    @OptionsItem(R.id.action_terms)
    void onTermsMenuItem() {
        TermsOfUseReadingActivity_
                .intent(BaseActivity.this)
                .start();
    }


    // ------------------------------------------------------------------------------------------ //
    // ----------------------------------      DIALOG       ------------------------------------- //
    // ------------------------------------------------------------------------------------------ //

    @OptionsItem(R.id.action_settings)
    void onSettingsMenuItem() {
        SettingsActivity_.intent(this).start();
    }

    @OptionsItem(R.id.action_disconnect)
    void onDisconnect() {
        navController.terminate(this);
    }

    // ------------------------------------------------------------------------------------------ //
    // ----------------------------------      LOADING       ------------------------------------ //
    // ------------------------------------------------------------------------------------------ //

    /**
     * To display an error box
     *
     * @param titleResId   title resource id
     * @param messageResId message resource id
     */
    void displayErrorBox(int titleResId, int messageResId) {
        displayErrorBox(getString(titleResId), getString(messageResId));
    }

    /**
     * To display an error box.
     *
     * @param title   title
     * @param message message
     */
    void displayErrorBox(String title, String message) {

        if (errorDialog != null && errorDialog.isShowing()) {
            errorDialog.dismiss();
        }

        errorDialog = new OcaraDialogBuilder(this)
                .setTitle(title) // title
                .setMessage(message) // message
                .setPositiveButton(R.string.action_ok, null)
                .create();

        errorDialog.show();
    }


    /**
     * To set the loading state.
     *
     * @param isLoading true for loading, false otherwise
     */
    public void setLoading(boolean isLoading) {
        progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            boolean permissionIsMissing = false;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    permissionIsMissing = true;
                }
            }
            if (permissionIsMissing) {
                Toast.makeText(this, R.string.error_missing_permission, Toast.LENGTH_LONG).show();
            }
        }
    }

    /***********************************************************************************************
     *
     * The following elements are dedicated to the handling of Terms-Of-Use. That includes :
     * - injection of {@link TermsCheckingUiConfig}
     * - initialisation of a dedicated {@link TermsOfUseCheckingUserActionsListener}
     * - checking if the user has already accepted the terms or not
     * - implementation of the view behaviour, aka {@link com.orange.ocara.ui.contract.TermsOfUseDisplayContract.TermsOfUseDisplayView}
     */
    @Bean(TermsCheckingUiConfig.class)
    TermsCheckingUiConfig termsUiConfig;

    /**
     * User's actions listener for terms-of-use (which may also be known as TOU)
     */
    protected TermsOfUseCheckingUserActionsListener touActionsListener;

    @AfterInject
    public void initTermsOfUse() {
        d("ActivityMessage=Initializing the terms of use");

        touActionsListener = termsUiConfig.readingListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkTerms();
    }

    @Background
    void checkTerms() {
        d("ActivityMessage=Checking the terms of use while resuming");
        touActionsListener.checkTerms(new BaseActivityCheckTermsOfUseCallback(this));
    }

    @UiThread
    @Override
    public void showTerms() {
        d("ActivityMessage=Showing terms");
        TermsOfUseAcceptanceFragment_
                .intent(BaseActivity.this)
                .start();
    }

    @Override
    public void showError(String message) {
        makeText(getApplicationContext(), "Error while handling terms", LENGTH_SHORT)
                .show();
    }

    /**
     * Callback class for {@link CheckTermsOfUseTask} dedicated to the current activity
     */
    @RequiredArgsConstructor
    public static class BaseActivityCheckTermsOfUseCallback implements UseCaseCallback<CheckTermsOfUseResponse> {

        private final TermsOfUseDisplayView view;

        @Override
        public void onComplete(CheckTermsOfUseResponse terms) {

            if (!terms.isAccepted()) {
                d("Message=Terms not checked yet");
                view.showTerms();
            }
        }

        @Override
        public void onError(ErrorBundle errors) {
            e(errors.getCause(), "PresenterMessage=Item upgrading failed;PresenterError=%s", errors.getMessage());
            view.showError(errors.getMessage());
        }
    }
}
