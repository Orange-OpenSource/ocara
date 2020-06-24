package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.ui.activity.UpdateAuditActivity;
import com.orange.ocara.ui.contract.UpdateAuditContract;

import org.junit.Before;
import org.junit.Test;

import static com.orange.ocara.utils.TestUtils.randomAuditLevel;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see UpdateAuditPresenter#loadSite(AuditEntity)
 */
public class UpdateAuditPresenterLoadLevelTest {

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
    public void shouldShowLevelInView() {
        // Given
        AuditEntity input = mock(AuditEntity.class);
        AuditEntity.Level expectedLevel = randomAuditLevel();
        when(input.getLevel()).thenReturn(expectedLevel);

        // When
        subject.loadLevel(input);

        // Then
        verify(view).showLevel(expectedLevel);
    }

}