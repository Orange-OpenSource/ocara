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

public class ResponseTest {

    @Test
    public void compute_should_return_NOK_if_present() {

        // Given
        Response[] allResponses = new Response[]{
                Response.DOUBT,
                Response.NoAnswer,
                Response.OK,
                Response.NOK,
                Response.DOUBT,
                Response.NotApplicable
        };

        // When
        Response merged = Response.compute(allResponses);

        // Then
        Assertions.assertThat(merged).isEqualTo(Response.NOK);
    }



    @Test
    public void compute_should_return_DOUBT_if_present() {

        // Given
        Response[] allResponses = new Response[]{
                Response.DOUBT,
                Response.NoAnswer,
                Response.OK,
                Response.NoAnswer,
                Response.NotApplicable
        };

        // When
        Response merged = Response.compute(allResponses);

        // Then
        Assertions.assertThat(merged).isEqualTo(Response.DOUBT);
    }



    @Test
    public void compute_should_return_NoAnswer_if_present() {

        // Given
        Response[] allResponses = new Response[]{
                Response.NotApplicable,
                Response.NoAnswer,
                Response.OK,
                Response.NotApplicable
        };

        // When
        Response merged = Response.compute(allResponses);

        // Then
        Assertions.assertThat(merged).isEqualTo(Response.NoAnswer);
    }



    @Test
    public void compute_should_return_OK_if_present() {

        // Given
        Response[] allResponses = new Response[]{
                Response.OK,
                Response.NotApplicable
        };

        // When
        Response merged = Response.compute(allResponses);

        // Then
        Assertions.assertThat(merged).isEqualTo(Response.OK);
    }



    @Test
    public void compute_should_return_NoApplicable_if_present() {

        // Given
        Response[] allResponses = new Response[]{
                Response.NotApplicable,
                Response.NotApplicable
        };

        // When
        Response merged = Response.compute(allResponses);

        // Then
        Assertions.assertThat(merged).isEqualTo(Response.NotApplicable);
    }
}
