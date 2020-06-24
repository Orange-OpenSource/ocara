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

package com.orange.ocara.ui.intent.export.docx;

import android.content.Context;

import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.data.cache.model.ResponseModel;
import com.orange.ocara.data.net.model.ProfileTypeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Value;
import timber.log.Timber;

@Value
@EqualsAndHashCode(callSuper = true)
class AuditPresenter extends Presenter<AuditEntity> {

    private final Context mContext;
    private AuditDetailPresenter details;
    private AuditStatsPresenter stats;

    private List<AuditObjectPresenter> auditObjects = new ArrayList<>();
    private List<AuditObjectPresenter> auditObjectsWithChildren = new ArrayList<>();
    private List<AuditObjectPresenter> auditObjectsWithAnomalyOrDoubt = new ArrayList<>();

    private List<CommentPresenter> audioAuditComments = new ArrayList<>();
    private List<CommentPresenter> textAuditComments = new ArrayList<>();
    private List<CommentPresenter> photoAuditComments = new ArrayList<>();
    private List<CommentPresenter> fileAuditComments = new ArrayList<>();

    private boolean hasAudioAuditComments;
    private boolean hasPhotoAuditComments;
    private boolean hasFileAuditComments;
    private boolean hasTextAuditComments;
    private boolean hasAuditComments;

    AuditPresenter(AuditEntity audit, final Map<String, ProfileTypeEntity> profilTypeFormRuleSet, Context context) {
        super(audit);
        mContext = context;

        details = new AuditDetailPresenter(value);
        stats = new AuditStatsPresenter(value, profilTypeFormRuleSet);

        buildAuditObjects();

        buildComments(audioAuditComments, CommentEntity.Type.AUDIO);
        hasAudioAuditComments = !audioAuditComments.isEmpty();

        buildComments(textAuditComments, CommentEntity.Type.TEXT);
        hasTextAuditComments = !textAuditComments.isEmpty();

        buildComments(photoAuditComments, CommentEntity.Type.PHOTO);
        hasPhotoAuditComments = !photoAuditComments.isEmpty();

        buildComments(fileAuditComments, CommentEntity.Type.FILE);
        hasFileAuditComments = !fileAuditComments.isEmpty();

        hasAuditComments = hasAudioAuditComments || hasPhotoAuditComments || hasTextAuditComments || hasFileAuditComments;

    }

    public List<QuestionAnswerPresenter> getQuestionsWithAnomaly() {
        return computeQuestionsHavingResponse(ResponseModel.NOK);
    }

    public List<QuestionAnswerPresenter> getQuestionsWithDoubt() {
        return computeQuestionsHavingResponse(ResponseModel.DOUBT);
    }

    private void buildComments(List<CommentPresenter> commentPresenters, CommentEntity.Type type) {
        commentPresenters.clear();

        for (CommentEntity comment : value.getComments()) {
            if (type.equals(comment.getType())) {
                Timber.i("CommentType=%s;AttachmentName=%s", comment.getType(), comment.getAttachment());
                commentPresenters.add(new CommentPresenter(comment));
            }
        }
    }

    private void buildAuditObjects() {
        auditObjects.clear();
        auditObjectsWithChildren.clear();
        auditObjectsWithAnomalyOrDoubt.clear();

        for (AuditObjectEntity auditObject : value.getObjects()) {
            final AuditObjectPresenter auditObjectPresenter = new AuditObjectPresenter(auditObject, mContext);

            auditObjects.add(auditObjectPresenter);
            auditObjectsWithChildren.add(auditObjectPresenter);

            // Only add children
            for (AuditObjectEntity child : auditObject.getChildren()) {
                auditObjectsWithChildren.add(new AuditObjectPresenter(child, mContext));
            }

            if (!auditObjectPresenter.getQuestionsInAnomalyOrDoubt().isEmpty()) {
                auditObjectsWithAnomalyOrDoubt.add(auditObjectPresenter);
            }
        }
    }

    private List<QuestionAnswerPresenter> computeQuestionsHavingResponse(ResponseModel response) {
        List<QuestionAnswerPresenter> ret = new ArrayList<>();

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
