package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.data.cache.model.AuditorEntity;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.business.model.UserModel;
import com.orange.ocara.ui.activity.UpdateAuditActivity;

import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see UpdateAuditPresenter#validateAuthor(UserModel)
 */
public class UpdateAuditPresenterValidateAuthorTest {

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
        AuditorEntity author = mock(AuditorEntity.class);
        when(author.getUserName()).thenReturn(null);

        // When
        subject.validateAuthor(author);

        // Then
        verify(view).disableSaveButton();
    }

    @Test
    public void shouldNotDisableSaveButtonWhenInputIsValid() {
        // Given
        AuditorEntity author = mock(AuditorEntity.class);
        when(author.getUserName()).thenReturn(randomAlphabetic(10));

        // When
        subject.validateAuthor(author);

        // Then
        verify(view, never()).disableSaveButton();
    }
}