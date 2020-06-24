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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.orange.ocara.BuildConfig;
import com.orange.ocara.R;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.business.service.impl.RuleSetServiceImpl;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.data.cache.model.ResponseModel;
import com.orange.ocara.data.net.model.ImpactValueEntity;
import com.orange.ocara.data.net.model.ProfileTypeEntity;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.tools.FileUtils;
import com.orange.ocara.ui.adapter.AuditObjectsAdapter;
import com.orange.ocara.ui.adapter.ChartChoiceAdapter;
import com.orange.ocara.ui.adapter.ChartPagerAdapter;
import com.orange.ocara.ui.dialog.AudioPlayerDialog;
import com.orange.ocara.ui.dialog.OcaraDialogBuilder;
import com.orange.ocara.ui.intent.export.AuditExportService;
import com.orange.ocara.ui.intent.export.AuditExportService_;
import com.orange.ocara.ui.model.AccessibilityStatsUiModel;
import com.orange.ocara.ui.model.HandicapAggregateUiModel;
import com.orange.ocara.ui.tools.RefreshStrategy;
import com.orange.ocara.ui.view.AuditObjectAnomalyView;
import com.orange.ocara.ui.view.AuditObjectAnomalyView_;
import com.orange.ocara.ui.view.AuditObjectsView;
import com.orange.ocara.ui.view.CommentItemView;
import com.orange.ocara.ui.view.CommentItemView_;
import com.orange.ocara.ui.view.ResultAuditView;
import com.orange.ocara.ui.widget.AuditorAutoCompleteView;
import com.orange.ocara.ui.widget.SiteAutoCompleteView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

import static com.orange.ocara.ui.model.AccessibilityStatsUiModel.NA_IMPACT_REF;

/**
 * Activity that display the content of the report
 */
@EActivity(R.layout.activity_audit_results)
@OptionsMenu(R.menu.audit_result)
public class ResultAuditActivity extends BaseActivityManagingAudit implements ResultAuditView {

    private static final int ACTION_SAVE_DOCUMENT = 1;
    private static final int ACTION_SHARE_DOCUMENT = 2;

    private static final RefreshStrategy STRATEGY = RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.RULE_ANSWER).commentsNeeded(true).build();
    private static final String FRAGMENT_EDIT_NAME = "fragment_edit_name";
    private final HandicapAggregateUiModel handicapAggregator = new HandicapAggregateUiModel();
    @ViewById(R.id.scrollview_container)
    ScrollView scrollContainer;
    @ViewById(R.id.audit_site)
    TextView auditSite;
    @ViewById(R.id.audit_name)
    TextView auditName;
    @ViewById(R.id.audit_user)
    TextView auditUser;
    @ViewById(R.id.audit_type)
    TextView auditType;
    @ViewById(R.id.audit_date)
    TextView auditDate;
    @ViewById(R.id.chart_pager)
    ViewPager auditChartPager;
    @ViewById(R.id.chart_title)
    TextView auditChartTitle;
    @ViewById(R.id.chart_choices)
    GridView auditChartChoices;
    @ViewById(R.id.path_layout)
    ViewGroup pathLayout;
    @ViewById(R.id.audited_objects)
    AuditObjectsView auditObjects;
    @ViewById(R.id.result_resume_table)
    TableLayout resumeTable;
    @ViewById(R.id.table_title)
    TableRow resumeTitle;
    @ViewById(R.id.audit_comment_layout)
    ViewGroup auditCommentLayout;
    @ViewById(R.id.audit_comment_container)
    ViewGroup auditCommentContainer;
    @ViewById(R.id.anomaly_layout)
    ViewGroup anomalyLayout;
    @Bean
    ChartChoiceAdapter chartChoicesAdapter;
    @Bean(RuleSetServiceImpl.class)
    RuleSetService mRuleSetService;
    private ChartPagerAdapter chartPagerAdapter;
    private AuditObjectsAdapter auditObjectsAdapter;

    private int exportAction = -1;
    private File exportFile;
    private boolean shouldFinishAfterExport = false;
    private RulesetEntity mRuleSet;

    @Override
    RefreshStrategy getRefreshStrategy() {
        return STRATEGY;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(R.string.audit_results_title);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean ret = super.onPrepareOptionsMenu(menu);
        if (audit != null) {
            menu.findItem(R.id.action_lock).setVisible(!AuditEntity.Status.TERMINATED.equals(audit.getStatus()));
        }
        return ret;
    }

    @Override
    void auditRefreshed() {
        super.auditRefreshed();

        if (audit != null) {
            updateDetails(audit);
        }
    }

    @AfterViews
    void afterViews() {
        initAdapter();
        setUpAuditedPath();
        setUpChart();
    }

    private void initAdapter() {
        chartPagerAdapter = new ChartPagerAdapter(this, handicapAggregator);
    }

    private void setUpAuditedPath() {
        auditObjectsAdapter = new AuditObjectsAdapter(this);
        auditObjects.setAdapter(auditObjectsAdapter);

        auditObjects.getAuditPath().getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            float pathWidth = auditObjects.getAuditPath().getWidth();
            int auditObjectItemWidth = getResources().getInteger(R.integer.auditObjectItemWidth);
            BigDecimal pathWithBg = BigDecimal.valueOf(pathWidth);
            BigDecimal auditObjectItemWidthBg = new BigDecimal(auditObjectItemWidth);
            BigDecimal divide = pathWithBg.divide(auditObjectItemWidthBg, BigDecimal.ROUND_HALF_UP);
            int numberAuditedObjects = divide.intValue();
            float freeSpace = pathWidth % auditObjectItemWidth;
            BigDecimal freeSpaceBg = BigDecimal.valueOf(freeSpace);
            BigDecimal numberAuditedObjectsLessOneBg = new BigDecimal(numberAuditedObjects - 1);
            BigDecimal result = freeSpaceBg.divide(numberAuditedObjectsLessOneBg, BigDecimal.ROUND_HALF_UP);
            int margin = result.intValue();

            Timber.v("auditPath setUpAuditedPath " + pathWidth + "auditObjectItemWitdh " + auditObjectItemWidth + " freeSpace " + freeSpace + " numberAuditedObjects " + numberAuditedObjects + " margin " + margin);

            auditObjects.getAuditPath().setItemMargin(margin);
        });
    }

    private void setUpChart() {
        chartChoicesAdapter.setHandicapAggregator(handicapAggregator);

        auditChartPager.setAdapter(chartPagerAdapter);

        auditChartChoices.setAdapter(chartChoicesAdapter);

        auditChartChoices.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        checkUniqueChoice(0);

        auditChartPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // no-op
            }

            @Override
            public void onPageSelected(int position) {
                checkUniqueChoice(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // no-op
            }
        });

        auditChartChoices.setOnItemClickListener((parent, view, position, id) -> {

            // Case of Total
            if (position == 0) {
                chartPagerAdapter.setCustomStat(null);
                auditChartPager.setCurrentItem(0, true);

                checkUniqueChoice(0);
                return;
            }

            final SparseBooleanArray checkedItemPositions = auditChartChoices.getCheckedItemPositions();

            List<Integer> selectedItems = new ArrayList<>();
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
                AccessibilityStatsUiModel custom = new AccessibilityStatsUiModel();

                for (int i : selectedItems) {
                    Timber.d("Message=Computing AccessibilityStats;SelectedItem=%d", i);
                    custom.plus(handicapAggregator.getStat(i - 1));
                }

                chartPagerAdapter.setCustomStat(custom);
                auditChartPager.setCurrentItem(0, true);

                auditChartChoices.clearChoices();
                for (int i : selectedItems) {
                    auditChartChoices.setItemChecked(i, true);
                }
            }
        });
    }

    protected void checkUniqueChoice(int position) {
        auditChartChoices.clearChoices();
        auditChartChoices.setItemChecked(position, true);

        auditChartTitle.setText(chartPagerAdapter.getPageTitle(position));
    }

    private void updateDetails(AuditEntity audit) {
        auditName.setText(getString(R.string.audit_item_name_format, audit.getName(), audit.getVersion()));
        updateRuleSetDetail(audit);
    }

    @Background
    void updateRuleSetDetail(final AuditEntity audit) {
        mRuleSet = audit.getRuleSet();
        updateAuditType(mRuleSet);
        updateHandicapAggregator(mRuleSet);
    }

    void updateHandicapAggregator(final RulesetEntity ruleSet) {
        final Map<String, ProfileTypeEntity> profileTypeFormRuleSet = mRuleSetService.getProfilTypeFormRuleSet(ruleSet);
        updateProfileTypeAdapter(profileTypeFormRuleSet);
    }

    @UiThread
    void updateProfileTypeAdapter(final Map<String, ProfileTypeEntity> profileTypeFormRuleSet) {
        handicapAggregator.update(audit, profileTypeFormRuleSet);

        final DateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.FRANCE);

        auditDate.setText(dateFormat.format(audit.getDate()));
        auditUser.setText(AuditorAutoCompleteView.format(audit.getAuthor()));
        auditSite.setText(SiteAutoCompleteView.format(audit.getSite()));

        updateChart();
        updatePath(audit);
        updateResumeTable(audit);
        updateAuditComments(audit);
        updateAnomalies(audit);
    }

    @UiThread
    void updateAuditType(RulesetEntity ruleSet) {
        auditType.setText(ruleSet.getType());
    }

    private void updateChart() {
        chartPagerAdapter.notifyDataSetChanged();
        chartChoicesAdapter.notifyDataSetChanged();
        auditChartTitle.setText(chartPagerAdapter.getPageTitle(auditChartPager.getCurrentItem()));
    }

    private void updatePath(AuditEntity audit) {
        auditObjectsAdapter.update(audit.getObjects());
    }

    private void updateResumeTable(AuditEntity audit) {

        addTableHeader(R.string.audit_results_table_no_impact_title);
        addTableHeader(R.string.audit_results_table_doubt_title);

        for (ImpactValueEntity impactValue : audit.getRuleSet().getImpactValuesDb()) {
            if (!NA_IMPACT_REF.equals(impactValue.getReference())) {
                addTableHeader(impactValue.getName());
            }
        }

        addTableValues();
    }

    private void addTableHeader(int textId) {
        TextView headerTextView = new TextView(this, null, R.style.ResumeCellHeader);
        headerTextView.setText(textId);
        headerTextView.setGravity(Gravity.CENTER);

        setTextAppearance(headerTextView, R.style.ResumeCellHeader);

        resumeTitle.addView(headerTextView);
    }

    private void addTableHeader(String text) {
        TextView headerTextView = new TextView(this, null, R.style.ResumeCellHeader);
        headerTextView.setText(text);
        headerTextView.setGravity(Gravity.CENTER);

        setTextAppearance(headerTextView, R.style.ResumeCellHeader);

        resumeTitle.addView(headerTextView);
    }

    /**
     * Handicap lines are created here. See the string "Tous-profils".
     */
    private void addTableValues() {
        int rowIndex = 0;
        for (String handicapId : handicapAggregator.getHandicapRef()) {
            // Inflate a new row and fill it
            final AccessibilityStatsUiModel stats = handicapAggregator.getStat(handicapId);
            final ProfileTypeEntity handicap = handicapAggregator.getHandicap(handicapId);
            Timber.i(
                    "Message=Adding a new row in the table;handicapId=%s;HandicapName=%s;OK=%d;Doubt=%d;Annoying=%d;Blocking=%d",
                    handicapId, handicap.getName(), stats.getMControleOkScore(), stats.getMControleDoubtScore(), stats.getMAnnoyingScore(), stats.getMBlockingScore());
            final View child = buildResumeRow(handicap.getIcon(), handicap.getName(), stats, rowIndex++);
            resumeTable.addView(child);
        }

        // Add total row
        final View child = buildResumeRow(R.drawable.ic_handicap_all, getString(R.string.audit_results_table_all_handicap), handicapAggregator.getTotalStat(), rowIndex++);
        resumeTable.addView(child);
    }

    private View buildResumeRow(int handicapIconResId, String handicapName, AccessibilityStatsUiModel stats, int rowIndex) {
        Timber.v("Message=Trying to load image;Icon=%s;RowName=%s", handicapIconResId, handicapName);
        return buildResumeRow(Picasso.with(this).load(handicapIconResId), handicapName, stats, rowIndex);
    }

    private View buildResumeRow(String handicapIcon, String handicapName, AccessibilityStatsUiModel stats, int rowIndex) {
        final String path = getExternalCacheDir() + File.separator + handicapIcon;
        File icon = new File(path);
        return buildResumeRow(Picasso.with(this).load(icon), handicapName, stats, rowIndex);
    }

    private View buildResumeRow(RequestCreator handicapIconRequest, String handicapName, AccessibilityStatsUiModel stats, int rowIndex) {

        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.result_resume_item, null);

        final List<ImpactValueEntity> impactValues = audit.getRuleSet().getImpactValuesDb();

        if (stats != null) {
            TextView accessibleResultTextView = new TextView(this, null, R.style.ResumeCellHeader);
            accessibleResultTextView.setText(String.valueOf(stats.getMControleOkScore()));
            accessibleResultTextView.setGravity(Gravity.CENTER);
            row.addView(accessibleResultTextView);

            TextView doubtResultTextView = new TextView(this, null, R.style.ResumeCellHeader);
            doubtResultTextView.setText(String.valueOf(stats.getMControleDoubtScore()));
            doubtResultTextView.setGravity(Gravity.CENTER);
            row.addView(doubtResultTextView);

            for (ImpactValueEntity impactValue : impactValues) {
                if (!NA_IMPACT_REF.equals(impactValue.getReference())) {
                    TextView textView = new TextView(this, null, R.style.ResumeCellHeader);
                    String counter = String.valueOf(stats.getCounter(impactValue));
                    textView.setText(counter);
                    textView.setGravity(Gravity.CENTER);
                    row.addView(textView);
                }
            }
        }

        final TextView handicapType = row.findViewById(R.id.resume_handicap_type);
        handicapType.setText(handicapName);

        if (handicapIconRequest != null) {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    handicapType.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(getResources(), bitmap), null, null, null);
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
            final int maxSize = getResources().getDimensionPixelSize(com.orange.ocara.R.dimen.resultResumeHandicapIconSize);
            handicapIconRequest.placeholder(android.R.color.black).resize(maxSize, maxSize).into(target);
        }

        int backGroundColor = (rowIndex % 2 == 0) ? com.orange.ocara.R.color.resultTableColorEven : com.orange.ocara.R.color.resultTableColorOdd;
        row.setBackgroundResource(backGroundColor);

        return row;
    }


    private void updateAnomalies(AuditEntity audit) {
        ViewGroup anomalyContainerView = findViewById(R.id.anomaly_container);
        anomalyContainerView.removeAllViews();


        for (AuditObjectEntity auditObject : audit.getObjects()) {
            final List<CommentEntity> comments = auditObject.getComments();
            final String auditObjectName = auditObject.getName();
            final String auditObjectIcon = auditObject.getObjectDescription() != null ? auditObject.getObjectDescription().getIcon() : "";
            if (ResponseModel.NOK.equals(auditObject.getResponse())
                    || ResponseModel.BLOCKING.equals(auditObject.getResponse())
                    || ResponseModel.ANNOYING.equals(auditObject.getResponse())
                    || ResponseModel.DOUBT.equals(auditObject.getResponse())
                    || !auditObject.getComments().isEmpty()) {

                final AuditObjectAnomalyView auditObjectAnomalyView = AuditObjectAnomalyView_.build(getApplicationContext());
                auditObjectAnomalyView.setOnGroupClickListener(this::scrollTo);

                auditObjectAnomalyView.setOnCommentClickListener((comment, commentView) -> ResultAuditActivity.this.onCommentClicked(auditObjectIcon, auditObjectName, comments, comment, commentView));
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
        while (true) {
            if (parentView.equals(scrollContainer)) {
                break;
            }
            yDistanceToControlsView += parentView.getTop();
            parentView = (View) parentView.getParent();
        }

        // Compute the final position value for the top and bottom of the control in the scroll view.
        final int topInScrollView = yDistanceToControlsView + view.getTop();

        // Post the scroll action to happen on the scrollView with the UI thread.
        scrollContainer.post(new Runnable() {
            @Override
            public void run() {
                scrollContainer.smoothScrollTo(0, topInScrollView);
                view.requestFocus();
            }
        });
    }


    private void updateAuditComments(AuditEntity audit) {
        if (audit.getComments().isEmpty()) {
            auditCommentLayout.setVisibility(View.GONE);
            return;
        }

        auditCommentLayout.setVisibility(View.VISIBLE);
        auditCommentContainer.removeAllViews();
        final List<CommentEntity> comments = audit.getComments();
        final String name = audit.getName();
        for (final CommentEntity comment : audit.getComments()) {
            CommentItemView commentItemView = CommentItemView_.build(this);
            commentItemView.bind(comment);
            auditCommentContainer.addView(commentItemView);
            commentItemView.setOnClickListener(v -> onCommentClicked(null, name, comments, comment, v));
        }
    }

    @Click(R.id.previous_chart_button)
    void onPreviousChartClicked() {
        int currentItem = auditChartPager.getCurrentItem();
        if (currentItem > 0) {
            currentItem--;
        }

        auditChartPager.setCurrentItem(currentItem, true);
    }

    @Click(R.id.next_chart_button)
    void onNextChartClicked() {
        int currentItem = auditChartPager.getCurrentItem();
        if (currentItem < auditChartPager.getAdapter().getCount() - 1) {
            currentItem++;
        }

        auditChartPager.setCurrentItem(currentItem, true);
    }

    private void onCommentClicked(String iconName, String name, List<CommentEntity> comments, CommentEntity comment, View commentView) {
        switch (comment.getType()) {
            case AUDIO:
                startAudioPlayer(comment.getAttachment(), commentView);
                break;
            case TEXT:
            case PHOTO:
            case FILE:
                showComment(iconName, name, comments, comment);
                break;
            default:
                break;
        }
    }

    private void showComment(String iconName, String name, List<CommentEntity> commentList, CommentEntity comment) {
        int selectedIndex = 0;
        List<CommentEntity> commentPhotoAndText = extractCommentPhotoAndText(commentList);
        int nbCommentPhotoAndText = commentPhotoAndText.size();
        String[] titles = new String[nbCommentPhotoAndText];
        String[] comments = new String[nbCommentPhotoAndText];
        String[] images = new String[nbCommentPhotoAndText];

        Timber.v("nbCommentPhotoAndText " + nbCommentPhotoAndText);
        for (int i = 0; i < nbCommentPhotoAndText; i++) {
            if (commentPhotoAndText.get(i).equals(comment)) {
                selectedIndex = i;
            }

            if (iconName != null) {
                titles[i] = getString(com.orange.ocara.R.string.illustration_activity_comment_auditobject_title, i + 1, name);
            } else {
                titles[i] = getString(com.orange.ocara.R.string.illustration_activity_comment_audit_title, i + 1, name);
            }

            comments[i] = commentPhotoAndText.get(i).getContent();

            if (commentPhotoAndText.get(i).getType().equals(CommentEntity.Type.PHOTO)
                    || commentPhotoAndText.get(i).getType().equals(CommentEntity.Type.FILE)) {
                images[i] = commentPhotoAndText.get(i).getAttachment();
            }

        }

        BrowseIllustrationsActivity_.intent(ResultAuditActivity.this)
                .selectedIndex(selectedIndex)
                .iconName(iconName)
                .titles(titles)
                .comments(comments)
                .images(images)
                .start();

    }

    private List<CommentEntity> extractCommentPhotoAndText(List<CommentEntity> comments) {
        List<CommentEntity> ret = new ArrayList<>();

        for (CommentEntity comment : comments) {
            if (comment.getType().equals(CommentEntity.Type.PHOTO) || comment.getType().equals(CommentEntity.Type.FILE) || comment.getType().equals(CommentEntity.Type.TEXT)) {
                ret.add(comment);
            }
        }
        return (ret);
    }

    private void startAudioPlayer(String attachment, View commentView) {
        FragmentManager fm = getSupportFragmentManager();

        int[] location = new int[2];
        commentView.getLocationOnScreen(location);

        AudioPlayerDialog audioPlayerDialog = AudioPlayerDialog.newInstance(attachment, -1, location[1]);
        audioPlayerDialog.show(fm, FRAGMENT_EDIT_NAME);
    }

    @OptionsItem(R.id.action_lock)
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
        audit.setStatus(AuditEntity.Status.TERMINATED);
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

    @OptionsItem(R.id.action_export_audit_docx)
    void onExportAuditToWordMenuItem() {
        exportAction = -1;
        exportFile = null;

        AlertDialog dialog = buildExportDialog().create();
        dialog.show();
    }

    private AlertDialog.Builder buildExportDialog() {
        int message;
        if (audit.getStatus().equals(AuditEntity.Status.TERMINATED)) {
            message = com.orange.ocara.R.string.export_docx_message_closed_audit;
        } else {
            message = com.orange.ocara.R.string.export_docx_message;
        }

        return new OcaraDialogBuilder(this)
                .setTitle(com.orange.ocara.R.string.export_docx_title) // title
                .setMessage(message) // message
                .setNeutralButton(com.orange.ocara.R.string.action_cancel, null)
                .setNegativeButton(com.orange.ocara.R.string.action_share, (dialog, which) -> {
                    exportAction = ACTION_SHARE_DOCUMENT;

                    setLoading(true);
                    AuditExportService_
                            .intent(ResultAuditActivity.this)
                            .toDocx(audit.getId())
                            .start();
                })
                .setPositiveButton(com.orange.ocara.R.string.action_save, (dialog, which) -> {
                    exportAction = ACTION_SAVE_DOCUMENT;

                    setLoading(true);
                    AuditExportService_
                            .intent(ResultAuditActivity.this)
                            .toDocx(audit.getId())
                            .start();
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
            default:
                throw new IllegalStateException("Should not findAll here");
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
            ListAuditActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
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
        exportFile = new File(path);

        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Timber.i("Message=Configuring intent for version >= VERSION_CODES.N");
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", exportFile);
        } else {
            Timber.i("Message=Configuring intent for version < VERSION_CODES.N");
            uri = Uri.fromFile(exportFile);
        }

        final Intent intent = ShareCompat.IntentBuilder.from(this)
                .setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path)))
                .setStream(uri)
                .setChooserTitle(R.string.list_audit_export_action)
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
     * To save a document by its path.
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

    private void setTextAppearance(TextView headerTextView, int styleId) {
        if (Build.VERSION.SDK_INT < 23) {
            TextViewCompat.setTextAppearance(headerTextView, styleId);
        } else {
            headerTextView.setTextAppearance(styleId);
        }
    }
}


