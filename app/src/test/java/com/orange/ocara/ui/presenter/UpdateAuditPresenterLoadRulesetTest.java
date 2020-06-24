package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.ui.activity.UpdateAuditActivity;
import com.orange.ocara.ui.contract.UpdateAuditContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @see UpdateAuditPresenter#loadRuleset(AuditEntity)
 */
public class UpdateAuditPresenterLoadRulesetTest {

    private UpdateAuditPresenter subject;

    private ModelManager modelManager;

    private UpdateAuditContract.UpdateAuditView view;

    private RuleSetService ruleSetService;

    @Before
    public void setUp() {
        modelManager = mock(ModelManager.class);
        view = mock(UpdateAuditActivity.class);
        ruleSetService = mock(RuleSetService.class);

        subject = new UpdateAuditPresenter(view, ruleSetService, modelManager);
    }

    @Test
    public void shouldDoNothingWhenRulesetDoesNotExist() {
        // Given
        when(ruleSetService.getRuleSet(Mockito.anyString(), Mockito.anyInt(), Mockito.anyBoolean())).thenReturn(null);

        // When
        subject.loadRuleset(mock(AuditEntity.class));

        // Then
        verify(view).disableSaveButton();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void shouldShowRulesetInViewWhenRulesetExists() {

        // Given
        AuditEntity input = mock(AuditEntity.class);
        String reference = randomAlphabetic(5);
        when(input.getRuleSetRef()).thenReturn(reference);
        Integer version = nextInt();
        when(input.getRuleSetVersion()).thenReturn(version);

        RulesetEntity expectedRuleset = mock(RulesetEntity.class);
        when(expectedRuleset.getReference()).thenReturn(reference);
        when(expectedRuleset.getVersion()).thenReturn(version + "");
        when(ruleSetService.getRuleSet(reference, version, false)).thenReturn(expectedRuleset);

        // When
        subject.loadRuleset(input);

        // Then
        ArgumentCaptor<RulesetModel> argumentCaptor = ArgumentCaptor.forClass(RulesetModel.class);
        verify(view).showRuleset(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getReference()).isEqualTo(reference);
        assertThat(argumentCaptor.getValue().getVersion()).isEqualTo(version + "");
        verifyNoMoreInteractions(view);
    }
}