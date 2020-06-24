package com.orange.ocara.data.cache.db;

import android.os.Build;

import com.orange.ocara.TestOcaraApplication;
import com.orange.ocara.business.model.SiteModel;
import com.orange.ocara.utils.TestUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(
        application = TestOcaraApplication.class,
        sdk = Build.VERSION_CODES.KITKAT)
public class DemoSiteDaoFindAllIT {

    private DemoSiteDao subject;

    @Test
    public void shouldRetrieveAllSitesWhenInputSiteHasSites() {

        // given
        subject = new DemoSiteDaoImpl(
                TestUtils.instrumentationContext());

        DbTestUtils.SiteSample expected = DbTestUtils.site();

        // when
        List<SiteModel> result = subject.findAll();

        // then
        assertThat(result).hasSize(expected.getCount());
        assertThat(result.get(0))
                .hasFieldOrPropertyWithValue("ugi", expected.getIdUgi())
                .hasFieldOrPropertyWithValue("labelUgi", expected.getLabelUgi())
                .hasFieldOrPropertyWithValue("noImmo", expected.getNoImmo());
    }
}