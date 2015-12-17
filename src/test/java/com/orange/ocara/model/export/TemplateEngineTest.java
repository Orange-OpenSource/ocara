/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", resourceDir = "../../build/intermediates/res/debug", emulateSdk = 18)
public class TemplateEngineTest {


    private Mustache.Compiler engine;

    @Before
    public void createEngine() throws IOException {

        engine = Mustache.compiler().withLoader(new Mustache.TemplateLoader() {
            @Override
            public Reader getTemplate(String name) throws Exception {
                return buildTemplateReader(name);
            }
        });


    }


    @Test
    public void should_render_simple_data() throws IOException {
        // Given

        // When
        Template template = createTemplate("simpleTemplate");
        String result = template.execute("foo");


        assertThat(result).contains("Hello foo");

    }



    public Template createTemplate(String templateId) throws IOException {
        return engine.compile(buildTemplateReader(templateId));
    }


    private static Reader buildTemplateReader(String name) throws IOException {
        return new FileReader(new File("src/test/resources/templates", name + ".template"));
    }

}
