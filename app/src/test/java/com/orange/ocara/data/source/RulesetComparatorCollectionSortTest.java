package com.orange.ocara.data.source;

import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.business.model.RulesetModel;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * see {@link RulesetComparator#compare(RulesetModel, RulesetModel)}
 */
public class RulesetComparatorCollectionSortTest {

    @Test
    public void shouldSortRulesetsWithoutDefault() {
        // Given
        RulesetModel o1 = givenRulesetInfo("h","1", "3", RuleSetStat.OFFLINE);
        RulesetModel o2 = givenRulesetInfo("r","2", "3", RuleSetStat.OFFLINE_WITH_NEW_VERSION);
        RulesetModel o3 = givenRulesetInfo("z","3", "2", RuleSetStat.ONLINE);
        RulesetModel o4 = givenRulesetInfo("a","4", "1", RuleSetStat.ONLINE);

        List<RulesetModel> allRulSetInfo = givenList(o1, o2, o3, o4);

        // When
        Collections.sort(allRulSetInfo, new RulesetComparator());

        // Then
        assertThat(allRulSetInfo.get(0)).isEqualTo(o1);
        assertThat(allRulSetInfo.get(1)).isEqualTo(o2);
        assertThat(allRulSetInfo.get(2)).isEqualTo(o4);
        assertThat(allRulSetInfo.get(3)).isEqualTo(o3);
    }

    @Test
    public void shouldSortRulesetsWithDefault() {
        // Given
        RulesetModel o1 = givenRulesetInfo("h","1", "3", RuleSetStat.OFFLINE);
        RulesetModel o2 = givenRulesetInfo("r","2", "3", RuleSetStat.OFFLINE);
        RulesetModel o3 = givenRulesetInfo("z","3", "2", RuleSetStat.ONLINE);
        RulesetModel o4 = givenRulesetInfo("a","4", "1", RuleSetStat.ONLINE);
        RulesetModel o5 = givenRulesetInfo("r","2", "2", RuleSetStat.OFFLINE_WITH_NEW_VERSION);


        List<RulesetModel> allRulSetInfo = givenList(o1, o2, o3, o4, o5);

        // When
        Collections.sort(allRulSetInfo, new RulesetComparator(givenRulesetInfo("X","2", "2", RuleSetStat.INVALID)));

        // Then
        assertThat(allRulSetInfo.get(0)).isEqualTo(o5);
        assertThat(allRulSetInfo.get(1)).isEqualTo(o1);
        assertThat(allRulSetInfo.get(2)).isEqualTo(o2);
        assertThat(allRulSetInfo.get(3)).isEqualTo(o4);
        assertThat(allRulSetInfo.get(4)).isEqualTo(o3);
    }

    RulesetModel givenRulesetInfo(String label, String ref, String version, RuleSetStat stat) {
        return RulesetModel.builder()
                .type(label)
                .reference(ref)
                .version(version)
                .stat(stat).build();
    }

    List<RulesetModel> givenList(RulesetModel... rulesets) {
        List<RulesetModel> list = Lists.newArrayList(rulesets);
        Collections.shuffle(list);
        return list;
    }
}