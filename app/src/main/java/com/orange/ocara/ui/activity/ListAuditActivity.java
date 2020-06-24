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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;

import com.orange.ocara.BuildConfig;
import com.orange.ocara.R;
import com.orange.ocara.business.BizConfig;
import com.orange.ocara.business.model.AuditModel;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.db.ModelManagerImpl;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.SortCriteria;
import com.orange.ocara.tools.FileUtils;
import com.orange.ocara.ui.adapter.AuditListAdapter;
import com.orange.ocara.ui.contract.ListAuditContract;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.orange.ocara.ui.intent.export.AuditExportService;
import com.orange.ocara.ui.intent.export.AuditExportService_;
import com.orange.ocara.ui.presenter.ListAuditPresenter;
import com.orange.ocara.ui.routing.DestinationController;
import com.orange.ocara.ui.routing.Navigation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.List;

import timber.log.Timber;

/**
 * View dedicated to the display of a list of audits
 */
/**
 * a view dedicated to displaying a list of audits
 */
@EActivity(R.layout.activity_list_audit)
@OptionsMenu(R.menu.list_all_audits)
public class ListAuditActivity extends BaseActivity
        implements MenuItemCompat.OnActionExpandListener, SearchView.OnQueryTextListener,
        AuditListAdapter.AuditListAdapterListener, ListAuditContract.ListAuditView {

    private static final int ACTION_CREATE_NEW_DOCUMENT = 1;

    private static final int EXPORT_ACTION_SHARE = 0;
    private static final int EXPORT_ACTION_SAVE = 1;

    private static final int DELAY_IN_MILLISECONDS = 200;

    @Bean(ModelManagerImpl.class)
    ModelManager modelManager;

    @Bean(DestinationController.class)
    Navigation navController;

    @ViewById(R.id.audit_list)
    ListView auditList;

    @ViewById(R.id.audit_list_empty)
    View emptyAuditList;

    @Bean(BizConfig.class)
    BizConfig bizConfig;


    private MenuItem searchMenuItem = null;
    private SearchView searchView = null;
    private boolean searchActive = false;
    private long selectedAuditId = 0;
    private int selectedIndex = -1;
    private AuditListAdapter auditListAdapter;
    private int exportAction = -1;
    private File exportFile;

    private ListAuditContract.ListAuditUserActionsListener actionsListener;

    public long getSelectedAuditId() {
        return selectedAuditId;
    }

    @AfterViews
    void setUpAuditList() {
        View auditListHeader = getLayoutInflater().inflate(R.layout.list_audit_header, null, false);
        auditListAdapter = new AuditListAdapter(this, modelManager, this);
        auditList.setEmptyView(emptyAuditList);
        auditList.addHeaderView(auditListHeader, null, false);
        auditList.setAdapter(auditListAdapter);

        actionsListener = new ListAuditPresenter(this, bizConfig.redoAuditTask());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Tous les audits");
        auditListAdapter.refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);

        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        if (searchView != null) {
            searchView.setIconifiedByDefault(true);
            searchView.setOnQueryTextListener(this);
        }

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, this);
        return ret;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (auditListAdapter.getSortCriteria().getType()) {
            case SITE:
                menu.findItem(R.id.sort_by_site).setChecked(true);
                break;

            case DATE:
                menu.findItem(R.id.sort_by_date).setChecked(true);
                break;

            case STATUS:
            default:
                menu.findItem(R.id.sort_by_status).setChecked(true);
                break;
        }

        menu.findItem(R.id.sort).setIcon(auditListAdapter.getSortCriteria().isAscending() ? R.drawable.ic_sort_asc : R.drawable.ic_sort_desc);

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Callback triggered when an item is clicked in the sub-menu
     *
     * @param item a {@link MenuItem}
     * @param position an index
     * @return true, if action matches an element of the menu. false, if not.
     */
    public boolean onAuditSubMenuItemClick(MenuItem item, final int position) {
        final AuditEntity audit = auditListAdapter.getItem(position);
        selectedAuditId = audit.getId();
        Timber.d("Message=Selecting an item for audit;AuditId=%d;", selectedAuditId);

        int i = item.getItemId();
        if (i == R.id.action_remove) {
            askToDeleteAudit(audit);
            return true;
        } else if (i == R.id.action_complete_record) {
            askToCompleteAudit(audit);
            return true;
        } else if (i == R.id.action_edit_audit) {
            navController.navigateToAuditEditView(this, selectedAuditId, 0);
            return true;
        } else if (i == R.id.action_retest) {
            askToRetest(audit);
            return true;
        }

        return false;
    }

    private void askToDeleteAudit(final AuditEntity audit) {
        AlertDialog confirmDialog = new OcaraDialogBuilder(this)
                .setTitle(R.string.audit_list_delete_audit_title) // title
                .setMessage(getString(R.string.audit_list_delete_audit_message, audit.getName())) // message
                .setPositiveButton(R.string.action_remove, (dialog, which) -> deleteAudit(audit))
                .setNegativeButton(R.string.action_cancel, null)
                .create();

        confirmDialog.show();
    }

    private void askToCompleteAudit(final AuditEntity audit) {
        AlertDialog confirmDialog = new OcaraDialogBuilder(this)
                .setTitle(R.string.audit_list_complete_audit_title) // title
                .setMessage(R.string.audit_list_complete_audit_message) // message
                .setPositiveButton(R.string.action_complete, (dialog, which) -> SetupAuditPathActivity_
                        .intent(ListAuditActivity.this)
                        .fromListAudit(true)
                        .auditId(audit.getId())
                        .start())
                .setNegativeButton(R.string.action_cancel, null)
                .create();

        confirmDialog.show();
    }

    private void askToRetest(final AuditEntity audit) {

        Timber.d("Message=Requesting the redo of an audit;AuditId=%d;AuditName=%s;", audit.getId(), audit.getName());

        AlertDialog confirmDialog = new OcaraDialogBuilder(this)
                .setTitle(R.string.audit_list_retest_audit_title) // title
                .setMessage(getString(R.string.audit_list_retest_audit_message, audit.getName())) // message
                .setPositiveButton(R.string.action_retest, (dialog, which) -> createNewAudit(audit))
                .setNegativeButton(R.string.action_cancel, null)
                .create();

        confirmDialog.show();
    }

    @OptionsItem(R.id.add_audit)
    void onCreateAuditMenuItem() {
        navController.navigateToHome(this);
    }

    @OptionsItem(R.id.sort_by_status)
    void onSortByStatusMenuItem(MenuItem menuItem) {
        onSortMenuItem(menuItem, SortCriteria.Type.STATUS);
    }

    @OptionsItem(R.id.sort_by_site)
    void onSortBySiteMenuItem(MenuItem menuItem) {
        onSortMenuItem(menuItem, SortCriteria.Type.SITE);
    }

    @OptionsItem(R.id.sort_by_date)
    void onSortByDateMenuItem(MenuItem menuItem) {
        onSortMenuItem(menuItem, SortCriteria.Type.DATE);
    }

    void onSortMenuItem(MenuItem menuItem, SortCriteria.Type sortCriteriaType) {

        SortCriteria sortCriteria;

        // If already checked toggle current sortCriteria
        if (menuItem.isChecked()) {
            sortCriteria = auditListAdapter.getSortCriteria();
            sortCriteria.toggleOrder();
        } else {
            sortCriteria = sortCriteriaType.build();
        }

        auditListAdapter.refresh(sortCriteria);
        supportInvalidateOptionsMenu();
    }

    @ItemClick(R.id.audit_list)
    void auditListItemClicked(AuditEntity audit) {
        selectedAuditId = audit.getId();
        if (AuditEntity.Status.TERMINATED.equals(audit.getStatus())) {
            ResultAuditActivity_.intent(this).auditId(audit.getId()).start();
        } else {
            SetupAuditPathActivity_.intent(this).fromListAudit(true).auditId(audit.getId()).start();
        }
    }


    @Background
    void deleteAudit(AuditEntity audit) {
        modelManager.deleteAudit(audit.getId());

        auditDeleted();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditDeleted() {
        auditListAdapter.refresh();
    }

    @Background
    void createNewAudit(AuditEntity audit) {

        actionsListener.createNewAudit(audit);
    }

    @Override
    @UiThread(propagation = UiThread.Propagation.REUSE)
    public void showSetupPath(AuditModel audit) {
        selectedAuditId = audit.getId();
        SetupAuditPathActivity_
                .intent(this)
                .fromListAudit(true)
                .auditId(audit.getId())
                .start();
    }

    /**
     * Converts an audit into a document
     *
     * @param auditId an identifier for an {@link AuditEntity}
     */
    public void exportAuditToWord(final long auditId) {
        exportAction = -1;
        exportFile = null;
        AuditEntity audit = modelManager.getAudit(auditId);

        int message;
        if (audit.getStatus().equals(AuditEntity.Status.TERMINATED)) {
            message = R.string.export_docx_message_closed_audit;
        } else {
            message = R.string.export_docx_message;
        }

        AlertDialog dialog = new OcaraDialogBuilder(this)
                .setTitle(R.string.export_docx_title) // title
                .setMessage(message) // message
                .setNeutralButton(R.string.action_cancel, null)
                .setNegativeButton(R.string.action_share, (dialog1, which) -> {
                    exportAction = EXPORT_ACTION_SHARE;

                    setLoading(true);
                    AuditExportService_
                            .intent(ListAuditActivity.this)
                            .toDocx(auditId)
                            .start();
                })
                .setPositiveButton(R.string.action_save, (dialog12, which) -> {
                    exportAction = EXPORT_ACTION_SAVE;

                    setLoading(true);
                    AuditExportService_
                            .intent(ListAuditActivity.this)
                            .toDocx(auditId)
                            .start();
                })

                .create();

        dialog.show();
    }

    @Receiver(actions = AuditExportService.EXPORT_SUCCESS, local = true, registerAt = Receiver.RegisterAt.OnResumeOnPause)
    @UiThread(propagation = UiThread.Propagation.REUSE)
    void onExportAuditDone(@Receiver.Extra String path) {
        setLoading(false);

        exportFile = new File(path);

        switch (exportAction) {
            case EXPORT_ACTION_SHARE: {

                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Timber.i("Message=Configuring intent for version >= VERSION_CODES.N");
                    uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", exportFile);
                } else {
                    Timber.i("Message=Configuring intent for version < VERSION_CODES.N");
                    uri = Uri.fromFile(exportFile);
                }

                // create an intent, so the user can choose which application he/she wants to use to share this file
                final Intent intent = ShareCompat
                        .IntentBuilder
                        .from(this)
                        .setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path)))
                        .setStream(uri)
                        .setChooserTitle(R.string.list_audit_export_action)
                        .createChooserIntent()
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(intent);
                break;
            }

            case EXPORT_ACTION_SAVE: {
                createNewDocument(path);
                break;
            }
            default:
                throw new IllegalStateException("Should not get here");
        }
    }

    @Receiver(actions = AuditExportService.EXPORT_FAILED, local = true, registerAt = Receiver.RegisterAt.OnResumeOnPause)
    @UiThread(propagation = UiThread.Propagation.REUSE)
    void exportAuditDocxFailed() {
        setLoading(false);
    }

    private void createNewDocument(String path) {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        } else {
            intent = new Intent();
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path)));
        startActivityForResult(intent, ACTION_CREATE_NEW_DOCUMENT);
    }

    @OnActivityResult(ACTION_CREATE_NEW_DOCUMENT)
    void onCreateDocument(int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            copyFile(exportFile, uri);
        }
    }

    @Background
    void copyFile(File file, Uri uri) {
        try {
            FileUtils.copyFile(file, getContentResolver().openOutputStream(uri, "w"));
            fileSaved(uri);
        } catch (Exception e) {
            Timber.e(e, "Failed to copy file");
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void fileSaved(Uri uri) {
        Toast.makeText(this, "File  saved to " + uri.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        searchActive = true;
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        searchActive = false;
        auditListAdapter.refresh("");
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Timber.d("text change = %s active=%b", query, searchActive);

        if (searchActive) {
            auditListAdapter.refresh(query);
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Timber.d("text submit = %s", query);

        searchView.setQuery(query, false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow((this.getCurrentFocus()).getWindowToken(), 0);
        }
        return true;
    }

    @Override
    public void refreshAuditList(final List<AuditEntity> audits) {
        Handler handler = new Handler();

        handler.postDelayed(() -> {
            Timber.v("ListAuditActivity nombre " + auditListAdapter.getCount());
            Timber.v("ListAuditActivity selectedAuditId " + selectedAuditId);

            if (selectedAuditId != 0) {

                for (int i = 0; i < auditListAdapter.getCount(); i++) {
                    AuditEntity audit = auditListAdapter.getItem(i);
                    Timber.v("ListAuditActivity auditId " + audit.getId());
                    if (audit.getId() == selectedAuditId) {
                        selectedIndex = i;
                        auditList.post(() -> auditList.smoothScrollToPosition(selectedIndex));
                        break;
                    }
                }
            }

        }, DELAY_IN_MILLISECONDS);
    }

    @Override
    public void onBackPressed() {
        navController.navigateToHome(this);
        super.onBackPressed();
    }
}
