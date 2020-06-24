package com.orange.ocara.data.source;

import com.orange.ocara.business.model.RulesetModel;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextBoolean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * see {@link RulesetComparator#compare(RulesetModel, RulesetModel)}
 */
public class RulesetComparatorCompareTest {

    private RulesetComparator subject;

    @Test
    public void shouldReturnNegativeWhenFirstInputIsEqualToReference() {
        // Given
        String versionName = randomAlphanumeric(10);
        RulesetModel reference = mock(RulesetModel.class);
        when(reference.getVersionName()).thenReturn(versionName);

        RulesetModel input = mock(RulesetModel.class);
        when(input.getVersionName()).thenReturn(versionName);

        subject = new RulesetComparator(reference);

        // When
        int result = subject.compare(input, mock(RulesetModel.class));

        // Then
        assertThat(result).isLessThan(0);
    }

    @Test
    public void shouldReturnPositiveWhenSecondInputIsEqualToReference() {
        // Given
        String versionName = randomAlphanumeric(10);
        RulesetModel reference = mock(RulesetModel.class);
        when(reference.getVersionName()).thenReturn(versionName);

        RulesetModel input = mock(RulesetModel.class);
        when(input.getVersionName()).thenReturn(versionName);

        subject = new RulesetComparator(reference);

        // When
        int result = subject.compare(mock(RulesetModel.class), input);

        // Then
        assertThat(result).isGreaterThan(0);
    }

    @Test
    public void shouldReturnNegativeWhenFirstInputIsLocalAndSecondInputIsRemote() {
        // Given
        RulesetModel first = mock(RulesetModel.class);
        when(first.isLocallyAvailable()).thenReturn(true);

        RulesetModel second = mock(RulesetModel.class);
        when(second.isRemotelyAvailable()).thenReturn(true);

        subject = new RulesetComparator();

        // When
        int result = subject.compare(first, second);

        // Then
        assertThat(result).isLessThan(0);
    }

    @Test
    public void shouldReturnPositiveWhenFirstInputIsRemoteAndSecondInputIsLocal() {
        // Given
        RulesetModel first = mock(RulesetModel.class);
        when(first.isRemotelyAvailable()).thenReturn(true);

        RulesetModel second = mock(RulesetModel.class);
        when(second.isLocallyAvailable()).thenReturn(true);

        subject = new RulesetComparator();

        // When
        int result = subject.compare(first, second);

        // Then
        assertThat(result).isGreaterThan(0);
    }

    @Test
    public void shouldReturnAlphabeticalCompareResultWhenBothInputsAreLocalOrRemote() {
        // Given
        boolean isLocal = nextBoolean();
        boolean isRemote = !isLocal;

        String firstType = RandomStringUtils.randomAlphanumeric(10);
        RulesetModel first = mock(RulesetModel.class);
        when(first.isLocallyAvailable()).thenReturn(isLocal);
        when(first.isRemotelyAvailable()).thenReturn(isRemote);
        when(first.getType()).thenReturn(firstType);

        String secondType = RandomStringUtils.randomAlphanumeric(10);
        RulesetModel second = mock(RulesetModel.class);
        when(second.isLocallyAvailable()).thenReturn(isLocal);
        when(second.isRemotelyAvailable()).thenReturn(isRemote);
        when(second.getType()).thenReturn(secondType);

        int expected = firstType.compareTo(secondType);

        subject = new RulesetComparator();

        // When
        int result = subject.compare(first, second);

        // Then
        assertThat(result).isEqualTo(expected);
    }
}