package com.orange.ocara.data;

import com.orange.ocara.business.model.ExplanationModel;
import com.orange.ocara.data.net.model.Explanation;
import com.orange.ocara.data.source.ImageSource.ImageDataStore;
import com.orange.ocara.utils.TestUtils;

import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Condition;
import org.assertj.core.data.Index;
import org.assertj.core.util.Files;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static com.orange.ocara.data.source.IllustrationSource.IllustrationDataStore;
import static com.orange.ocara.tools.ListUtils.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** see {@link ExplanationDataRepository#findAllByRuleId(Long)}  }*/
public class ExplanationDataRepositoryFindAllByRuleIdTest {

    private ExplanationDataRepository subject;

    private IllustrationDataStore dataStore;

    private ImageDataStore fileStore;

    @Test
    public void shouldReturnEmptyListWhenDataStoreHasNoExplanation() {

        // given
        dataStore = mock(IllustrationDataStore.class);
        fileStore = mock(ImageDataStore.class);

        subject = new ExplanationDataRepository(dataStore, fileStore);

        Long inputRuleId = RandomUtils.nextLong();
        when(dataStore.findAllByRuleId(inputRuleId)).thenReturn(Collections.emptyList());

        // when
        List<ExplanationModel> result = subject.findAllByRuleId(inputRuleId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnListWith1ItemWhenDataStoreHas1ExplanationWithNoIcon() {

        // given
        dataStore = mock(IllustrationDataStore.class);
        fileStore = mock(ImageDataStore.class);

        subject = new ExplanationDataRepository(dataStore, fileStore);

        Long expectedId = TestUtils.longNb();
        String expectedLabel = TestUtils.str();
        String expectedComment = TestUtils.str();
        Explanation mock = mock(Explanation.class);
        when(mock.getId()).thenReturn(expectedId);
        when(mock.getComment()).thenReturn(expectedComment);
        when(mock.getReference()).thenReturn(expectedLabel);

        Long inputRuleId = RandomUtils.nextLong();
        when(dataStore.findAllByRuleId(inputRuleId)).thenReturn(newArrayList(mock));

        // when
        List<ExplanationModel> result = subject.findAllByRuleId(inputRuleId);

        // then
        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .has(new Condition<>(ExplanationModel::isTextOnly, "should be a text-only explanation model"), Index.atIndex(0))
                .has(new Condition<>(model -> expectedId == model.getId(), "should have expected id"), Index.atIndex(0))
                .has(new Condition<>(model -> expectedLabel == model.getLabel(), "should have expected label"), Index.atIndex(0))
                .has(new Condition<>(model -> expectedComment == model.getComment(), "should have expected comment"), Index.atIndex(0));
    }

    @Test
    public void shouldReturnListWith1ItemWhenDataStoreHas1ExplanationWithUnknownIcon() {

        // given
        dataStore = mock(IllustrationDataStore.class);
        fileStore = mock(ImageDataStore.class);

        subject = new ExplanationDataRepository(dataStore, fileStore);

        Long expectedId = TestUtils.longNb();
        String expectedLabel = TestUtils.str();
        String expectedComment = TestUtils.str();
        String expectedIcon = TestUtils.str();
        Explanation mock = mock(Explanation.class);
        when(mock.getId()).thenReturn(expectedId);
        when(mock.getComment()).thenReturn(expectedComment);
        when(mock.getReference()).thenReturn(expectedLabel);
        when(mock.getIcon()).thenReturn(expectedIcon);

        when(fileStore.get(expectedIcon)).thenReturn(null);

        Long inputRuleId = RandomUtils.nextLong();
        when(dataStore.findAllByRuleId(inputRuleId)).thenReturn(newArrayList(mock));

        // when
        List<ExplanationModel> result = subject.findAllByRuleId(inputRuleId);

        // then
        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .has(new Condition<>(ExplanationModel::isTextOnly, "should be a text-only explanation model"), Index.atIndex(0))
                .has(new Condition<>(model -> expectedId == model.getId(), "should have expected id"), Index.atIndex(0))
                .has(new Condition<>(model -> expectedLabel == model.getLabel(), "should have expected label"), Index.atIndex(0))
                .has(new Condition<>(model -> expectedComment == model.getComment(), "should have expected comment"), Index.atIndex(0));
    }

    @Test
    public void shouldReturnListWith1ItemWhenDataStoreHas1ExplanationWith1Icon() {

        // given
        dataStore = mock(IllustrationDataStore.class);
        fileStore = mock(ImageDataStore.class);

        subject = new ExplanationDataRepository(dataStore, fileStore);

        Long expectedId = TestUtils.longNb();
        String expectedLabel = TestUtils.str();
        String expectedComment = TestUtils.str();
        String expectedIcon = TestUtils.str();
        Explanation mock = mock(Explanation.class);
        when(mock.getId()).thenReturn(expectedId);
        when(mock.getComment()).thenReturn(expectedComment);
        when(mock.getReference()).thenReturn(expectedLabel);
        when(mock.getIcon()).thenReturn(expectedIcon);
        when(mock.hasIcon()).thenReturn(true);

        File expectedFile = Files.newTemporaryFile();
        when(fileStore.get(expectedIcon)).thenReturn(expectedFile);
        when(fileStore.exists(expectedIcon)).thenReturn(true);

        Long inputRuleId = RandomUtils.nextLong();
        when(dataStore.findAllByRuleId(inputRuleId)).thenReturn(newArrayList(mock));

        // when
        List<ExplanationModel> result = subject.findAllByRuleId(inputRuleId);

        // then
        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .has(new Condition<>(ExplanationModel::isIllustrated, "should be an illustrated explanation model"), Index.atIndex(0))
                .has(new Condition<>(model -> expectedId == model.getId(), "should have expected id"), Index.atIndex(0))
                .has(new Condition<>(model -> expectedLabel == model.getLabel(), "should have expected label"), Index.atIndex(0))
                .has(new Condition<>(model -> expectedFile == model.getImage(), "should have expected file"), Index.atIndex(0))
                .has(new Condition<>(model -> expectedComment == model.getComment(), "should have expected comment"), Index.atIndex(0));
    }

    @Test
    public void shouldReturnListWith2ItemsWhenDataStoreHas2Explanations() {

        // given
        dataStore = mock(IllustrationDataStore.class);
        fileStore = mock(ImageDataStore.class);

        subject = new ExplanationDataRepository(dataStore, fileStore);

        Explanation mock = mock(Explanation.class);
        Explanation otherMock = mock(Explanation.class);
        Long inputRuleId = RandomUtils.nextLong();
        when(dataStore.findAllByRuleId(inputRuleId)).thenReturn(newArrayList(mock, otherMock));

        // when
        List<ExplanationModel> result = subject.findAllByRuleId(inputRuleId);

        // then
        assertThat(result)
                .isNotEmpty()
                .hasSize(2);
    }
}