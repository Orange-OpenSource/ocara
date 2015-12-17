/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.orange.ocara.model.AuditObject;
import com.orange.ocara.model.Comment;
import com.orange.ocara.model.QuestionAnswer;
import com.orange.ocara.model.RuleAnswer;
import com.orange.ocara.modelStatic.Response;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EViewGroup(resName="anomaly_accordion")
public class AuditObjectAnomalyView extends LinearLayout {


    /**
     * Interface definition for a callback to be invoked when a group in this
     * expandable list has been clicked.
     */
    public interface OnGroupClickListener {

        void onExpand(AuditObjectAnomalyView view);
    }

    public interface OnCommentClickListener {

        void onCommentClick(Comment comment, View commentView);
    }

    private  OnGroupClickListener onGroupClickListener;
    private  OnCommentClickListener onCommentClickListener;

    @ViewById(resName="accordion_header_button")
    Button headerButton;

    @ViewById(resName="anomaly_details_view")
    View detailsView;


    AuditObject auditObject;

    List<RuleAnswer> anomalies;
    List<Comment> comments;

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


    public void bind(AuditObject auditObject) {
        this.auditObject = auditObject;
        headerButton.setText(auditObject.getName());
    }


    @Click(resName="accordion_header_button")
    public void toogleExpanding() {
        if (detailsView.getVisibility() == GONE) {
            expandView();
        } else {
            collapseView();
        }
    }

    protected void collapseView() {

        detailsView.setVisibility(GONE);
        headerButton.setCompoundDrawablesWithIntrinsicBounds(com.orange.ocara.R.drawable.ic_hardware_keyboard_arrow_down, 0, 0, 0);
    }

    protected void expandView() {
        if (anomalies == null || comments == null) {
            buildDetailsView();
        }
        detailsView.setVisibility(VISIBLE);
        headerButton.setCompoundDrawablesWithIntrinsicBounds(com.orange.ocara.R.drawable.ic_hardware_keyboard_arrow_up, 0, 0, 0);
        if (onGroupClickListener != null) {
            onGroupClickListener.onExpand(this);
        }
    }

    private void buildDetailsView() {
        anomalies = new ArrayList<RuleAnswer>();
        comments = new ArrayList<Comment>();

        buildAnomalies();
        comments.addAll(auditObject.getComments());

        ViewGroup rulesView = (ViewGroup) detailsView.findViewById(com.orange.ocara.R.id.rules_section);
        ViewGroup commentsView = (ViewGroup) detailsView.findViewById(com.orange.ocara.R.id.comments_section);


        if (anomalies.isEmpty()) {
            rulesView.setVisibility(GONE);
        }
        if (comments.isEmpty()) {
            commentsView.setVisibility(GONE);
        }

        for(RuleAnswer ruleAnswer : anomalies) {
            AnomalyRuleView anomalyRuleView = AnomalyRuleView_.build(getContext());
            anomalyRuleView.bind(ruleAnswer);
            rulesView.addView(anomalyRuleView);
        }

        for(final Comment comment : comments) {
            final CommentItemView commentView = CommentItemView_.build(getContext());
            commentView.bind(comment);
            commentsView.addView(commentView);
            commentView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCommentClickListener != null) {
                        onCommentClickListener.onCommentClick(comment, commentView);
                    }
                }
            });
        }
    }



    private void buildAnomalies() {
        if (auditObject == null) {
            return;
        }
        buildAnomalies(auditObject);
        for (AuditObject child : auditObject.getChildren()) {
            buildAnomalies(child);
        }
    }


    private void buildAnomalies(AuditObject auditObject) {
        for (QuestionAnswer questionAnswer : auditObject.getQuestionAnswers()) {
            for (RuleAnswer ruleAnswer : questionAnswer.getRuleAnswers()) {
                if (Response.NOK.equals(ruleAnswer.getResponse())) {
                    anomalies.add(ruleAnswer);
                }
            }
        }
    }
}
