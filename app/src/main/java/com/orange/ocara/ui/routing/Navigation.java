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

package com.orange.ocara.ui.routing;

import android.content.Context;

import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.SiteEntity;

/** description of a controller that aims to redirect to the appropriate view */
public interface Navigation {

    /**
     * redirects to the main view
     *
     * @param context the source {@link Context}
     */
    void navigateToHome(Context context);

    /**
     * redirects to the main view and expects a result code
     *
     * @param context the source {@link Context}
     * @param resultCode the expected response code
     */
    void navigateToHome(Context context, int resultCode);

    /**
     * redirects to a view that helps on editing {@link AuditEntity}s
     * @param context the source {@link Context}
     * @param auditId an identifier for the targeted {@link AuditEntity}
     * @param resultCode the expected response code
     */
    void navigateToAuditEditView(Context context, long auditId, int resultCode);

    /**
     * redirects to a view that creates {@link SiteEntity}s
     *
     * @param context the source {@link Context}
     * @param resultCode the expected response code
     */
    void navigateToSiteCreateView(Context context, int resultCode);

    /**
     * ends the app
     * @param context the source {@link Context}
     */
    void terminate(Context context);
}
