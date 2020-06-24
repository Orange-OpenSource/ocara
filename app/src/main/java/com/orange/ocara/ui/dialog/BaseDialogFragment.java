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

package com.orange.ocara.ui.dialog;

import androidx.fragment.app.DialogFragment;

import com.orange.ocara.R;

/*package.*/ abstract class BaseDialogFragment extends DialogFragment {

    @Override
    public void setStyle(int style, int theme) {
        super.setStyle(style, R.style.Dialog);
    }
}
