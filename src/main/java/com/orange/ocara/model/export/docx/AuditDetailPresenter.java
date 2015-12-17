/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import com.orange.ocara.model.Audit;

import java.text.SimpleDateFormat;
import java.util.Locale;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
class AuditDetailPresenter extends Presenter<Audit> {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("d MMMM yyyy", Locale.FRANCE);

    public AuditDetailPresenter(Audit audit) {
        super(audit);
    }

    public String getAuditName() {
        return notNull(value.getName());
    }

    public String getAuditType() {
        return notNull(value.getRuleSet().getType());
    }

    public String getSiteNoImm() {
        return notNull(value.getSite().getNoImmo());
    }

    public String getSiteName() {
        return notNull(value.getSite().getName());
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
        return notNull(value.getAuthor().getUserName());
    }

    public String getAuditDate() {
        return SIMPLE_DATE_FORMAT.format(value.getDate());
    }

    public String getAuditVersion() { return notNull("v"+value.getVersion());}


}
