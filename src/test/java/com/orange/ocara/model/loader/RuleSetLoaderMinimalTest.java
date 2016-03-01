/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.loader;

import com.orange.ocara.modelStatic.Accessibility;
import com.orange.ocara.modelStatic.Handicap;
import com.orange.ocara.modelStatic.Illustration;
import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.modelStatic.Question;
import com.orange.ocara.modelStatic.Rule;
import com.orange.ocara.modelStatic.RuleSet;

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
import java.net.URISyntaxException;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", resourceDir = "../../build/intermediates/res/debug", emulateSdk = 18)
public class RuleSetLoaderMinimalTest {


    static RuleSetJsonParser loader;
    static File pictosPath;
    static File imagesPath;


    @BeforeClass
    public static void loadFile() throws IOException {

        loader = new RuleSetJsonParser();

        pictosPath = new File("src/test/resources/", "pictos");
        imagesPath =  new File("src/test/resources/", "images");
        loader.setPictosPath(pictosPath);
        loader.setImagesPath(imagesPath);

        File file = new File("src/test/resources/minimal.json");
        InputStream stream = new FileInputStream(file); // JsonLoaderMinimalTest.class.getResourceAsStream("/minimal.json");

        loader.load(stream);

    }

    @Test
    public void should_Load_All_Questions() {
        // Given

        // ==> file is loaded

        // When
        Map<String, Question> questions = loader.getQuestions();

        // Then
        assertThat(questions)
                .containsKey("I-1")
                .containsKey("I-1-a1")
                .containsKey("I-1-b1")
                .containsKey("I-1-c1");
    }



    @Test
    public void should_Load_All_Handicaps() {
        // Given

        // ==> file is loaded

        // When
        RuleSet ruleSet = loader.getRuleSet();
        Handicap handicap = ruleSet.getHandicapById("H1");

        // Then
        assertThat(handicap).isNotNull();
        assertThat(handicap.getName()).isEqualTo("fauteuil roulant électrique");
    }





    @Test
    public void should_Load_ReferenceData() {
        // Given

        // ==> file is loaded

        // When
        RuleSet referenceData = loader.getRuleSet();

        // Then;
        assertThat(referenceData).isNotNull();
        assertThat(referenceData.getVersion()).isNotEmpty();
        assertThat(referenceData.getType()).isEqualTo("Audit de parcours");
    }




    @Test
    public void should_Load_Categories() {
        // Given

        // ==> file is loaded

        // When
        RuleSet referenceData = loader.getRuleSet();

        // Then;
        assertThat(referenceData).isNotNull();
        assertThat(referenceData.getCategories()).hasSize(1);
        assertThat(referenceData.getCategories().get(0).getObjects()).hasSize(1);
    }


    @Test
    public void should_Parse_Question() {
        // Given

        // ==> file is loaded

        // When
        Map<String, Question> questions = loader.getQuestions();

        // Then;

        Question q1 = questions.get("I-1");
        assertThat(q1).isNotNull();
        assertThat(q1.getSubject()).startsWith("Places de stationnement");
        assertThat(q1.getRulesById()).containsKey("R3.1a");

        Rule r1 = q1.getRulesById().get("R3.1a");

        assertThat(r1.getDescription()).startsWith("Distance maximale");
        assertThat(r1.getIllustrations()).hasSize(1);
        Illustration illustration = r1.getIllustrations().get(0);
        assertThat(illustration.getImage()).isEqualTo(new File(imagesPath, "sonorisation.png").toURI());
        assertThat(illustration.getComment()).isEqualTo("Ceci est un commentaire");
        assertThat(r1.getHandicapAccessibilities().getHandicapAccessibility("H1")).isEqualTo(Accessibility.ANNOYING);
    }


    @Test
    public void should_Parse_Object() throws URISyntaxException {
        // Given

        // ==> file is loaded

        // When
        RuleSet referenceData = loader.getRuleSet();

        assertThat(referenceData.getObjectDescriptions()).hasSize(2);
        ObjectDescription obj1 = referenceData.getObjectDescriptions().get(0);
        assertThat(obj1.getName()).isEqualTo("Obj1");
        assertThat(obj1.getDescription()).isEqualTo("stationnement");
        assertThat(obj1.getIcon()).isEqualTo( new File(pictosPath,"parking.svg").toURI());
        assertThat(obj1.getChildren()).hasSize(1);
        assertThat(obj1.getIllustrations()).hasSize(2);
    }






    @Test
    public void should_Store_Resources() {
        // Given
        // ==> file is loaded

        // When

        // Then;

        assertThat( new File(pictosPath,"parking.svg") ).exists();
        assertThat( new File(imagesPath,"cheminement.jpg") ).exists();
        assertThat( new File(imagesPath,"sonorisation.png") ).exists();
    }

}
