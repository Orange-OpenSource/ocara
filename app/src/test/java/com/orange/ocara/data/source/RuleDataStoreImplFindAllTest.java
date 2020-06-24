package com.orange.ocara.data.source;

import com.orange.ocara.utils.TestUtils;

import org.junit.Test;
import org.mockito.Mockito;

/** see {@link RuleDataStoreImpl#findAll(Long)} */
public class RuleDataStoreImplFindAllTest {

    private RuleDataStoreImpl subject;

    private RuleSource.RuleCache ruleCache;

    @Test
    public void shouldDelegateToCache() {

        // given
        ruleCache = Mockito.mock(RuleSource.RuleCache.class);

        subject= new RuleDataStoreImpl(ruleCache);

        Long inputId = TestUtils.longNb();

        // when
        subject.findAll(inputId);

        // then
        Mockito.verify(ruleCache).findAll(inputId);
    }
}