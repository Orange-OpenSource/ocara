/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.conf;

import com.orange.ocara.R;
import com.orange.ocara.ui.audit.CreateAudit;
import com.orange.ocara.ui.audit.UpdateAudit;
import com.orange.ocara.ui.audit.impl.CreateAuditImpl;
import com.orange.ocara.ui.audit.impl.UpdateAuditImpl;
import com.orange.ocara.ui.disconnect.Disconnect;
import com.orange.ocara.ui.site.CreateSite;
import com.orange.ocara.ui.site.impl.CreateSiteImpl;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OcaraConfiguration {

    private static OcaraConfiguration ocaraConfiguration = new OcaraConfiguration();

    public static OcaraConfiguration get() {
        return ocaraConfiguration;
    }
    private int aboutXmlId = R.layout.activity_about;
    private int menuGlobalXmlId = R.menu.global;
    private CreateAudit createAudit = new CreateAuditImpl();
    private UpdateAudit updateAudit = new UpdateAuditImpl();
    private Disconnect disconnect;
    private CreateSite createSite = new CreateSiteImpl();

}
