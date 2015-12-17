/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

import com.orange.ocara.model.Auditor;

public class AuditorAutoCompleteView extends AppCompatAutoCompleteTextView {

    public AuditorAutoCompleteView(Context context) {
        super(context);
    }

    public AuditorAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AuditorAutoCompleteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCurrentAuditor(Auditor auditor) {
        setText(convertSelectionToString(auditor));
    }

    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        return super.convertSelectionToString(format((Auditor) selectedItem));
    }

    public static String format(Auditor auditor) {
        StringBuilder result = new StringBuilder();

        if (auditor != null) {
            result.append(auditor.getUserName());
        }

        return result.toString();
    }
}