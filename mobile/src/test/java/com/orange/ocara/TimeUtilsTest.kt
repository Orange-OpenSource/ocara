/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara

import com.orange.ocara.utils.TimeUtils
import org.junit.Assert
import org.junit.Test

class TimeUtilsTest {
    @Test
    fun getTime(){
        Assert.assertEquals("1:03",TimeUtils.getTime(63000))
        Assert.assertEquals("1:03",TimeUtils.getTime(63200))
        Assert.assertEquals("1:04",TimeUtils.getTime(63900))
        Assert.assertEquals("1:04",TimeUtils.getTime(64000))
        Assert.assertEquals("1:00",TimeUtils.getTime(60200))
        Assert.assertEquals("0:54",TimeUtils.getTime(54250))
        Assert.assertEquals("0:01",TimeUtils.getTime(900))
        Assert.assertEquals("10:00",TimeUtils.getTime(600000))
        Assert.assertEquals("10:01",TimeUtils.getTime(600500))
        Assert.assertEquals("10:06",TimeUtils.getTime(605500))

    }
}