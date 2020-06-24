package com.orange.ocara.data.cache.db;

import com.orange.ocara.BuildConfig;

import java.util.Arrays;
import java.util.Collection;

/**
 * Toolbox for DB integration tests
 */
class DbTestUtils {

    /**
     *
     * @return an array of elements with 3 fields
     * - a count of items in the path
     * - the name of a file sample in the path
     */
    static SiteSample site() {

        if (BuildConfig.FLAVOR_mode == "opensource") {
            return SiteSample.OPENSOURCE;
        } else if (BuildConfig.FLAVOR_mode == "orangewithoutsmtk") {
            return SiteSample.ORANGE_WO_SMTK;
        } else {
            throw new RuntimeException("Unexpected BuildConfig.FLAVOR_mode : " + BuildConfig.FLAVOR_mode);
        }
    }

    enum SiteSample {
        OPENSOURCE(1, "OCARA", "999999", "Unité de Gestion Immobilière OCARA - TEST"),

        ORANGE_WO_SMTK(442, "IDFE", "940036", "UGI EST-OUEST");

        private int count;

        private String idUgi;

        private String noImmo;

        private String labelUgi;

        SiteSample(int count, String idUgi, String noImmo, String labelUgi) {
            this.count = count;
            this.idUgi = idUgi;
            this.noImmo = noImmo;
            this.labelUgi = labelUgi;
        }

        public int getCount() {
            return count;
        }

        public String getIdUgi() {
            return idUgi;
        }

        public String getNoImmo() {
            return noImmo;
        }

        public String getLabelUgi() {
            return labelUgi;
        }
    }
}
