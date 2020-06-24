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

package com.orange.ocara.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.orange.ocara.data.cache.model.AuditorEntity;

/**
 * an implementation {@link AutoCompleteTextView} dedicated to {@link AuditorEntity}s
 */
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

    public static String format(AuditorEntity auditor) {
        StringBuilder result = new StringBuilder();

        if (auditor != null) {
            result.append(auditor.getUserName());
        }

        return result.toString();
    }

    public void setCurrentAuditor(AuditorEntity auditor) {
        setText(convertSelectionToString(auditor));
    }

    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        return super.convertSelectionToString(format((AuditorEntity) selectedItem));
    }
}