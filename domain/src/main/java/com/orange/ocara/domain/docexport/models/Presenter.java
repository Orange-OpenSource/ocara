/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */
package com.orange.ocara.domain.docexport.models;

import android.text.Html;

import com.orange.ocara.utils.StringUtils;

public class Presenter<T> {

    protected T value;

    public Presenter(T value) {
        this.value = value;
    }

    protected static String notNull(String value) {
        return StringUtils.trimToEmpty(value);
    }

    protected static String toHtmlNotNull(String value) {
        return notNull(Html.fromHtml(value).toString());
    }

    public boolean hasSameValue(T value) {
        return this.value.equals(value);
    }

}
