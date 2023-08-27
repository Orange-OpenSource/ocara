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

import com.orange.ocara.domain.models.AuditEquipmentModel;
import com.orange.ocara.domain.models.CommentModel;
import com.orange.ocara.domain.models.QuestionAnswerModel;
import com.orange.ocara.data.cache.database.Tables.Comment;
import com.orange.ocara.utils.enums.CommentType;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class AuditObjectPresenter extends Presenter<AuditEquipmentModel> {

    private final List<QuestionAnswerPresenter> questionsInAnomalyOrDoubt = new ArrayList<>();
    private final List<AuditEquipmentModel> auditObjectCharacteristic = new ArrayList<>();

    private final List<CommentPresenter> audioComments = new ArrayList<>();
    private final List<CommentPresenter> textComments = new ArrayList<>();
    private final List<CommentPresenter> photoComments = new ArrayList<>();
    private final List<CommentPresenter> fileComments = new ArrayList<>();

    private final boolean hasAudioComments;
    private final boolean hasPhotoComments;
    private final boolean hasTextComments;
    private final boolean hasFileComments;
    private final Context mContext;


    AuditObjectPresenter(AuditEquipmentModel auditObject, Context context) {
        super(auditObject);
        mContext = context;

        buildAnomaliesOrDoubts();

        buildComments(audioComments, CommentType.AUDIO);
        hasAudioComments = !audioComments.isEmpty();

        buildComments(textComments, CommentType.TEXT);
        hasTextComments = !textComments.isEmpty();

        buildComments(photoComments, CommentType.PHOTO);
        hasPhotoComments = !photoComments.isEmpty();

        buildComments(fileComments, CommentType.FILE);
        hasFileComments = !fileComments.isEmpty();
    }

    private static String getAuditObjectName(AuditEquipmentModel auditObject) {
        if (auditObject.getParent() == null) {
//            return auditObject.getEquipment().getName();
            return auditObject.getName();

        }
//        return auditObject.getParent().getEquipment().getName() + " / " + auditObject.getEquipment().getName();
        return auditObject.getParent().getName() + " / " + auditObject.getName();

    }

    public ResponsePresenter getResponse() {
        return new ResponsePresenter(value.getAnswer());
    }

    public String getId() {
        return notNull("" + value.getId());
    }

    public String getName() {
        return notNull(getAuditObjectName(value));
    }

    public String getIconName() {
        return value.getEquipment().getIcon();
    }


    private void buildAnomaliesOrDoubts() {
        questionsInAnomalyOrDoubt.clear();
        auditObjectCharacteristic.clear();


        List<QuestionAnswerModel> questionAnswers = value.getQuestionsAnswers();
        buildAnomaliesOrDoubts(questionsInAnomalyOrDoubt, questionAnswers);
        for (AuditEquipmentModel characteristic : value.getChildren()) {

            auditObjectCharacteristic.add(characteristic);
            buildAnomaliesOrDoubts(questionsInAnomalyOrDoubt, characteristic.getQuestionsAnswers());
        }
    }

    private void buildAnomaliesOrDoubts(List<QuestionAnswerPresenter> questionsInAnomalyOrDoubt, List<QuestionAnswerModel> questionAnswers) {
        for (QuestionAnswerModel questionAnswer : questionAnswers) {

            QuestionAnswerPresenter questionPresenter = new QuestionAnswerPresenter(value,questionAnswer);

            if (questionPresenter.isAnomalyOrDoubt()) {
                questionsInAnomalyOrDoubt.add(questionPresenter);
            }
        }
    }

    private void buildComments(List<CommentPresenter> commentPresenters, CommentType type) {
        commentPresenters.clear();

        for (CommentModel comment : value.getComments()) {
            if (type.equals(comment.getType())) {
                commentPresenters.add(new CommentPresenter(comment));
            }
        }
    }

    public List<QuestionAnswerPresenter> getQuestionsInAnomalyOrDoubt() {
        return questionsInAnomalyOrDoubt;
    }
}
