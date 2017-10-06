/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
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

import com.orange.ocara.R;
import com.orange.ocara.conf.OcaraConfiguration;
import com.orange.ocara.tools.injection.DaggerActivity;
import com.orange.ocara.ui.UiModule;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import javax.inject.Inject;

import timber.log.Timber;

@EActivity
/*package. */ abstract class BaseActivity extends DaggerActivity {

    @Inject
    Picasso picasso;

    public static final int PERMISSION_REQUEST = 10;

    @Extra("mustExitApplication")
    boolean mustExitApplication = false;

    private boolean isLoading = false;
    private AlertDialog errorDialog = null;

    protected ActionBarDrawerToggle drawerToggle;
    protected Toolbar toolbar;
    protected ProgressBar progress;

    protected DrawerLayout drawerLayout;
    protected ListView drawerList;

    protected FrameLayout contentLayout;


    // ------------------------------------------------------------------------------------------ //
    // ----------------------------------      LIFECYCLE        --------------------------------- //
    // ------------------------------------------------------------------------------------------ //


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean b = super.onCreateOptionsMenu(menu);

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(OcaraConfiguration.get().getMenuGlobalXmlId(), menu);

        return b;
    }

    @Override
    public void setContentView(int layoutResID) {
        View fullLayout = getLayoutInflater().inflate(R.layout.activity_base, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            requestPermissions(new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE
                    },
                    PERMISSION_REQUEST);

        }


        this.drawerLayout = (DrawerLayout) fullLayout.findViewById(R.id.drawer_layout);
        this.contentLayout = (FrameLayout) drawerLayout.findViewById(R.id.content);

        this.drawerList = (ListView) drawerLayout.findViewById(R.id.navigation_menu);
        setUpDrawerList();

        this.toolbar = (Toolbar) fullLayout.findViewById(R.id.toolbar);
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

        this.progress = (ProgressBar) toolbar.findViewById(R.id.progress);

        getLayoutInflater().inflate(layoutResID, contentLayout, true);
        super.setContentView(fullLayout);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerToggle.isDrawerIndicatorEnabled()) {
                    return drawerToggle.onOptionsItemSelected(item);
                } else {
                    onBackPressed();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }



    /**
     * @return true if the activity is a child activity, false if it is a root. By default always consider activity as child
     */
    protected boolean isChildActivity() {
        return true;
    }

    // ------------------------------------------------------------------------------------------ //
    // -------------------------------      NAVIGATION MENU       ------------------------------- //
    // ------------------------------------------------------------------------------------------ //


    /**
     * To setup the drawer list.
     */
    void setUpDrawerList() {
        drawerList.setAdapter(navigationItemAdapter);
        drawerList.setOnItemClickListener(navigationItemClickListener);
    }


    private static final int[] TITLE_RES_IDS = new int[]{R.string.navigation_item_new_audit, R.string.navigation_item_list_audit, R.string.navigation_item_show_ruleset,R.string.navigation_item_show_help,};
    private static final int[] ICON_RES_IDS = new int[]{R.drawable.ic_new, R.drawable.ic_list, R.drawable.ic_ruleset,R.drawable.ic_help};

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

            TextView text = (TextView) convertView.findViewById(R.id.item_text);

            text.setCompoundDrawablesWithIntrinsicBounds(ICON_RES_IDS[position], 0, 0, 0);
            text.setText(TITLE_RES_IDS[position]);

            return convertView;
        }
    };

    private final AdapterView.OnItemClickListener navigationItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    onNewAuditItemClicked();
                    break;

                case 1:
                    onAllAuditItemClicked();
                    break;

                case 2:
                    onAllRuleSetItemClicked();
                    break;
                case 3:
                    final String pdfName = "Guide_de_formation_V14.pdf";
                    showHelp(pdfName);
                    break;
                default:
                    Timber.e("Unknown navigation item (position=%d)", position);
                    break;
            }
            drawerLayout.closeDrawers();
        }
    };


    /**
     * New audit item clicked.
     */
    protected void onNewAuditItemClicked() {
        OcaraConfiguration.get().getCreateAudit().createAuditWithNullAuditId(this);
    }

    /**
     * All audits item clicked.
     */
    protected void onAllAuditItemClicked() {
        ListAuditActivity_.intent(this).start();
    }

    /**
     * All RuleSet item clicked.
     */
    protected void onAllRuleSetItemClicked() {
        String ruleSetId = null;
        String objectDescriptionId = null;
        ListRulesActivity_.intent(this).ruleSetId(ruleSetId).objectDescriptionId(objectDescriptionId).start();
    }

    /**
     * Show help item clicked.
     */

    protected void showHelp(String pdfName) {
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String locale = Locale.getDefault().getLanguage();
        String folder;
        if (locale.equals("en")) {
            folder = "pdf/";
        } else {
            folder = "pdf-"+locale+"/";
        }
        Timber.v("locale "+locale+ " folder "+folder);


        File file = new File(getFilesDir(), pdfName);


        try {
            inputStream = assetManager.open(folder+pdfName);

            viewFile(inputStream, outputStream, file, pdfName);
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
            try {
                inputStream = assetManager.open("pdf/" + pdfName);
                viewFile(inputStream, outputStream, file, pdfName);

            } catch (Exception a) {
                Log.e("tag", a.getMessage());
            }

        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException

    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)

        {
            out.write(buffer, 0, read);
        }
    }

    @SuppressWarnings("deprecation")
    private void viewFile(InputStream in, OutputStream out, File file, String pdfName) throws IOException
    {
        out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

        copyFile(in, out);
        in.close();
        in = null;
        out.flush();
        out.close();
        out = null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + getFilesDir() + File.separator + pdfName), "application/pdf");
        startActivity(intent);
    }

    // ------------------------------------------------------------------------------------------ //
    // ---------------------------------      ACTION BAR       ---------------------------------- //
    // ------------------------------------------------------------------------------------------ //

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
     * @param uri logo uri
     */
    void updateLogo(Uri uri) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                getSupportActionBar().setLogo(new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        final int maxSize = getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_height_material);
        picasso.load(uri).placeholder(android.R.color.black).resize(maxSize, maxSize).into(target);
    }

    // ------------------------------------------------------------------------------------------ //
    // ---------------------------------      MENU ITEMS       ---------------------------------- //
    // ------------------------------------------------------------------------------------------ //
    @OptionsItem(resName="action_about")
    void onAboutMenuItem() {
        AboutActivity_.intent(this).start();
    }

    @OptionsItem(resName="action_help")
    void onHelpMenuItem()
    {
        final String activityName = this.getClass().getName();
        String pdfName= activityName.substring(29, activityName.length() - 1)+".pdf";
      showHelp(pdfName);
    }

    @OptionsItem(resName="action_settings")
    void onSettingsMenuItem() {
        SettingsActivity_.intent(this).start();
    }

    @OptionsItem(resName="action_disconnect")
    void onDisconnect() {
        if (OcaraConfiguration.get().getDisconnect() != null ) {
            OcaraConfiguration.get().getDisconnect().disconnect(this);
        }
    }


    // ------------------------------------------------------------------------------------------ //
    // ----------------------------------      DIALOG       ------------------------------------- //
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

    // ------------------------------------------------------------------------------------------ //
    // ----------------------------------      LOADING       ------------------------------------ //
    // ------------------------------------------------------------------------------------------ //

    /**
     * To set the loading state.
     *
     * @param isLoading true for loading, false otherwise
     */
    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;

        progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    /**
     * @return loading state
     */
    public boolean isLoading() {
        return isLoading;
    }


    @Override
    protected Object[] getActivityModules() {
        return new Object[]{new UiModule()};
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }

    }

    public static void showKeyboard(Context context) {
        InputMethodManager service = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        service.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.SHOW_IMPLICIT);
    }

}
