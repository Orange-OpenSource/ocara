package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.model.SiteEntity;
import com.orange.ocara.ui.activity.UpdateAuditActivity;
import com.orange.ocara.ui.contract.UpdateAuditContract;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see UpdateAuditPresenter#loadSite(AuditEntity)
 */
public class UpdateAuditPresenterLoadSiteTest {

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
    public void shouldDisableSaveButtonWhenSiteDoesNotExist() {
        // Given
        AuditEntity input = mock(AuditEntity.class);
        SiteEntity expectedSite = null;
        when(input.getSite()).thenReturn(expectedSite);

        // When
        subject.loadSite(input);

        // Then
        verify(view).disableSaveButton();
        verify(view).showSite(expectedSite);
    }

    @Test
    public void shouldNotDisableSaveButtonWhenSiteExists() {

        // Given
        AuditEntity input = mock(AuditEntity.class);
        SiteEntity expectedSite = mock(SiteEntity.class);
        when(input.getSite()).thenReturn(expectedSite);

        // When
        subject.loadSite(input);

        // Then
        verify(view, never()).disableSaveButton();
        verify(view).showSite(expectedSite);
    }
}