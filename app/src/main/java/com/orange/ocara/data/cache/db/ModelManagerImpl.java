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

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.business.service.impl.RuleSetServiceImpl;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.data.cache.model.AuditorEntity;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.data.cache.model.QuestionAnswerEntity;
import com.orange.ocara.data.cache.model.Refreshable;
import com.orange.ocara.data.cache.model.RuleAnswerEntity;
import com.orange.ocara.data.cache.model.SiteEntity;
import com.orange.ocara.data.cache.model.SortCriteria;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.data.net.model.QuestionEntity;
import com.orange.ocara.data.net.model.RuleEntity;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.tools.FileUtils;
import com.orange.ocara.tools.StringUtils;
import com.orange.ocara.ui.tools.RefreshStrategy;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static timber.log.Timber.d;
import static timber.log.Timber.v;

/**
 * The model manager class.
 */
@EBean
public class ModelManagerImpl implements ModelManager {

    private static final String AUDITS_DIRECTORY = "audits";
    private static final String LIKE = " LIKE ?";
    private final Map<String, RulesetEntity> allRuleSetsById = new HashMap<>();
    private final Set<RulesetEntity> allRuleSets = new HashSet<>();

    @RootContext
    Context context;

    @Bean(RuleSetServiceImpl.class)
    RuleSetService mRuleSetService;

    @AfterInject
    void afterInject() {
        ActiveAndroid.initialize(context, true);
        allRuleSetsById.clear();
        allRuleSets.clear();
    }

    @Override
    public void terminate() {
        ActiveAndroid.dispose();
    }

    @Override
    public AuditEntity createAudit(RulesetModel ruleSet, SiteEntity site, String name, AuditorEntity author, AuditEntity.Level level) {
        AuditEntity audit = new AuditEntity(ruleSet.getReference(), ruleSet.getVersion(), site, name, author, level);
        d("Message=Creating a new audit;AuditName=%s;AuditStatus=%s", audit.getName(), audit.getStatus().name());
        checkAndSaveAudit(audit);
        v("Message=New audit created;AuditId=%d", audit.getId());
        return audit;
    }

    @Override
    public AuditEntity createAuditWithNewVersion(AuditEntity sourceAudit) {
        v("audit new version creating %d", sourceAudit.getId());
        long now = System.currentTimeMillis();
        AuditEntity audit = new AuditEntity(sourceAudit);

        ActiveAndroid.beginTransaction();
        refresh(sourceAudit, RefreshStrategy
                .builder()
                .depth(RefreshStrategy.DependencyDepth.RULE_ANSWER)
                .build());

        try {
            // Retrieve last one in order to know the last version number
            AuditEntity lastAuditVersion = new Select()
                    .from(AuditEntity.class)
                    .where(AuditEntity.COLUMN_NAME + " = ?", audit.getName())
                    .orderBy(AuditEntity.COLUMN_VERSION + " DESC")
                    .executeSingle();

            v("last audit version : %s ", lastAuditVersion);
            audit.setVersion(lastAuditVersion.getVersion() + 1);

            audit.save();

            // Create a copy of each AuditObject
            String rulesetRef = sourceAudit.getRuleSetRef();
            Integer rulesetVersion = sourceAudit.getRuleSetVersion();
            List<AuditObjectEntity> auditObjects = sourceAudit.getObjects();

            d(
                    "Message=Creating new audit's objects;AuditId=%d;RulesetRef=%s;RulesetVersion=%d;AuditObjectsCount=%d",
                    audit.getId(), rulesetRef, rulesetVersion, auditObjects.size());

            EquipmentEntity description;
            EquipmentEntity subDescription;
            List<AuditObjectEntity> childrenObjects;
            for (AuditObjectEntity sourceAuditObject : auditObjects) {
                description = mRuleSetService.getObjectDescriptionFromRef(
                        rulesetRef, rulesetVersion, sourceAuditObject.getObjectDescriptionId());

                AuditObjectEntity auditObject = createAuditObject(audit, description);

                // children copy
                childrenObjects = sourceAuditObject.getChildren();
                d(
                        "Message=Creating new audit's sub objects;AuditId=%d;AuditSubObjectId=%d;AuditChildrenObjectsCount=%d",
                        audit.getId(), auditObject.getId(), childrenObjects.size());

                for (AuditObjectEntity sourceAuditObjectChild : childrenObjects) {
                    subDescription = mRuleSetService.getObjectDescriptionFromRef(
                            rulesetRef, rulesetVersion, sourceAuditObjectChild.getObjectDescriptionId());

                    createChildAuditObject(auditObject, subDescription);
                }
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        v("audit new version created with id %d in %d", audit.getId(), System.currentTimeMillis() - now);
        return audit;
    }

    @Override
    public List<AuditEntity> getAllAudits(String name, SortCriteria sortCriteria) {
        v("select all audits name=%s, sortCriteria=%s", name, sortCriteria);
        long now = System.currentTimeMillis();

        From from = new Select()
                .all()
                .from(AuditEntity.class);

        if (StringUtils.isNotBlank(name)) {
            from = from.where(AuditEntity.TABLE_NAME + "." + AuditEntity.COLUMN_NAME + LIKE, "%" + name + "%");
        }

        from = sortCriteria.upgrade(from);
        List<AuditEntity> audits = from.execute();

        if (audits == null) {
            audits = new ArrayList<>();
        }

        v("select all audits done in %d", System.currentTimeMillis() - now);
        return audits;
    }

    @Override
    public SiteEntity getSite(Long siteId) {
        v("site retrieve : %d", siteId);

        SiteEntity site = Model.load(SiteEntity.class, siteId);

        v("site retrieved : %s", site);
        return site;
    }

    @Override
    public AuditEntity getAudit(Long auditId) {
        v("audit retrieve : %d", auditId);

        return Model.load(AuditEntity.class, auditId);
    }

    @Override
    public boolean checkAuditExistenceByNameAndVersion(String name, SiteEntity site, int version) {
        v("audit check existence : name %s version %d ", name, version);

        From from = new Select().from(AuditEntity.class).where(AuditEntity.COLUMN_NAME + " = ?", name);

        if (version != 0) {
            from = from.and(AuditEntity.COLUMN_VERSION + " = ?", version);
        }

        from = from.and(AuditEntity.COLUMN_SITE + " = ?", site.getId());

        boolean result = from.exists();

        v("audit check existence retrieved : %b", result);
        return result;
    }

    @Override
    public boolean checkAuditExistence(Long auditId, String auditName, Integer auditVersion) {
        d("Message=Checking audit existence;AuditName=%s;AuditVersion=%d", auditName, auditVersion);

        List<AuditEntity> audits = new Select()
                .from(AuditEntity.class)
                .where(AuditEntity.COLUMN_NAME + " = ?", auditName)
                .and(AuditEntity.COLUMN_VERSION + " = ?", auditVersion)
                .execute();

        boolean noAuditFound = audits.isEmpty();
        boolean sameAuditFound = (audits.size() == 1 && audits.get(0).getId() == auditId);

        d("Message=Audit checking results;NoAuditFound=%b;SameAuditFound=%b", noAuditFound, sameAuditFound);

        return !noAuditFound && !sameAuditFound;
    }


    public boolean checkSiteExistenceByNoImmo(String noImmo) {
        v("site check existence : NoImmo %s", noImmo);


        From from = new Select().from(SiteEntity.class);

        if (StringUtils.isNotBlank(noImmo)) {
            from = from.where(SiteEntity.COLUMN_NOIMMO + " = ?", noImmo);
        }


        boolean result = from.exists();

        v("site check existence retrieved : %b", result);
        return result;
    }

    public boolean checkSiteExistenceByName(String name) {
        v("site check existence : Name %s ", name);

        From from = new Select().from(SiteEntity.class);

        if (StringUtils.isNotBlank(name)) {
            from = from.where(SiteEntity.COLUMN_NAME + " = ?", name);
        }

        boolean result = from.exists();

        v("site check existence retrieved : %b", result);
        return result;
    }

    @Override
    public void deleteAudit(Long auditId) {
        v("audit delete : %d", auditId);

        Model.delete(AuditEntity.class, auditId);
        FileUtils.deleteQuietly(getAttachmentDirectory(auditId));

        v("audit deleted");
    }

    @Override
    public void updateAudit(AuditEntity audit) {
        v("audit update : %d", audit.getId());

        checkAndSaveAudit(audit);

        v("audit updated");
    }

    private void checkAndSaveAudit(AuditEntity audit) {
        v("audit checkAndSaveAudit %d", audit.getId());

        ActiveAndroid.beginTransaction();

        try {
            SiteEntity site = audit.getSite();
            if (site.getId() == null) {
                site.save();
            }

            AuditorEntity author = audit.getAuthor();
            if (author == null) {
                author = new AuditorEntity();
            }

            if (author.getId() == null && author.getUserName() != null) {
                AuditorEntity existing = findExistingAuditor(author);

                if (existing != null) {
                    audit.setAuthor(existing);
                } else {
                    author.save();
                }
            } else {
                author.save();
            }
            audit.setDate(Calendar.getInstance().getTime());
            audit.save();

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        v("audit checkAndSaveAudit done");
    }

    @Override
    public void updateAuditObject(AuditObjectEntity auditObject, Set<RuleAnswerEntity> updatedRules) {
        v("audit object update %d", auditObject.getId());
        long now = System.currentTimeMillis();

        ActiveAndroid.beginTransaction();

        try {
            Map<Long, AuditObjectEntity> characteristicsToUpdate = new HashMap<>();


            for (RuleAnswerEntity ruleAnswer : updatedRules) {
                ruleAnswer.save();
                AuditObjectEntity ruleAuditObject = ruleAnswer.getQuestionAnswer().getAuditObject();
                if (!ruleAuditObject.equals(auditObject)) {
                    characteristicsToUpdate.put(ruleAuditObject.getId(), ruleAuditObject);
                }
            }

            for (AuditObjectEntity characteristicToUpdate : characteristicsToUpdate.values()) {
                characteristicToUpdate.save();
            }

            auditObject.save();

            updateModificationDate(auditObject.getAudit());

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        v("audit object updated in %d", System.currentTimeMillis() - now);
    }

    @Override
    public File getAttachmentDirectory(Long auditId) {
        File auditsDirectory = getAuditsDirectory();
        File result = new File(auditsDirectory, "" + auditId);
        if (!result.exists()) {
            result.mkdirs();
        }
        return result;
    }


    @Override
    public AuditObjectEntity createAuditObject(AuditEntity audit, EquipmentEntity objectDescription) {
        AuditObjectEntity auditObject = new AuditObjectEntity(audit, objectDescription);
        return createAuditObject(auditObject, auditObject);
    }


    @Override
    public AuditObjectEntity createChildAuditObject(AuditObjectEntity parent, EquipmentEntity objectDescription) {
        AuditObjectEntity auditObject = new AuditObjectEntity(parent, objectDescription);
        return createAuditObject(auditObject, parent);
    }

    @Override
    public AuditObjectEntity updateAuditObjectChildren(AuditObjectEntity parent, List<EquipmentEntity> childrenToCreate, List<AuditObjectEntity> childrenToRemove) {

        v("auditObject children updating");
        long now = System.currentTimeMillis();

        ActiveAndroid.beginTransaction();

        try {
            for (EquipmentEntity childToCreate : childrenToCreate) {
                createChildAuditObject(parent, childToCreate);
            }

            for (AuditObjectEntity childToRemove : childrenToRemove) {
                deleteAuditObject(childToRemove);
            }

            parent.refresh(RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.RULE_ANSWER).build());

            parent.updateResponse();
            parent.save();

            updateModificationDate(parent.getAudit());

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        v("auditObject children updating : %d in %d", parent.getId(), System.currentTimeMillis() - now);
        return parent;
    }


    private AuditObjectEntity createAuditObject(final AuditObjectEntity object, AuditObjectEntity auditObjectParent) {
        v("auditObject creating");
        long now = System.currentTimeMillis();

        ActiveAndroid.beginTransaction();
        try {
            object.save();
            computeAllQuestionAnswers(object, auditObjectParent.getObjectDescriptionId());
            updateModificationDate(object.getAudit());

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        v("auditObject created : %d in %d", object.getId(), System.currentTimeMillis() - now);
        return object;
    }


    @Override
    public AuditObjectEntity getAuditObject(long auditObjectId) {
        v("auditObject retrieve : %d", auditObjectId);

        AuditObjectEntity auditObject = Model.load(AuditObjectEntity.class, auditObjectId);

        v("auditObject retrieved : %s", auditObjectId);
        return auditObject;
    }

    @Override
    public void deleteAuditObject(AuditObjectEntity auditObject) {
        v("auditObject delete : %d", auditObject.getId());

        ActiveAndroid.beginTransaction();

        try {
            deleteAllComments(auditObject.getComments());
            updateModificationDate(auditObject.getAudit());
            Model.delete(AuditObjectEntity.class, auditObject.getId());

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        v("auditObject deleted");
    }

    @Override
    public void deleteAllAuditObjects(AuditEntity audit) {
        v("deleting all auditObjects of audit %d", audit.getId());
        long now = System.currentTimeMillis();

        ActiveAndroid.beginTransaction();

        try {
            for (AuditObjectEntity auditObject : audit.getObjects()) {
                deleteAuditObject(auditObject);
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        v("deleting all auditObject done in %d", System.currentTimeMillis() - now);
    }

    @Override
    public void deleteComment(CommentEntity comment) {
        v("comment delete : %d", comment.getId());

        String attachment = comment.getAttachment();

        if (StringUtils.isNotBlank(attachment)
                && comment.getType() != CommentEntity.Type.FILE) {
            File attachementFile = new File(attachment);
            FileUtils.deleteQuietly(attachementFile);
        }
        AuditEntity audit = comment.getAudit();

        updateModificationDate(audit);
        Model.delete(CommentEntity.class, comment.getId());

        v("comment deleted");
    }

    @Override
    public void deleteAllComments(List<CommentEntity> comments) {
        v("deleting all comments");
        long now = System.currentTimeMillis();

        ActiveAndroid.beginTransaction();

        try {
            for (CommentEntity comment : comments) {
                deleteComment(comment);
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        v("deleting all comments done in %d", System.currentTimeMillis() - now);
    }

    @Override
    public void refresh(Refreshable entity, RefreshStrategy strategy) {
        if (entity != null) {
            v("refresh entity with strategy : %s", strategy);
            long now = System.currentTimeMillis();

            ActiveAndroid.beginTransaction();

            try {
                entity.refresh(strategy);
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }

            v("refresh entity done in %d", System.currentTimeMillis() - now);
        }
    }

    /**
     * To compute all QuestionAnswer related to an AuditObject
     *
     * @param auditObject AuditObject
     */
    private void computeAllQuestionAnswers(AuditObjectEntity auditObject, String objectIdParent) {
        final String ruleSetRef = auditObject.getAudit().getRuleSetRef();
        final Integer ruleSetVersion = auditObject.getAudit().getRuleSetVersion();

        final List<QuestionEntity> questionFormObjectRef = mRuleSetService.getQuestionsFromObjectRef(
                ruleSetRef,
                ruleSetVersion,
                auditObject.getObjectDescriptionId(),
                objectIdParent);

        for (QuestionEntity q : questionFormObjectRef) {
            createQuestionAnswer(auditObject, q);
        }
    }

    /**
     * To create a new QuestionAnswer from a question.
     *
     * @param auditObject AuditObject to attach the QuestionAnswer
     * @param question    Question
     * @return QuestionAnswer created
     */
    private QuestionAnswerEntity createQuestionAnswer(AuditObjectEntity auditObject, QuestionEntity question) {
        v("QuestionAnswer creating");
        QuestionAnswerEntity questionAnswer = new QuestionAnswerEntity(auditObject, question);
        questionAnswer.save();

        updateModificationDate(auditObject.getAudit());

        computeAllRuleAnswers(questionAnswer);

        v("QuestionAnswer created : %d", questionAnswer.getId());
        return questionAnswer;
    }

    /**
     * To compute all RuleAnswer releated to a QuestionAnswer.
     *
     * @param questionAnswer QuestionAnswer
     */
    private void computeAllRuleAnswers(QuestionAnswerEntity questionAnswer) {
        final RulesetEntity ruleSet = questionAnswer.getRuleSet();
        for (String ref : questionAnswer.getQuestion().getRulesRef()) {
            createRuleAnswer(questionAnswer, ruleSet.getRule(ref));
        }
    }

    /**
     * To create a RuleAnswer from a rule.
     *
     * @param questionAnswer QuestionAnswer to attach to
     * @param rule           Rule
     * @return RuleAnswer created
     */
    private RuleAnswerEntity createRuleAnswer(QuestionAnswerEntity questionAnswer, RuleEntity rule) {
        ActiveAndroid.beginTransaction();
        RuleAnswerEntity ruleAnswer = new RuleAnswerEntity(questionAnswer, rule);
        ruleAnswer.save();
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        return ruleAnswer;
    }


    @Override
    public List<SiteEntity> searchSites(String constraint) {
        From from = new Select().from(SiteEntity.class);

        if (StringUtils.isNotBlank(constraint)) {
            from = from.where(SiteEntity.COLUMN_NOIMMO + LIKE, "" + constraint + "%")
                    .or(SiteEntity.COLUMN_NAME + LIKE, "%" + constraint + "%")
                    .orderBy(SiteEntity.COLUMN_NOIMMO + " ASC");
        }

        List<SiteEntity> sites = from.execute();
        d("SQL Results : " + sites.size());
        return sites;
    }


    @Override
    public List<AuditorEntity> searchAuditors(String name) {
        From from = new Select().from(AuditorEntity.class);

        if (StringUtils.isNotBlank(name)) {
            from = from.where(AuditorEntity.COLUMN_USER_NAME + LIKE, "%" + name + "%").orderBy(AuditorEntity.COLUMN_USER_NAME + " ASC");
        }

        return from.execute();
    }

    private AuditorEntity findExistingAuditor(AuditorEntity author) {
        return new Select()
                .from(AuditorEntity.class)
                .where(AuditorEntity.COLUMN_USER_NAME + " = ?", author.getUserName())
                .executeSingle();
    }

    private File getAuditsDirectory() {
        return context.getDir(AUDITS_DIRECTORY, Context.MODE_PRIVATE);
    }


    private void updateModificationDate(AuditEntity audit) {
        audit.setDate(Calendar.getInstance().getTime());
        audit.save();
    }
}
