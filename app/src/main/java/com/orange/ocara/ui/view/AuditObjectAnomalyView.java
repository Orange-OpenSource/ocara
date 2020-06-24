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

package com.orange.ocara.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.orange.ocara.R;
import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.data.cache.model.QuestionAnswerEntity;
import com.orange.ocara.data.cache.model.ResponseModel;
import com.orange.ocara.data.cache.model.RuleAnswerEntity;
import com.orange.ocara.tools.ListUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

@EViewGroup(resName = "anomaly_accordion")
public class AuditObjectAnomalyView extends LinearLayout {


    @ViewById(resName = "accordion_header_button")
    Button headerButton;
    @ViewById(resName = "anomaly_details_view")
    View detailsView;
    AuditObjectEntity auditObject;
    List<RuleAnswerEntity> anomalies;
    List<CommentEntity> comments;
    private OnGroupClickListener onGroupClickListener;
    private OnCommentClickListener onCommentClickListener;

    /**
     * Constructor.
     *
     * @param context Context
     */
    public AuditObjectAnomalyView(Context context) {
        this(context, null);
    }

    /**
     * Constructor.
     *
     * @param context Context
     * @param attrs   AttributeSet)))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))
     */
    public AuditObjectAnomalyView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setOnGroupClickListener(OnGroupClickListener onGroupClickListener) {
        this.onGroupClickListener = onGroupClickListener;
    }

    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
    }

    public void bind(AuditObjectEntity auditObject) {
        this.auditObject = auditObject;
        headerButton.setText(auditObject.getName());
    }

    @Click(resName = "accordion_header_button")
    public void toggleExpanding() {
        if (detailsView.getVisibility() == GONE) {
            expandView();
        } else {
            collapseView();
        }
    }

    protected void collapseView() {

        detailsView.setVisibility(GONE);
        headerButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hardware_keyboard_arrow_down, 0, 0, 0);
    }

    protected void expandView() {
        if (anomalies == null || comments == null) {
            buildDetailsView();
        }
        detailsView.setVisibility(VISIBLE);
        headerButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hardware_keyboard_arrow_up, 0, 0, 0);
        if (onGroupClickListener != null) {
            onGroupClickListener.onExpand(this);
        }
    }

    private void buildDetailsView() {
        ViewGroup rulesView = detailsView.findViewById(R.id.rules_section);
        ViewGroup commentsView = detailsView.findViewById(R.id.comments_section);

        anomalies = new ArrayList<>();
        buildAnomalies();
        if (anomalies.isEmpty()) {
            rulesView.setVisibility(GONE);
        }
        for (RuleAnswerEntity ruleAnswer : anomalies) {
            AnomalyRuleView anomalyRuleView = AnomalyRuleView_.build(getContext());
            anomalyRuleView.bind(ruleAnswer);
            rulesView.addView(anomalyRuleView);
        }

        comments = ListUtils.newArrayList(auditObject.getComments());
        if (comments.isEmpty()) {
            commentsView.setVisibility(GONE);
        } else {
            Timber.d(
                    "Message=Adding comment to the AnomalyView for auditObject;AuditObjectId=%s;AuditObjectName=%s;AuditObjectResponse=%s;CommentsCount=%d;",
                    auditObject.getId(),
                    auditObject.getName(),
                    auditObject.getResponse(),
                    comments.size());
        }
        for (final CommentEntity comment : comments) {
            final CommentItemView commentView = CommentItemView_.build(getContext());
            commentView.bind(comment);
            commentsView.addView(commentView);
            commentView.setOnClickListener(v -> {
                if (onCommentClickListener != null) {
                    onCommentClickListener.onCommentClick(comment, commentView);
                }
            });
        }
    }

    private void buildAnomalies() {
        if (auditObject == null) {
            return;
        }
        buildAnomalies(auditObject);
        for (AuditObjectEntity child : auditObject.getChildren()) {
            buildAnomalies(child);
        }
    }

    private void buildAnomalies(AuditObjectEntity auditObject) {
        for (QuestionAnswerEntity questionAnswer : auditObject.getQuestionAnswers()) {
            for (RuleAnswerEntity ruleAnswer : questionAnswer.getRuleAnswers()) {
                if (ResponseModel.NOK.equals(ruleAnswer.getResponse())
                        || ResponseModel.BLOCKING.equals(ruleAnswer.getResponse())
                        || ResponseModel.ANNOYING.equals(ruleAnswer.getResponse())
                        || ResponseModel.DOUBT.equals(ruleAnswer.getResponse())) {
                    Timber.d(
                            "Message=Adding anomaly to the AnomalyView for auditObject;AuditObjectId=%s;AuditObjectName=%s;AuditObjectResponse=%s;Answer=%s;",
                            auditObject.getId(),
                            auditObject.getName(),
                            auditObject.getResponse(),
                            ruleAnswer.getResponse());
                    anomalies.add(ruleAnswer);
                }
            }
        }
    }


    /**
     * Interface definition for a callback to be invoked when a group in this
     * expandable list has been clicked.
     */
    public interface OnGroupClickListener {

        void onExpand(AuditObjectAnomalyView view);
    }


    public interface OnCommentClickListener {

        void onCommentClick(CommentEntity comment, View commentView);
    }
}
