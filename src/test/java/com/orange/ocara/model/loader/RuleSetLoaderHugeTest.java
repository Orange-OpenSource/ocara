/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.loader;

import com.orange.ocara.modelStatic.Question;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", resourceDir = "../../build/intermediates/res/debug", emulateSdk = 18)
public class RuleSetLoaderHugeTest {


    static RuleSetJsonParser loader;
    static File tempResourcesFolder;
    static File pictosPath;
    static File imagesPath;


    @BeforeClass
    public static void loadFile() throws IOException {

        loader = new RuleSetJsonParser();

        pictosPath = new File("src/test/resources/", "pictos");
        imagesPath =  new File("src/test/resources/", "images");

        loader.setPictosPath(pictosPath);
        loader.setImagesPath(imagesPath);

        File file = new File("src/test/resources/huge.json");
        InputStream stream = new FileInputStream(file); // JsonLoaderMinimalTest.class.getResourceAsStream("/minimal.json");

        loader.load(stream);

    }





    @Test
    public void questions_should_not_contains_circular_dependencies() {
        // Given

        // ==> file is loaded

        // When
        Map<String, Question> questions = loader.getQuestions();


        // Then
        for(Question question : questions.values()) {
            questionContainsNoCircularDependency(question, question, new HashSet<String>());
        }

    }



    private void questionContainsNoCircularDependency(Question initialQuestion, Question question, Set<String> questionIds) {
        if (question == null) {
            return;
        }

        String questionId = question.getQuestionId();
        if (questionIds.contains(questionId)) {
            throw new StackOverflowError("Question '"+initialQuestion.getQuestionId()+"'  has circular dependency" );
        }

        questionIds.add(questionId);
        for(Question nextQuestion : question.getNextQuestionByResponseType().values()) {
            Set<String> nextQuestionIds = new HashSet<String>(questionIds);
            questionContainsNoCircularDependency(initialQuestion, nextQuestion, nextQuestionIds);
        }

    }

}
