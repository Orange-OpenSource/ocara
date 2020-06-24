package com.orange.ocara;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Toolbox for tests
 */
public class TestUtils {

    private TestUtils() {
        /* do nothing */
    }

    /**
     * @return A random string value whose length is <= 20.
     */
    public static String str() {
        return RandomStringUtils.randomAlphanumeric(2, 21);
    }

    public static InputStream is() {
        return new ByteArrayInputStream(str().getBytes());
    }

    /** @return A random integer value. */
    public static int intNb() {
        return RandomUtils.nextInt(0, 100000000);
    }

    public static Context instrumentationContext() {
        return InstrumentationRegistry.getInstrumentation().getContext();
    }
}
