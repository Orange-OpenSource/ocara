package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.ui.contract.ListRulesetsContract;

import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/** see {@link ListRulesPresenter#loadRulesets()} */
public class ListRulesPresenterLoadAllRulesetDetailsTest {

    private ListRulesPresenter subject;

    @Test
    public void shouldRetrieveDataFromServiceAndBringDataToView() {

        // given
        subject = new ListRulesPresenter();

        RuleSetService service = mock(RuleSetService.class);
        subject.setMRuleSetService(service);

        ListRulesetsContract.ListRulesView view = mock(ListRulesetsContract.ListRulesView.class);
        subject.setView(view);

        List<RulesetEntity> data = mock(List.class);
        when(service.getDownloadedRulesetDetails()).thenReturn(data);

        // when
        subject.loadRulesets();

        // then
        verify(service).getDownloadedRulesetDetails();
        verify(view).showRulesets(data);
        verifyNoMoreInteractions(service, view);
    }
}