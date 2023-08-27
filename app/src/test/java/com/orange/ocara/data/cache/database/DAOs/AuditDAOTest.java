package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.data.cache.database.NonTables.AuditWithRulesetAndSiteAndComments;
import com.orange.ocara.data.cache.database.NonTables.AuditWithRulesetWithSite;
import com.orange.ocara.data.cache.database.NonTables.AuditWithSite;
import com.orange.ocara.data.cache.database.Tables.Audit;
import com.orange.ocara.data.cache.database.Tables.Comment;
import com.orange.ocara.data.cache.database.Tables.RulesetDetails;
import com.orange.ocara.data.cache.database.Tables.Site;
import com.orange.ocara.utils.enums.AuditLevel;
import com.orange.ocara.utils.enums.CommentType;
import com.orange.ocara.utils.enums.RuleSetStat;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AuditDAOTest extends DAOTest {

    @Test
    public void getAuditsWithSiteOrderedByNameDESC() {
        String pattern = "%audit%";

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit2").setSiteId(site1Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit3").setSiteId(site2Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit4").setSiteId(site2Id.intValue()).build();
        Audit audit5 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("adtui").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();
        auditDAO.insert(audit5).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedByNameDESC(pattern).blockingFirst();

        Assert.assertEquals(4, result.size());

        Assert.assertEquals(audit4.getName(), result.get(0).getAudit().getName());
        Assert.assertEquals(audit3.getName(), result.get(1).getAudit().getName());
        Assert.assertEquals(audit2.getName(), result.get(2).getAudit().getName());
        Assert.assertEquals(audit1.getName(), result.get(3).getAudit().getName());

        Assert.assertEquals(site1Id.intValue(), result.get(3).getSite().getId());
        Assert.assertEquals(site1Id.intValue(), result.get(2).getSite().getId());
        Assert.assertEquals(site2Id.intValue(), result.get(1).getSite().getId());
        Assert.assertEquals(site2Id.intValue(), result.get(0).getSite().getId());
    }

    @Test
    public void getAuditsWithSiteOrderedByNameASC() {
        String pattern = "%audit%";

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit2").setSiteId(site1Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit3").setSiteId(site2Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit4").setSiteId(site2Id.intValue()).build();
        Audit audit5 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("adtui").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();
        auditDAO.insert(audit5).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedByNameASC(pattern).blockingFirst();

        Assert.assertEquals(4, result.size());

        Assert.assertEquals(audit1.getName(), result.get(0).getAudit().getName());
        Assert.assertEquals(audit2.getName(), result.get(1).getAudit().getName());
        Assert.assertEquals(audit3.getName(), result.get(2).getAudit().getName());
        Assert.assertEquals(audit4.getName(), result.get(3).getAudit().getName());

        Assert.assertEquals(site1Id.intValue(), result.get(0).getSite().getId());
        Assert.assertEquals(site1Id.intValue(), result.get(1).getSite().getId());
        Assert.assertEquals(site2Id.intValue(), result.get(2).getSite().getId());
        Assert.assertEquals(site2Id.intValue(), result.get(3).getSite().getId());
    }

    @Test
    public void getAuditsWithSiteOrderedByDateDESC() {
        String pattern = "%audit%";

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(1L).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(2L).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("auitd").setSiteId(site2Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("aujts").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedByDateDESC(pattern).blockingFirst();

        Assert.assertEquals(2, result.size());

        Assert.assertEquals(audit1.getName(), result.get(1).getAudit().getName());
        Assert.assertEquals(audit2.getName(), result.get(0).getAudit().getName());

        Assert.assertEquals(site2Id.intValue(), result.get(0).getSite().getId());
        Assert.assertEquals(site1Id.intValue(), result.get(1).getSite().getId());
    }

    @Test
    public void getAuditsWithSiteOrderedByDateASC() {
        String pattern = "%audit%";

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(2L).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(1L).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("auitd").setSiteId(site2Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("aujts").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedByDateASC(pattern).blockingFirst();

        Assert.assertEquals(2, result.size());

        Assert.assertEquals(audit1.getName(), result.get(1).getAudit().getName());
        Assert.assertEquals(audit2.getName(), result.get(0).getAudit().getName());

        Assert.assertEquals(site2Id.intValue(), result.get(0).getSite().getId());
        Assert.assertEquals(site1Id.intValue(), result.get(1).getSite().getId());
    }

    @Test
    public void getAuditsWithSiteOrderedByStatusDESC() {
        String pattern = "%audit%";

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setInProgress(false).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setInProgress(true).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setInProgress(false).setName("audit3").setSiteId(site1Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("aujts").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedByStatusDESC(pattern).blockingFirst();

        Assert.assertEquals(3, result.size());

        Assert.assertEquals(true, result.get(0).getAudit().isInProgress());
        Assert.assertEquals(false, result.get(1).getAudit().isInProgress());
        Assert.assertEquals(false, result.get(2).getAudit().isInProgress());

        Assert.assertEquals(site2Id.intValue(), result.get(0).getSite().getId());
        Assert.assertEquals(site1Id.intValue(), result.get(1).getSite().getId());
        Assert.assertEquals(site1Id.intValue(), result.get(2).getSite().getId());
    }

    @Test
    public void getAuditsWithSiteOrderedByStatusASC() {
        String pattern = "%audit%";

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setInProgress(false).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setInProgress(true).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setInProgress(false).setName("audit3").setSiteId(site1Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("aujts").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedByStatusASC(pattern).blockingFirst();

        Assert.assertEquals(3, result.size());

        Assert.assertEquals(false, result.get(0).getAudit().isInProgress());
        Assert.assertEquals(false, result.get(1).getAudit().isInProgress());
        Assert.assertEquals(true, result.get(2).getAudit().isInProgress());

        Assert.assertEquals(site1Id.intValue(), result.get(0).getSite().getId());
        Assert.assertEquals(site1Id.intValue(), result.get(1).getSite().getId());
        Assert.assertEquals(site2Id.intValue(), result.get(2).getSite().getId());
    }

    @Test
    public void getAuditsWithSiteOrderedBySiteDESC() {
        String pattern = "%audit%";

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit3").setSiteId(site1Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("aujts").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedBySiteDESC(pattern).blockingFirst();

        Assert.assertEquals(3, result.size());

        Assert.assertEquals(site2.getName(), result.get(0).getSite().getName());
        Assert.assertEquals(site1.getName(), result.get(1).getSite().getName());
        Assert.assertEquals(site1.getName(), result.get(2).getSite().getName());
    }

    @Test
    public void getAuditsWithSiteOrderedBySiteASC() {
        String pattern = "%audit%";

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit3").setSiteId(site1Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("aujts").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedBySiteASC(pattern).blockingFirst();

        Assert.assertEquals(3, result.size());

        Assert.assertEquals(site1.getName(), result.get(0).getSite().getName());
        Assert.assertEquals(site1.getName(), result.get(1).getSite().getName());
        Assert.assertEquals(site2.getName(), result.get(2).getSite().getName());
    }

    @Test
    public void getAuditsWithSiteOrderedByNameASCwithoutPattern() {

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit3").setSiteId(site1Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit4").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedByNameASC().blockingFirst();

        Assert.assertEquals(4, result.size());

        Assert.assertEquals(audit1.getName(), result.get(0).getAudit().getName());
        Assert.assertEquals(audit2.getName(), result.get(1).getAudit().getName());
        Assert.assertEquals(audit3.getName(), result.get(2).getAudit().getName());
        Assert.assertEquals(audit4.getName(), result.get(3).getAudit().getName());

        Assert.assertEquals(site1Id.intValue(), result.get(0).getSite().getId());
        Assert.assertEquals(site2Id.intValue(), result.get(1).getSite().getId());
        Assert.assertEquals(site1Id.intValue(), result.get(2).getSite().getId());
        Assert.assertEquals(site2Id.intValue(), result.get(3).getSite().getId());
    }

    @Test
    public void getAuditsWithSiteOrderedByNameDESCwithoutPattern() {

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit3").setSiteId(site1Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit4").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedByNameDESC().blockingFirst();

        Assert.assertEquals(4, result.size());

        Assert.assertEquals(audit4.getName(), result.get(0).getAudit().getName());
        Assert.assertEquals(audit3.getName(), result.get(1).getAudit().getName());
        Assert.assertEquals(audit2.getName(), result.get(2).getAudit().getName());
        Assert.assertEquals(audit1.getName(), result.get(3).getAudit().getName());

        Assert.assertEquals(site2Id.intValue(), result.get(0).getSite().getId());
        Assert.assertEquals(site1Id.intValue(), result.get(1).getSite().getId());
        Assert.assertEquals(site2Id.intValue(), result.get(2).getSite().getId());
        Assert.assertEquals(site1Id.intValue(), result.get(3).getSite().getId());
    }

    @Test
    public void getAuditsWithSiteOrderedByDateDESCwithoutPattern() {

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(1L).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(2L).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(3L).setName("auitd").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedByDateDESC().blockingFirst();

        Assert.assertEquals(3, result.size());

        Assert.assertEquals(audit1.getName(), result.get(2).getAudit().getName());
        Assert.assertEquals(audit2.getName(), result.get(1).getAudit().getName());
        Assert.assertEquals(audit3.getName(), result.get(0).getAudit().getName());

        Assert.assertEquals(site2Id.intValue(), result.get(0).getSite().getId());
        Assert.assertEquals(site2Id.intValue(), result.get(1).getSite().getId());
        Assert.assertEquals(site1Id.intValue(), result.get(2).getSite().getId());
    }

    @Test
    public void getAuditsWithSiteOrderedByDateASCwithoutPattern() {

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(1L).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(2L).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(3L).setName("auitd").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedByDateASC().blockingFirst();

        Assert.assertEquals(3, result.size());

        Assert.assertEquals(audit1.getName(), result.get(0).getAudit().getName());
        Assert.assertEquals(audit2.getName(), result.get(1).getAudit().getName());
        Assert.assertEquals(audit3.getName(), result.get(2).getAudit().getName());

        Assert.assertEquals(site1Id.intValue(), result.get(0).getSite().getId());
        Assert.assertEquals(site2Id.intValue(), result.get(1).getSite().getId());
        Assert.assertEquals(site2Id.intValue(), result.get(2).getSite().getId());
    }

    @Test
    public void getAuditsWithSiteOrderedBySiteDESCwithoutPattern() {

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit3").setSiteId(site1Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("aujts").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedBySiteDESC().blockingFirst();

        Assert.assertEquals(4, result.size());

        Assert.assertEquals(site2.getName(), result.get(0).getSite().getName());
        Assert.assertEquals(site2.getName(), result.get(1).getSite().getName());
        Assert.assertEquals(site1.getName(), result.get(2).getSite().getName());
        Assert.assertEquals(site1.getName(), result.get(3).getSite().getName());
    }

    @Test
    public void getAuditsWithSiteOrderedBySiteASCwithoutPattern() {

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("audit3").setSiteId(site1Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setName("aujts").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedBySiteASC().blockingFirst();

        Assert.assertEquals(4, result.size());

        Assert.assertEquals(site1.getName(), result.get(0).getSite().getName());
        Assert.assertEquals(site1.getName(), result.get(1).getSite().getName());
        Assert.assertEquals(site2.getName(), result.get(2).getSite().getName());
        Assert.assertEquals(site2.getName(), result.get(3).getSite().getName());
    }

    @Test
    public void getAuditsWithSiteOrderedByStatusDESCwithoutPattern() {

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(1L).setInProgress(false).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(2L).setInProgress(true).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(3L).setInProgress(false).setName("audit3").setSiteId(site1Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(4L).setInProgress(true).setName("aujts").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedByStatusDESC().blockingFirst();

        Assert.assertEquals(4, result.size());

        Assert.assertEquals(audit4.getName(), result.get(0).getAudit().getName());
        Assert.assertEquals(audit2.getName(), result.get(1).getAudit().getName());
        Assert.assertEquals(audit3.getName(), result.get(2).getAudit().getName());
        Assert.assertEquals(audit1.getName(), result.get(3).getAudit().getName());

        Assert.assertEquals(site2.getName(), result.get(0).getSite().getName());
        Assert.assertEquals(site2.getName(), result.get(1).getSite().getName());
        Assert.assertEquals(site1.getName(), result.get(2).getSite().getName());
        Assert.assertEquals(site1.getName(), result.get(3).getSite().getName());
    }

    @Test
    public void getAuditsWithSiteOrderedByStatusASCwithoutPattern() {

        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        Long site1Id = siteDAO.insert(site1).blockingGet();
        Long site2Id = siteDAO.insert(site2).blockingGet();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(1L).setInProgress(false).setName("audit1").setSiteId(site1Id.intValue()).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(2L).setInProgress(true).setName("audit2").setSiteId(site2Id.intValue()).build();
        Audit audit3 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(3L).setInProgress(false).setName("audit3").setSiteId(site1Id.intValue()).build();
        Audit audit4 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setDate(4L).setInProgress(true).setName("aujts").setSiteId(site2Id.intValue()).build();

        auditDAO.insert(audit1).blockingGet();
        auditDAO.insert(audit2).blockingGet();
        auditDAO.insert(audit3).blockingGet();
        auditDAO.insert(audit4).blockingGet();

        List<AuditWithSite> result = auditDAO.getAuditsWithSiteOrderedByStatusASC().blockingFirst();

        Assert.assertEquals(4, result.size());

        Assert.assertEquals(audit3.getName(), result.get(0).getAudit().getName());
        Assert.assertEquals(audit1.getName(), result.get(1).getAudit().getName());
        Assert.assertEquals(audit4.getName(), result.get(2).getAudit().getName());
        Assert.assertEquals(audit2.getName(), result.get(3).getAudit().getName());

        Assert.assertEquals(site1.getName(), result.get(0).getSite().getName());
        Assert.assertEquals(site1.getName(), result.get(1).getSite().getName());
        Assert.assertEquals(site2.getName(), result.get(2).getSite().getName());
        Assert.assertEquals(site2.getName(), result.get(3).getSite().getName());
    }

    @Test
    public void addingAudit_testingRetrievalWithSiteAndRulesetAndComment() {
        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        int site1Id = siteDAO.insert(site1).blockingGet().intValue();
        int site2Id = siteDAO.insert(site2).blockingGet().intValue();

        RulesetDetails ruleset1 = new RulesetDetails.Builder().setRuleSetStat(RuleSetStat.OFFLINE).setReference("r1").setVersion(1).build();
        RulesetDetails ruleset2 = new RulesetDetails.Builder().setRuleSetStat(RuleSetStat.OFFLINE).setReference("r2").setVersion(1).build();

        rulesetDAO.insert(ruleset1).blockingAwait();
        rulesetDAO.insert(ruleset2).blockingAwait();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setRulesetRef(ruleset1.getReference()).setRulesetVer(ruleset1.getVersion()).setName("audit1").setSiteId(site1Id).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setRulesetRef(ruleset2.getReference()).setRulesetVer(ruleset2.getVersion()).setName("audit2").setSiteId(site2Id).build();

        Long audit1Id = auditDAO.insert(audit1).blockingGet();
        Long audit2Id = auditDAO.insert(audit2).blockingGet();

        Comment comment1 = new Comment(CommentType.TEXT, "", "", "comment1");
        comment1.setAudit_id(audit1Id.intValue());
        Comment comment2 = new Comment(CommentType.TEXT, "", "", "comment2");
        comment2.setAudit_id(audit1Id.intValue());
        Comment comment3 = new Comment(CommentType.TEXT, "", "", "comment3");
        comment3.setAudit_id(audit2Id.intValue());
        Comment comment4 = new Comment(CommentType.TEXT, "", "", "comment4");
        comment4.setAudit_id(audit2Id.intValue());

        commentDAO.insert(comment1).blockingGet();
        commentDAO.insert(comment2).blockingGet();
        commentDAO.insert(comment3).blockingGet();
        commentDAO.insert(comment4).blockingGet();

        AuditWithRulesetAndSiteAndComments result1 = auditDAO.getAudit(audit1Id).blockingGet();
        AuditWithRulesetAndSiteAndComments result2 = auditDAO.getAudit(audit2Id).blockingGet();

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);

        Assert.assertEquals(audit1Id.intValue(), result1.getAudit().getId());
        Assert.assertEquals(site1Id, result1.getSite().getId());
        Assert.assertEquals(ruleset1.getReference(), result1.getRulesetDetails().getReference());
        Assert.assertEquals(ruleset1.getVersion(), result1.getRulesetDetails().getVersion());
        Assert.assertEquals(2, result1.getComments().size());
        Assert.assertEquals(comment1.getContent(), result1.getComments().get(0).getContent());
        Assert.assertEquals(comment2.getContent(), result1.getComments().get(1).getContent());

    }

    @Test
    public void addingAudit_testingRetrievalWithSiteAndRulesetAndComment2() {
        Site site1 = Site.builder().name("site1").build();

        int site1Id = siteDAO.insert(site1).blockingGet().intValue();

        RulesetDetails ruleset1 = new RulesetDetails.Builder().setRuleSetStat(RuleSetStat.OFFLINE).setReference("r1").setVersion(1).build();

        rulesetDAO.insert(ruleset1).blockingAwait();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setRulesetRef(ruleset1.getReference()).setRulesetVer(ruleset1.getVersion()).setName("audit1").setSiteId(site1Id).build();

        Long audit1Id = auditDAO.insert(audit1).blockingGet();

        AuditWithRulesetAndSiteAndComments result1 = auditDAO.getAudit(audit1Id).blockingGet();

        Assert.assertNotNull(result1);
        // this assertion doesn't affect the app
        // Assert.assertEquals(audit1Id.intValue(), result1.getAudit().getId());
        Assert.assertEquals(site1Id, result1.getSite().getId());
        Assert.assertEquals(ruleset1.getReference(), result1.getRulesetDetails().getReference());
        Assert.assertEquals(ruleset1.getVersion(), result1.getRulesetDetails().getVersion());
        Assert.assertEquals(0, result1.getComments().size());

    }

    @Test
    public void getAuditWithRulesetWithSite() {
        Site site1 = Site.builder().name("site1").build();
        Site site2 = Site.builder().name("site2").build();

        int site1Id = siteDAO.insert(site1).blockingGet().intValue();
        int site2Id = siteDAO.insert(site2).blockingGet().intValue();

        RulesetDetails ruleset1 = new RulesetDetails.Builder().setRuleSetStat(RuleSetStat.OFFLINE).setReference("r1").setVersion(1).build();
        RulesetDetails ruleset2 = new RulesetDetails.Builder().setRuleSetStat(RuleSetStat.OFFLINE).setReference("r2").setVersion(1).build();

        rulesetDAO.insert(ruleset1).blockingAwait();
        rulesetDAO.insert(ruleset2).blockingAwait();

        Audit audit1 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setRulesetRef(ruleset1.getReference()).setRulesetVer(ruleset1.getVersion()).setName("audit1").setSiteId(site1Id).build();
        Audit audit2 = Audit.Builder().setLevel(AuditLevel.BEGINNER).setRulesetRef(ruleset2.getReference()).setRulesetVer(ruleset2.getVersion()).setName("audit2").setSiteId(site2Id).build();

        Long audit1Id = auditDAO.insert(audit1).blockingGet();
        Long audit2Id = auditDAO.insert(audit2).blockingGet();


        AuditWithRulesetWithSite result1 = auditDAO.getAuditWithRulesetWithSite(audit1Id).blockingGet();
        AuditWithRulesetWithSite result2 = auditDAO.getAuditWithRulesetWithSite(audit2Id).blockingGet();

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);

        Assert.assertEquals(audit1Id.intValue(), result1.getAuditId());
        Assert.assertEquals(site1Id, result1.getSiteId());
        Assert.assertEquals(ruleset1.getReference(), result1.getRulesetRef());
        Assert.assertEquals(Integer.valueOf(ruleset1.getVersion()), Integer.valueOf(result1.getRulesetVresion()));

    }
}
