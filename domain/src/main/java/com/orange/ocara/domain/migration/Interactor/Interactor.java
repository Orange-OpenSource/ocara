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


package com.orange.ocara.domain.migration.Interactor;

import com.activeandroid.Model;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.List;

import io.reactivex.Completable;

public abstract class Interactor<T extends Model> {
    public abstract Completable execute();
    List<T> getAll(Class<? extends Model> table){
        From from = new Select().all().from(table);
        return from.execute();
    }
}
