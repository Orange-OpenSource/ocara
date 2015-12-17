/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.audit.impl;

import android.content.Context;

import com.orange.ocara.ui.activity.CreateAuditActivity_;
import com.orange.ocara.ui.audit.CreateAudit;

public class CreateAuditImpl implements CreateAudit {
    @Override
    public void createAudit(Context context) {
            CreateAuditActivity_.intent(context).start();

    }

    @Override
    public void createAuditWithNullAuditId(Context context) {
            CreateAuditActivity_.intent(context).auditId(null).start();
    }
}
