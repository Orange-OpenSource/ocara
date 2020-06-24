package com.orange.ocara.ui.presenter;

import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.business.model.AuditModel;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.ui.contract.UpdateAuditContract;
import com.orange.ocara.utils.ReflectionTestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see UpdateAuditPresenter#updateAudit(Long, AuditModel)
 */
public class UpdateAuditPresenterUpdateAuditTest {

    private UpdateAuditPresenter subject;

    private ModelManager modelManager;

    private UpdateAuditContract.UpdateAuditView view;

    @Before
    public void setUp() {
        modelManager = mock(ModelManager.class);
        view = mock(UpdateAuditContract.UpdateAuditView.class);

        subject = mock(UpdateAuditPresenter.class);
        ReflectionTestUtils.setField(subject, "modelManager", modelManager);
        ReflectionTestUtils.setField(subject, "view", view);
        doCallRealMethod().when(subject).updateAudit(Mockito.anyLong(), Mockito.any(AuditModel.class));
    }

    @Test
    public void shouldMergeAuditAndDelegateSaving() {

        // Given
        AuditEntity mock = mock(AuditEntity.class);
        when(modelManager.getAudit(Mockito.anyLong())).thenReturn(mock);

        // When
        subject.updateAudit(nextLong(), mock(AuditModel.class));

        // Then
        verify(mock).setAuthor(ArgumentMatchers.any());
        verify(mock).setLevel(ArgumentMatchers.any());
        verify(mock).setName(ArgumentMatchers.any());

        verify(modelManager).updateAudit(mock);
        verify(view).showAuditUpdatedSuccessfully();
    }
}