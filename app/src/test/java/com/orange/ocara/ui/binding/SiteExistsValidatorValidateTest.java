package com.orange.ocara.ui.binding;

import com.orange.ocara.data.cache.model.SiteEntity;
import com.orange.ocara.ui.model.AuditFormUiModel;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @see SiteExistsValidator#validate(AuditFormUiModel, BindingResult)
 */
public class SiteExistsValidatorValidateTest {
    private SiteExistsValidator subject = new SiteExistsValidator();

    @Test
    public void shouldRejectValueWhenGivenSiteIsNull() {
        // Given
        AuditFormUiModel input = mock(AuditFormUiModel.class);
        when(input.getActualSite()).thenReturn(null);

        BindingResult errors = new BindingResult();

        // When
        subject.validate(input, errors);

        // Then
        assertThat(errors.hasErrors()).isTrue();
    }

    @Test
    public void shouldRejectNoValueWhenGivenSiteIsNotNull() {
        // Given
        AuditFormUiModel input = mock(AuditFormUiModel.class);
        when(input.getActualSite()).thenReturn(mock(SiteEntity.class));

        BindingResult errors = new BindingResult();

        // When
        subject.validate(input, errors);

        // Then
        assertThat(errors.hasErrors()).isFalse();
    }
}