package com.orange.ocara.data.cache.database.Tables;
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
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import com.orange.ocara.utils.enums.RuleSetStat;

import java.util.Objects;

@Entity(tableName = "ruleset_details")
public class RulesetDetails {
    @PrimaryKey(autoGenerate = true)
    private Long rulesetId;
    private int isDemo;
    private String reference;
    private String language;
    private String type;
    private String comment;
    private String authorName;
    private String categoryName;
    @ColumnInfo(name = "ruleset_date")
    private String date;
    @ColumnInfo(name = "ruleset_version")
    private int version;
    private RuleSetStat ruleSetStat=RuleSetStat.OFFLINE;

    public Long getRulesetId() {
        return rulesetId;
    }

    public void setRulesetId(Long rulesetId) {
        this.rulesetId = rulesetId;
    }

    public RuleSetStat getRuleSetStat() {
        return ruleSetStat;
    }

    public void setRuleSetStat(RuleSetStat ruleSetStat) {
        this.ruleSetStat = ruleSetStat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsDemo() {
        return isDemo;
    }

    public void setIsDemo(int isDemo) {
        this.isDemo = isDemo;
    }

    @NonNull
    public String getReference() {
        return reference;
    }

    public void setReference(@NonNull String reference) {
        this.reference = reference;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RulesetDetails that = (RulesetDetails) o;
        return version == that.version &&
                Objects.equals(reference, that.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference, version);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static class Builder {
        private String reference;
        private String language;
        private String type;
        private String comment;
        private String authorName;
        private String categoryName;
        private String date;
        private int version;
        private Long id;
        private RuleSetStat ruleSetStat;

        public Builder setRuleSetStat(RuleSetStat ruleSetStat) {
            this.ruleSetStat = ruleSetStat;
            return this;
        }
        public Builder id(Long id){
            this.id=id;
            return this;
        }
        public Builder setDate(String date) {
            this.date = date;
            return this;
        }

        public RulesetDetails build() {
            RulesetDetails rulesetDetails = new RulesetDetails();
            rulesetDetails.setAuthorName(authorName);
            rulesetDetails.setRulesetId(id);
            rulesetDetails.setLanguage(language);
            rulesetDetails.setType(type);
            rulesetDetails.setCategoryName(categoryName);
            rulesetDetails.setRuleSetStat(ruleSetStat);
            rulesetDetails.setDate(date);
            rulesetDetails.setComment(comment);
            rulesetDetails.setReference(reference);
            rulesetDetails.setVersion(version);
            return rulesetDetails;
        }

        public Builder setReference(String reference) {
            this.reference = reference;
            return this;
        }


        public Builder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder setAuthorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

        public Builder setCategoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

    }

}
