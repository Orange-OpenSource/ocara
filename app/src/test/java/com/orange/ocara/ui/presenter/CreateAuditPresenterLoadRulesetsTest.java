package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.RetrieveRulesetsTask;
import com.orange.ocara.business.interactor.RetrieveRulesetsTask.RetrieveRulesetsResponse;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.data.net.model.Ruleset;
import com.orange.ocara.ui.activity.CreateAuditActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.List;

import static com.orange.ocara.tools.ListUtils.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see CreateAuditPresenter#loadRulesets()
 */
public class CreateAuditPresenterLoadRulesetsTest {

    private CreateAuditPresenter subject;

    private CreateAuditActivity view;

    @Before
    public void setUp() {
        view = mock(CreateAuditActivity.class);
    }

    @Test
    public void shouldShowElementsWhenResponseIsSuccessful() {

        // Given
        UseCase<RetrieveRulesetsTask.RetrieveRulesetsRequest, RetrieveRulesetsResponse> task  = (request, callback) -> {
            List<RulesetModel> list = newArrayList(mock(RulesetModel.class));
            RetrieveRulesetsResponse response = RetrieveRulesetsResponse.ok(list);
            callback.onComplete(response);
        };
        prepareSubject(task);

        // When
        subject.loadRulesets();

        // Then
        verify(view).showRulesetList(ArgumentMatchers.anyList());
        verify(view).enableRulesInfo();
        verify(view).hideProgressDialog();
    }

    @Test
    public void shouldShowErrorWhenResponseIsFailure() {

        // Given
        UseCase<RetrieveRulesetsTask.RetrieveRulesetsRequest, RetrieveRulesetsResponse> task  = (request, callback) -> callback.onError(mock(ErrorBundle.class));
        prepareSubject(task);

        // When
        subject.loadRulesets();

        // Then
        verify(view).showDownloadError();
        verify(view).hideProgressDialog();
    }

    private void prepareSubject(UseCase<RetrieveRulesetsTask.RetrieveRulesetsRequest, RetrieveRulesetsResponse> task) {

        subject = new CreateAuditPresenter(view, task, mock(UseCase.class));
    }
}