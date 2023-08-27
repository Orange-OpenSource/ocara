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

@Entity(tableName = "illustrations")
public class Illustration {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "illustRef")
    private String reference;
    private String image;
    private String comment;
    private String date;

    public Illustration(@NonNull String reference, String image, String comment, String date) {
        this.reference = reference;
        this.image = image;
        this.comment = comment;
        this.date = date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public static class IllustrationBuilder {
        private String reference;
        private String image;
        private String comment;
        private String date;

        private IllustrationBuilder() {
        }

        public static IllustrationBuilder anIllustration() {
            return new IllustrationBuilder();
        }

        public IllustrationBuilder withReference(String reference) {
            this.reference = reference;
            return this;
        }

        public IllustrationBuilder withImage(String image) {
            this.image = image;
            return this;
        }

        public IllustrationBuilder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public IllustrationBuilder withDate(String date) {
            this.date = date;
            return this;
        }

        public Illustration build() {
            return new Illustration(reference, image, comment, date);
        }
    }

}
