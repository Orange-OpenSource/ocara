package com.orange.ocara.data;

import com.orange.ocara.data.AuditDataRepository;
import com.orange.ocara.data.source.AuditSource.AuditDataStore;

import org.junit.Test;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see AuditDataRepository#cloneOne(Long)
 */
public class AuditDataRepositoryCloneOneTest {

    private AuditDataRepository subject;

    private AuditDataStore dataStore;

    @Test
    public void shouldDelegateToCache() {
        // Given
        dataStore = mock(AuditDataStore.class);
        subject = new AuditDataRepository(dataStore);

        Long expected = nextLong();

        // When
        subject.cloneOne(expected);

        // Then
        verify(dataStore).cloneOne(expected);
    }
}