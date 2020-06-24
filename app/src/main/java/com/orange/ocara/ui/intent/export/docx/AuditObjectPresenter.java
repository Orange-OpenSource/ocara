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

import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.data.cache.model.QuestionAnswerEntity;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class AuditObjectPresenter extends Presenter<AuditObjectEntity> {

    private List<QuestionAnswerPresenter> questionsInAnomalyOrDoubt = new ArrayList<>();
    private List<AuditObjectEntity> auditObjectCharacteristic = new ArrayList<>();

    private List<CommentPresenter> audioComments = new ArrayList<>();
    private List<CommentPresenter> textComments = new ArrayList<>();
    private List<CommentPresenter> photoComments = new ArrayList<>();
    private List<CommentPresenter> fileComments = new ArrayList<>();

    private boolean hasAudioComments;
    private boolean hasPhotoComments;
    private boolean hasTextComments;
    private boolean hasFileComments;
    private Context mContext;


    AuditObjectPresenter(AuditObjectEntity auditObject, Context context) {
        super(auditObject);
        mContext = context;

        buildAnomaliesOrDoubts();

        buildComments(audioComments, CommentEntity.Type.AUDIO);
        hasAudioComments = !audioComments.isEmpty();

        buildComments(textComments, CommentEntity.Type.TEXT);
        hasTextComments = !textComments.isEmpty();

        buildComments(photoComments, CommentEntity.Type.PHOTO);
        hasPhotoComments = !photoComments.isEmpty();

        buildComments(fileComments, CommentEntity.Type.FILE);
        hasFileComments = !fileComments.isEmpty();
    }

    private static String getAuditObjectName(AuditObjectEntity auditObject) {
        if (auditObject.getParent() == null) {
            return auditObject.getName();
        }
        return auditObject.getParent().getName() + " / " + auditObject.getName();
    }

    public ResponsePresenter getResponse() {
        return new ResponsePresenter(value.getResponse());
    }

    public String getId() {
        return notNull("" + value.getId());
    }

    public String getName() {
        return notNull(getAuditObjectName(value));
    }

    public String getIconName() {
        return value.getObjectDescription().getIcon();
    }


    private void buildAnomaliesOrDoubts() {
        questionsInAnomalyOrDoubt.clear();
        auditObjectCharacteristic.clear();


        List<QuestionAnswerEntity> questionAnswers = value.getQuestionAnswers();
        buildAnomaliesOrDoubts(questionsInAnomalyOrDoubt, questionAnswers);
        for (AuditObjectEntity characteristic : value.getChildren()) {

            auditObjectCharacteristic.add(characteristic);
            buildAnomaliesOrDoubts(questionsInAnomalyOrDoubt, characteristic.getQuestionAnswers());
        }
    }

    private void buildAnomaliesOrDoubts(List<QuestionAnswerPresenter> questionsInAnomalyOrDoubt, List<QuestionAnswerEntity> questionAnswers) {
        for (QuestionAnswerEntity questionAnswer : questionAnswers) {
            QuestionAnswerPresenter questionPresenter = new QuestionAnswerPresenter(questionAnswer);

            if (questionPresenter.isAnomalyOrDoubt()) {
                questionsInAnomalyOrDoubt.add(questionPresenter);
            }
        }
    }

    private void buildComments(List<CommentPresenter> commentPresenters, CommentEntity.Type type) {
        commentPresenters.clear();

        for (CommentEntity comment : value.getComments()) {
            if (type.equals(comment.getType())) {
                commentPresenters.add(new CommentPresenter(comment));
            }
        }
    }
}
