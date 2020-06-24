package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableRequest;
import com.orange.ocara.business.interactor.CheckRulesetIsUpgradableTask.CheckRulesetIsUpgradableResponse;
import com.orange.ocara.business.interactor.CheckRulesetRulesExistenceTask.CheckRulesetRulesExistenceRequest;
import com.orange.ocara.business.interactor.CheckRulesetRulesExistenceTask.CheckRulesetRulesExistenceResponse;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.ui.contract.RulesetInfoContract;
import com.orange.ocara.ui.contract.RulesetInfoContract.RulesetInfoView;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @see RulesetInfoContract.RulesetInfoUserActionsListener#checkRulesetIsValid(VersionableModel)
 */
public class RulesetInfoPresenterCheckRulesetIsValidTest {

    private RulesetInfoPresenter subject;

    private RulesetInfoView view;

    @Before
    public void setUp() {
        view = mock(RulesetInfoView.class);
    }

    @Test
    public void shouldDisableDetailsWhenCheckRulesetRulesExistenceTaskFails() {

        // Given
        prepareSubject(
                (request, callback) -> callback.onError(mock(ErrorBundle.class)),
                mock(UseCase.class));

        // When
        subject.checkRulesetIsValid(mock(RulesetModel.class));

        // Then
        verify(view, times(2)).disableRulesetDetails();
    }

    @Test
    public void shouldDisableDetailsWhenCheckRulesetRulesExistenceTaskResponseIsKo() {

        // Given
        prepareSubject(
                (request, callback) -> callback.onComplete(CheckRulesetRulesExistenceResponse.ko()),
                mock(UseCase.class));

        // When
        subject.checkRulesetIsValid(mock(RulesetModel.class));

        // Then
        verify(view, times(2)).disableRulesetDetails();
    }

    @Test
    public void shouldEnableDetailsWhenCheckRulesetRulesExistenceTaskResponseIsOk() {

        // Given
        prepareSubject(
                (request, callback) -> callback.onComplete(CheckRulesetRulesExistenceResponse.ok()),
                mock(UseCase.class));

        // When
        subject.checkRulesetIsValid(mock(RulesetModel.class));

        // Then
        verify(view).enableRulesetDetails();
    }

    @Test
    public void shouldDisableUpgradeWhenCheckRulesetIsUpgradableTaskFails() {
        // Given
        prepareSubject(
                mock(UseCase.class),
                (request, callback) -> callback.onError(mock(ErrorBundle.class)));

        // When
        subject.checkRulesetIsValid(mock(RulesetModel.class));

        // Then
        verify(view, times(2)).disableRulesetUpgrade();
    }

    @Test
    public void shouldDisableUpgradeWhenCheckRulesetIsUpgradableTaskResponseIsKo() {
        // Given
        prepareSubject(
                mock(UseCase.class),
                (request, callback) -> callback.onComplete(CheckRulesetIsUpgradableResponse.ko()));

        // When
        subject.checkRulesetIsValid(mock(RulesetModel.class));

        // Then
        verify(view, times(2)).disableRulesetUpgrade();
    }

    @Test
    public void shouldEnableUpgradeWhenCheckRulesetIsUpgradableTaskResponseIsOk() {
        // Given
        prepareSubject(
                mock(UseCase.class),
                (request, callback) -> callback.onComplete(CheckRulesetIsUpgradableResponse.ok()));

        // When
        subject.checkRulesetIsValid(mock(RulesetModel.class));

        // Then
        verify(view).disableRulesetUpgrade();
    }

    private void prepareSubject(UseCase<CheckRulesetRulesExistenceRequest, CheckRulesetRulesExistenceResponse> aTask,
                                UseCase<CheckRulesetIsUpgradableRequest, CheckRulesetIsUpgradableResponse> anotherTask) {

        subject = new RulesetInfoPresenter(view, mock(UseCase.class), aTask, anotherTask, mock(UseCase.class));
    }
}