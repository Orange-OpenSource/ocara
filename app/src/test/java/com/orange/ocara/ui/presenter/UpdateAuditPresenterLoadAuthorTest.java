package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.AuditorEntity;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.ui.activity.UpdateAuditActivity;
import com.orange.ocara.ui.contract.UpdateAuditContract;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see UpdateAuditPresenter#loadSite(AuditEntity)
 */
public class UpdateAuditPresenterLoadAuthorTest {

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
    public void shouldShowAuthorInView() {

        // Given
        AuditEntity input = mock(AuditEntity.class);
        AuditorEntity expectedAuthor = mock(AuditorEntity.class);
        when(input.getAuthor()).thenReturn(expectedAuthor);

        // When
        subject.loadAuthor(input);

        // Then
        verify(view).showAuthor(expectedAuthor);
    }
}