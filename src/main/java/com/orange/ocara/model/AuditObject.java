/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.modelStatic.Response;
import com.orange.ocara.modelStatic.RuleSet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Table(name = AuditObject.TABLE_NAME)
@Data
@ToString(exclude = {"audit", "parent"})
@EqualsAndHashCode(callSuper = true, exclude = {"audit", "parent"})
public class AuditObject extends Model implements Refreshable, Commentable {

    static final String TABLE_NAME = "auditObjects";

    static final String COLUMN_OBJECT_DESCRIPTION_ID = "descriptionId";
    static final String COLUMN_OBJECT_NAME = "name";
    static final String COLUMN_GLOBAL_RESPONSE = "globalResponse";
    static final String COLUMN_AUDIT = "audit";
    static final String COLUMN_PARENT_OBJECT = "parent";

    @Column(name = COLUMN_OBJECT_DESCRIPTION_ID, notNull = true)
    private String objectDescriptionId;
    @Column(name = COLUMN_OBJECT_NAME, notNull = true)
    private String name;
    @Column(name = COLUMN_GLOBAL_RESPONSE, notNull = true)
    private Response response = Response.NoAnswer;
    @Column(name = COLUMN_AUDIT, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private Audit audit;
    @Column(name = COLUMN_PARENT_OBJECT, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private AuditObject parent;

    private final List<Comment> comments = new ArrayList<Comment>();
    private final List<QuestionAnswer> questionAnswers = new ArrayList<QuestionAnswer>();
    private final List<AuditObject> children = new ArrayList<AuditObject>();



    /**
     * Default constructor. Needed by ORM.
     */
    public AuditObject() {
        super();
    }

    /**
     * Constructor.
     *
     * @param audit       Audit
     * @param description ObjectDescription
     */
    public AuditObject(Audit audit, ObjectDescription description) {
        super();

        this.audit = audit;
        this.objectDescriptionId = description.getName();

        this.name = description.getDescription();
    }


    /**
     * Constructor.
     *
     * @param parent      parent object
     * @param description ObjectDescription
     */
    /*package.*/ AuditObject(AuditObject parent, ObjectDescription description) {
        super();

        this.audit = null;
        this.objectDescriptionId = description.getName();

        this.name = description.getDescription();
        this.parent = parent;
    }


    /**
     * @return Associated RuleSet
     */
    public RuleSet getRuleSet() {
        if (audit != null) {
            return audit.getRuleSet();
        }
        return parent.getRuleSet();
    }

    public Audit getAudit() {
        if (audit != null) {
            return audit;
        }
        return parent.getAudit();
    }

    /**
     * @return Associated ObjectDescription
     */
    public ObjectDescription getObjectDescription() {
        return getRuleSet().getObjectDescription(objectDescriptionId);
    }



    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }



    /**
     * Returns true if this question contains at least one blocking rule with 'NOK' answer
     */
    public boolean hasAtLeastOneBlockingRule() {
        for(QuestionAnswer questionAnswer : getQuestionAnswers()) {
            if (questionAnswer.hasAtLeastOneBlockingRule()) {
                return true;
            }
        }
        return false;
    }

    /**
     * To compute QuestionAnswer by subject.
     *
     * @return QuestionAnswers by subject
     */
    public Map<String, List<QuestionAnswer>> computeQuestionAnswersBySubject() {

        Map<String, List<QuestionAnswer>> result = new LinkedHashMap<String, List<QuestionAnswer>>();
        computeQuestionAnswersBySubject(result);
        // add all characteristics questions
        for(AuditObject characteristic : getChildren()) {
            characteristic.computeQuestionAnswersBySubject(result);
        }
        return result;
    }

    protected void computeQuestionAnswersBySubject(Map<String, List<QuestionAnswer>> questionAnswersBySubject) {
        final List<QuestionAnswer> allQuestionAnswers = getQuestionAnswers();
        for (QuestionAnswer qa : allQuestionAnswers) {
            String subject = qa.getQuestion().getSubject();
            List<QuestionAnswer> qaList = questionAnswersBySubject.get(subject);
            if (qaList == null) {
                qaList = new ArrayList<QuestionAnswer>();
                questionAnswersBySubject.put(subject, qaList);
            }
            qaList.add(qa);
        }
    }


    /**
     * Builds a complete list of question answers from object content and
     * characteristics content
     * @return
     */
    public List<QuestionAnswer> computeAllQuestionAnswers() {
        List<QuestionAnswer> result = new LinkedList<>();
        result.addAll( getQuestionAnswers() );

        for(AuditObject characteristic : getChildren()) {
            result.addAll(characteristic.computeAllQuestionAnswers());
        }

        return result;
    }

    /**
     * Builds a complete list of rule answers from object content and
     * characteristics content
     * @return
     */
    public List<RuleAnswer> computeAllRuleAnswers() {
        List<RuleAnswer> result = new LinkedList<>();
        computeAllRuleAnswers(result);
        return result;
    }

    protected void computeAllRuleAnswers(List<RuleAnswer> allRuleAnswsers) {
        for (QuestionAnswer questionAnswer : getQuestionAnswers()) {
            allRuleAnswsers.addAll( questionAnswer.getRuleAnswers() );
        }
        for(AuditObject characteristic : getChildren()) {
            characteristic.computeAllRuleAnswers(allRuleAnswsers);
        }
    }

    /**
     * To compute AccessibilityValue by response.
     *
     * @param result AccessibilityValue by response to update
     */
    public void computeStatsByHandicap(Map<String, AccessibilityStats> result) {
        for (QuestionAnswer qa : getQuestionAnswers()) {
            qa.computeStatsByHandicap(result);
        }

        for(AuditObject characteristic : getChildren()) {
            characteristic.computeStatsByHandicap(result);
        }
    }



    /* package */ void updateReponse() {
        this.response = computeResponse();
        if (parent != null) {
            parent.updateReponse();
        }
    }

    /**
     * To compute the Response from each QuestionAnswer.
     *
     * @return Response computed
     */
    /* package*/ Response computeResponse() {

        if (getQuestionAnswers().isEmpty() && getChildren().isEmpty()) {
            return Response.NoAnswer;
        }

        Response result = Response.NotApplicable;

        for (QuestionAnswer questionAnswer : getQuestionAnswers()) {
            Response questionResponse = questionAnswer.getResponse();
            result = Response.max(result, questionResponse);
        }

        for (AuditObject characteristic : getChildren()) {
            Response characteristicResponse = characteristic.getResponse();
            result = Response.max(result, characteristicResponse);
        }

        return result;

    }

    @Override
    public void refresh(RefreshStrategy strategy) {


        if (strategy.isCommentsNeeded()) {
            refreshComments();
        }


        questionAnswers.clear();

        if (strategy.getDepth() != null && RefreshStrategy.DependencyDepth.QUESTION_ANSWER.compareTo(strategy.getDepth()) <= 0) {
            refreshQuestionAnswers();

            for (QuestionAnswer questionAnswer : getQuestionAnswers()) {
                questionAnswer.refresh(strategy);
            }

            updateReponse();
        }

        refreshChildren(strategy);

    }



    /**
     * To know if an auditObject has been audited or not.
     *
     * @return true if audited, false otherwise
     */
    public boolean isAudited() {
        return !Response.NoAnswer.equals(getResponse());
    }

    @Override
    public void attachComment(Comment comment) {
        comment.setAuditObject(this);
    }

    private void refreshComments() {
        comments.clear();
        comments.addAll(getMany(Comment.class, Comment.COLUMN_AUDIT_OBJECT));
    }

    private void refreshChildren(RefreshStrategy strategy) {
        children.clear();
        children.addAll(getMany(AuditObject.class, AuditObject.COLUMN_PARENT_OBJECT));

        for (AuditObject child : children) {
            child.refresh(strategy);
        }
    }

    private void refreshQuestionAnswers() {
        questionAnswers.addAll(getMany(QuestionAnswer.class, QuestionAnswer.COLUMN_AUDIT_OJBECT));
    }


    public boolean hasCharacteristic(String name) {
        for(AuditObject characteristic : getChildren()) {
            if (name.equals(characteristic.getObjectDescription().getName())) {
                return true;
            }
        }
        return false;
    }
}
