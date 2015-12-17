/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.modelStatic.Response;
import com.orange.ocara.modelStatic.RuleSet;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ModelManager {

    /**
     * To initialize the manager.
     */
    void initialize();

    /**
     * To terminate the manager.
     */
    void terminate();

    /**
     * Create an audit
     *
     * @param ruleSet RuleSet
     * @param site    Site
     * @param name    the audit name
     * @param author  the auditor
     * @param level   audit level
     */
    Audit createAudit(RuleSet ruleSet, Site site, String name, Auditor author, Audit.Level level);

    /**
     * Create a new version for the audit, with same audited objects
     *
     * @param audit for which a new version must be created
     * @return a new audit
     */
    Audit createAuditWithNewVersion(Audit audit);

    /**
     * Returns all the known audits
     *
     * @param name filter
     * @param sortCriteria Sort criteria
     * @return all the known audits
     */
    List<Audit> getAllAudits(String name, SortCriteria sortCriteria);

    /**
     * Returns the existing site for given id
     *
     * @param siteId site id
     * @return Site retrieved
     */
    Site getSite(Long siteId);

    /**
     * Returns the existing audit for given id
     *
     * @param auditId audit id
     * @return Audit retrieved
     */
    Audit getAudit(Long auditId);

    /**
     * Check if an audit already exists from name and version.
     *
     * @param name    audit name
     * @param version audit version
     * @return true if exists, false otherwise
     */
    boolean checkAuditExistenceByNameAndVersion(String name, Site site, int version);

    /**
     * Check if a site already exists from noImmo.
     *
     * @param noImmo
     * @return true if exists, false otherwise
     */
    boolean checkSiteExistenceByNoImmo(String noImmo);

    /**
     * Check if a site already exists from name.
     *
     * @param name
     * @return true if exists, false otherwise
     */
    boolean checkSiteExistenceByName(String name);

    /**
     * Delete an audit
     *
     * @param auditId audit id
     */
    void deleteAudit(Long auditId);

    /**
     * Update an audit
     *
     * @param audit audit to update
     */
    void updateAudit(Audit audit);


    /**
     * Update all audit response from 'fromResponse' to 'toResponse'
     *
     */
    void updateAuditRules(Audit audit, Response fromResponse, Response toResponse);

    /**
     * Update an auditor
     *
     * @param auditor auditor to update
     */
    void updateAuditor(Auditor auditor);

    /**
     * To update an AuditObject from a set of updated ruleAnswer.
     *
     * @param auditObject  AuditObject
     * @param updatedRules updated RuleAnswer set
     */
    void updateAuditObject(AuditObject auditObject, Set<RuleAnswer> updatedRules);

    /**
     * Returns the folder where are stored the audit attachments
     *
     * @param auditId audit id
     * @return Audit attachment directory
     */
    File getAttachmentDirectory(Long auditId);

    /**
     * To persist an AuditObject
     *
     * @param audit             audit
     * @param objectDescription ObjectDescription
     */
    AuditObject createAuditObject(Audit audit, ObjectDescription objectDescription);

    /**
     * To persist a child AuditObject
     *
     * @param parent            parent audit object
     * @param objectDescription ObjectDescription
     */
    AuditObject createChildAuditObject(AuditObject parent, ObjectDescription objectDescription);

    /**
     * To Update the auditobject children
     * @param parent
     * @param childrenToCreate
     * @param childrenToRemove
     * @return
     */
    AuditObject updateAuditObjectChildren(AuditObject parent, List<ObjectDescription> childrenToCreate, List<AuditObject> childrenToRemove);

    /**
     * Returns the existing auditObject for given id
     *
     * @param auditObjectId audit object id
     * @return AuditObject retrieved
     */
    AuditObject getAuditObject(Long auditObjectId);

    /**
     * To delete an AuditObject
     *
     * @param auditObject auditObject to delete
     */
    void deleteAuditObject(AuditObject auditObject);


    /**
     * To delete all AuditObject of the given audit
     *
     * @param audit audit whose auditObject will be deleted
     */
    void deleteAllAuditObjects(Audit audit);


    /**
     * Delete a single comment
     *
     * @param comment
     */
    void deleteComment(Comment comment);


    /**
     * Delete a list of comments
     *
     * @param comments
     */
    void deleteAllComments(List<Comment> comments);

    /**
     * Refresh the model entity.
     *
     * @param entity   entity to refreh
     * @param strategy refresh strategy
     */
    void refresh(Refreshable entity, RefreshStrategy strategy);

    /**
     * Returns the rules set for given id
     *
     * @param ruleSetId ruleSet id
     * @return RuleSet retrieved
     */
    RuleSet getRuleSet(String ruleSetId);

    /**
     * Returns all existing RuleSet
     *
     * @return all RuleSet
     */
    Collection<RuleSet> getAllRuleSet();

    /**
     * Returns a list of site match the given noimmo and/or name
     *
     * @param noImmo noImmo
     * @param name   name
     * @return a list of site whose names match the given query
     */
    List<Site> searchSites(String noImmo, String name);


    /**
     * Returns a list of usernames matching the given name prefix
     *
     * @param name user name prefix
     * @return a list of Auditor matching the given name prefix
     */
    List<Auditor> searchAuditors(String name);
}
