package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.data.net.model.Ruleset;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.business.model.AuditModel;
import com.orange.ocara.data.cache.model.AuditorEntity;
import com.orange.ocara.ui.model.AuditFormUiModel;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.model.SiteEntity;
import com.orange.ocara.ui.activity.UpdateAuditActivity;

import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see UpdateAuditPresenter#validateAudit(AuditFormUiModel)
 */
public class UpdateAuditPresenterValidateAuditTest {

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
    public void shouldEnableSaveButtonWhenValidationFindsNoErrors() {
        // Given
        AuditFormUiModel input = givenValidAuditForm();

        // When
        subject.validateAudit(input);

        // Then
        verify(view).enableSaveButton();
    }

    @Test
    public void shouldDisableSaveButtonWhenValidationFindsErrors() {
        // Given
        AuditFormUiModel input = givenInvalidAuditForm();

        // When
        subject.validateAudit(input);

        // Then
        verify(view).disableSaveButton();
        verify(view, never()).enableSaveButton();
    }

    private AuditFormUiModel givenValidAuditForm() {
        AuditFormUiModel mock = mock(AuditFormUiModel.class);

        AuditModel reference = mock(AuditModel.class);
        when(reference.getName()).thenReturn(randomAlphabetic(10));
        when(reference.getAuthorName()).thenReturn(randomAlphabetic(10));
        when(reference.getLevel()).thenReturn(AuditEntity.Level.EXPERT);
        when(mock.getInitialAudit()).thenReturn(reference);

        AuditModel actual = mock(AuditModel.class);
        when(actual.getName()).thenReturn(randomAlphabetic(10));
        when(actual.getAuthorName()).thenReturn(randomAlphabetic(10));
        when(actual.getLevel()).thenReturn(AuditEntity.Level.EXPERT);
        when(mock.getActualAudit()).thenReturn(actual);

        when(mock.getActualSite()).thenReturn(mock(SiteEntity.class));

        AuditorEntity author = mock(AuditorEntity.class);
        when(author.getUserName()).thenReturn(randomAlphabetic(10));
        when(mock.getActualAuthor()).thenReturn(author);
        when(mock.getActualRuleset()).thenReturn(mock(RulesetModel.class));

        return mock;
    }

    private AuditFormUiModel givenInvalidAuditForm() {
        AuditFormUiModel mock = mock(AuditFormUiModel.class);

        AuditModel reference = mock(AuditModel.class);
        when(reference.getName()).thenReturn(randomAlphabetic(10));
        when(reference.getAuthorName()).thenReturn(randomAlphabetic(10));
        when(reference.getLevel()).thenReturn(AuditEntity.Level.EXPERT);

        AuditModel actual = mock(AuditModel.class);
        when(actual.getName()).thenReturn(randomAlphabetic(10));
        when(actual.getAuthorName()).thenReturn(randomAlphabetic(10));
        when(actual.getLevel()).thenReturn(AuditEntity.Level.EXPERT);
        when(mock.getInitialAudit()).thenReturn(reference);
        when(mock.getActualAudit()).thenReturn(actual);

        // validation should fail : site is null
        when(mock.getActualSite()).thenReturn(null);

        // validation should fail : author has no username
        when(mock.getActualAuthor()).thenReturn(mock(AuditorEntity.class));

        when(mock.getActualRuleset()).thenReturn(mock(RulesetModel.class));

        return mock;
    }
}