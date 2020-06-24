package com.orange.ocara.tools;

import com.orange.ocara.utils.TestUtils;

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/** see {@link CollectionUtils#intersection(Collection, Collection)} */
public class CollectionUtilsIntersectionTest {

    @Test
    public void shouldReturnEmptySetWhenInputCollectionsAreEmpty() {

        // given
        Collection<String> input = Collections.emptyList();
        Collection<String> otherInput = Collections.emptyList();

        // when
        Set result = CollectionUtils.intersection(input, otherInput);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnEmptySetWhenInputCollectionsHaveNoCommonItems() {

        // given
        Collection<String> input = ListUtils.newArrayList(TestUtils.str());
        Collection<String> otherInput = ListUtils.newArrayList(TestUtils.str());

        // when
        Set result = CollectionUtils.intersection(input, otherInput);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnOneElementWhenInputCollectionsHaveOneCommonItem() {

        // given
        String commonValue = TestUtils.str();
        Collection<String> input = ListUtils.newArrayList(commonValue, TestUtils.str());
        Collection<String> otherInput = ListUtils.newArrayList(TestUtils.str(), commonValue);

        // when
        Set<String> result = CollectionUtils.intersection(input, otherInput);

        // then
        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .contains(commonValue);
    }
}