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
import com.activeandroid.query.Select;

import com.orange.ocara.utils.tools.RefreshStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import timber.log.Timber;

/**
 * an audit is an instance of a {@link RulesetEntity}
 */
@Builder
@Table(name = AuditEntity.TABLE_NAME)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class AuditEntity extends Model implements Refreshable, Commentable {

    public static final String TABLE_NAME = "audits";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_VERSION = "version";
    public static final String COLUMN_RULESET_ID = "ruleSetRef";
    public static final String COLUMN_SITE = "site";
    public static final String COLUMN_RULESET_VERSION = "rulesetVersion";
    static final String COLUMN_DATE = "date";
    static final String COLUMN_STATUS = "status";
    static final String COLUMN_LEVEL = "level";
    static final String COLUMN_AUTHOR = "author";
    /**
     * Minimum version.
     */
    static final int MIN_VERSION = 1;
    private final List<CommentEntity> comments = new ArrayList<>();
    private final List<AuditObjectEntity> objects = new ArrayList<>();
    @Column(name = COLUMN_NAME, notNull = true)
    private String name;

    @Column(name = COLUMN_AUTHOR, notNull = true)
    private AuditorEntity author;
    @Builder.Default
    @Column(name = COLUMN_VERSION, notNull = true)
    private int version = 1;
    @Column(name = COLUMN_RULESET_ID, notNull = true)
    private String ruleSetRef;
    @Column(name = COLUMN_RULESET_VERSION, notNull = true)
    private Integer ruleSetVersion;
    @Column(name = COLUMN_DATE, notNull = true)
    private Date date;
    @Builder.Default
    @Column(name = COLUMN_STATUS, notNull = true)
    private Status status = Status.IN_PROGRESS;
    @Builder.Default
    @Column(name = COLUMN_LEVEL, notNull = true)
    private Level level = Level.EXPERT;
    @Column(name = COLUMN_SITE, notNull = true)
    private SiteEntity site;

    /**
     * Default constructor. Needed by ORM.
     */
    public AuditEntity() {
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
    AuditEntity(RulesetEntity ruleSet, SiteEntity site, String name, AuditorEntity author, Level level) {
        super();

        this.site = site;
        this.name = name;
        this.author = author;
        this.level = level;
        this.date = Calendar.getInstance().getTime();
        this.version = MIN_VERSION;
        this.ruleSetRef = ruleSet.getReference();
        this.ruleSetVersion = Integer.valueOf(ruleSet.getVersion());
        this.status = Status.IN_PROGRESS;
    }

    public AuditEntity(String ruleSetReference, String ruleSetVersion, SiteEntity site, String name, AuditorEntity author, Level level) {
        super();

        this.site = site;
        this.name = name;
        this.author = author;
        this.level = level;
        this.date = Calendar.getInstance().getTime();
        this.version = MIN_VERSION;
        this.ruleSetRef = ruleSetReference;
        this.ruleSetVersion = Integer.valueOf(ruleSetVersion);
        this.status = Status.IN_PROGRESS;
    }

    /**
     * Copy constructor.
     *
     * @param audit audit to copy
     */
    public AuditEntity(AuditEntity audit) {
        this(audit.getRuleSet(), audit.getSite(), audit.getName(), audit.getAuthor(), audit.getLevel());
    }

    @Override
    public List<CommentEntity> getComments() {
        return comments;
    }

    public List<AuditObjectEntity> getObjects() {
        return objects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuditorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuditorEntity author) {
        this.author = author;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getRuleSetRef() {
        return ruleSetRef;
    }

    public void setRuleSetRef(String ruleSetRef) {
        this.ruleSetRef = ruleSetRef;
    }

    public Integer getRuleSetVersion() {
        return ruleSetVersion;
    }

    public void setRuleSetVersion(Integer ruleSetVersion) {
        this.ruleSetVersion = ruleSetVersion;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public SiteEntity getSite() {
        return site;
    }

    public void setSite(SiteEntity site) {
        this.site = site;
    }

    /**
     * Retrieves the ruleset assigned to the audit
     *
     * @return an instance of {@link RulesetEntity}. May be null, if it does not exist or if it is
     * not specified yet.
     */
    public RulesetEntity getRuleSet() {
        RulesetEntity ruleset = null;
        if (ruleSetRef != null && !ruleSetRef.isEmpty() && ruleSetVersion != null) {
            ruleset = new Select()
                    .from(RulesetEntity.class)
                    .where(RulesetEntity.REFERENCE + " = ?", ruleSetRef)
                    .and(RulesetEntity.VERSION + " = ?", ruleSetVersion)
                    .executeSingle();
            Timber.d("Message=Retrieving ruleset from audit;RulesetRef=%s;RulesetVersion=%d;", ruleSetRef, ruleSetVersion);
        } else {
            Timber.d("Message=No ruleset defined yet for current audit;");
        }
        return ruleset;
    }

    @Override
    public void refresh(RefreshStrategy strategy) {
        Timber.d("Audit is being refreshed...");
        if (strategy.isCommentsNeeded()) {
            Timber.d("... with comments");
            refreshComments();
        }

        if (strategy.getDepth() != null && RefreshStrategy.DependencyDepth.AUDIT_OBJECT.compareTo(strategy.getDepth()) <= 0) {
            refreshAuditObjects();

            for (AuditObjectEntity auditObject : getObjects()) {
                auditObject.refresh(strategy);
            }
        }
    }

    /**
     * To retrieve associated AuditObject.
     */
    public void refreshAuditObjects() {
        objects.clear();
        objects.addAll(getMany(AuditObjectEntity.class, AuditObjectEntity.COLUMN_AUDIT));
    }

    @Override
    public void attachComment(CommentEntity comment) {
        comment.setAudit(this);
    }

    public void refreshComments() {
        comments.clear();
        comments.addAll(getMany(CommentEntity.class, CommentEntity.COLUMN_AUDIT));
    }

    public String getAuthorName() {
        return getAuthor() != null ? getAuthor().getUserName() : "";
    }


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
}
