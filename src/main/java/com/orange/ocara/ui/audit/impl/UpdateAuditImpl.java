/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.audit.impl;

import android.content.Context;

import com.orange.ocara.ui.activity.UpdateAuditActivity_;
import com.orange.ocara.ui.audit.UpdateAudit;

public class UpdateAuditImpl implements UpdateAudit {

    @Override
    public void updateAudit(Context context ,long auditId) {
        UpdateAuditActivity_.intent(context).auditId(auditId).startForResult(0);
    }
}
