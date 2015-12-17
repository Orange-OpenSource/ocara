/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.tools.injection;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import dagger.ObjectGraph;

public abstract class DaggerDialogFragment extends DialogFragment implements DaggerInjector {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inject(this);
    }

    @Override
    public void inject(Object object) {
        ObjectGraph og = getObjectGraph();
        if (og != null) {
            og.inject(this);
        }
    }

    @Override
    public ObjectGraph getObjectGraph() {
        return DaggerHelper.getObjectGraph(getActivity());
    }
}
