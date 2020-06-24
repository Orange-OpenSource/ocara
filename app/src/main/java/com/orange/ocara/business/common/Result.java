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

package com.orange.ocara.business.common;

import com.orange.ocara.business.interactor.UseCase;

public class Result<T> implements UseCase.ResponseValue {

    protected final TaskStatus state;

    protected T data;

    protected String message;

    public Result(TaskStatus state, T data, String message) {
        this.state = state;
        this.data = data;
        this.message = message;
    }

    public String getCode() {
        return state.name();
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

}
