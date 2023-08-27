/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.domain.docexport.models;

import android.content.Context;

import com.orange.ocara.domain.interactors.ComputeAuditStatsByHandicapForExportTask;
import com.orange.ocara.domain.interactors.ComputeAuditStatsByHandicapTask;
import com.orange.ocara.domain.models.AuditEquipmentModel;
import com.orange.ocara.domain.models.AuditModel;
import com.orange.ocara.domain.models.CommentModel;
import com.orange.ocara.domain.models.DocReportProfileQuestionsRulesAnswersModel;
import com.orange.ocara.domain.models.ProfileTypeModel;
import com.orange.ocara.utils.enums.Answer;
import com.orange.ocara.utils.enums.CommentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Value;
import timber.log.Timber;

@Value
@EqualsAndHashCode(callSuper = true)
class AuditPresenter extends Presenter<AuditModel> {

    private final Context mContext;
    private final AuditDetailPresenter details;
    private final AuditStatsPresenter stats;

    private final List<AuditObjectPresenter> auditObjects = new ArrayList<>();
    private final List<AuditObjectPresenter> auditObjectsWithChildren = new ArrayList<>();
    private final List<AuditObjectPresenter> auditObjectsWithAnomalyOrDoubt = new ArrayList<>();

    private final List<CommentPresenter> audioAuditComments = new ArrayList<>();
    private final List<CommentPresenter> textAuditComments = new ArrayList<>();
    private final List<CommentPresenter> photoAuditComments = new ArrayList<>();
    private final List<CommentPresenter> fileAuditComments = new ArrayList<>();

    private final boolean hasAudioAuditComments;
    private final boolean hasPhotoAuditComments;
    private final boolean hasFileAuditComments;
    private final boolean hasTextAuditComments;
    private final boolean hasAuditComments;
//    ComputeAuditStatsByHandicapTask computeAuditStatsByHandicapTask;

    private final List<AuditProfileAnomalyPresenter> auditProfileAnomaly = new ArrayList<>();
    private final List<Integer> auditProfileNoAnomaly = new ArrayList<>();

//    private boolean hasNoAnomaly= false;

    AuditPresenter(ComputeAuditStatsByHandicapForExportTask computeAuditStatsByHandicapTask, AuditModel audit, final Map<String, ProfileTypeModel> profilTypeFormRuleSet, Context context) {
        super(audit);
        mContext = context;

        details = new AuditDetailPresenter(value);
        stats = new AuditStatsPresenter(computeAuditStatsByHandicapTask, value, profilTypeFormRuleSet);

        buildAuditObjects();

        buildComments(audioAuditComments, CommentType.AUDIO);
        hasAudioAuditComments = !audioAuditComments.isEmpty();

        buildComments(textAuditComments, CommentType.TEXT);
        hasTextAuditComments = !textAuditComments.isEmpty();

        buildComments(photoAuditComments, CommentType.PHOTO);
        hasPhotoAuditComments = !photoAuditComments.isEmpty();

        buildComments(fileAuditComments, CommentType.FILE);
        hasFileAuditComments = !fileAuditComments.isEmpty();

        hasAuditComments = hasAudioAuditComments || hasPhotoAuditComments || hasTextAuditComments || hasFileAuditComments;

    }

    AuditPresenter(ComputeAuditStatsByHandicapForExportTask computeAuditStatsByHandicapTask, AuditModel audit, final Map<String, ProfileTypeModel> profilTypeFormRuleSet, Context context, ArrayList<DocReportProfileQuestionsRulesAnswersModel> profiles) {
        this(computeAuditStatsByHandicapTask, audit, profilTypeFormRuleSet, context);
        buildAuditProfilesAnomalies(profiles);
//        hasNoAnomaly = auditProfileAnomaly.isEmpty();

    }

    private void buildAuditProfilesAnomalies(ArrayList<DocReportProfileQuestionsRulesAnswersModel> profiles) {
        auditProfileAnomaly.clear();

        for (int i = 0; i < profiles.size(); i++) {
            auditProfileAnomaly.add(new AuditProfileAnomalyPresenter(profiles.get(i)));
        }
        if (auditProfileAnomaly.isEmpty())
            auditProfileNoAnomaly.add(1);
        else
            auditProfileNoAnomaly.clear();


    }

    public boolean getHasNoProfileAnomalies() {
        return auditProfileAnomaly.isEmpty();
    }
    public List<QuestionAnswerPresenter> getQuestionsWithAnomaly() {
        return computeQuestionsHavingResponse(Answer.NOK);
    }

    public List<QuestionAnswerPresenter> getQuestionsWithDoubt() {
        return computeQuestionsHavingResponse(Answer.DOUBT);
    }

    private void buildComments(List<CommentPresenter> commentPresenters, CommentType type) {
        commentPresenters.clear();

        for (CommentModel comment : value.getComments()) {
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


        for (AuditEquipmentModel auditObject : value.getObjects()) {
            final AuditObjectPresenter auditObjectPresenter = new AuditObjectPresenter(auditObject, mContext);

            auditObjects.add(auditObjectPresenter);
            auditObjectsWithChildren.add(auditObjectPresenter);

            // Only add children
            for (AuditEquipmentModel child : auditObject.getChildren()) {
                auditObjectsWithChildren.add(new AuditObjectPresenter(child, mContext));
            }

            if (!auditObjectPresenter.getQuestionsInAnomalyOrDoubt().isEmpty()) {
                auditObjectsWithAnomalyOrDoubt.add(auditObjectPresenter);
            }
        }
    }

    private List<QuestionAnswerPresenter> computeQuestionsHavingResponse(Answer response) {
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
