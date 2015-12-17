/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import android.text.Html;

import org.apache.commons.lang3.StringUtils;

class Presenter<T> {

    protected T value;

    public Presenter(T value) {
        this.value = value;
    }

    public boolean hasSameValue(T value) {
        return this.value.equals(value);
    }

    protected static String notNull(String value) {
        return StringUtils.trimToEmpty(value);
    }

    protected static String toHtmlNotNull(String value) {
        return notNull(Html.fromHtml(value).toString());
    }

}
