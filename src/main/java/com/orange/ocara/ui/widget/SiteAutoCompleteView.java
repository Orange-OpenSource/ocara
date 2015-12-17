/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import com.orange.ocara.model.Site;

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

    public void setCurrentSite(Site site) {
        setText(convertSelectionToString(site));
    }

    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        return format((Site) selectedItem, true);
    }

    public static SpannableStringBuilder format(Site site, boolean isNOImmoBold) {
        SpannableStringBuilder result = new SpannableStringBuilder();

        if (site != null) {
            if (site.getNoImmo() != null) {
                result.append(site.getNoImmo());
                if (isNOImmoBold) {
                    result.setSpan(new StyleSpan(Typeface.BOLD), 0, result.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                result.append(" -- ");
            }

            result.append(site.getName());
        }

        return result;
    }

}