/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.modelStatic;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

public class ObjectDescriptionTest {

    @Test
    public void getName_Without_Set_In_Contructor_Should_Return_Null() {

        // Given
        ObjectDescription objectDescription = new ObjectDescription();

        // When
        String name = objectDescription.getName();

        // Then
        Assertions.assertThat(name).isNull();
    }

    @Test
    public void getName_With_Set_In_Contructor_Should_Return_Set_Name() {

        // Given
        String expectedName = "name";
        ObjectDescription objectDescription = new ObjectDescription();
        objectDescription.setName(expectedName);

        // When
        String name = objectDescription.getName();

        // Then
        Assertions.assertThat(name).isEqualTo(expectedName);
    }
}
