/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.ui.intent.export.docx;

import com.orange.ocara.data.cache.model.AuditEntity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class AuditDetailPresenter extends Presenter<AuditEntity> {

    private SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("d MMMM yyyy", Locale.FRANCE);

    public AuditDetailPresenter(AuditEntity audit) {
        super(audit);
    }

    private static String wrapText(String value, int textviewWidth) {

        String temp = "";
        String sentence = "";

        // split by space
        String[] array = value.split(" ");

        for (String word : array) {

            // create a temp variable and check if length with new word exceeds textview width.
            if ((temp.length() + word.length()) < textviewWidth) {

                temp += " " + word;

            } else {
                // add new line character
                sentence += temp + "\n";
                temp = word;
            }

        }

        return (sentence.replaceFirst(" ", "") + temp);

    }

    public String getAuditName() {
        return wrapText(notNull(value.getName()), 16);
    }

    public String getAuditType() {
        return wrapText(notNull(value.getRuleSet().getType()) + " (v" + notNull(value.getRuleSet().getVersion()) + ")", 16);
    }

    public String getSiteNoImm() {
        return wrapText(notNull(value.getSite().getNoImmo()), 16);
    }

    public String getSiteName() {
        return wrapText(notNull(value.getSite().getName()), 16);
    }

    public String getAuditLevel() {
        switch (value.getLevel()) {
            case EXPERT:
                return "Expert";

            case BEGINNER:
                return "Guidé";
        }
        return null;
    }

    public String getAuditor() {
        return wrapText(notNull(value.getAuthor().getUserName()), 16);
    }

    public String getAuditDate() {
        return SIMPLE_DATE_FORMAT.format(value.getDate());
    }

    public String getAuditVersion() {
        return wrapText(notNull("v" + value.getVersion()), 16);
    }


}
