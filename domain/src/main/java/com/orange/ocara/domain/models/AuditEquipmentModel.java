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


package com.orange.ocara.domain.models;

import com.orange.ocara.utils.enums.Answer;

import java.util.ArrayList;
import java.util.List;

public class AuditEquipmentModel {
    private int id;
    private EquipmentModel equipment;
    private Answer answer;
    private AuditModel audit;
    private String name;
    private AuditEquipmentModel parent;
    private final List<AuditEquipmentModel> children = new ArrayList<>();
    private final List<CommentModel> comments = new ArrayList<>();
    private final List<QuestionAnswerModel> questionsAnswers = new ArrayList<>();
    private List<RuleModel> anomalies = new ArrayList<>();


    protected AuditEquipmentModel(BuilderWrapper<?> builder) {
        this.id = builder.id;
        this.equipment = builder.equipment;
        this.audit = builder.audit;
        this.answer = builder.answer;
        this.name = builder.name;
    }

    public boolean isTested() {
        return answer != Answer.NO_ANSWER;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuestionAnswerModel> getQuestionsAnswers() {
        return questionsAnswers;
    }

    public void addQuestionAnswer(QuestionAnswerModel questionAnswerModel) {
        questionsAnswers.add(questionAnswerModel);
    }

    public void setQuestionAnswers(List<QuestionAnswerModel> questionAnswerModels) {
        questionsAnswers.clear();
        questionsAnswers.addAll(questionAnswerModels);
    }

    public AuditEquipmentModel getParent() {
        return parent;
    }

    public void setParent(AuditEquipmentModel parent) {
        this.parent = parent;
    }

    public AuditModel getAudit() {
        return audit;
    }

    public void setAudit(AuditModel audit) {
        this.audit = audit;
    }

    public List<CommentModel> getComments() {
        return comments;
    }

    public EquipmentModel getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentModel equipment) {
        this.equipment = equipment;
    }

    public boolean isAudited() {
        return !answer.equals(Answer.NO_ANSWER);
    }

    public List<AuditEquipmentModel> getChildren() {
        return children;
    }

    public List<String> getChildrenReferences() {
        List<String> refs = new ArrayList<>();
        for (AuditEquipmentModel child : children) {
            refs.add(child.equipment.getReference());
        }
        return refs;
    }

    public void addChild(AuditEquipmentModel auditEquipmentModel) {
        children.add(auditEquipmentModel);
    }

    public void addComment(CommentModel comment) {
        comments.add(comment);
    }

    public void setComments(List<CommentModel> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAudit_id() {
        return audit.getId();
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }


    public List<RuleModel> getAnomalies() {
        return anomalies;
    }

    public void setAnomalies(List<RuleModel> anomalies) {
        this.anomalies = anomalies;
    }

    /**
     * Checks if the current element has some characteristic children or not
     *
     * @param name a string to compare with
     * @return true if there is at least one child whose name matches the argument
     */
    public boolean hasCharacteristic(String name) {
        for (AuditEquipmentModel characteristic : getChildren()) {
            if (name.equals(characteristic.getEquipment().getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * this class is here to add inheritance with builder class
     * reference : https://stackoverflow.com/a/52294689/9559548
     */
    protected abstract static class BuilderWrapper<T extends BuilderWrapper<T>> {
        private int id = -1;
        private EquipmentModel equipment;
        private Answer answer = Answer.NO_ANSWER;
        private AuditModel audit;
        private String name;

        // this method will return an object with the builder currently being used
        abstract protected T self();

        public T setId(int id) {
            this.id = id;
            return self();
        }

        public T setEquipment(EquipmentModel equipment) {
            this.equipment = equipment;
            return self();
        }

        public T setAudit(AuditModel audit) {
            this.audit = audit;
            return self();
        }

        public T setAnswer(Answer answer) {
            this.answer = answer;
            return self();
        }

        public T setName(String name) {
            this.name = name;
            return self();
        }

        public AuditEquipmentModel build() {
            return new AuditEquipmentModel(this);
        }
    }

    public static class Builder extends BuilderWrapper<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }
}
