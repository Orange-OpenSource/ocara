package com.orange.ocara.tools;

import com.orange.ocara.utils.TestUtils;

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static com.orange.ocara.tools.ListUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/** see {@link CollectionUtils#disjunction(Collection, Collection)}*/
public class CollectionUtilsDisjunctionTest {

    @Test
    public void shouldReturnEmptySetWhenInputCollectionsAreEmpty() {

        // given
        Collection<String> input = Collections.emptyList();
        Collection<String> otherInput = Collections.emptyList();

        // when
        Set result = CollectionUtils.disjunction(input, otherInput);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturn2ElementsWhenInputCollectionsHaveOneItemsButOneIsCommonAndNoCommonItems() {

        // given
        String inputValue = TestUtils.str();
        Collection<String> input = newArrayList(inputValue);

        String otherInputValue = TestUtils.str();
        Collection<String> otherInput = newArrayList(otherInputValue);

        // when
        Set<String> result = CollectionUtils.disjunction(input, otherInput);

        // then
        assertThat(result)
                .isNotEmpty()
                .hasSize(2)
                .contains(inputValue, otherInputValue);
    }

    @Test
    public void shouldReturnTwoElementsWhenInputCollectionsHaveTwoItemsButOneIsCommon() {

        // given
        String commonValue = TestUtils.str();
        String inputValue = TestUtils.str();
        Collection<String> input = newArrayList(inputValue, commonValue);

        String otherInputValue = TestUtils.str();
        Collection<String> otherInput = newArrayList(otherInputValue, commonValue);

        // when
        Set<String> result = CollectionUtils.disjunction(input, otherInput);

        // then
        assertThat(result)
                .isNotEmpty()
                .hasSize(2)
                .contains(inputValue, otherInputValue);
    }
}