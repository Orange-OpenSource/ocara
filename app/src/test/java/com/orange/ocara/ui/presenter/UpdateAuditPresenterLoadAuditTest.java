package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.ui.activity.UpdateAuditActivity;
import com.orange.ocara.ui.contract.UpdateAuditContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see UpdateAuditPresenter#loadAudit(Long)
 */
public class UpdateAuditPresenterLoadAuditTest {

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
    public void shouldDoNothingWhenAuditDoesNotExist() {
        // Given
        when(modelManager.getAudit(Mockito.anyLong())).thenReturn(null);

        // When
        subject.loadAudit(nextLong());

        // Then
        verify(view, never()).showAudit(ArgumentMatchers.any(AuditEntity.class));
     }

    @Test
    public void shouldShowAuditInViewWhenAuditExists() {

        // Given
        AuditEntity expectedAudit = mock(AuditEntity.class);
        when(modelManager.getAudit(Mockito.anyLong())).thenReturn(expectedAudit);

        // When
        subject.loadAudit(nextLong());

        // Then
        verify(view).showAudit(expectedAudit);
    }
}