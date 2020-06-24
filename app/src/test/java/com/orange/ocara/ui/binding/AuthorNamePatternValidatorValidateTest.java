package com.orange.ocara.ui.binding;

import com.orange.ocara.ui.model.AuditFormUiModel;
import com.orange.ocara.business.model.UserModel;

import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @see AuthorNamePatternValidator#validate(AuditFormUiModel, BindingResult)
 */
public class AuthorNamePatternValidatorValidateTest {

    private AuthorNamePatternValidator subject = new AuthorNamePatternValidator();

    @Test
    public void shouldRejectValueWhenAuthorNameIsNotValid() {
        // Given
        UserModel mock = mock(UserModel.class);
        when(mock.getUserName()).thenReturn(randomAlphabetic(2));

        AuditFormUiModel input = mock(AuditFormUiModel.class);
        when(input.getActualAuthor()).thenReturn(mock);

        BindingResult errors = new BindingResult();

        // When
        subject.validate(input, errors);

        // Then
        assertThat(errors.hasErrors()).isTrue();
    }

    @Test
    public void shouldNotRejectValueWhenAuthorNameIsValid() {
        // Given
        UserModel mock = mock(UserModel.class);
        when(mock.getUserName()).thenReturn(randomAlphabetic(3));

        AuditFormUiModel input = mock(AuditFormUiModel.class);
        when(input.getActualAuthor()).thenReturn(mock);

        BindingResult errors = new BindingResult();

        // When
        subject.validate(input, errors);

        // Then
        assertThat(errors.hasErrors()).isFalse();
    }
}