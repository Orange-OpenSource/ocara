package com.orange.ocara.ui.binding;

import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.business.model.AuditModel;
import com.orange.ocara.ui.model.AuditFormUiModel;
import com.orange.ocara.data.cache.db.ModelManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @see AuditNameDoesNotExistValidator#validate(AuditFormUiModel, BindingResult)
 */
public class AuditNameDoesNotExistValidatorValidateTest {

    private AuditNameDoesNotExistValidator subject;

    private ModelManager modelManager;

    @Before
    public void setUp() {
        modelManager = mock(ModelManager.class);

        subject = new AuditNameDoesNotExistValidator(modelManager);
    }

    @Test
    public void shouldRejectValueWhenAuditDoesNotExist() {
        // Given
        AuditFormUiModel input = mock(AuditFormUiModel.class);
        when(input.getActualAudit()).thenReturn(null);

        BindingResult errors = new BindingResult();

        // When
        subject.validate(input, errors);

        // Then
        assertThat(errors.hasErrors()).isTrue();
    }

    @Test
    public void shouldRejectValueWhenAuditNameIsNull() {
        // Given
        AuditEntity mock = mock(AuditEntity.class);
        when(mock.getName()).thenReturn(null);

        AuditFormUiModel input = mock(AuditFormUiModel.class);
        when(input.getActualAudit()).thenReturn(mock);

        BindingResult errors = new BindingResult();

        // When
        subject.validate(input, errors);

        // Then
        assertThat(errors.hasErrors()).isTrue();
    }

    @Test
    public void shouldRejectValueWhenAuditNameIsEmpty() {
        // Given
        AuditEntity mock = mock(AuditEntity.class);
        when(mock.getName()).thenReturn("");

        AuditFormUiModel input = mock(AuditFormUiModel.class);
        when(input.getActualAudit()).thenReturn(mock);

        BindingResult errors = new BindingResult();

        // When
        subject.validate(input, errors);

        // Then
        assertThat(errors.hasErrors()).isTrue();
    }

    @Test
    public void shouldRejectValueWhenAuditAlreadyExists() {
        // Given
        String auditName = randomAlphabetic(10);
        Integer auditVersion = nextInt();
        Long auditId = nextLong();
        AuditModel mock = mock(AuditModel.class);

        when(mock.getId()).thenReturn(auditId);
        when(mock.getName()).thenReturn(auditName);
        when(mock.getVersion()).thenReturn(auditVersion);

        AuditFormUiModel input = mock(AuditFormUiModel.class);
        when(input.getActualAudit()).thenReturn(mock);

        BindingResult errors = new BindingResult();

        when(modelManager.checkAuditExistence(auditId, auditName, auditVersion))
                .thenReturn(true);

        // When
        subject.validate(input, errors);

        // Then
        assertThat(errors.hasErrors()).isTrue();
    }

    @Test
    public void shouldNotRejectValueWhenAuditIsValid() {
        // Given
        AuditModel mock = mock(AuditModel.class);
        when(mock.getId()).thenReturn(nextLong());
        when(mock.getName()).thenReturn(randomAlphabetic(10));
        when(mock.getVersion()).thenReturn(nextInt());

        AuditFormUiModel input = mock(AuditFormUiModel.class);
        when(input.getActualAudit()).thenReturn(mock);

        BindingResult errors = new BindingResult();

        when(modelManager.checkAuditExistence(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(false);

        // When
        subject.validate(input, errors);

        // Then
        assertThat(errors.hasErrors()).isFalse();
    }
}