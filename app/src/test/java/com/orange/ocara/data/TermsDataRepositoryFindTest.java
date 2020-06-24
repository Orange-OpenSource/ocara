package com.orange.ocara.data;

import com.orange.ocara.data.TermsDataRepository;
import com.orange.ocara.data.source.TermsSource;

import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @see TermsDataRepository#find()
 */
public class TermsDataRepositoryFindTest {

    private TermsDataRepository subject;

    private TermsSource.TermsDataStore cache;

    @Before
    public void setUp() {
        cache = mock(TermsSource.TermsDataStore.class);

        subject = new TermsDataRepository(cache);
    }

    @Test
    public void shouldDelegateToApi() {
        // Given
        when(cache.find()).thenReturn(randomAlphabetic(100));

        // When
        subject.find();

        // Then
    }
}