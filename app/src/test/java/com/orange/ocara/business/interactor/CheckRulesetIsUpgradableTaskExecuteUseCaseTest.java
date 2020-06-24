package com.orange.ocara.business.interactor;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableRequest;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.business.repository.RulesetRepository;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.utils.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * see {@link CheckRulesetIsUpgradableTask#executeUseCase(CheckRulesetIsUpgradableRequest, UseCaseCallback)}
 */
public class CheckRulesetIsUpgradableTaskExecuteUseCaseTest {

    private CheckRulesetIsUpgradableTask subject;

    private RulesetRepository repository;

    @Before
    public void setUp() {
        repository = mock(RulesetRepository.class);
        subject = new CheckRulesetIsUpgradableTask(repository);
    }

    @Test
    public void shouldReturnResponseOKWhenCheckingIsValid() {
        // Given
        String reference = TestUtils.str();
        Integer version = TestUtils.digit();

        VersionableModel inputRuleset = mock(VersionableModel.class);
        when(inputRuleset.getReference()).thenReturn(reference);
        when(inputRuleset.getVersion()).thenReturn(version + "");

        RulesetModel ruleset = RulesetModel.builder().reference(reference).version(version + "").stat(RuleSetStat.ONLINE).build();
        when(repository.findOne(reference, version)).thenReturn(ruleset);

        CheckRulesetIsUpgradableRequest input = new CheckRulesetIsUpgradableRequest(inputRuleset);
        UseCaseCallback callback = mock(UseCaseCallback.class);

        // When
        subject.executeUseCase(input, callback);

        // Then
        verify(callback, never()).onError(any(ErrorBundle.class));

        ArgumentCaptor<CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableResponse> argumentCaptor = ArgumentCaptor.forClass(CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().isOk()).isTrue();
    }

    @Test
    public void shouldReturnResponseKOWhenCheckingIsNotValid() {
        // Given
        String reference = TestUtils.str();
        Integer version = TestUtils.digit();

        VersionableModel inputRuleset = mock(VersionableModel.class);
        when(inputRuleset.getReference()).thenReturn(reference);
        when(inputRuleset.getVersion()).thenReturn(version + "");

        RulesetModel ruleset = RulesetModel.builder().reference(reference).version(version + "").stat(RuleSetStat.OFFLINE).build();
        when(repository.findOne(reference, version)).thenReturn(ruleset);

        CheckRulesetIsUpgradableRequest input = new CheckRulesetIsUpgradableRequest(ruleset);
        UseCaseCallback callback = mock(UseCaseCallback.class);

        // When
        subject.executeUseCase(input, callback);

        // Then
        verify(callback, never()).onError(any(ErrorBundle.class));

        ArgumentCaptor<CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableResponse> argumentCaptor = ArgumentCaptor.forClass(CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableResponse.class);
        verify(callback).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().isOk()).isFalse();
    }
}