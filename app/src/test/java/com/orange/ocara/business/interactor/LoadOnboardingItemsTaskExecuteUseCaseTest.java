package com.orange.ocara.business.interactor;

import com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsRequest;
import com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsResponse;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.business.repository.OnboardingRepository;
import com.orange.ocara.business.model.OnboardingItemModel;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/** see {@link LoadOnboardingItemsTask#executeUseCase(LoadOnboardingItemsRequest, UseCaseCallback)}*/
public class LoadOnboardingItemsTaskExecuteUseCaseTest {

    private LoadOnboardingItemsTask subject;

    private OnboardingRepository repository;

    @Test
    public void shouldTriggerCallbackOnCompleteWithListResponseWhenGivenRepositoryWithList() {

        // given
        List<OnboardingItemModel> expectedList = Lists.newArrayList(mock(OnboardingItemModel.class));
        repository = mock(OnboardingRepository.class);
        when(repository.findAll()).thenReturn(expectedList);
        subject = new LoadOnboardingItemsTask(repository);

        LoadOnboardingItemsRequest inputRequest = new LoadOnboardingItemsRequest();
        UseCaseCallback<LoadOnboardingItemsResponse > inputCallback = mock(UseCaseCallback.class);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onComplete(argThat(argument -> argument.getItems() == expectedList && argument.getSize() == expectedList.size()));
        verifyNoMoreInteractions(inputCallback);
    }

    @Test
    public void shouldTriggerCallbackOnErrorWhenGivenRepositoryTriggersException() {

        // given
        repository = mock(OnboardingRepository.class);
        when(repository.findAll()).thenThrow(RuntimeException.class);
        subject = new LoadOnboardingItemsTask(repository);

        LoadOnboardingItemsRequest inputRequest = new LoadOnboardingItemsRequest();
        UseCaseCallback<LoadOnboardingItemsResponse > inputCallback = mock(UseCaseCallback.class);

        // when
        subject.executeUseCase(inputRequest, inputCallback);

        // then
        verify(inputCallback).onError(ArgumentMatchers.any());
        verifyNoMoreInteractions(inputCallback);
    }
}