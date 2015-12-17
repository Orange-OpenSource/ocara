/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.dialog;

import com.orange.ocara.R;
import com.orange.ocara.tools.injection.DaggerDialogFragment;

/*package.*/ abstract class BaseDialogFragment extends DaggerDialogFragment {

    @Override
    public void setStyle(int style, int theme) {
        super.setStyle(style, R.style.Dialog);
    }
}
