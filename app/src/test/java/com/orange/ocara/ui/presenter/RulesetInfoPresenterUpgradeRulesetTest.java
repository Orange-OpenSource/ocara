package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.UpgradeRulesetTask;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.ui.contract.RulesetInfoContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see RulesetInfoPresenter#upgradeRuleset(VersionableModel)
 */
public class RulesetInfoPresenterUpgradeRulesetTest {

    private RulesetInfoPresenter subject;

    private RulesetInfoContract.RulesetInfoView view;

    @Before
    public void setUp() {

        view = mock(RulesetInfoContract.RulesetInfoView.class);
    }

    @Test
    public void shouldShowFailureWhenTaskIsOnError() {

        // Given
        prepareSubject((upgradeRulesetRequest, callback) -> callback.onError(mock(ErrorBundle.class)));

        // When
        subject.upgradeRuleset(mock(RulesetModel.class));

        // Then
        verify(view).showDownloadFailed();
    }

    @Test
    public void shouldShowRulesetWhenTaskIsCompleted() {

        // Given
        prepareSubject((upgradeRulesetRequest, callback) -> callback.onComplete(UpgradeRulesetTask.UpgradeRulesetResponse.ok(mock(RulesetModel.class))));

        // When
        subject.upgradeRuleset(mock(RulesetModel.class));

        // Then
        verify(view).showRuleset(ArgumentMatchers.any());
    }

    private void prepareSubject(UseCase<UpgradeRulesetTask.UpgradeRulesetRequest, UpgradeRulesetTask.UpgradeRulesetResponse> task) {

        subject = new RulesetInfoPresenter(view, task, mock(UseCase.class), mock(UseCase.class), mock(UseCase.class));
    }
}