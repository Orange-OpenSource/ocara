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

package com.orange.ocara.data.oldEntities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.orange.ocara.utils.tools.RefreshStrategy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import timber.log.Timber;

/**
 * Entity class for {@link AuditEntity}'s elements
 */
@Table(name = AuditObjectEntity.TABLE_NAME)
@Data
@ToString(exclude = {"audit", "parent", "questionAnswers"})
@EqualsAndHashCode(callSuper = true, exclude = {"audit", "parent", "questionAnswers"})
public class AuditObjectEntity extends Model implements Refreshable, Commentable, ReviewItemResponse {

    public static final String TABLE_NAME = "auditObjects";

    public static final String COLUMN_OBJECT_DESCRIPTION_ID = "descriptionId";
    public static final String COLUMN_OBJECT_NAME = "name";
    public static final String COLUMN_GLOBAL_RESPONSE = "globalResponse";
    public static final String COLUMN_AUDIT = "audit";
    public static final String COLUMN_PARENT_OBJECT = "parent";
    private final List<CommentEntity> comments = new ArrayList<>();
    private final List<QuestionAnswerEntity> questionAnswers = new ArrayList<>();
    private final List<AuditObjectEntity> children = new ArrayList<>();
    @Column(name = COLUMN_OBJECT_DESCRIPTION_ID, notNull = true)
    private String objectDescriptionId;
    @Column(name = COLUMN_OBJECT_NAME, notNull = true)
    private String name;
    @Column(name = COLUMN_GLOBAL_RESPONSE, notNull = true)
    private ResponseModel response = ResponseModel.NO_ANSWER;
    @Column(name = COLUMN_AUDIT, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private AuditEntity audit;
    @Column(name = COLUMN_PARENT_OBJECT, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private AuditObjectEntity parent;

    /**
     * Default constructor. Needed by ORM.
     */
    public AuditObjectEntity() {
        super();
    }

    /**
     * Constructor.
     *
     * @param audit       Audit
     * @param description ObjectDescription
     */
    public AuditObjectEntity(AuditEntity audit, EquipmentEntity description) {
        super();

        this.audit = audit;
        this.objectDescriptionId = description.getReference();

        this.name = description.getName();
    }


    /**
     * Constructor.
     *
     * @param parent      parent object
     * @param description ObjectDescription
     */
    public AuditObjectEntity(AuditObjectEntity parent, EquipmentEntity description) {
        super();

        this.audit = null;
        this.objectDescriptionId = description.getReference();

        this.name = description.getName();
        this.parent = parent;
    }

    public String getObjectDescriptionId() {
        return objectDescriptionId;
    }

    public String getName() {
        return this.name;
    }

    public List<AuditObjectEntity> getChildren() {
        return children;
    }

    public List<QuestionAnswerEntity> getQuestionAnswers() {
        return getMany(QuestionAnswerEntity.class, QuestionAnswerEntity.COLUMN_AUDIT_OBJECT);
    }

    /**
     * @return Associated RuleSet
     */
    public RulesetEntity getRuleSet() {
        if (audit != null) {
            Timber.d("Message=Retrieving ruleset for audit;AuditId=%d;AuditName=%s", audit.getId(), audit.getName());
            return audit.getRuleSet();
        }
        Timber.d("Message=Retrieving ruleset for parent auditobject;AuditObjectId=%d;AuditObjectName=%s", parent.getId(), parent.getName());
        return parent.getRuleSet();
    }

    public AuditEntity getAudit() {
        if (audit != null) {
            return audit;
        }
        return parent.getAudit();
    }

    /**
     * @return Associated ObjectDescription
     */
    @Override
    public EquipmentEntity getObjectDescription() {
        if (getRuleSet() != null) {
            for (EquipmentEntity objectDescription : getRuleSet().getObjectDescriptionsDb()) {
                if (objectDescriptionId.equals(objectDescription.getReference())) {
                    return objectDescription;
                }
            }
        }

        return null;
    }

    @Override
    public ResponseModel getResponse() {
        return response;
    }

    public void setResponse(ResponseModel response) {
        this.response = response;
    }


    /**
     * Returns true if this question contains at least one blocking rule with 'NOK' answer
     *
     * @return true if the current {@link AuditObjectEntity} has one blocking rule or more.
     */
    public boolean hasAtLeastOneBlockingRule() {
        for (QuestionAnswerEntity questionAnswer : getQuestionAnswers()) {
            if (questionAnswer.hasAtLeastOneBlockingRule()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Builds a complete list of rule answers from object content and
     * characteristics content
     *
     * @return a {@link List} of {@link RuleAnswerEntity}s
     */
    public List<RuleAnswerEntity> computeAllRuleAnswers() {
        List<RuleAnswerEntity> result = new LinkedList<>();
        computeAllRuleAnswers(result);
        return result;
    }

    public void computeAllRuleAnswers(List<RuleAnswerEntity> allRuleAnswsers) {
        for (QuestionAnswerEntity questionAnswer : getQuestionAnswers()) {
            allRuleAnswsers.addAll(questionAnswer.getRuleAnswers());
        }
        for (AuditObjectEntity characteristic : getChildren()) {
            characteristic.computeAllRuleAnswers(allRuleAnswsers);
        }
    }

    /**
     * To compute AccessibilityValue by response.
     *
     *  result AccessibilityValue by response to update
     */
//    public void computeStatsByHandicap(Map<String, AccessibilityStatsUiModel> result) {
//        Timber.i("Message=Updating AccessibilityStats for auditObject;AuditObjectId=%d", getId());
//        for (QuestionAnswerEntity qa : getQuestionAnswers()) {
//            qa.computeStatsByHandicap(result);
//        }
//
//        for (AuditObjectEntity characteristic : getChildren()) {
//            characteristic.computeStatsByHandicap(result);
//        }
//    }


    public void updateResponse() {
        this.response = computeResponse();
        if (parent != null) {
            parent.updateResponse();
        }
    }

    /**
     * To compute the Response from each QuestionAnswer.
     *
     * @return Response computed
     */
    ResponseModel computeResponse() {

        if (getQuestionAnswers().isEmpty() && getChildren().isEmpty()) {
            return ResponseModel.NO_ANSWER;
        }

        ResponseModel result = ResponseModel.NOT_APPLICABLE;

        for (QuestionAnswerEntity questionAnswer : getQuestionAnswers()) {
            ResponseModel questionResponse = questionAnswer.getResponse();
            result = ResponseModel.max(result, questionResponse);
        }

        for (AuditObjectEntity characteristic : getChildren()) {
            ResponseModel characteristicResponse = characteristic.getResponse();
            result = ResponseModel.max(result, characteristicResponse);
        }

        return result;

    }

    @Override
    public void refresh(RefreshStrategy strategy) {
        Timber.d("AuditObject is being refreshed...");
        if (strategy.isCommentsNeeded()) {
            Timber.d("... with comments");
            refreshComments();
        }

        questionAnswers.clear();

        if (strategy.getDepth() != null && RefreshStrategy.DependencyDepth.QUESTION_ANSWER.compareTo(strategy.getDepth()) <= 0) {
            refreshQuestionAnswers();

            for (QuestionAnswerEntity questionAnswer : getQuestionAnswers()) {
                questionAnswer.refresh(strategy);
            }

            updateResponse();
        }

        refreshChildren(strategy);
    }


    /**
     * To know if an auditObject has been audited or not.
     *
     * @return true if audited, false otherwise
     */
    public boolean isAudited() {
        return !ResponseModel.NO_ANSWER.equals(getResponse());
    }

    @Override
    public List<CommentEntity> getComments() {
        return null;
    }

    @Override
    public void attachComment(CommentEntity comment) {
        comment.setAuditObject(this);
    }

    private void refreshComments() {
        comments.clear();
        comments.addAll(getMany(CommentEntity.class, CommentEntity.COLUMN_AUDIT_OBJECT));
    }

    public void refreshChildren(RefreshStrategy strategy) {
        refreshChildren();

        for (AuditObjectEntity child : children) {
            child.refresh(strategy);
        }
    }

    public void refreshChildren() {
        children.clear();
        children.addAll(getMany(AuditObjectEntity.class, AuditObjectEntity.COLUMN_PARENT_OBJECT));
    }

    private void refreshQuestionAnswers() {
        questionAnswers.addAll(getMany(QuestionAnswerEntity.class, QuestionAnswerEntity.COLUMN_AUDIT_OBJECT));
    }

    /**
     * Checks if the current element has some characteristic children or not
     *
     * @param name a string to compare with
     * @return true if there is at least one child whose name matches the argument
     */
    public boolean hasCharacteristic(String name) {
        for (AuditObjectEntity characteristic : getChildren()) {
            if (name.equals(characteristic.getName())) {
                return true;
            }
        }
        return false;
    }
}
