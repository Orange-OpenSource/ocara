package com.orange.ocara.ui.binding;

import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.data.net.model.Ruleset;
import com.orange.ocara.ui.model.AuditFormUiModel;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @see RulesetStateValidator#validate(AuditFormUiModel, BindingResult)
 */
public class RulesetStateValidatorValidateTest {

    private RulesetStateValidator subject = new RulesetStateValidator();

    @Test
    public void shouldRejectValueWhenRulesetIsNull() {
        // given
        AuditFormUiModel input = mock(AuditFormUiModel.class);
        when(input.getActualRuleset()).thenReturn(null);

        BindingResult errors = new BindingResult();

        // When
        subject.validate(input, errors);

        // Then
        assertThat(errors.hasErrors()).isTrue();
    }

    @Test
    public void shouldRejectValueWhenRulesetHasInvalidState() {
        // given
        RulesetModel mock = mock(RulesetModel.class);
        when(mock.getStat()).thenReturn(RuleSetStat.ONLINE);

        AuditFormUiModel input = mock(AuditFormUiModel.class);
        when(input.getActualRuleset()).thenReturn(mock);

        BindingResult errors = new BindingResult();

        // When
        subject.validate(input, errors);

        // Then
        assertThat(errors.hasErrors()).isTrue();
    }

    @Test
    public void shouldRejectNoValueWhenRulesetIsValid() {
        // given
        RulesetModel mock = mock(RulesetModel.class);
        when(mock.getStat()).thenReturn(RuleSetStat.OFFLINE);

        AuditFormUiModel input = mock(AuditFormUiModel.class);
        when(input.getActualRuleset()).thenReturn(mock);

        BindingResult errors = new BindingResult();

        // When
        subject.validate(input, errors);

        // Then
        assertThat(errors.hasErrors()).isFalse();
    }
}