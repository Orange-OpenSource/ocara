/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import com.orange.ocara.model.Audit;
import com.orange.ocara.model.AuditObject;
import com.orange.ocara.model.Comment;
import com.orange.ocara.modelStatic.Response;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class AuditPresenter extends Presenter<Audit> {

    private AuditDetailPresenter details;
    private AuditStatsPresenter stats;

    private List<AuditObjectPresenter> auditObjects = new ArrayList<AuditObjectPresenter>();
    private List<AuditObjectPresenter> auditObjectsWithChildren = new ArrayList<AuditObjectPresenter>();
    private List<AuditObjectPresenter> auditObjectsWithAnomalyOrDoubt = new ArrayList<AuditObjectPresenter>();

    private List<CommentPresenter> audioAuditComments = new ArrayList<CommentPresenter>();
    private List<CommentPresenter> textAuditComments = new ArrayList<CommentPresenter>();
    private List<CommentPresenter> photoAuditComments = new ArrayList<CommentPresenter>();

    private boolean hasAudioAuditComments;
    private boolean hasPhotoAuditComments;
    private boolean hasTextAuditComments;
    private boolean hasAuditComments;



    AuditPresenter(Audit audit) {
        super(audit);

        details = new AuditDetailPresenter(value);
        stats = new AuditStatsPresenter(value);

        buildAuditObjects();

        buildComments(audioAuditComments, Comment.Type.AUDIO);
        hasAudioAuditComments = !audioAuditComments.isEmpty();

        buildComments(textAuditComments, Comment.Type.TEXT);
        hasTextAuditComments = !textAuditComments.isEmpty();

        buildComments(photoAuditComments, Comment.Type.PHOTO);
        hasPhotoAuditComments = !photoAuditComments.isEmpty();

        hasAuditComments = hasAudioAuditComments || hasPhotoAuditComments || hasTextAuditComments;

    }

    public List<QuestionAnswerPresenter> getQuestionsWithAnomaly() {
        return computeQuestionsHavingResponse(Response.NOK);
    }

    public List<QuestionAnswerPresenter> getQuestionsWithDoubt() {
        return computeQuestionsHavingResponse(Response.DOUBT);
    }

    private void buildComments(List<CommentPresenter> commentPresenters, Comment.Type type) {
        commentPresenters.clear();

        for (Comment comment : value.getComments()) {
            if (type.equals(comment.getType())) {
                commentPresenters.add(new CommentPresenter(comment));
            }
        }
    }

    private void buildAuditObjects() {
        auditObjects.clear();
        auditObjectsWithChildren.clear();
        auditObjectsWithAnomalyOrDoubt.clear();

        for (AuditObject auditObject : value.getObjects()) {
            final AuditObjectPresenter auditObjectPresenter = new AuditObjectPresenter(auditObject);

            auditObjects.add(auditObjectPresenter);
            auditObjectsWithChildren.add(auditObjectPresenter);

            // Only add children
            for (AuditObject child : auditObject.getChildren()) {
                auditObjectsWithChildren.add(new AuditObjectPresenter(child));
            }

            if (!auditObjectPresenter.getQuestionsInAnomalyOrDoubt().isEmpty()) {
                auditObjectsWithAnomalyOrDoubt.add(auditObjectPresenter);
            }
        }



    }

    private List<QuestionAnswerPresenter> computeQuestionsHavingResponse(Response response) {
        List<QuestionAnswerPresenter> ret = new ArrayList<QuestionAnswerPresenter>();

        for (AuditObjectPresenter auditObjectPresenter : auditObjectsWithChildren) {
            for (QuestionAnswerPresenter anomalyQuestionAnswerPresenter : auditObjectPresenter.getQuestionsInAnomalyOrDoubt()) {

                if (anomalyQuestionAnswerPresenter.getResponse().hasSameValue(response)) {
                    ret.add(anomalyQuestionAnswerPresenter);
                }
            }
        }
        return ret;
    }


}
