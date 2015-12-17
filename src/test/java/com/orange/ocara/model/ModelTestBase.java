/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import com.activeandroid.Model;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

abstract class ModelTestBase<T extends Model> {

    private Class<T> clazz;

    protected ModelTestBase(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Test
    public void equals_With_Same_Id_Should_Return_True() throws Exception {

        // Given
        long sameId = 777L;
        T a = buildEmptyWithId(sameId);
        T b = buildEmptyWithId(sameId);

        // When
        boolean equals = a.equals(b);

        // Then
        Assertions.assertThat(equals).isTrue();
    }

    @Test
    public void equal_With_Different_Id_Should_Return_False() throws Exception {
        // Given
        T a = buildEmptyWithId(1);
        T b = buildEmptyWithId(2);

        // When
        boolean equals = a.equals(b);

        // Then
        Assertions.assertThat(equals).isFalse();
    }

    @Test
    public void hashCode_With_Same_Id_Should_Return_True() throws Exception {
        // Given
        long sameId = 777L;
        T a = buildEmptyWithId(sameId);
        T b = buildEmptyWithId(sameId);

        // When
        boolean equals = a.hashCode() == b.hashCode();

        // Then
        Assertions.assertThat(equals).isTrue();
    }

    @Test
    public void hashCode_With_Different_Id_Should_Return_False() throws Exception {
        // Given
        T a = buildEmptyWithId(1);
        T b = buildEmptyWithId(2);

        // When
        boolean equals = a.hashCode() == b.hashCode();

        // Then
        Assertions.assertThat(equals).isFalse();
    }

    private T buildEmptyWithId(long id) throws InstantiationException,
            IllegalAccessException, NoSuchFieldException {
        T emptyInstance = clazz.newInstance();
        ModelUtils.setId(emptyInstance, id);

        return emptyInstance;
    }

}
