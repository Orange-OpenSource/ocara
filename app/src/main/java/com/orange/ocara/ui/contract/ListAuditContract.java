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

package com.orange.ocara.ui.contract;

import com.orange.ocara.business.model.AuditModel;
import com.orange.ocara.data.cache.model.AuditEntity;

/**
 * Contract between a view and the listener for its user's possible actions
 */
public interface ListAuditContract {

    /**
     * Behaviour of the view
     */
    interface ListAuditView {

        /**
         * displays the setup path that matches the given audit
         *
         * @param audit an {@link AuditModel}
         */
        void showSetupPath(AuditModel audit);
    }

    /**
     * Behaviour of the listener
     */
    interface ListAuditUserActionsListener {

        /**
         * creates a copy of the given audit
         *
         * @param itemToCopy an Audit
         */
        void createNewAudit(AuditEntity itemToCopy);
    }
}
