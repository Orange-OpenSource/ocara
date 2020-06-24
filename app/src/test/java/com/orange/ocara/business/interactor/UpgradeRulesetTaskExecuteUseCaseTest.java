package com.orange.ocara.business.interactor;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.UpgradeRulesetTask.UpgradeRulesetRequest;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.business.repository.RulesetRepository;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.utils.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * see {@link UpgradeRulesetTask#executeUseCase(UpgradeRulesetRequest, UseCaseCallback)}
 */
public class UpgradeRulesetTaskExecuteUseCaseTest {

    private UpgradeRulesetTask subject;

    private RulesetRepository repository;

    @Before
    public void setUp() {
        repository = mock(RulesetRepository.class);

        subject = new UpgradeRulesetTask(repository);
    }

    @Test
    public void shouldDelegateWhenProcessSuccessful() {
        // Given
        String reference = TestUtils.str();
        Integer version = TestUtils.digit();

        UseCaseCallback output = mock(UseCaseCallback.class);
        VersionableModel inputRuleset = mock(VersionableModel.class);
        when(inputRuleset.getReference()).thenReturn(reference);
        when(inputRuleset.getVersion()).thenReturn(version + "");

        RulesetModel expectedRuleset = mock(RulesetModel.class);
        UpgradeRulesetRequest request = new UpgradeRulesetRequest(inputRuleset);
        when(repository.findOne(reference, version)).thenReturn(expectedRuleset);

        // When
        subject.executeUseCase(request, output);

        // Then
        verify(output).onComplete(any(UpgradeRulesetTask.UpgradeRulesetResponse.class));
        verify(output, never()).onError(any(ErrorBundle.class));
    }

    @Test
    public void shouldRetrieveExceptionWhenProcessFails() {
        // Given
        doThrow(RuntimeException.class).when(repository).upgradeRuleset(Mockito.any(RulesetModel.class));

        UseCaseCallback output = mock(UseCaseCallback.class);
        UpgradeRulesetRequest request = new UpgradeRulesetRequest(mock(RulesetModel.class));

        // When
        subject.executeUseCase(request, output);

        // Then
        verify(output, never()).onComplete(any(UpgradeRulesetTask.UpgradeRulesetResponse.class));
        verify(output).onError(any(ErrorBundle.class));
    }
}