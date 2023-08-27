/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara.mobile.ui.view.piechart;

import android.animation.Animator;
import android.view.animation.Interpolator;

/**
 * Created by DouglasW on 6/8/2014.
 */
public interface HoloGraphAnimate {

    int ANIMATE_NORMAL = 0;
    int ANIMATE_INSERT = 1;
    int ANIMATE_DELETE = 2;

    int getDuration();

    void setDuration(int duration);

    Interpolator getInterpolator();

    void setInterpolator(Interpolator interpolator);

    boolean isAnimating();

    boolean cancelAnimating();

    void animateToGoalValues();

    void setAnimationListener(Animator.AnimatorListener animationListener);
}
