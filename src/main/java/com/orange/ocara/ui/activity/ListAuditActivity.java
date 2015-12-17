/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.orange.ocara.R;
import com.orange.ocara.conf.OcaraConfiguration;
import com.orange.ocara.model.Audit;
import com.orange.ocara.model.ModelManager;
import com.orange.ocara.model.SortCriteria;
import com.orange.ocara.model.export.AuditExportService;
import com.orange.ocara.model.export.AuditExportService_;
import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.ui.adapter.ItemListAdapter;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.orange.ocara.ui.view.AuditItemView;
import com.orange.ocara.ui.view.AuditItemView_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import timber.log.Timber;

@EActivity(resName="activity_list_audit")
@OptionsMenu(resName="list_all_audits")
public class ListAuditActivity extends BaseActivity implements MenuItemCompat.OnActionExpandListener, SearchView.OnQueryTextListener {

    private static final int ACTION_CREATE_NEW_DOCUMENT = 1;

    private static final int EXPORT_ACTION_SHARE = 0;
    private static final int EXPORT_ACTION_SAVE = 1;

    @Inject
    ModelManager modelManager;

    @ViewById(resName="audit_list")
    ListView auditList;
    @ViewById(resName="audit_list_empty")
    View emptyAuditList;


    private MenuItem searchMenuItem = null;
    private SearchView searchView = null;
    private boolean searchActive = false;

    private final AuditListAdapter auditListAdapter = new AuditListAdapter();

    private int exportAction = -1;
    private File exportFile;

    private long selectedAuditId = 0;
    private int selectedIndex = -1;

    @AfterViews
    void setUpAuditList() {
        View auditListHeader = getLayoutInflater().inflate(com.orange.ocara.R.layout.list_audit_header, null, false);

        auditList.setEmptyView(emptyAuditList);
        auditList.addHeaderView(auditListHeader, null, false);
        auditList.setAdapter(auditListAdapter);


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

        searchMenuItem = menu.findItem(com.orange.ocara.R.id.action_search);
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
                menu.findItem(com.orange.ocara.R.id.sort_by_site).setChecked(true);
                break;

            case DATE:
                menu.findItem(com.orange.ocara.R.id.sort_by_date).setChecked(true);
                break;

            default:
            case STATUS:
                menu.findItem(com.orange.ocara.R.id.sort_by_status).setChecked(true);
                break;
        }

        menu.findItem(com.orange.ocara.R.id.sort).setIcon(auditListAdapter.getSortCriteria().isAscending() ? com.orange.ocara.R.drawable.ic_sort_asc : com.orange.ocara.R.drawable.ic_sort_desc);

        return super.onPrepareOptionsMenu(menu);
    }

    private boolean onAuditSubMenuItemClick(MenuItem item, final int position) {
        final Audit audit = auditListAdapter.getItem(position);
        selectedAuditId = audit.getId();

        int i = item.getItemId();
        if (i == com.orange.ocara.R.id.action_remove) {
            askToDeleteAudit(audit);
            return true;
        } else if (i == com.orange.ocara.R.id.action_complete_record) {
            askToCompleteAudit(audit);
            return true;
        } else if (i == com.orange.ocara.R.id.action_retest) {
            askToRetest(audit);
            return true;
        }

        return false;
    }

    private void askToDeleteAudit(final Audit audit) {
        AlertDialog confirmDialog = new OcaraDialogBuilder(this)
                .setTitle(com.orange.ocara.R.string.audit_list_delete_audit_title) // title
                .setMessage(getString(com.orange.ocara.R.string.audit_list_delete_audit_message, audit.getName())) // message
                .setPositiveButton(com.orange.ocara.R.string.action_remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAudit(audit);
                    }
                })
                .setNegativeButton(com.orange.ocara.R.string.action_cancel, null)
                .create();

        confirmDialog.show();
    }

    private void askToCompleteAudit(final Audit audit) {
        AlertDialog confirmDialog = new OcaraDialogBuilder(this)
                .setTitle(com.orange.ocara.R.string.audit_list_complete_audit_title) // title
                .setMessage(com.orange.ocara.R.string.audit_list_complete_audit_message) // message
                .setPositiveButton(com.orange.ocara.R.string.action_complete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SetupAuditPathActivity_.intent(ListAuditActivity.this).auditId(audit.getId()).start();
                    }
                })
                .setNegativeButton(com.orange.ocara.R.string.action_cancel, null)
                .create();

        confirmDialog.show();
    }

    private void askToRetest(final Audit audit) {

        AlertDialog confirmDialog = new OcaraDialogBuilder(this)
                .setTitle(com.orange.ocara.R.string.audit_list_retest_audit_title) // title
                .setMessage(getString(com.orange.ocara.R.string.audit_list_retest_audit_message, audit.getName())) // message
                .setPositiveButton(com.orange.ocara.R.string.action_retest, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createNewAudit(audit);
                    }
                })
                .setNegativeButton(com.orange.ocara.R.string.action_cancel, null)
                .create();

        confirmDialog.show();
    }

    @OptionsItem(resName="add_audit")
    void onCreateAuditMenuItem() {
        OcaraConfiguration.get().getCreateAudit().createAudit(this);
    }

    @OptionsItem(resName="sort_by_status")
    void onSortByStatusMenuItem(MenuItem menuItem) {
        onSortMenuItem(menuItem, SortCriteria.Type.STATUS);
    }

    @OptionsItem(resName="sort_by_site")
    void onSortBySiteMenuItem(MenuItem menuItem) {
        onSortMenuItem(menuItem, SortCriteria.Type.SITE);
    }

    @OptionsItem(resName="sort_by_date")
    void onSortByDateMenuItem(MenuItem menuItem) {
        onSortMenuItem(menuItem, SortCriteria.Type.DATE);
    }

    void onSortMenuItem(MenuItem menuItem, SortCriteria.Type sortCriteriaType) {

        SortCriteria sortCriteria = null;

        // If already checked toggle current sortCriteria
        if (menuItem.isChecked()) {
            sortCriteria = auditListAdapter.getSortCriteria();
            sortCriteria.toggleOrder();
        } else {
            sortCriteria = sortCriteriaType.build();
        }

        auditListAdapter.refreshWithSortCriteria(sortCriteria);
        supportInvalidateOptionsMenu();
    }

    @ItemClick(resName="audit_list")
    void auditListItemClicked(Audit audit) {
        selectedAuditId = audit.getId();
        if (Audit.Status.TERMINATED.equals(audit.getStatus())) {
            ResultAuditActivity_.intent(this).auditId(audit.getId()).start();
        } else {
            SetupAuditPathActivity_.intent(this).auditId(audit.getId()).start();
        }
    }



    @Background
    void deleteAudit(Audit audit) {
        modelManager.deleteAudit(audit.getId());

        auditDeleted(audit);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditDeleted(Audit audit) {
        auditListAdapter.refresh();
    }

    @Background
    void createNewAudit(Audit audit) {
        Audit newAudit = modelManager.createAuditWithNewVersion(audit);
        newAuditCreated(newAudit);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void newAuditCreated(Audit audit) {
        selectedAuditId = audit.getId();
        SetupAuditPathActivity_.intent(this).auditId(audit.getId()).start();
    }


    private void exportAuditToWord(final long auditId) {

        exportAction = -1;
        exportFile = null;
        Audit audit = modelManager.getAudit(auditId);

        int message;
        if (audit.getStatus().equals(Audit.Status.TERMINATED)) {
            message = com.orange.ocara.R.string.export_docx_message_closed_audit;
        } else {
            message = com.orange.ocara.R.string.export_docx_message;
        }

        AlertDialog dialog = new OcaraDialogBuilder(this)
                .setTitle(com.orange.ocara.R.string.export_docx_title) // title
                .setMessage(message) // message
                .setNeutralButton(com.orange.ocara.R.string.action_cancel, null)
                .setNegativeButton(com.orange.ocara.R.string.action_share, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exportAction = EXPORT_ACTION_SHARE;

                        setLoading(true);
                        AuditExportService_.intent(ListAuditActivity.this).toDocx(auditId).start();
                    }
                })
                .setPositiveButton(com.orange.ocara.R.string.action_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exportAction = EXPORT_ACTION_SAVE;

                        setLoading(true);
                        AuditExportService_.intent(ListAuditActivity.this).toDocx(auditId).start();
                    }
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

                // create an intent, so the user can choose which application he/she wants to use to share this file
                final Intent intent = ShareCompat.IntentBuilder.from(this)
                        .setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path)))
                        .setStream(FileProvider.getUriForFile(this, "com.orange.ocara", exportFile))
                        .setChooserTitle("How do you want to share?")
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
        }
    }

    @Receiver(actions = AuditExportService.EXPORT_FAILED, local = true, registerAt = Receiver.RegisterAt.OnResumeOnPause)
    @UiThread(propagation = UiThread.Propagation.REUSE)
    void exportAuditDocxFailed() {
        setLoading(false);
    }

    private void createNewDocument(String path) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
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
            fileSaved(file, uri);
        } catch (Exception e) {
            Timber.e(e, "Failed to copy file");
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void fileSaved(File file, Uri uri) {
        //Toast.makeText(this, "File  saved to " + uri.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        searchActive = true;
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        searchActive = false;
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Timber.d("text change = %s active=%b", query, searchActive);

        if (searchActive) {
            auditListAdapter.refreshWithQuery(query);
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Timber.d("text submit = %s", query);

        MenuItemCompat.collapseActionView(searchMenuItem);
        searchView.setQuery(query, false);

        return true;
    }

    /**
     * AuditList adapter.
     */
    private class AuditListAdapter extends ItemListAdapter<Audit> {

        private Filter filter;

        @Getter
        private String queryFilter = "";
        @Getter
        private SortCriteria sortCriteria = SortCriteria.Type.STATUS.build();

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            AuditItemView auditItemView;


            if (convertView == null) {
                auditItemView = AuditItemView_.build(ListAuditActivity.this);
            } else {
                auditItemView = (AuditItemView) convertView;
            }

            final Audit audit = getItem(position);
            auditItemView.bind(audit);

            if (audit.getId() == selectedAuditId ) {
                auditItemView.setBackgroundColor(getResources().getColor(R.color.orange_light));
            } else {
                auditItemView.setBackgroundColor(getResources().getColor(R.color.transparent));
            }

            auditItemView.getExportAuditDocx().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exportAuditToWord(audit.getId());
                }
            });

            auditItemView.getMore().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(ListAuditActivity.this, v);
                    if (Audit.Status.TERMINATED.equals(audit.getStatus())) {
                        popupMenu.inflate(com.orange.ocara.R.menu.list_all_audit_terminated);
                    } else {
                        popupMenu.inflate(com.orange.ocara.R.menu.list_all_audit_in_progress);
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            return onAuditSubMenuItemClick(item, position);
                        }
                    });
                    popupMenu.show();
                }
            });

            return auditItemView;
        }

        public void refresh() {
            refresh(queryFilter, sortCriteria);
        }

        public void refreshWithSortCriteria(SortCriteria sortCriteria) {
            refresh(queryFilter, sortCriteria);
        }

        public void refreshWithQuery(String query) {
            refresh(query, sortCriteria);
        }

        private void refresh(String queryFilter, SortCriteria sortCriteria) {
            this.queryFilter = queryFilter;
            this.sortCriteria = sortCriteria;

            auditListAdapter.getFilter().filter(queryFilter);
            Handler handler = new Handler();


            handler.postDelayed(new Runnable() {
                                    @java.lang.Override
                                    public void run() {
                                        Timber.v("ListAuditActivity nombre " + auditListAdapter.getCount());
                                        Timber.v("ListAuditActivity selectedAuditId " + selectedAuditId);

                                        if (selectedAuditId != 0) {

                                            for (int i = 0; i < auditListAdapter.getCount(); i++) {
                                                Audit audit = auditListAdapter.getItem(i);
                                                Timber.v("ListAuditActivity auditId " + audit.getId());
                                                if (audit.getId() == selectedAuditId) {
                                                    selectedIndex = i;
                                                    auditList.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            auditList.smoothScrollToPosition(selectedIndex);
                                                        }
                                                    });
                                                    break;
                                                }
                                            }
                                        }

                                    }
                                }, 200
            );
        }

        private Filter getFilter() {
            if (filter == null) {
                filter = new AuditFilter();
            }

            return filter;
        }

        /**
         * Internal user name Filter.
         */
        private class AuditFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint != null) {
                    List<Audit> audits = modelManager.getAllAudits(constraint.toString(), sortCriteria);

                    results.values = audits;
                    results.count = audits.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                update((Collection<Audit>) results.values);
                if (results.count == 0) {
                    notifyDataSetInvalidated();
                }
            }
        }

    }
}
