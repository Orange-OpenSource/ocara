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
import com.orange.ocara.modelStatic.RuleSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Table(name = Audit.TABLE_NAME)
@Data
@ToString(exclude = {"ruleSet", "objects"})
@EqualsAndHashCode(callSuper = true, exclude = {"ruleSet", "objects"})
public class Audit extends Model implements Refreshable, Commentable {

    static final String TABLE_NAME = "audits";

    static final String COLUMN_NAME = "name";
    static final String COLUMN_VERSION = "version";
    static final String COLUMN_RULESET_ID = "ruleSetId";
    static final String COLUMN_DATE = "date";
    static final String COLUMN_STATUS = "status";
    static final String COLUMN_LEVEL = "level";
    static final String COLUMN_SITE = "site";
    static final String COLUMN_AUTHOR = "author";

    /**
     * Minimum version.
     */
    static final int MIN_VERSION = 1;

    /**
     * Audit status.
     */
    public enum Status {
        /**
         * Audit start or in progress.
         */
        IN_PROGRESS,
        /**
         * Audit terminated.
         */
        TERMINATED
    }

    /**
     * Audit level.
     */
    public enum Level {
        /**
         * Audit level for beginner.
         */
        BEGINNER,
        /**
         * Audit level for expert.
         */
        EXPERT
    }

    @Column(name = COLUMN_NAME, notNull = true)
    private String name;
    @Column(name = COLUMN_AUTHOR, notNull = true)
    private Auditor author;
    @Column(name = COLUMN_VERSION, notNull = true)
    private int version = 1;
    @Column(name = COLUMN_RULESET_ID, notNull = true)
    private String ruleSetId;
    @Column(name = COLUMN_DATE, notNull = true)
    private Date date;
    @Column(name = COLUMN_STATUS, notNull = true)
    private Status status = Status.IN_PROGRESS;
    @Column(name = COLUMN_LEVEL, notNull = true)
    private Level level = Level.EXPERT;

    @Column(name = COLUMN_SITE, notNull = true)
    private Site site;

    private RuleSet ruleSet;

    private final List<Comment> comments = new ArrayList<Comment>();
    private final List<AuditObject> objects = new ArrayList<AuditObject>();

    /**
     * Default constructor. Needed by ORM.
     */
    public Audit() {
        super();
    }

    /**
     * Constructor.
     *
     * @param ruleSet ruleSet
     * @param site    Site
     * @param name    name
     * @param author  author
     * @param level   Level
     */
    /*package. */ Audit(RuleSet ruleSet, Site site, String name, Auditor author, Level level) {
        super();

        this.ruleSet = ruleSet;
        this.site = site;
        this.name = name;
        this.author = author;
        this.level = level;
        this.date = Calendar.getInstance().getTime();
        this.version = MIN_VERSION;
        this.ruleSetId = ruleSet.getId();
    }

    /**
     * Copy constructor.
     *
     * @param audit audit to copy
     */
    /*package.*/ Audit(Audit audit) {
        this(audit.getRuleSet(), audit.getSite(), audit.getName(), audit.getAuthor(), audit.getLevel());
    }

    /**
     * To compute stats by Handicap
     *
     * @return AccessibilityStats by Handicap
     */
    public Map<String, AccessibilityStats> computeStatsByHandicap() {
        Map<String, AccessibilityStats> result = new LinkedHashMap<String, AccessibilityStats>();

        // Initialize
        for (String handicapId : getRuleSet().getHandicapsById().keySet()) {
            result.put(handicapId, new AccessibilityStats());
        }

        // Compute
        for (AuditObject object : getObjects()) {
            object.computeStatsByHandicap(result);
        }

        return result;
    }

    @Override
    public void refresh(RefreshStrategy strategy) {

        if (strategy.isCommentsNeeded()) {
            refreshComments();
        }

        if (strategy.getDepth() != null && RefreshStrategy.DependencyDepth.AUDIT_OBJECT.compareTo(strategy.getDepth()) <= 0) {
            refreshAuditObjects();

            for (AuditObject auditObject : getObjects()) {
                auditObject.refresh(strategy);
            }
        }
    }

    /**
     * To retrieve associated AuditObject.
     */
    private void refreshAuditObjects() {
        objects.clear();
        objects.addAll(getMany(AuditObject.class, AuditObject.COLUMN_AUDIT));
    }

    @Override
    public void attachComment(Comment comment) {
        comment.setAudit(this);
    }

    private void refreshComments() {
        comments.clear();
        comments.addAll(getMany(Comment.class, Comment.COLUMN_AUDIT));
    }


}
