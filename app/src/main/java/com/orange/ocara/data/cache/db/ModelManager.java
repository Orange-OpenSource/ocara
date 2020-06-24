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

package com.orange.ocara.data.cache.db;

import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.data.cache.model.AuditorEntity;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.data.cache.model.Refreshable;
import com.orange.ocara.data.cache.model.RuleAnswerEntity;
import com.orange.ocara.data.cache.model.SiteEntity;
import com.orange.ocara.data.cache.model.SortCriteria;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.ui.tools.RefreshStrategy;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface ModelManager {

    /**
     * To terminate the manager.
     */
    void terminate();

    /**
     * Create an audit
     * @param ruleSet RulsetDetails
     * @param site    Site
     * @param name    the audit name
     * @param author  the auditor
     * @param level   audit level
     */
    AuditEntity createAudit(RulesetModel ruleSet, SiteEntity site, String name, AuditorEntity author, AuditEntity.Level level);

    /**
     * Create a new version for the audit, with same audited subObject
     *
     * @param audit for which a new version must be created
     * @return a new audit
     */
    AuditEntity createAuditWithNewVersion(AuditEntity audit);

    /**
     * Returns all the known audits
     *
     * @param name         filter
     * @param sortCriteria Sort criteria
     * @return all the known audits
     */
    List<AuditEntity> getAllAudits(String name, SortCriteria sortCriteria);

    /**
     * Returns the existing site for given id
     *
     * @param siteId site id
     * @return Site retrieved
     */
    SiteEntity getSite(Long siteId);

    /**
     * Returns the existing audit for given id
     *
     * @param auditId audit id
     * @return Audit retrieved
     */
    AuditEntity getAudit(Long auditId);

    /**
     * Check if an audit already exists from name and version.
     *
     * @param name    audit name
     * @param version audit version
     * @return true if exists, false otherwise
     */
    boolean checkAuditExistenceByNameAndVersion(String name, SiteEntity site, int version);

    boolean checkAuditExistence(Long auditId, String auditName, Integer auditVersion);

    /**
     * Check if a site already exists from noImmo.
     *
     * @return true if exists, false otherwise
     */
    boolean checkSiteExistenceByNoImmo(String noImmo);

    /**
     * Check if a site already exists from name.
     *
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
    void updateAudit(AuditEntity audit);

    /**
     * To update an AuditObject from a set of updated ruleAnswer.
     *
     * @param auditObject  AuditObject
     * @param updatedRules updated RuleAnswer set
     */
    void updateAuditObject(AuditObjectEntity auditObject, Set<RuleAnswerEntity> updatedRules);

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
    AuditObjectEntity createAuditObject(AuditEntity audit, EquipmentEntity objectDescription);

    /**
     * To persist a child AuditObject
     *
     * @param parent            parent audit object
     * @param objectDescription ObjectDescription
     */
    AuditObjectEntity createChildAuditObject(AuditObjectEntity parent, EquipmentEntity objectDescription);

    /**
     * To Update the auditobject children
     */
    AuditObjectEntity updateAuditObjectChildren(AuditObjectEntity parent, List<EquipmentEntity> childrenToCreate, List<AuditObjectEntity> childrenToRemove);

    /**
     * Returns the existing auditObject for given id
     *
     * @param auditObjectId audit object id
     * @return AuditObject retrieved
     */
    AuditObjectEntity getAuditObject(long auditObjectId);

    /**
     * To delete an AuditObject
     *
     * @param auditObject auditObject to delete
     */
    void deleteAuditObject(AuditObjectEntity auditObject);

    /**
     * To delete all AuditObject of the given audit
     *
     * @param audit audit whose auditObject will be deleted
     */
    void deleteAllAuditObjects(AuditEntity audit);

    /**
     * Delete a single comment
     */
    void deleteComment(CommentEntity comment);

    /**
     * Delete a list of comments
     */
    void deleteAllComments(List<CommentEntity> comments);

    /**
     * Refresh the model entity.
     *
     * @param entity   entity to refreh
     * @param strategy refresh strategy
     */
    void refresh(Refreshable entity, RefreshStrategy strategy);

    /**
     * Returns a list of site match the given noimmo or name
     *
     * @param constraint contraint
     * @return a list of site whose names match the given query
     */
    List<SiteEntity> searchSites(String constraint);

    /**
     * Returns a list of usernames matching the given name prefix
     *
     * @param name user name prefix
     * @return a list of Auditor matching the given name prefix
     */
    List<AuditorEntity> searchAuditors(String name);

}
