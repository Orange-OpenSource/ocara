package com.orange.ocara.utils;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.data.cache.model.AuditEntity;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.util.Files;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Utility box for tests
 */
public class TestUtils {

    private TestUtils() {
        /* do nothing */
    }

    private static final Random random = new Random();

    private static final List<RuleSetStat> rulesetStats = ImmutableList.<RuleSetStat>builder()
            .addAll(Arrays.asList(RuleSetStat.values())).build();

    public static RuleSetStat randomRulesetStat() {
        return value(RuleSetStat.class);
    }

    public static RuleSetStat randomUserTypeExcluding(RuleSetStat... exclusions) {
        List<RuleSetStat> list = Lists.newArrayList(rulesetStats);
        list.removeAll(Lists.newArrayList(exclusions));
        return list.get(random.nextInt(list.size()));
    }

    public static RuleSetStat randomUserTypeAmong(RuleSetStat... inclusions) {
        List<RuleSetStat> list = Lists.newArrayList(inclusions);
        return list.get(random.nextInt(list.size()));
    }

    public static AuditEntity.Level randomAuditLevel() {
        return value(AuditEntity.Level.class);
    }

    /**
     * @return A random string value whose length is <= 20.
     */
    public static String str() {
        return RandomStringUtils.randomAlphanumeric(2, 21);
    }

    /**
     * @return A random enum constant value
     */
    public static <T extends Enum<T>> T value(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[nextInt(0, values.length)];
    }

    public static InputStream is() {
        return new ByteArrayInputStream(str().getBytes());
    }

    /**
     * @param excluded Enumeration constant to be excluded
     * @return A random enum constant value, not being the passed excluded ones
     */
    public static <T extends Enum<T>> T value(T excluded) {
        List<T> values = new ArrayList<>(EnumSet.complementOf(EnumSet.of(excluded)));
        return values.get(nextInt(0, values.size()));
    }

    /** @return A random value with only 1 digit. */
    public static int digit() {
        return Math.abs(RandomUtils.nextInt(0, 10));
    }

    /** @return A random integer value. */
    public static int intNb() {
        return RandomUtils.nextInt(0, 100000000);
    }

    public static long longNb() {
        return RandomUtils.nextLong(0, 100000000);
    }


    public static Context mockContext(ClassLoader classLoader, String filename, String directory) throws IOException {
        Context mock = mock(Context.class);
        when(mock.getExternalCacheDir()).thenReturn(Files.temporaryFolder());
        when(mock.getFilesDir()).thenReturn(Files.temporaryFolder());

        InputStream is = classLoader.getResourceAsStream(directory + File.separator + filename);
        AssetManager assetManager = mock(AssetManager.class);
        when(assetManager.open(filename)).thenReturn(is);
        when(assetManager.list(directory)).thenReturn(new String[]{filename});
        when(mock.getAssets()).thenReturn(assetManager);

        return mock;
    }

    public static Context mockContext(ClassLoader classLoader) throws IOException {
        Context mock = mock(Context.class);

        File tmpDirectory = Files.temporaryFolder();
        when(mock.getExternalCacheDir()).thenReturn(tmpDirectory);
        when(mock.getFilesDir()).thenReturn(tmpDirectory);
        when(mock.getCacheDir()).thenReturn(tmpDirectory);

        AssetManager assetManager = mock(AssetManager.class);

        InputStream rulesetInputStream = classLoader.getResourceAsStream("ruleset" + File.separator + "ruleset_TEST_V1.json");
        when(assetManager.open("ruleset_TEST_V1.json")).thenReturn(rulesetInputStream);
        when(assetManager.open("ruleset" + File.separator + "ruleset_TEST_V1.json")).thenReturn(rulesetInputStream);
        when(assetManager.list("ruleset")).thenReturn(new String[]{"ruleset_TEST_V1.json"});

        InputStream sitesInputStream = classLoader.getResourceAsStream("sites" + File.separator + "sites_test.json");
        when(assetManager.open("sites_test.json")).thenReturn(sitesInputStream);
        when(assetManager.open("sites" + File.separator + "sites_test.json")).thenReturn(sitesInputStream);
        when(assetManager.list("sites")).thenReturn(new String[]{"sites_test.json"});

        when(mock.getAssets()).thenReturn(assetManager);

        return mock;
    }

    public static Context instrumentationContext() {
        return InstrumentationRegistry.getInstrumentation().getContext();
    }
}
