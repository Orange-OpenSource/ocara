/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import com.orange.ocara.model.AuditObject;
import com.orange.ocara.model.Comment;
import com.orange.ocara.model.QuestionAnswer;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class AuditObjectPresenter extends Presenter<AuditObject> {

    private List<QuestionAnswerPresenter> questionsInAnomalyOrDoubt = new ArrayList<QuestionAnswerPresenter>();
    private List<AuditObject> auditObjectCharacteristic = new ArrayList<AuditObject>();

    private List<CommentPresenter> audioComments = new ArrayList<CommentPresenter>();
    private List<CommentPresenter> textComments = new ArrayList<CommentPresenter>();
    private List<CommentPresenter> photoComments = new ArrayList<CommentPresenter>();

    private boolean hasAudioComments;
    private boolean hasPhotoComments;
    private boolean hasTextComments;

    public AuditObjectPresenter(AuditObject auditObject) {
        super(auditObject);

        buildAnomaliesOrDoubts();

        buildComments(audioComments, Comment.Type.AUDIO);
        hasAudioComments = !audioComments.isEmpty();

        buildComments(textComments, Comment.Type.TEXT);
        hasTextComments = !textComments.isEmpty();

        buildComments(photoComments, Comment.Type.PHOTO);
        hasPhotoComments = !photoComments.isEmpty();
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

    protected static String getAuditObjectName(AuditObject auditObject) {
        if (auditObject.getParent() == null) {
            return auditObject.getName();
        }
        return auditObject.getParent().getName() + " / " + auditObject.getName();
    }



    public String getIconName() {
        String extension = FilenameUtils.getExtension(value.getObjectDescription().getIcon().getPath());

        return notNull(String.format ("%s.%s", value.getObjectDescriptionId(), extension));
    }

    private void buildAnomaliesOrDoubts() {
        questionsInAnomalyOrDoubt.clear();
        auditObjectCharacteristic.clear();

        buildAnomaliesOrDoubts(questionsInAnomalyOrDoubt, value.getQuestionAnswers());
        for(AuditObject characteristic : value.getChildren()) {

            auditObjectCharacteristic.add(characteristic);
            buildAnomaliesOrDoubts(questionsInAnomalyOrDoubt, characteristic.getQuestionAnswers());
        }
    }

    private void buildAnomaliesOrDoubts(List<QuestionAnswerPresenter> questionsInAnomalyOrDoubt, List<QuestionAnswer> questionAnswers) {
        for (QuestionAnswer questionAnswer : questionAnswers) {
            QuestionAnswerPresenter questionPresenter = new QuestionAnswerPresenter(questionAnswer);

            if (questionPresenter.isAnomalyOrDoubt()) {
                questionsInAnomalyOrDoubt.add(questionPresenter);
            }
        }
    }

    private void buildComments(List<CommentPresenter> commentPresenters, Comment.Type type) {
        commentPresenters.clear();

        for (Comment comment : value.getComments()) {
            if (type.equals(comment.getType())) {
                commentPresenters.add(new CommentPresenter(comment));
            }
        }
    }
}
