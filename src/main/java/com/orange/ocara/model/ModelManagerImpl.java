/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.modelStatic.Question;
import com.orange.ocara.modelStatic.Response;
import com.orange.ocara.modelStatic.Rule;
import com.orange.ocara.modelStatic.RuleSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
/* package. */ class ModelManagerImpl implements ModelManager {

    private static final String AUDITS_DIRECTORY = "audits";

    private final Context context;
    private final Map<String, RuleSet> allRuleSetsById = new HashMap<String, RuleSet>();
    private final Set<RuleSet> allRuleSets = new HashSet<RuleSet>();

    @Inject
    RuleSetLoader ruleSetLoader;
    @Inject
    SitesLoader sitesLoader;

    /**
     * Constructor.
     *
     * @param context
     */
    @Inject
    public ModelManagerImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void initialize() {
        ActiveAndroid.initialize(context, true);

        allRuleSetsById.clear();
        allRuleSets.clear();

        Set<String> ruleSetIds = ruleSetLoader.getInstalledRuleSetIds();
        for (String ruleSetId : ruleSetIds) {
            RuleSet ruleSet = ruleSetLoader.loadInstalledRuleSet(ruleSetId);
            allRuleSetsById.put(ruleSetId, ruleSet);
            allRuleSets.add(ruleSet);
        }

        // creates/updates all uninstalled sites
        Set<String> sitesPackages = sitesLoader.getUninstalledSitePackages();
        for (String packageName : sitesPackages) {
            installSitePackage(packageName);
        }
    }

    private void installSitePackage(String packageName) {
        Timber.v("site package installing %s", packageName);
        long now = System.currentTimeMillis();
        List<Site> sites = sitesLoader.installSitePackage(packageName);

        Timber.v("package contains %d sites", sites.size());

        ActiveAndroid.beginTransaction();

        try {
            for (Site site : sites) {
                site.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        Timber.v("site package installed %s in %d", packageName, System.currentTimeMillis() - now);
    }

    @Override
    public void terminate() {
        ActiveAndroid.dispose();
    }

    @Override
    public Audit createAudit(RuleSet ruleSet, Site site, String name, Auditor author, Audit.Level level) {
        Timber.v("audit creating");

        Audit audit = new Audit(ruleSet, site, name, author, level);
        checkAndSaveAudit(audit);

        Timber.v("audit created : %d", audit.getId());
        return audit;
    }

    @Override
    public Audit createAuditWithNewVersion(Audit sourceAudit) {
        Timber.v("audit new version creating %d", sourceAudit.getId());
        long now = System.currentTimeMillis();

        Audit audit = new Audit(sourceAudit);

        ActiveAndroid.beginTransaction();

        try {
            // Retrieve last one in order to know the last version number
            Audit lastAuditVersion = new Select().from(Audit.class)
                    .where(Audit.COLUMN_NAME + " = ?", audit.getName())
                    .orderBy(Audit.COLUMN_VERSION + " DESC")
                    .executeSingle();

            Timber.v("last audit version : %s ", lastAuditVersion);
            audit.setVersion(lastAuditVersion.getVersion() + 1);

            audit.save();

            // Create a copy of each AuditObject
            List<AuditObject> auditObjects = sourceAudit.getObjects();
            for (AuditObject sourceAuditObject : auditObjects) {


                AuditObject auditObject = createAuditObject(audit, sourceAuditObject.getObjectDescription());

                // children copy
                for (AuditObject sourceAuditObjectChild : sourceAuditObject.getChildren()) {
                    createChildAuditObject(auditObject, sourceAuditObjectChild.getObjectDescription());
                }
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        Timber.v("audit new version created with id %d in %d", audit.getId(), System.currentTimeMillis() - now);
        return audit;
    }

    @Override
    public List<Audit> getAllAudits(String name, SortCriteria sortCriteria) {
        Timber.v("select all audits name=%s, sortCriteria=%s", name, sortCriteria);
        long now = System.currentTimeMillis();

        From from = new Select()
                .all()
                .from(Audit.class);

        if (StringUtils.isNotBlank(name)) {
            from = from.where(Audit.TABLE_NAME + "." + Audit.COLUMN_NAME + " LIKE ?", "%" + name + "%");
        }

        from = sortCriteria.upgrade(from);
        List<Audit> audits = from.execute();
        for (Audit audit : audits) {
            updateAuditRuleSet(audit);
        }

        Timber.v("select all audits done in %d", System.currentTimeMillis() - now);
        return audits;
    }

    @Override
    public Site getSite(Long siteId) {
        Timber.v("site retrieve : %d", siteId);

        Site site = Model.load(Site.class, siteId);

        Timber.v("site retrieved : %s", site);
        return site;
    }

    @Override
    public Audit getAudit(Long auditId) {
        Timber.v("audit retrieve : %d", auditId);

        Audit audit = Model.load(Audit.class, auditId);
        updateAuditRuleSet(audit);

        Timber.v("audit retrieved : %s", audit);
        return audit;
    }

    @Override
    public boolean checkAuditExistenceByNameAndVersion(String name, Site site, int version) {
        Timber.v("audit check existence : name %s version %d ", name, version);

        From from = new Select().from(Audit.class)
                .where(Audit.COLUMN_NAME + " = ?", name);

        if (version != 0) {
            from = from.and(Audit.COLUMN_VERSION + " = ?", version);
        }

        from = from.and(Audit.COLUMN_SITE + " = ?", site.getId());

        boolean result = from.exists();

        Timber.v("audit check existence retrieved : %b", result);
        return result;
    }


    public boolean checkSiteExistenceByNoImmo(String noImmo) {
        Timber.v("site check existence : NoImmo %s", noImmo);


        From from = new Select().from(Site.class);

        if (StringUtils.isNotBlank(noImmo)) {
            from = from.where(Site.COLUMN_NOIMMO + " = ?", noImmo );
        }



        boolean result = from.exists();

        Timber.v("site check existence retrieved : %b", result);
        return result;
    }

    public boolean checkSiteExistenceByName(String name) {
        Timber.v("site check existence : Name %s ", name);


        From from = new Select().from(Site.class);


        if (StringUtils.isNotBlank(name)) {
            from = from.where(Site.COLUMN_NAME + " = ?", name);
        }


        boolean result = from.exists();

        Timber.v("site check existence retrieved : %b", result);
        return result;
    }

    @Override
    public void deleteAudit(Long auditId) {
        Timber.v("audit delete : %d", auditId);

        Model.delete(Audit.class, auditId);
        FileUtils.deleteQuietly(getAttachmentDirectory(auditId));

        Timber.v("audit deleted");
    }

    @Override
    public void updateAudit(Audit audit) {
        Timber.v("audit update : %d", audit.getId());

        checkAndSaveAudit(audit);

        Timber.v("audit updated");
    }

    @Override
    public void updateAuditRules(Audit audit, Response fromResponse, Response toResponse) {

        Timber.v("audit updating rules : %d", audit.getId());

        ActiveAndroid.beginTransaction();
        try {

            audit.refresh(RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.RULE_ANSWER).build());

            for(AuditObject auditObject : audit.getObjects()) {
                for(AuditObject characteristic : auditObject.getChildren()) {
                    updateAuditObjectRules(characteristic, fromResponse, toResponse);
                }
                updateAuditObjectRules(auditObject, fromResponse, toResponse);
            }

            ActiveAndroid.setTransactionSuccessful();

        } finally {
            ActiveAndroid.endTransaction();
        }

        Timber.v("audit updating rules");
    }

    private void updateAuditObjectRules(AuditObject auditObject, Response fromResponse, Response toResponse) {
        for(QuestionAnswer questionAnswer : auditObject.getQuestionAnswers()) {
            for(RuleAnswer ruleAnswer : questionAnswer.getRuleAnswers()) {
                if (ruleAnswer.getResponse().equals(fromResponse)) {
                    ruleAnswer.setResponse(toResponse);
                    ruleAnswer.save();
                }
            }
            Response questionAnswerResponse = questionAnswer.computeResponse();
            questionAnswer.setResponse(questionAnswerResponse);
        }
        Response auditObjectResponse = auditObject.computeResponse();
        if (auditObjectResponse != auditObject.getResponse()) {
            // global object response has changed
            auditObject.setResponse(auditObjectResponse);
            auditObject.save();
        }
    }

    @Override
    public void updateAuditor(Auditor auditor) {
        Timber.v("auditor update : %d", auditor.getId());


        ActiveAndroid.beginTransaction();
        try {

            auditor.save();

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        Timber.v("auditor updated");

    }

    private void checkAndSaveAudit(Audit audit) {
        Timber.v("audit checkAndSaveAudit %d", audit.getId());

        ActiveAndroid.beginTransaction();

        try {
            Site site = audit.getSite();
            if (site.getId() == null) {
                site.save();
            }

            Auditor author = audit.getAuthor();
            if (author.getId() == null) {
                Auditor existing = findExistingAuditor(author);

                if (existing != null) {
                    audit.setAuthor(existing);
                } else {
                    author.save();
                }
            }
            audit.setDate(Calendar.getInstance().getTime());
            audit.save();

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        Timber.v("audit checkAndSaveAudit done");
    }

    @Override
    public void updateAuditObject(AuditObject auditObject, Set<RuleAnswer> updatedRules) {
        Timber.v("audit object update %d", auditObject.getId());
        long now = System.currentTimeMillis();

        ActiveAndroid.beginTransaction();

        try {
            Map<Long, AuditObject> characteristicsToUpdate = new HashMap<Long, AuditObject>();


            for (RuleAnswer ruleAnswer : updatedRules) {
                ruleAnswer.save();
                AuditObject ruleAuditObject = ruleAnswer.getQuestionAnswer().getAuditObject();
                if (ruleAuditObject != auditObject) {
                    characteristicsToUpdate.put(ruleAuditObject.getId(), ruleAuditObject);
                }
            }

            for(AuditObject characteristicToUpdate : characteristicsToUpdate.values()) {
                characteristicToUpdate.save();
            }

            auditObject.save();

            updateModificationDate(auditObject.getAudit());

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        Timber.v("audit object updated in %d", System.currentTimeMillis() - now);
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
    public AuditObject createAuditObject(Audit audit, ObjectDescription objectDescription) {
        AuditObject auditObject = new AuditObject(audit, objectDescription);
        return createAuditObject(auditObject);
    }


    @Override
    public AuditObject createChildAuditObject(AuditObject parent, ObjectDescription objectDescription) {
        AuditObject auditObject = new AuditObject(parent, objectDescription);
        return createAuditObject(auditObject);
    }




    @Override
    public AuditObject updateAuditObjectChildren(AuditObject parent, List<ObjectDescription> childrenToCreate, List<AuditObject> childrenToRemove) {

        Timber.v("auditObject children updating");
        long now = System.currentTimeMillis();

        ActiveAndroid.beginTransaction();


        try {
            for(ObjectDescription childToCreate : childrenToCreate) {
                createChildAuditObject(parent, childToCreate);
            }

            for(AuditObject childToRemove : childrenToRemove) {
                deleteAuditObject(childToRemove);
            }


            parent.refresh(RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.RULE_ANSWER).build());

            parent.updateReponse();
            parent.save();

            updateModificationDate(parent.getAudit());

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        Timber.v("auditObject children updating : %d in %d", parent.getId(), System.currentTimeMillis() - now);
        return parent;
    }


    private AuditObject createAuditObject(AuditObject auditObject) {
        Timber.v("auditObject creating");
        long now = System.currentTimeMillis();

        ActiveAndroid.beginTransaction();
        try {
            auditObject.save();
            computeAllQuestionAnswers(auditObject);
            updateModificationDate(auditObject.getAudit());

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        Timber.v("auditObject created : %d in %d", auditObject.getId(), System.currentTimeMillis() - now);
        return auditObject;
    }


    @Override
    public AuditObject getAuditObject(Long auditObjectId) {
        Timber.v("auditObject retrieve : %d", auditObjectId);

        AuditObject auditObject = Model.load(AuditObject.class, auditObjectId);
        updateAuditRuleSet(auditObject.getAudit());

        Timber.v("auditObject retrieved : %s", auditObjectId);
        return auditObject;
    }

    @Override
    public void deleteAuditObject(AuditObject auditObject) {
        Timber.v("auditObject delete : %d", auditObject.getId());

        ActiveAndroid.beginTransaction();

        try {
            deleteAllComments(auditObject.getComments());
            updateModificationDate(auditObject.getAudit());
            Model.delete(AuditObject.class, auditObject.getId());

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        Timber.v("auditObject deleted");
    }

    @Override
    public void deleteAllAuditObjects(Audit audit) {
        Timber.v("deleting all auditObjects of audit %d", audit.getId());
        long now = System.currentTimeMillis();

        ActiveAndroid.beginTransaction();

        try {
            for (AuditObject auditObject : audit.getObjects()) {
                deleteAuditObject(auditObject);
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        Timber.v("deleting all auditObject done in %d", System.currentTimeMillis() - now);
    }

    @Override
    public void deleteComment(Comment comment) {
        Timber.v("comment delete : %d", comment.getId());

        String attachment = comment.getAttachment();

        if (StringUtils.isNotBlank(attachment)) {
            File attachementFile = new File(attachment);
            FileUtils.deleteQuietly(attachementFile);
        }
        Audit audit = comment.getAudit();

        updateModificationDate(audit);
        Model.delete(Comment.class, comment.getId());

        Timber.v("comment deleted");
    }

    @Override
    public void deleteAllComments(List<Comment> comments) {
        Timber.v("deleting all comments");
        long now = System.currentTimeMillis();

        ActiveAndroid.beginTransaction();

        try {
            for (Comment comment : comments) {
                deleteComment(comment);
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        Timber.v("deleting all comments done in %d", System.currentTimeMillis() - now);
    }

    @Override
    public void refresh(Refreshable entity, RefreshStrategy strategy) {
        Timber.v("refresh entity %s with strategy : %s", entity, strategy);
        long now = System.currentTimeMillis();

        ActiveAndroid.beginTransaction();

        try {
            entity.refresh(strategy);
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        Timber.v("refresh entity %s done in %d", entity, System.currentTimeMillis() - now);
    }

    /**
     * To compute all QuestionAnswer related to an AuditObject
     *
     * @param auditObject AuditObject
     */
    private void computeAllQuestionAnswers(AuditObject auditObject) {
        Collection<Question> questions = auditObject.getObjectDescription().computeAllQuestions();
        for (Question q : questions) {
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
    private QuestionAnswer createQuestionAnswer(AuditObject auditObject, Question question) {
        Timber.v("QuestionAnswer creating");

        QuestionAnswer questionAnswer = new QuestionAnswer(auditObject, question);
        questionAnswer.save();
        updateModificationDate(auditObject.getAudit());

        computeAllRuleAnswers(questionAnswer);

        Timber.v("QuestionAnswer created : %d", questionAnswer.getId());
        return questionAnswer;
    }

    /**
     * To compute all RuleAnswer releated to a QuestionAnswer.
     *
     * @param questionAnswer QuestionAnswer
     */
    private void computeAllRuleAnswers(QuestionAnswer questionAnswer) {
        Map<String, Rule> rules = questionAnswer.getQuestion().getRulesById();
        for (Rule rule : rules.values()) {
            createRuleAnswer(questionAnswer, rule);
        }
    }

    /**
     * To create a RuleAnswer from a rule.
     *
     * @param questionAnswer QuestionAnswer to attach to
     * @param rule           Rule
     * @return RuleAnswer created
     */
    private RuleAnswer createRuleAnswer(QuestionAnswer questionAnswer, Rule rule) {
        RuleAnswer ruleAnswer = new RuleAnswer(questionAnswer, rule);
        ruleAnswer.save();

        return ruleAnswer;
    }

    @Override
    public RuleSet getRuleSet(String ruleSetId) {
        RuleSet result = allRuleSetsById.get(ruleSetId);
        if (result == null) {
            // TODO remove default ruleset behaviour
            result = allRuleSetsById.values().iterator().next();
        }
        return result;
    }

    @Override
    public Collection<RuleSet> getAllRuleSet() {
        return Collections.unmodifiableSet(allRuleSets);
    }

    @Override
    public List<Site> searchSites(String noImmo, String name) {
        From from = new Select().from(Site.class);

        if (StringUtils.isNotBlank(noImmo)) {
            from = from.where(Site.COLUMN_NOIMMO + " LIKE ?", "" + noImmo + "%").orderBy(Site.COLUMN_NOIMMO + " ASC");
        }

        if (StringUtils.isNotBlank(name)) {
            from = from.where(Site.COLUMN_NAME + " LIKE ?", "%" + name + "%").orderBy(Site.COLUMN_NAME + " ASC");
        }

        List<Site> sites = from.execute();
        Timber.d("SQL Results : " + sites.size());
        return sites;
    }

    @Override
    public List<Auditor> searchAuditors(String name) {
        From from = new Select().from(Auditor.class);

        if (StringUtils.isNotBlank(name)) {
            from = from.where(Auditor.COLUMN_USER_NAME + " LIKE ?", "%" + name + "%").orderBy(Auditor.COLUMN_USER_NAME + " ASC");
        }

        return from.execute();
    }

    private Auditor findExistingAuditor(Auditor author) {
        return new Select()
                .from(Auditor.class)
                .where(Auditor.COLUMN_USER_NAME + " = ?", author.getUserName())
                .executeSingle();
    }

    /**
     * TO update the audit RuleSet from the persisted RuleSet id
     *
     * @param audit Audit to update
     */
    private void updateAuditRuleSet(Audit audit) {
        String ruleSetId = audit.getRuleSetId();
        audit.setRuleSet(getRuleSet(ruleSetId));
    }

    private File getAuditsDirectory() {
        return context.getDir(AUDITS_DIRECTORY, Context.MODE_PRIVATE);
    }


    private void updateModificationDate(Audit audit) {
        audit.setDate(Calendar.getInstance().getTime());
        audit.save();
    }

}
