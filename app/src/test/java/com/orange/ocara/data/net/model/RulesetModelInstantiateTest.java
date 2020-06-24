package com.orange.ocara.data.net.model;

import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.utils.TestUtils;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * see {@link RulesetModel#newRuleSetInfo(RulesetEntity)}
 */
public class RulesetModelInstantiateTest {

    @Test
    public void shouldInstantiateAsInvalidWhenInputIsNull() {
        // Given


        // When
        RulesetModel result = RulesetModel.newRuleSetInfo(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStat()).isEqualTo(RuleSetStat.INVALID);
    }

    @Test
    public void shouldInstantiateWithInputAttributesWhenInputIsNotNull() {
        // Given
        RulesetEntity input = mock(RulesetEntity.class);
        when(input.getVersion()).thenReturn(RandomUtils.nextInt() + "");
        when(input.getReference()).thenReturn(randomAlphabetic(10));
        when(input.getType()).thenReturn(randomAlphabetic(10));
        when(input.getDate()).thenReturn(randomAlphabetic(10));
        when(input.getComment()).thenReturn(randomAlphabetic(10));
        when(input.getStat()).thenReturn(TestUtils.randomRulesetStat());
        when(input.getAuthorName()).thenReturn(randomAlphabetic(10));

        // When
        RulesetModel result = RulesetModel.newRuleSetInfo(input);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStat()).isEqualTo(input.getStat());
        assertThat(result.getVersion()).isEqualTo(input.getVersion());
        assertThat(result.getReference()).isEqualTo(input.getReference());
        assertThat(result.getType()).isEqualTo(input.getType());
        assertThat(result.getDate()).isEqualTo(input.getDate());
        assertThat(result.getComment()).isEqualTo(input.getComment());
        assertThat(result.getAuthor()).isEqualTo(input.getAuthorName());
    }
}