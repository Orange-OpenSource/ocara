package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.data.cache.database.NonTables.ImpactValueWithRuleset;
import com.orange.ocara.data.cache.database.Tables.ImpactValue;
import com.orange.ocara.data.cache.database.Tables.RulesetDetails;
import com.orange.ocara.data.cache.database.crossRef.ImpactValueRulesetCrossref;
import com.orange.ocara.utils.enums.RuleSetStat;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ImpactValueDAOTest extends DAOTest {

    // left join ruleset_details on impact_ruleset.rulesetRef=" +
    //            "ruleset_details.reference  and impact_ruleset.version=ruleset_details.version
    @Test
    public void getImpactValuesForRuleset() {
        List<ImpactValue> impactValueList = new ArrayList<>();
        impactValueList.add(ImpactValue.ImpactValueBuilder.anImpactValue()
                .withReference("i1")
                .build());
        impactValueList.add(ImpactValue.ImpactValueBuilder.anImpactValue()
                .withReference("i2")
                .build());
        impactValueList.add(ImpactValue.ImpactValueBuilder.anImpactValue()
                .withReference("i3")
                .build());
        impactValueList.add(ImpactValue.ImpactValueBuilder.anImpactValue()
                .withReference("i4")
                .build());
        impactValueDAO.insert(impactValueList).blockingAwait();

        rulesetDAO.insert(new RulesetDetails.Builder()
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .setReference("r1")
                .setVersion(1)
                .build()).blockingAwait();

        rulesetDAO.insert(new RulesetDetails.Builder()
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .setReference("r1")
                .setVersion(2)
                .build()).blockingAwait();
        rulesetDAO.insert(new RulesetDetails.Builder()
                .setRuleSetStat(RuleSetStat.OFFLINE)
                .setReference("r2")
                .setVersion(1)
                .build()).blockingAwait();

        impactValueRulesetDao.insert(new ImpactValueRulesetCrossref("r1", "i1", 1)).blockingAwait();
        impactValueRulesetDao.insert(new ImpactValueRulesetCrossref("r1", "i2", 1)).blockingAwait();
        impactValueRulesetDao.insert(new ImpactValueRulesetCrossref("r1", "i3", 1)).blockingAwait();

        impactValueRulesetDao.insert(new ImpactValueRulesetCrossref("r1", "i4", 2)).blockingAwait();
        impactValueRulesetDao.insert(new ImpactValueRulesetCrossref("r1", "i2", 2)).blockingAwait();

        impactValueRulesetDao.insert(new ImpactValueRulesetCrossref("r2", "i1", 1)).blockingAwait();
        impactValueRulesetDao.insert(new ImpactValueRulesetCrossref("r2", "i3", 1)).blockingAwait();

        List<ImpactValueWithRuleset> r1v1 = impactValueDAO.getImpactValuesForRuleset("r1", 1).blockingGet();
        List<ImpactValueWithRuleset> r1v2 = impactValueDAO.getImpactValuesForRuleset("r1", 2).blockingGet();
        List<ImpactValueWithRuleset> r2v1 = impactValueDAO.getImpactValuesForRuleset("r2", 1).blockingGet();

        Assert.assertArrayEquals(new int[]{r1v1.size(), r1v2.size(), r2v1.size()}, new int[]{3, 2, 2});
        int[] arr = new int[4];
        for (ImpactValueWithRuleset impactValueWithRuleset : r1v1) {
            Assert.assertEquals(impactValueWithRuleset.getRulesetDetails().getReference(), "r1");
            Assert.assertEquals(impactValueWithRuleset.getRulesetDetails().getVersion(), 1);
            if (impactValueWithRuleset.getImpactValue().getReference().equals("i1")) {
                arr[0]++;
            } else if (impactValueWithRuleset.getImpactValue().getReference().equals("i2")) {
                arr[1]++;
            } else if (impactValueWithRuleset.getImpactValue().getReference().equals("i3")) {
                arr[2]++;
            } else if (impactValueWithRuleset.getImpactValue().getReference().equals("i4")) {
                arr[3]++;
            }
        }
        Assert.assertArrayEquals(new int[]{1, 1, 1, 0}, arr);

        arr = new int[]{0, 0, 0, 0};
        for (ImpactValueWithRuleset impactValueWithRuleset : r1v2) {
            Assert.assertEquals(impactValueWithRuleset.getRulesetDetails().getReference(), "r1");
            Assert.assertEquals(impactValueWithRuleset.getRulesetDetails().getVersion(), 2);
            if (impactValueWithRuleset.getImpactValue().getReference().equals("i1")) {
                arr[0]++;
            } else if (impactValueWithRuleset.getImpactValue().getReference().equals("i2")) {
                arr[1]++;
            } else if (impactValueWithRuleset.getImpactValue().getReference().equals("i3")) {
                arr[2]++;
            } else if (impactValueWithRuleset.getImpactValue().getReference().equals("i4")) {
                arr[3]++;
            }
        }
        Assert.assertArrayEquals(new int[]{0, 1, 0, 1}, arr);

        arr = new int[]{0, 0, 0, 0};
        for (ImpactValueWithRuleset impactValueWithRuleset : r2v1) {
            Assert.assertEquals(impactValueWithRuleset.getRulesetDetails().getReference(), "r2");
            Assert.assertEquals(impactValueWithRuleset.getRulesetDetails().getVersion(), 1);
            if (impactValueWithRuleset.getImpactValue().getReference().equals("i1")) {
                arr[0]++;
            } else if (impactValueWithRuleset.getImpactValue().getReference().equals("i2")) {
                arr[1]++;
            } else if (impactValueWithRuleset.getImpactValue().getReference().equals("i3")) {
                arr[2]++;
            } else if (impactValueWithRuleset.getImpactValue().getReference().equals("i4")) {
                arr[3]++;
            }
        }
        Assert.assertArrayEquals(new int[]{1, 0, 1, 0}, arr);
    }
}