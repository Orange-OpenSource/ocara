package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.data.net.model.Ruleset;
import com.orange.ocara.ui.model.AuditFormUiModel;
import com.orange.ocara.data.cache.model.SiteEntity;
import com.orange.ocara.business.model.UserModel;
import com.orange.ocara.ui.activity.CreateAuditActivity;

import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see CreateAuditPresenter#validateAudit(AuditFormUiModel)
 */
public class CreateAuditPresenterValidateAuditTest {

    private CreateAuditPresenter subject;

    private CreateAuditActivity view;

    @Before
    public void setUp() {
        view = mock(CreateAuditActivity.class);
        subject = new CreateAuditPresenter(view, mock(UseCase.class), mock(UseCase.class));
    }

    @Test
    public void shouldEnableSaveButtonWhenNoErrorFound() {

        // Given
        UserModel user = mock(UserModel.class);
        when(user.getUserName()).thenReturn(randomAlphabetic(15));

        RulesetModel ruleset  = mock(RulesetModel.class);
        when(ruleset.getStat()).thenReturn(RuleSetStat.OFFLINE);

        SiteEntity site = mock(SiteEntity.class);

        AuditFormUiModel input = mock(AuditFormUiModel.class);
        when(input.getActualAuthor()).thenReturn(user);
        when(input.getActualRuleset()).thenReturn(ruleset);
        when(input.getActualSite()).thenReturn(site);

        // When
        subject.validateAudit(input);

        // Then
        verify(view).enableSaveButton();
    }

    @Test
    public void shouldDisableSaveButtonWhenErrorsAreFound() {

        // Given
        AuditFormUiModel input = mock(AuditFormUiModel.class);

        // When
        subject.validateAudit(input);

        // Then
        verify(view).disableSaveButton();
    }
}