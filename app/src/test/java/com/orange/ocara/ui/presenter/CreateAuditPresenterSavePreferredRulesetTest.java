package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.SavePreferredRulesetTask.SavePreferredRulesetRequest;
import com.orange.ocara.business.interactor.SavePreferredRulesetTask.SavePreferredRulesetResponse;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.data.net.model.Ruleset;
import com.orange.ocara.ui.activity.CreateAuditActivity;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see CreateAuditPresenter#savePreferredRuleset(Ruleset)
 */
public class CreateAuditPresenterSavePreferredRulesetTest {

    private CreateAuditPresenter subject;

    private CreateAuditActivity view;

    @Before
    public void setUp() {
        view = mock(CreateAuditActivity.class);
    }

    @Test
    public void shouldShowElementsWhenResponseIsSuccessful() {

        // Given
        UseCase<SavePreferredRulesetRequest, SavePreferredRulesetResponse> task  = (request, callback) -> callback.onComplete(SavePreferredRulesetResponse.ok());
        prepareSubject(task);

        RulesetModel input = mock(RulesetModel.class);

        // When
        subject.savePreferredRuleset(input);

        // Then
        verify(view).validateFields();
        verify(view).hideProgressDialog();
    }

    @Test
    public void shouldShowErrorWhenResponseIsFailure() {

        // Given
        UseCase<SavePreferredRulesetRequest, SavePreferredRulesetResponse> task  = (request, callback) -> callback.onError(mock(ErrorBundle.class));
        prepareSubject(task);

        RulesetModel input = mock(RulesetModel.class);

        // When
        subject.savePreferredRuleset(input);

        // Then
        verify(view).hideProgressDialog();
    }

    private void prepareSubject(UseCase<SavePreferredRulesetRequest, SavePreferredRulesetResponse> task) {

        subject = new CreateAuditPresenter(view, mock(UseCase.class), task);
    }
}