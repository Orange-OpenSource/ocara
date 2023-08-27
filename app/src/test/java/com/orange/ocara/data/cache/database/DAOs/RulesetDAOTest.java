package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.data.cache.database.Tables.RulesetDetails;
import com.orange.ocara.utils.enums.RuleSetStat;

import org.junit.Assert;
import org.junit.Test;

public class RulesetDAOTest extends DAOTest{

    @Test
    public void getLatestRulesetByReference() {
        rulesetDAO.insert(new RulesetDetails.Builder()
                .setReference("r1")
                .setVersion(1)
                .setAuthorName("a")
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .build()).blockingAwait();
        rulesetDAO.insert(new RulesetDetails.Builder()
                .setReference("r1")
                .setVersion(2)
                .setAuthorName("b")
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .build()).blockingAwait();
        rulesetDAO.insert(new RulesetDetails.Builder()
                .setReference("r2")
                .setVersion(1)
                .setAuthorName("c")
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .build()).blockingAwait();
        RulesetDetails ruleset1=rulesetDAO.getLatestRulesetByReference("r1").blockingGet();
        RulesetDetails ruleset2=rulesetDAO.getLatestRulesetByReference("r2").blockingGet();
        Assert.assertEquals(ruleset1.getReference(),"r1");
        Assert.assertEquals(ruleset1.getAuthorName(),"b");
        Assert.assertEquals(ruleset2.getReference(),"r2");
        Assert.assertEquals(ruleset2.getAuthorName(),"c");
    }
}