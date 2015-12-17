/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orange.ocara.model.AccessibilityStats;
import com.orange.ocara.model.Audit;
import com.orange.ocara.model.AuditObject;
import com.orange.ocara.model.Comment;
import com.orange.ocara.model.ModelManager;
import com.orange.ocara.model.RefreshStrategy;
import com.orange.ocara.model.export.AuditExportService;
import com.orange.ocara.model.export.AuditExportService_;
import com.orange.ocara.modelStatic.Handicap;
import com.orange.ocara.modelStatic.Response;
import com.orange.ocara.ui.adapter.AuditObjectsAdapter;
import com.orange.ocara.ui.dialog.AudioPlayerDialog;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.orange.ocara.ui.fragment.HandicapPieChartFragment_;
import com.orange.ocara.ui.view.AuditObjectAnomalyView;
import com.orange.ocara.ui.view.AuditObjectAnomalyView_;
import com.orange.ocara.ui.view.AuditObjectsView;
import com.orange.ocara.ui.view.CommentItemView;
import com.orange.ocara.ui.view.CommentItemView_;
import com.orange.ocara.ui.widget.AuditorAutoCompleteView;
import com.orange.ocara.ui.widget.SiteAutoCompleteView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;

@EActivity(resName="activity_audit_results")
@OptionsMenu(resName="audit_result")
public class ResultAuditActivity extends BaseActivityManagingAudit {

    /**
     * action for saving a new document.
     */
    private static final int ACTION_SAVE_DOCUMENT = 1;
    /**
     * action for sharing a new document.
     */
    private static final int ACTION_SHARE_DOCUMENT = 2;

    private static final RefreshStrategy STRATEGY = RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.RULE_ANSWER).commentsNeeded(true).build();

    @ViewById(resName="scrollview_container")
    ScrollView scrollContainer;
    @ViewById(resName="audit_site")
    TextView auditSite;
    @ViewById(resName="audit_name")
    TextView auditName;
    @ViewById(resName="audit_user")
    TextView auditUser;
    @ViewById(resName="audit_type")
    TextView auditType;
    @ViewById(resName="audit_date")
    TextView auditDate;
    @ViewById(resName="chart_pager")
    ViewPager auditChartPager;
    @ViewById(resName="chart_title")
    TextView auditChartTitle;
    @ViewById(resName="chart_choices")
    GridView auditChartChoices;

    @ViewById(resName="path_layout")
    ViewGroup pathLayout;
    @ViewById(resName="audited_objects")
    AuditObjectsView auditObjects;

    @ViewById(resName="result_resume_table")
    TableLayout resumeTable;

    @ViewById(resName="audit_comment_layout")
    ViewGroup auditCommentLayout;
    @ViewById(resName="audit_comment_container")
    ViewGroup auditCommentContainer;

    @ViewById(resName="anomaly_layout")
    ViewGroup anomalyLayout;

    @Inject
    ModelManager modelManager;
    @Inject
    Picasso picasso;

    private final HandicapAggregator handicapAggregator = new HandicapAggregator();

    private AuditObjectsAdapter auditObjectsAdapter;

    private int exportAction = -1;
    private File exportFile;
    private boolean shouldFinishAfterExport = false;

    @Override
    RefreshStrategy getRefreshStrategy() {
        return STRATEGY;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(com.orange.ocara.R.string.audit_results_title);
    }

    @Override
    void auditRefreshed() {
        super.auditRefreshed();

        handicapAggregator.update(audit);

        updateDetails(audit);
        updatePath(audit);
        updateResume(audit);
        updateAuditComments(audit);
        updateAnomalies(audit);
    }



    @AfterViews
    void setUpAuditedPath() {
        auditObjectsAdapter = new AuditObjectsAdapter(this);
        auditObjects.setAdapter(auditObjectsAdapter);

        auditObjects.getAuditPath().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float pathWitdh = auditObjects.getAuditPath().getWidth();
                int auditObjectItemWidth = getResources().getInteger(com.orange.ocara.R.integer.auditObjectItemWidth);
                int numberAuditedObjects = (int) (pathWitdh / auditObjectItemWidth);
                float freeSpace = pathWitdh % auditObjectItemWidth;
                int margin = (int) (freeSpace / (numberAuditedObjects - 1));


                Timber.v("auditPath setUpAuditedPath " + pathWitdh + "auditObjectItemWitdh " + auditObjectItemWidth + " freeSpace " + freeSpace + " numberAuditedObjects " + numberAuditedObjects + " margin " + margin);


                auditObjects.getAuditPath().setItemMargin(margin);

            }
        });
    }




    @AfterViews
    void setUpChart() {
        auditChartPager.setAdapter(chartPagerAdapter);
        auditChartChoices.setAdapter(chartChoicesAdapter);

        auditChartChoices.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        checkUniqueChoice(0);

        auditChartPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                checkUniqueChoice(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        auditChartChoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Case of Total
                if (position == 0) {
                    chartPagerAdapter.setCustomStat(null);
                    auditChartPager.setCurrentItem(0, true);

                    checkUniqueChoice(0);
                    return;
                }

                final SparseBooleanArray checkedItemPositions = auditChartChoices.getCheckedItemPositions();

                List<Integer> selectedItems = new ArrayList<Integer>();
                for (int i = 0; i < checkedItemPositions.size(); i++) {
                    // Item position in adapter
                    int pos = checkedItemPositions.keyAt(i);

                    if (pos != 0 && checkedItemPositions.valueAt(i)) {
                        selectedItems.add(pos);
                    }
                }

                if (selectedItems.isEmpty()) {
                    selectedItems.add(0);
                }

                // One choice checked (other than total)
                if (selectedItems.size() == 1) {
                    int pos = selectedItems.get(0);

                    chartPagerAdapter.setCustomStat(null);
                    auditChartPager.setCurrentItem(pos, true);
                } else {
                    // Multiple choices, build custom stats
                    AccessibilityStats custom = new AccessibilityStats();

                    for (int i : selectedItems) {
                        custom.plus(handicapAggregator.getStat(i - 1));
                    }

                    chartPagerAdapter.setCustomStat(custom);
                    auditChartPager.setCurrentItem(0, true);

                    auditChartChoices.clearChoices();
                    for (int i : selectedItems) {
                        auditChartChoices.setItemChecked(i, true);
                    }
                }
            }
        });
    }

    protected void checkUniqueChoice(int position) {
        auditChartChoices.clearChoices();
        auditChartChoices.setItemChecked(position, true);

        auditChartTitle.setText(chartPagerAdapter.getPageTitle(position));
    }

    private void updateDetails(Audit audit) {
        final DateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.FRANCE);

        auditName.setText(audit.getName());
        auditType.setText(audit.getRuleSet().getType());
        auditDate.setText(dateFormat.format(audit.getDate()));
        auditUser.setText(AuditorAutoCompleteView.format(audit.getAuthor()));
        auditSite.setText(SiteAutoCompleteView.format(audit.getSite(), false));

        updateChart(audit);
    }

    private void updateChart(Audit audit) {
        chartPagerAdapter.notifyDataSetChanged();
        chartChoicesAdapter.notifyDataSetChanged();
        auditChartTitle.setText(chartPagerAdapter.getPageTitle(auditChartPager.getCurrentItem()));
    }

    private void updatePath(Audit audit) {
        auditObjectsAdapter.update(audit.getObjects());
    }

    private void updateResume(Audit audit) {
        resumeTable.removeViews(1, resumeTable.getChildCount() - 1);

        final int doubtColumnIndex = 4;

        if (Audit.Level.BEGINNER.equals(audit.getLevel())) {
            resumeTable.setColumnCollapsed(doubtColumnIndex, false);
            resumeTable.setColumnStretchable(doubtColumnIndex, true);
        } else {
            resumeTable.setColumnCollapsed(doubtColumnIndex, true);

            // Don' set it. It seems to bug
            //resumeTable.setColumnStretchable(doubtColumnIndex, false);
        }

        int rowIndex = 0;

        for (String handicapId : handicapAggregator.getHandicapIds()) {
            // Inflate a new row and fill it
            final AccessibilityStats stats = handicapAggregator.getStat(handicapId);
            final Handicap handicap = handicapAggregator.getHandicap(handicapId);
            resumeTable.addView(buildResumeRow(handicap.getIcon(), handicap.getName(), stats, rowIndex++));
        }

        // Add total row
        resumeTable.addView(buildResumeRow(com.orange.ocara.R.drawable.ic_handicap_all, getString(com.orange.ocara.R.string.audit_results_table_all_handicap), handicapAggregator.getTotalStat(), rowIndex++));
    }

    private View buildResumeRow(int handicapIconResId, String handicapName, AccessibilityStats stats, int rowIndex) {
        return buildResumeRow(picasso.load(handicapIconResId), handicapName, stats, rowIndex);
    }

    private View buildResumeRow(URI handicapIcon, String handicapName, AccessibilityStats stats, int rowIndex) {
        return buildResumeRow(picasso.load(Uri.parse(handicapIcon.toString())), handicapName, stats, rowIndex);
    }

    private View buildResumeRow(RequestCreator handicapIconRequest, String handicapName, AccessibilityStats stats, int rowIndex) {

        TableRow row = (TableRow) getLayoutInflater().inflate(com.orange.ocara.R.layout.result_resume_item, null);

        TextView noImpact = (TextView) row.findViewById(com.orange.ocara.R.id.resume_handicap_no_impact);
        TextView annoying = (TextView) row.findViewById(com.orange.ocara.R.id.resume_handicap_annoying);
        TextView blocking = (TextView) row.findViewById(com.orange.ocara.R.id.resume_handicap_blocking);
        TextView doubt = (TextView) row.findViewById(com.orange.ocara.R.id.resume_handicap_doubt);

        noImpact.setText(Integer.toString(stats.getCounter(AccessibilityStats.Type.ACCESSIBLE)));
        annoying.setText(Integer.toString(stats.getCounter(AccessibilityStats.Type.ANNOYING)));
        blocking.setText(Integer.toString(stats.getCounter(AccessibilityStats.Type.BLOCKING)));
        doubt.setText(Integer.toString(stats.getCounter(AccessibilityStats.Type.DOUBT)));

        final TextView handicapType = (TextView) row.findViewById(com.orange.ocara.R.id.resume_handicap_type);
        handicapType.setText(handicapName);
        if (handicapIconRequest != null) {

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    handicapType.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(getResources(), bitmap), null, null, null);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
            final int maxSize = getResources().getDimensionPixelSize(com.orange.ocara.R.dimen.resultResumeHandicapIconSize);
            handicapIconRequest.placeholder(android.R.color.black).resize(maxSize, maxSize).into(target);
        }


        int backGroundColor = (rowIndex % 2 == 0) ? com.orange.ocara.R.color.resultTableColorEven : com.orange.ocara.R.color.resultTableColorOdd;
        row.setBackgroundResource(backGroundColor);

        return row;
    }


    private void updateAnomalies(Audit audit) {



        ViewGroup anomalyContainerView = (ViewGroup) findViewById(com.orange.ocara.R.id.anomaly_container);
        anomalyContainerView.removeAllViews();


        for(AuditObject auditObject : audit.getObjects()) {
            final List<Comment> comments = auditObject.getComments();
            final String auditObjectName = auditObject.getName();
            final Uri auditObjectIcon = Uri.parse(auditObject.getObjectDescription().getIcon().toString());
            if (auditObject.getResponse().equals(Response.NOK)
                || !auditObject.getComments().isEmpty() ) {
                final AuditObjectAnomalyView auditObjectAnomalyView = AuditObjectAnomalyView_.build(getApplicationContext());
                auditObjectAnomalyView.setOnGroupClickListener(new AuditObjectAnomalyView.OnGroupClickListener() {
                    @Override
                    public void onExpand(AuditObjectAnomalyView view) {
                        scrollTo(view);
                    }
                });

                auditObjectAnomalyView.setOnCommentClickListener(new AuditObjectAnomalyView.OnCommentClickListener() {
                    @Override
                    public void onCommentClick(Comment comment, View CommentView) {
                        ResultAuditActivity.this.onCommentClicked(auditObjectIcon, auditObjectName, comments, comment, CommentView);
                    }
                });
                auditObjectAnomalyView.bind(auditObject);
                anomalyContainerView.addView(auditObjectAnomalyView);
            }
        }



        anomalyLayout.setVisibility(anomalyContainerView.getChildCount() > 0 ? View.VISIBLE : View.GONE);
    }


    private void scrollTo(final View view) {

        final Rect scrollBounds = new Rect();
        scrollContainer.getHitRect(scrollBounds);

        scrollBounds.set(scrollBounds.left, scrollBounds.top, scrollBounds.right, scrollBounds.bottom - 200);

        view.getLocalVisibleRect(scrollBounds);

        if (scrollBounds.height() >= view.getHeight()) {
            return; // no scroll need
        }

        // Determine where to set the scroll-to to by measuring the distance from the top of the scroll view
        // to the control to focus on by summing the "top" position of each view in the hierarchy.
        int yDistanceToControlsView = 0;
        View parentView = (View) view.getParent();
        while (true)
        {
            if (parentView.equals(scrollContainer))
            {
                break;
            }
            yDistanceToControlsView += parentView.getTop();
            parentView = (View) parentView.getParent();
        }

        // Compute the final position value for the top and bottom of the control in the scroll view.
        final int topInScrollView = yDistanceToControlsView + view.getTop();
        final int bottomInScrollView = yDistanceToControlsView + view.getBottom();

        // Post the scroll action to happen on the scrollView with the UI thread.
        scrollContainer.post(new Runnable() {
            @Override
            public void run() {
                int height = view.getHeight();
                scrollContainer.smoothScrollTo(0, topInScrollView); //((topInScrollView + bottomInScrollView) / 2) - height);
                view.requestFocus();
            }
        });
    }



    private void updateAuditComments(Audit audit) {

        if (audit.getComments().isEmpty()) {
            auditCommentLayout.setVisibility(View.GONE);
            return;
        }

        auditCommentLayout.setVisibility(View.VISIBLE);
        auditCommentContainer.removeAllViews();
        final List<Comment> comments=audit.getComments();
        final String auditName = audit.getName();
        for(final Comment comment : audit.getComments()) {
            CommentItemView commentItemView = CommentItemView_.build(this);
            commentItemView.bind(comment);
            auditCommentContainer.addView(commentItemView);
            commentItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCommentClicked(null, auditName, comments, comment, v);
                }
            });
        }


    }

    @Click(resName="previous_chart_button")
    void onPreviousChartClicked() {
        int currentItem = auditChartPager.getCurrentItem();
        if (currentItem > 0) {
            currentItem--;
        }

        auditChartPager.setCurrentItem(currentItem, true);
    }

    @Click(resName="next_chart_button")
    void onNextChartClicked() {
        int currentItem = auditChartPager.getCurrentItem();
        if (currentItem < auditChartPager.getAdapter().getCount() - 1) {
            currentItem++;
        }

        auditChartPager.setCurrentItem(currentItem, true);
    }

    private void onCommentClicked(Uri icon, String name, List<Comment> comments, Comment comment, View commentView) {

        switch (comment.getType()) {
            case AUDIO:
                startAudioPlayer(comment.getAttachment(), commentView);
                break;

            case TEXT :
                showComment(icon, name, comments, comment);
                break;

            case PHOTO:
                showComment(icon, name, comments, comment);
                break;

            default:
                break;
        }
    }

    private void showComment(Uri icon, String name, List<Comment> commentList, Comment comment) {
        int selectedIndex =0;
        List<Comment> commentPhotoAndText = extractCommentPhotoAndText(commentList);
        int nbCommentPhotoAndText = commentPhotoAndText.size();
        String[] titles = new String[nbCommentPhotoAndText];
        String[] comments = new String[nbCommentPhotoAndText];
        String[] images = new String[nbCommentPhotoAndText];

        Timber.v("nbCommentPhotoAndText "+nbCommentPhotoAndText);
        for (int i = 0; i < nbCommentPhotoAndText; i++) {
            if (commentPhotoAndText.get(i).equals(comment)) {
                selectedIndex = i;
            }

            if (icon != null) {
                titles[i] = getString(com.orange.ocara.R.string.illustration_activity_comment_auditobject_title, i + 1, name );
            } else {
                titles[i] = getString(com.orange.ocara.R.string.illustration_activity_comment_audit_title, i + 1, name );
            }

            comments[i] = commentPhotoAndText.get(i).getContent();

            if (commentPhotoAndText.get(i).getType().equals(Comment.Type.PHOTO)) {
                images[i] = commentPhotoAndText.get(i).getAttachment();
            }

        }

        IllustrationsActivity_.intent(ResultAuditActivity.this)
                .selectedIndex(selectedIndex)
                .icon(icon)
                .titles(titles)
                .comments(comments)
                .images(images)
                .start();

    }

    private List<Comment> extractCommentPhotoAndText(List<Comment> comments) {
        List<Comment> ret = new ArrayList<Comment>();

        for (Comment comment : comments) {
            if (comment.getType().equals(Comment.Type.PHOTO) || comment.getType().equals(Comment.Type.TEXT)) {
                ret.add(comment);
            }
        }
        return(ret);
    }

    private void startAudioPlayer(String attachment, View commentView) {
        FragmentManager fm = getSupportFragmentManager();

        int[] location = new int[2];
        commentView.getLocationOnScreen(location);

        AudioPlayerDialog audioPlayerDialog = AudioPlayerDialog.newInstance(attachment, -1, location[1]);
        audioPlayerDialog.show(fm, "fragment_edit_name");
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean ret = super.onPrepareOptionsMenu(menu);
        menu.findItem(com.orange.ocara.R.id.action_lock).setVisible(!Audit.Status.TERMINATED.equals(audit.getStatus()));
        return ret;
    }

    @OptionsItem(resName="action_lock")
    void onLockAuditMenuItem() {

        AlertDialog dialog = new OcaraDialogBuilder(this)
                .setTitle(com.orange.ocara.R.string.audit_results_lock_dialog_title) // title
                .setMessage(com.orange.ocara.R.string.audit_results_lock_dialog_message) // message
                .setNeutralButton(com.orange.ocara.R.string.action_cancel, null)
                .setPositiveButton(com.orange.ocara.R.string.action_close_audit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setLoading(true);
                        lockAudit();
                    }
                })
                .create();

        dialog.show();
    }

    @Background
    void lockAudit() {
        audit.setStatus(Audit.Status.TERMINATED);
        modelManager.updateAudit(audit);

        auditLocked();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void auditLocked() {
        setLoading(false);
        supportInvalidateOptionsMenu();


        shouldFinishAfterExport = true;

        exportAction = -1;
        exportFile = null;

        AlertDialog dialog = buildExportDialog()
           .create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (exportAction < 1) {
                            onExportAuditDocxFinished();
                        }
                    }
                });
        dialog.show();
    }

    @OptionsItem(resName="action_export_audit_docx")
    void onExportAuditToWordMenuItem() {

        exportAction = -1;
        exportFile = null;

        AlertDialog dialog = buildExportDialog().create();
        dialog.show();
    }

    private AlertDialog.Builder buildExportDialog() {
        int message;
        if (audit.getStatus().equals(Audit.Status.TERMINATED)) {
            message = com.orange.ocara.R.string.export_docx_message_closed_audit;
        } else {
            message = com.orange.ocara.R.string.export_docx_message;
        }

        return new OcaraDialogBuilder(this)
                .setTitle(com.orange.ocara.R.string.export_docx_title) // title
                .setMessage(message) // message
                .setNeutralButton(com.orange.ocara.R.string.action_cancel, null)
                .setNegativeButton(com.orange.ocara.R.string.action_share, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exportAction = ACTION_SHARE_DOCUMENT;

                        setLoading(true);
                        AuditExportService_.intent(ResultAuditActivity.this).toDocx(audit.getId()).start();
                    }
                })
                .setPositiveButton(com.orange.ocara.R.string.action_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exportAction = ACTION_SAVE_DOCUMENT;

                        setLoading(true);
                        AuditExportService_.intent(ResultAuditActivity.this).toDocx(audit.getId()).start();
                    }
                });
    }

    @Receiver(actions = AuditExportService.EXPORT_SUCCESS, local = true, registerAt = Receiver.RegisterAt.OnResumeOnPause)
    @UiThread(propagation = UiThread.Propagation.REUSE)
    void onExportAuditDone(@Receiver.Extra String path) {
        setLoading(false);

        exportFile = new File(path);

        switch (exportAction) {
            case ACTION_SHARE_DOCUMENT: {
                shareDocument(path);
                break;
            }

            case ACTION_SAVE_DOCUMENT: {
                saveDocument(path);
                break;
            }
        }
    }

    @Receiver(actions = AuditExportService.EXPORT_FAILED, local = true, registerAt = Receiver.RegisterAt.OnResumeOnPause)
    @UiThread(propagation = UiThread.Propagation.REUSE)
    void onExportAuditDocxFailed() {
        setLoading(false);

        onExportAuditDocxFinished();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void onExportAuditDocxFinished() {

        if (shouldFinishAfterExport) {
            ListAuditActivity_.intent(this).start();
            finish();
        }
    }

    /**
     * To share a document by its path.<br/>
     * This will send a share intent.
     *
     * @param path the file path to share
     */
    private void shareDocument(String path) {
        // create an intent, so the user can choose which application he/she wants to use to share this file
        final Intent intent = ShareCompat.IntentBuilder.from(this)
                .setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path)))
                .setStream(FileProvider.getUriForFile(this, "com.orange.ocara", exportFile))
                .setChooserTitle("How do you want to share?")
                .createChooserIntent()
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(intent, ACTION_SHARE_DOCUMENT);
    }

    @OnActivityResult(ACTION_SHARE_DOCUMENT)
    void onDocumentShared(int resultCode, Intent data) {
        onExportAuditDocxFinished();
    }

    /**
     * To savge a document by its path.<br/>
     *
     * @param path the file path to share
     */
    private void saveDocument(String path) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path)));
        startActivityForResult(intent, ACTION_SAVE_DOCUMENT);
    }

    @OnActivityResult(ACTION_SAVE_DOCUMENT)
    void onDocumentSaved(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            copyFile(exportFile, uri);
        } else {
            onExportAuditDocxFinished();
        }
    }

    @Background
    void copyFile(File file, Uri uri) {
        try {
            FileUtils.copyFile(file, getContentResolver().openOutputStream(uri, "w"));
            fileCopied(file, uri);
        } catch (Exception e) {
            Timber.e(e, "Failed to copy file");
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void fileCopied(File file, Uri uri) {
        onExportAuditDocxFinished();
    }



    private final ChartPagerAdapter chartPagerAdapter = new ChartPagerAdapter();

    /**
     * Categories pager adapter.
     */
    private class ChartPagerAdapter extends FragmentStatePagerAdapter {

        private AccessibilityStats customStat = null;

        public ChartPagerAdapter() {
            super(getSupportFragmentManager());
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return customStat != null ? 1 : handicapAggregator.getHandicapIds().size() + 1;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            AccessibilityStats stat = new AccessibilityStats();

            if (customStat != null) {
                stat.plus(customStat);
            } else {
                if (position == 0) {
                    stat.plus(handicapAggregator.getTotalStat());
                } else {
                    stat.plus(handicapAggregator.getStat(position - 1));
                }
            }

            HandicapPieChartFragment_.FragmentBuilder_ builder = HandicapPieChartFragment_.builder()
                    .ok(stat.getCounter(AccessibilityStats.Type.ACCESSIBLE))
                    .nok(stat.getCounter(AccessibilityStats.Type.ANNOYING) + stat.getCounter(AccessibilityStats.Type.BLOCKING))
                    .doute(stat.getCounter(AccessibilityStats.Type.DOUBT))
                    .noAnswer(stat.getCounter(AccessibilityStats.Type.NOANSWER));

            if (Audit.Level.BEGINNER.equals(audit.getLevel())) {
                builder = builder.doute(stat.getCounter(AccessibilityStats.Type.DOUBT));
            }

            return builder.build();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (customStat != null) {
                return getString(com.orange.ocara.R.string.audit_results_custom_handicap);
            } else {
                if (position == 0) {
                    return getString(com.orange.ocara.R.string.audit_results_table_all_handicap);
                }

                final Handicap handicap = handicapAggregator.getHandicap(position - 1);
                return handicap.getName();
            }
        }

        public void setCustomStat(AccessibilityStats custom) {
            this.customStat = custom;

            notifyDataSetChanged();
        }
    }


    private final BaseAdapter chartChoicesAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return handicapAggregator.getHandicapIds().size() + 1;
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
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(com.orange.ocara.R.layout.handicap_type_choice, null, false);
            }

            ImageView icon = (ImageView) convertView.findViewById(com.orange.ocara.R.id.handicap_type_icon);
            TextView title = (TextView) convertView.findViewById(com.orange.ocara.R.id.handicap_type_title);

            if (position == 0) {
                icon.setImageResource(com.orange.ocara.R.drawable.ic_handicap_all);
                title.setText(com.orange.ocara.R.string.audit_results_table_all_handicap);
            } else {
                Handicap handicap = handicapAggregator.getHandicap(position - 1);

                picasso.load(Uri.parse(handicap.getIcon().toString())).placeholder(android.R.color.black).into(icon);
                title.setText(handicap.getName());
            }

            return convertView;
        }
    };

    private static class HandicapAggregator {
        private final List<String> handicapIds = new ArrayList<>();

        private final Map<String, AccessibilityStats> statsById = new LinkedHashMap<String, AccessibilityStats>();
        private final Map<String, Handicap> handicapById = new LinkedHashMap<String, Handicap>();

        private final AccessibilityStats totalStat = new AccessibilityStats();

        void update(Audit audit) {
            handicapIds.clear();

            statsById.clear();
            handicapById.clear();

            statsById.putAll(audit.computeStatsByHandicap());
            handicapById.putAll(audit.getRuleSet().getHandicapsById());
            handicapIds.addAll(handicapById.keySet());

            computeTotal();
        }

        private void computeTotal() {
            totalStat.clear();

            for (Map.Entry<String, Handicap> entry : handicapById.entrySet()) {
                AccessibilityStats stat = statsById.get(entry.getKey());

                if (stat == null) {
                    stat = new AccessibilityStats();
                    statsById.put(entry.getKey(), stat);
                }

                totalStat.plus(stat);
            }
        }

        List<String> getHandicapIds() {
            return handicapIds;
        }

        Handicap getHandicap(int position) {
            return getHandicap(handicapIds.get(position));
        }

        Handicap getHandicap(String handicapId) {
            return handicapById.get(handicapId);
        }

        AccessibilityStats getStat(int position) {
            return getStat(handicapIds.get(position));
        }

        AccessibilityStats getStat(String handicapId) {
            return statsById.get(handicapId);
        }

        AccessibilityStats getTotalStat() {
            return totalStat;
        }
    }
}


