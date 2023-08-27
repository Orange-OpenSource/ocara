package com.orange.ocara.data.cache.database.crossRef;
/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rule_illustrations")
public class RuleWithIllustrations {
    @PrimaryKey(autoGenerate = true)
    int id;
    @NonNull
    private String ruleRef;
    @NonNull
    private String illustRef;
    private int version;
    private String rulesetRef;

    public RuleWithIllustrations(@NonNull String ruleRef, @NonNull String illustRef, int version, String rulesetRef) {
        this.ruleRef = ruleRef;
        this.illustRef = illustRef;
        this.version = version;
        this.rulesetRef = rulesetRef;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getRulesetRef() {
        return rulesetRef;
    }

    public void setRulesetRef(String rulesetRef) {
        this.rulesetRef = rulesetRef;
    }

    @NonNull
    public String getRuleRef() {
        return ruleRef;
    }

    public void setRuleRef(@NonNull String ruleRef) {
        this.ruleRef = ruleRef;
    }

    @NonNull
    public String getIllustRef() {
        return illustRef;
    }

    public void setIllustRef(@NonNull String illustRef) {
        this.illustRef = illustRef;
    }

    public static class RuleWithIllustrationBuilder{

        private String ruleRef;
        private String illustRef;
        private int version;
        private String rulesetRef;

        public RuleWithIllustrationBuilder setRuleRef(String ruleRef) {
            this.ruleRef = ruleRef;
            return this;
        }

        public RuleWithIllustrationBuilder setIllustRef(String illustRef) {
            this.illustRef = illustRef;
            return this;
        }

        public RuleWithIllustrationBuilder setVersion(int version) {
            this.version = version;
            return this;
        }

        public RuleWithIllustrationBuilder setRulesetRef(String rulesetRef) {
            this.rulesetRef = rulesetRef;
            return this;
        }

        public RuleWithIllustrations createRuleWithIllustrations() {
            return new RuleWithIllustrations(ruleRef, illustRef, version, rulesetRef);
        }
    }
}
