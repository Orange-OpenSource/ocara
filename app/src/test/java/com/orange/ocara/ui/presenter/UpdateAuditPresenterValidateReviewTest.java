package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.business.model.AuditModel;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.ui.activity.UpdateAuditActivity;

import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see UpdateAuditPresenter#validateReview(AuditModel)
 */
public class UpdateAuditPresenterValidateReviewTest {

    private UpdateAuditPresenter subject;

    private UpdateAuditActivity view;

    private RuleSetService ruleSetService;

    private ModelManager modelManager;

    @Before
    public void setUp() {
        modelManager = mock(ModelManager.class);
        view = mock(UpdateAuditActivity.class);
        ruleSetService = mock(RuleSetService.class);

        subject = new UpdateAuditPresenter(view, ruleSetService, modelManager);
    }


    @Test
    public void shouldDisableSaveButtonWhenInputIsNotValid() {
        // Given
        AuditModel mock = mock(AuditModel.class);
        when(mock.getName()).thenReturn(null);

        // When
        subject.validateReview(mock);

        // Then
        verify(view).disableSaveButton();
    }

    @Test
    public void shouldNotDisableSaveButtonWhenInputIsValid() {
        // Given
        AuditModel mock = mock(AuditModel.class);
        when(mock.getName()).thenReturn(randomAlphabetic(10));

        // When
        subject.validateReview(mock);

        // Then
        verify(view, never()).disableSaveButton();
    }
}