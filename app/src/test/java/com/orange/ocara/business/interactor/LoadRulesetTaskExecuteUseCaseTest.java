package com.orange.ocara.business.interactor;

import com.orange.ocara.business.interactor.LoadRulesetTask.LoadRulesetRequest;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.business.repository.RulesetRepository;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.VersionableModel;
import com.orange.ocara.utils.TestUtils;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

import static com.orange.ocara.utils.TestUtils.intNb;
import static com.orange.ocara.utils.TestUtils.str;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/** see {@link LoadRulesetTask#executeUseCase(LoadRulesetRequest, UseCaseCallback)}*/
public class LoadRulesetTaskExecuteUseCaseTest {

    private LoadRulesetTask subject;

    private RulesetRepository rulesetRepository;

    @Test
    public void shouldCallOnCompleteWithDataWhenRepositoryHasData() {

        // given
        String ref = str();
        Integer version = intNb();
        LoadRulesetRequest inputRequest = givenLoadRulesetRequest(ref, version);

        UseCaseCallback<LoadRulesetTask.LoadRulesetResponse> inputCallback = mock(UseCaseCallback.class);

        prepareSubject(ref, version, mock(RulesetModel.class));

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onComplete(ArgumentMatchers.any(LoadRulesetTask.LoadRulesetResponse.class));
        verifyNoMoreInteractions(inputCallback);
    }

    @Test
    public void shouldCallOnErrorWithDataWhenRepositoryHasNoData() {

        // given
        String ref = str();
        Integer version = TestUtils.digit();
        LoadRulesetRequest inputRequest = givenLoadRulesetRequest(ref, version);

        UseCaseCallback<LoadRulesetTask.LoadRulesetResponse> inputCallback = mock(UseCaseCallback.class);

        prepareSubject(ref, version, null);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onError(ArgumentMatchers.any());
        verifyNoMoreInteractions(inputCallback);
    }

    private LoadRulesetRequest givenLoadRulesetRequest(String ref, Integer version) {

        VersionableModel mock = mock(VersionableModel.class);

        when(mock.getReference()).thenReturn(ref);
        when(mock.getVersion()).thenReturn(String.valueOf(version));

        return new LoadRulesetRequest(mock);
    }

    private void prepareSubject(String ref, Integer version, RulesetModel expectedData) {

        rulesetRepository = mock(RulesetRepository.class);
        when(rulesetRepository.findOne(ref, version)).thenReturn(expectedData);

        subject = new LoadRulesetTask(rulesetRepository);
    }
}