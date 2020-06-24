package com.orange.ocara.data.source;

import com.orange.ocara.utils.TestUtils;

import org.junit.Test;
import org.mockito.Mockito;

/** see {@link QuestionDataStoreImpl#findAll(Long, Long)}*/
public class QuestionDataStoreImplFindAllTest {

    private QuestionDataStoreImpl subject;

    private QuestionSource.QuestionCache questionCache;

    @Test
    public void shouldDelegateToCache() {

        // given
        questionCache = Mockito.mock(QuestionSource.QuestionCache.class);

        subject= new QuestionDataStoreImpl(questionCache);

        Long rulesetId = TestUtils.longNb();
        Long equipmentId = TestUtils.longNb();

        // when
        subject.findAll(rulesetId, equipmentId);

        // then
        Mockito.verify(questionCache).findAll(rulesetId, equipmentId);
    }
}