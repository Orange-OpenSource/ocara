package com.orange.ocara.business.interactor;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.RetrieveRulesetsTask.RetrieveRulesetsRequest;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.repository.RulesetRepository;
import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.data.net.model.Ruleset;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * see {@link RetrieveRulesetsTask#executeUseCase(RetrieveRulesetsRequest, UseCase.UseCaseCallback)}
 */
public class RetrieveRulesetsTaskExecuteUseCaseTest {

    private RetrieveRulesetsTask subject;

    private RulesetRepository rulesetRepository;

    @Before
    public void setUp() {
        rulesetRepository = mock(RulesetRepository.class);

        subject = new RetrieveRulesetsTask( rulesetRepository);
    }

    @Test
    public void shouldRetrieveListWithoutDefaultValueWhenSuccess() {
        // Given
        List<RulesetModel> list = givenList();
        Integer expectedSize = list.size();
        when(rulesetRepository.findAll()).thenReturn(list);

        UseCase.UseCaseCallback output = mock(UseCase.UseCaseCallback.class);
        RetrieveRulesetsRequest request = new RetrieveRulesetsRequest();

        // When
        subject.executeUseCase(request, output);

        // Then
        ArgumentCaptor<RetrieveRulesetsTask.RetrieveRulesetsResponse> argumentCaptor = forClass(RetrieveRulesetsTask.RetrieveRulesetsResponse.class);
        verify(output).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCount()).isEqualTo(expectedSize);
    }

    @Test
    public void shouldRetrieveListWithNewDefaultValueWhenSuccess() {
        // Given
        List<RulesetModel> list = givenList();
        Integer expectedSize = list.size();
        when(rulesetRepository.findAll()).thenReturn(list);

        UseCase.UseCaseCallback output = mock(UseCase.UseCaseCallback.class);
        RetrieveRulesetsRequest request = new RetrieveRulesetsRequest();

        // When
        subject.executeUseCase(request, output);

        // Then
        ArgumentCaptor<RetrieveRulesetsTask.RetrieveRulesetsResponse> argumentCaptor = forClass(RetrieveRulesetsTask.RetrieveRulesetsResponse.class);
        verify(output).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCount()).isEqualTo(expectedSize);
    }

    @Test
    public void shouldRetrieveListWithExistingDefaultValueWhenSuccess() {
        // Given
        List<RulesetModel> list = givenList();
        Integer expectedSize = list.size();
        when(rulesetRepository.findAll()).thenReturn(list);

        UseCase.UseCaseCallback output = mock(UseCase.UseCaseCallback.class);
        RetrieveRulesetsRequest request = new RetrieveRulesetsRequest();

        // When
        subject.executeUseCase(request, output);

        // Then
        ArgumentCaptor<RetrieveRulesetsTask.RetrieveRulesetsResponse> argumentCaptor = forClass(RetrieveRulesetsTask.RetrieveRulesetsResponse.class);
        verify(output).onComplete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCount()).isEqualTo(expectedSize);
    }

    @Test
    public void shouldRetrieveExceptionWhenProcessFails() {
        // Given
        when(rulesetRepository.findAll()).thenThrow(RuntimeException.class);

        UseCase.UseCaseCallback output = mock(UseCase.UseCaseCallback.class);
        RetrieveRulesetsRequest request = new RetrieveRulesetsRequest();

        // When
        subject.executeUseCase(request, output);

        // Then
        verify(output).onError(ArgumentMatchers.any(ErrorBundle.class));
    }

    List<RulesetModel> givenList() {

        RulesetModel o1 = givenRulesetInfo("91","1", "3", RuleSetStat.OFFLINE);
        RulesetModel o2 = givenRulesetInfo("52","2", "3", RuleSetStat.OFFLINE_WITH_NEW_VERSION);
        RulesetModel o3 = givenRulesetInfo("31","3", "2", RuleSetStat.ONLINE);
        RulesetModel o4 = givenRulesetInfo("14","4", "1", RuleSetStat.ONLINE);

        List<RulesetModel> list = Lists.newArrayList(o1, o2, o3, o4);
        Collections.shuffle(list);
        return list;
    }


    RulesetModel givenRulesetInfo(String label, String ref, String version, RuleSetStat stat) {
        return RulesetModel.builder()
                .type(label)
                .reference(ref)
                .version(version)
                .stat(stat)
                .build();
    }

}