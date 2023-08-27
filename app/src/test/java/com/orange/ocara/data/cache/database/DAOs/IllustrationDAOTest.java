package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.data.cache.database.NonTables.IllustrationWithRuleRef;
import com.orange.ocara.data.cache.database.Tables.Illustration;
import com.orange.ocara.data.cache.database.Tables.Rule;
import com.orange.ocara.data.cache.database.crossRef.RuleWithIllustrations;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class IllustrationDAOTest extends DAOTest {

    @Before
    public void setup() {
        super.setup();
        Rule r1 = Rule.RuleBuilder.aRule()
                .withReference("r1")
                .build();
        Rule r2 = Rule.RuleBuilder.aRule()
                .withReference("r2")
                .build();

        ruleDAO.insert(r1).blockingAwait();
        ruleDAO.insert(r2).blockingAwait();

        Illustration i1 = Illustration.IllustrationBuilder.anIllustration()
                .withReference("i1")
                .build();
        Illustration i2 = Illustration.IllustrationBuilder.anIllustration()
                .withReference("i2")
                .build();
        Illustration i3 = Illustration.IllustrationBuilder.anIllustration()
                .withReference("i3")
                .build();
        Illustration i4 = Illustration.IllustrationBuilder.anIllustration()
                .withReference("i4")
                .build();

        illustrationDAO.insert(i1).blockingAwait();
        illustrationDAO.insert(i2).blockingAwait();
        illustrationDAO.insert(i3).blockingAwait();
        illustrationDAO.insert(i4).blockingAwait();

        ruleWithIllustrationDAO.insert(new RuleWithIllustrations.RuleWithIllustrationBuilder()
                .setRuleRef("r1")
                .setIllustRef("i1")
                .setRulesetRef("r1")
                .setVersion(1)
                .createRuleWithIllustrations()).blockingAwait();
        ruleWithIllustrationDAO.insert(new RuleWithIllustrations.RuleWithIllustrationBuilder()
                .setRuleRef("r1")
                .setIllustRef("i2")
                .setRulesetRef("r1")
                .setVersion(1)
                .createRuleWithIllustrations()).blockingAwait();
        ruleWithIllustrationDAO.insert(new RuleWithIllustrations.RuleWithIllustrationBuilder()
                .setRuleRef("r1")
                .setIllustRef("i3")
                .setRulesetRef("r1")
                .setVersion(2)
                .createRuleWithIllustrations()).blockingAwait();


        ruleWithIllustrationDAO.insert(new RuleWithIllustrations.RuleWithIllustrationBuilder()
                .setRuleRef("r2")
                .setIllustRef("i1")
                .setRulesetRef("r1")
                .setVersion(1)
                .createRuleWithIllustrations()).blockingAwait();
        ruleWithIllustrationDAO.insert(new RuleWithIllustrations.RuleWithIllustrationBuilder()
                .setRuleRef("r2")
                .setIllustRef("i2")
                .setRulesetRef("r2")
                .setVersion(1)
                .createRuleWithIllustrations()).blockingAwait();
        ruleWithIllustrationDAO.insert(new RuleWithIllustrations.RuleWithIllustrationBuilder()
                .setRuleRef("r2")
                .setIllustRef("i3")
                .setRulesetRef("r1")
                .setVersion(2)
                .createRuleWithIllustrations()).blockingAwait();
        ruleWithIllustrationDAO.insert(new RuleWithIllustrations.RuleWithIllustrationBuilder()
                .setRuleRef("r2")
                .setIllustRef("i4")
                .setRulesetRef("r1")
                .setVersion(1)
                .createRuleWithIllustrations()).blockingAwait();
    }

    // r2 = i1 i4
    // r1 = i1 i2
    @Test
    public void getIllustrationsForRule() {
        List<String> ruleRefs = new ArrayList<>();
        ruleRefs.add("r1");
        ruleRefs.add("r2");
        List<IllustrationWithRuleRef> illustrationWithRuleRefs = illustrationDAO.getIllustrationsForRule(ruleRefs, "r1", 1).blockingGet();
        Assert.assertEquals(illustrationWithRuleRefs.size(), 4);
        int r1i1 = 0, r1i2 = 0, r2i1 = 0, r2i4 = 0;
        for (IllustrationWithRuleRef illustrationWithRuleRef : illustrationWithRuleRefs) {
            if (check(illustrationWithRuleRef, "r1", "i1")) {
                r1i1++;
            } else if (check(illustrationWithRuleRef, "r1", "i2")) {
                r1i2++;
            } else if (check(illustrationWithRuleRef, "r2", "i1")) {
                r2i1++;
            } else if (check(illustrationWithRuleRef, "r2", "i4")) {
                r2i4++;
            }
        }
        Assert.assertArrayEquals(new int[]{r1i1, r1i2, r2i1, r2i4}, new int[]{1, 1, 1, 1});
    }

    private boolean check(IllustrationWithRuleRef illustrationWithRuleRef, String ruleRef, String illRef) {
        return illustrationWithRuleRef.getRuleRef().equals(ruleRef) &&
                illustrationWithRuleRef.getIllustration().getReference().equals(illRef);
    }

    @Test
    public void getIllustrations() {
        List<Illustration> r1Ill = illustrationDAO.getIllustrations("r1", "r1", 1).blockingGet();
        List<Illustration> r2Ill = illustrationDAO.getIllustrations("r2", "r1", 1).blockingGet();
        Assert.assertArrayEquals(new int[]{r1Ill.size(), r2Ill.size()}, new int[]{2, 2});
        int r1i1 = 0, r1i2 = 0, r2i1 = 0, r2i4 = 0;
        for (Illustration illustration : r1Ill) {
            if (illustration.getReference().equals("i1")) {
                r1i1++;
            } else if (illustration.getReference().equals("i2")) {
                r1i2++;
            }
        }
        for (Illustration illustration : r2Ill) {
            if (illustration.getReference().equals("i1")) {
                r2i1++;
            } else if (illustration.getReference().equals("i4")) {
                r2i4++;
            }
        }
        Assert.assertArrayEquals(new int[]{r1i1, r1i2, r2i1, r2i4}, new int[]{1, 1, 1, 1});
    }
}