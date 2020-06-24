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
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.orange.ocara.data.cache.model.SiteEntity;

public class SiteAutoCompleteView extends AppCompatAutoCompleteTextView {

    public SiteAutoCompleteView(Context context) {
        super(context);
    }

    public SiteAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SiteAutoCompleteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static SpannableStringBuilder format(SiteEntity site) {
        SpannableStringBuilder result = new SpannableStringBuilder();

        if (site != null) {
            if (site.getNoImmo() != null) {
                result.append(site.getNoImmo());
                result.setSpan(new StyleSpan(Typeface.BOLD), 0, result.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                result.append(" -- ");
            }

            result.append(site.getName());
        }

        return result;
    }

    public void setCurrentSite(SiteEntity site) {
        setText(convertSelectionToString(site));
    }

    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        return format((SiteEntity) selectedItem);
    }

}