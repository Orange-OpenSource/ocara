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

package com.orange.ocara.domain.docexport.models;


import com.orange.ocara.domain.models.AuditModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class AuditDetailPresenter extends Presenter<AuditModel> {

    private final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("d MMMM yyyy", Locale.FRANCE);

    public AuditDetailPresenter(AuditModel audit) {
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
//        return wrapText(notNull(value.getName()), 16);
        return notNull(value.getName());

    }

    public String getAuditType() {
//        return wrapText(notNull(value.getRuleset().getType()) + " (v" + notNull(value.getRuleset().getVersion()) + ")", 16);
        return notNull(value.getRuleset().getType()) + " (v" + notNull(value.getRuleset().getVersion()) + ")";

    }

    public String getSiteNoImm() {
//        return wrapText(notNull(value.getSite().getNoImmo()), 16);
        return notNull(value.getSite().getNoImmo());

    }

    public String getSiteName() {
        return notNull(getFullAddress());
    }

    private String getFullAddress() {
        return value.getSite().getName() + " " + value.getSite().getAddressStreet() + " " + value.getSite().getAddressCode() + " " + value.getSite().getAddressCity();
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
        String auditorLastName = value.getAuditor().getLastName();
        auditorLastName = auditorLastName != null ? auditorLastName : "";
        String auditorFirst = value.getAuditor().getFirstName();
        auditorFirst = auditorFirst != null ? auditorFirst : "";
        return  notNull(auditorFirst + " " + auditorLastName);
    }

    public String getAuditDate() {
        return SIMPLE_DATE_FORMAT.format(value.getDate());
    }

    public String getAuditVersion() {
        return wrapText(notNull("v" + value.getVersion()), 16);
    }


}
