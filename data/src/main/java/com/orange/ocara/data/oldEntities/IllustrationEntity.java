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

package com.orange.ocara.data.oldEntities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orange.ocara.data.network.models.IllustrationWs;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** a model for cached illustrations */
@Table(name = IllustrationEntity.TABLE_NAME, id = IllustrationEntity.ID)
@Data
@EqualsAndHashCode(callSuper = false)
public class IllustrationEntity extends Model implements Serializable, Explanation {

    public static final String TABLE_NAME = "illustration";
    public static final String ID = "_id";
    public static final String REFERENCE = "reference";
    public static final String IMAGE = "image";
    public static final String COMMENT = "comment";
    public static final String DATE = "date";
    public static final String DETAIL = "detail";


    @SerializedName(REFERENCE)
    @Expose
    @Column(name = REFERENCE)
    public String reference;

    @SerializedName(IMAGE)
    @Expose
    @Column(name = IMAGE)
    public String image;

    @SerializedName(COMMENT)
    @Expose
    @Column(name = COMMENT)
    public String comment;

    @SerializedName(DATE)
    @Expose
    @Column(name = DATE)
    public String date;

    @Column(name = DETAIL)
    private RulesetEntity rulesetDetail;

    @Override
    public String getIcon() {
        return getImage();
    }

    @Override
    public boolean hasIcon() {
        return getIcon() != null && !getIcon().isEmpty();
    }

    public static IllustrationEntity toEntity(IllustrationWs input, RulesetEntity ruleset) {

        IllustrationEntity output = new IllustrationEntity();

        output.setComment(input.getComment());
        output.setDate(input.getDate());
        output.setImage(input.getIcon());
        output.setReference(input.getReference());
        output.setRulesetDetail(ruleset);

        return output;
    }
}
