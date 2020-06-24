package com.orange.ocara.data;

import com.orange.ocara.data.AuditDataRepository;
import com.orange.ocara.data.source.AuditSource.AuditDataStore;

import org.junit.Test;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see AuditDataRepository#findOne(Long)
 */
public class AuditDataRepositoryFindOneTest {

    private AuditDataRepository subject;

    private AuditDataStore dataStore;

    @Test
    public void shouldDelegateToCache() {
        // Given
        dataStore = mock(AuditDataStore.class);
        subject = new AuditDataRepository(dataStore);

        Long expected = nextLong();

        // When
        subject.findOne(expected);

        // Then
        verify(dataStore).findOne(expected);
    }
}