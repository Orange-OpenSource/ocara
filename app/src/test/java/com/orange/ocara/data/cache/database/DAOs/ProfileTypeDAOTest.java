package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.data.cache.database.NonTables.ProfileTypeWithRuleset;
import com.orange.ocara.data.cache.database.Tables.ProfileType;
import com.orange.ocara.data.cache.database.Tables.RulesetDetails;
import com.orange.ocara.data.cache.database.crossRef.ProfileTypeRulesetCrossref;
import com.orange.ocara.utils.enums.RuleSetStat;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ProfileTypeDAOTest extends DAOTest {

    // left join ruleset_details on impact_ruleset.rulesetRef=" +
    //            "ruleset_details.reference  and impact_ruleset.version=ruleset_details.version
    @Test
    public void getProfileTypesForRuleset() {
        List<ProfileType> profileTypeList = new ArrayList<>();
        profileTypeList.add(ProfileType.ProfileTypeBuilder.aProfileType()
                .withReference("p1")
                .build());
        profileTypeList.add(ProfileType.ProfileTypeBuilder.aProfileType()
                .withReference("p2")
                .build());
        profileTypeList.add(ProfileType.ProfileTypeBuilder.aProfileType()
                .withReference("p3")
                .build());
        profileTypeList.add(ProfileType.ProfileTypeBuilder.aProfileType()
                .withReference("p4")
                .build());
        profileTypeDAO.insert(profileTypeList).blockingAwait();

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

        profileTypeRulesetDao.insert(new ProfileTypeRulesetCrossref("r1", "p1", 1)).blockingAwait();
        profileTypeRulesetDao.insert(new ProfileTypeRulesetCrossref("r1", "p2", 1)).blockingAwait();
        profileTypeRulesetDao.insert(new ProfileTypeRulesetCrossref("r1", "p3", 1)).blockingAwait();

        profileTypeRulesetDao.insert(new ProfileTypeRulesetCrossref("r1", "p4", 2)).blockingAwait();
        profileTypeRulesetDao.insert(new ProfileTypeRulesetCrossref("r1", "p2", 2)).blockingAwait();

        profileTypeRulesetDao.insert(new ProfileTypeRulesetCrossref("r2", "p1", 1)).blockingAwait();
        profileTypeRulesetDao.insert(new ProfileTypeRulesetCrossref("r2", "p3", 1)).blockingAwait();

        List<ProfileTypeWithRuleset> r1v1 = profileTypeDAO.getProfileTypesForRuleset("r1", 1).blockingGet();
        List<ProfileTypeWithRuleset> r1v2 = profileTypeDAO.getProfileTypesForRuleset("r1", 2).blockingGet();
        List<ProfileTypeWithRuleset> r2v1 = profileTypeDAO.getProfileTypesForRuleset("r2", 1).blockingGet();

        Assert.assertArrayEquals(new int[]{r1v1.size(), r1v2.size(), r2v1.size()}, new int[]{3, 2, 2});
        int[] arr = new int[4];
        for (ProfileTypeWithRuleset impactValueWithRuleset : r1v1) {
            Assert.assertEquals(impactValueWithRuleset.getRulesetDetails().getReference(), "r1");
            Assert.assertEquals(impactValueWithRuleset.getRulesetDetails().getVersion(), 1);
            if (impactValueWithRuleset.getProfileType().getReference().equals("p1")) {
                arr[0]++;
            } else if (impactValueWithRuleset.getProfileType().getReference().equals("p2")) {
                arr[1]++;
            } else if (impactValueWithRuleset.getProfileType().getReference().equals("p3")) {
                arr[2]++;
            } else if (impactValueWithRuleset.getProfileType().getReference().equals("p4")) {
                arr[3]++;
            }
        }
        Assert.assertArrayEquals(new int[]{1, 1, 1, 0}, arr);

        arr = new int[]{0, 0, 0, 0};
        for (ProfileTypeWithRuleset impactValueWithRuleset : r1v2) {
            Assert.assertEquals(impactValueWithRuleset.getRulesetDetails().getReference(), "r1");
            Assert.assertEquals(impactValueWithRuleset.getRulesetDetails().getVersion(), 2);
            if (impactValueWithRuleset.getProfileType().getReference().equals("p1")) {
                arr[0]++;
            } else if (impactValueWithRuleset.getProfileType().getReference().equals("p2")) {
                arr[1]++;
            } else if (impactValueWithRuleset.getProfileType().getReference().equals("p3")) {
                arr[2]++;
            } else if (impactValueWithRuleset.getProfileType().getReference().equals("p4")) {
                arr[3]++;
            }
        }
        Assert.assertArrayEquals(new int[]{0, 1, 0, 1}, arr);

        arr = new int[]{0, 0, 0, 0};
        for (ProfileTypeWithRuleset impactValueWithRuleset : r2v1) {
            Assert.assertEquals(impactValueWithRuleset.getRulesetDetails().getReference(), "r2");
            Assert.assertEquals(impactValueWithRuleset.getRulesetDetails().getVersion(), 1);
            if (impactValueWithRuleset.getProfileType().getReference().equals("p1")) {
                arr[0]++;
            } else if (impactValueWithRuleset.getProfileType().getReference().equals("p2")) {
                arr[1]++;
            } else if (impactValueWithRuleset.getProfileType().getReference().equals("p3")) {
                arr[2]++;
            } else if (impactValueWithRuleset.getProfileType().getReference().equals("p4")) {
                arr[3]++;
            }
        }
        Assert.assertArrayEquals(new int[]{1, 0, 1, 0}, arr);
    }
}