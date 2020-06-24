package com.orange.ocara.data.net.model;

import com.orange.ocara.data.cache.model.ResponseModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
@RunWith(Parameterized.class)
public class ResponseModelMaxTest {

    private ResponseModel left;

    private ResponseModel right;

    private ResponseModel expected;

    public ResponseModelMaxTest(ResponseModel left, ResponseModel right, ResponseModel expected) {
        this.left = left;
        this.right = right;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ResponseModel.NOT_APPLICABLE, ResponseModel.OK, ResponseModel.OK},
                {ResponseModel.OK, ResponseModel.NO_ANSWER, ResponseModel.NO_ANSWER},
                {ResponseModel.NO_ANSWER, ResponseModel.DOUBT, ResponseModel.DOUBT},
                {ResponseModel.DOUBT, ResponseModel.ANNOYING, ResponseModel.ANNOYING},
                {ResponseModel.ANNOYING, ResponseModel.BLOCKING, ResponseModel.BLOCKING},
                {ResponseModel.BLOCKING, ResponseModel.NOK, ResponseModel.NOK},
                {ResponseModel.NOT_APPLICABLE, ResponseModel.NOK, ResponseModel.NOK},
                {ResponseModel.OK, ResponseModel.NOK, ResponseModel.NOK},
                {ResponseModel.NO_ANSWER, ResponseModel.NOK, ResponseModel.NOK},
                {ResponseModel.DOUBT, ResponseModel.NOK, ResponseModel.NOK},
                {ResponseModel.ANNOYING, ResponseModel.NOK, ResponseModel.NOK},
        });
    }

    @Test
    public void shouldReturnExpectedWhenComparingTwoValues() {
        // given

        // when
        ResponseModel result = ResponseModel.max(left, right);

        // then
        assertThat(result).isEqualTo(expected);
    }
}