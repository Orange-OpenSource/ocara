/**
 * Copyright (C) 2015 Orange
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.intent.export.docx;

import com.activeandroid.ActiveAndroid;
import com.orange.ocara.TestOcaraApplication;
import com.orange.ocara.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestOcaraApplication.class, sdk = 21)
public class DocxWriterTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private File workingDirectory;
    private File exportFile;
    private DocxExporter exporter;

    @After
    public void tearDown() {
        ActiveAndroid.dispose();
    }

    @Before
    public void setUp() throws IOException {
        ActiveAndroid.initialize(TestUtils.instrumentationContext());

        workingDirectory = testFolder.newFolder("working");
        System.out.println("Test folder: " + workingDirectory.getPath());
        exportFile = testFolder.newFile("export.docx");
        exporter = Mockito.mock(DocxExporter.class);
    }

    @Test(expected = DocxWriterException.class)
    public void export_With_NullWorkingDirectory_Should_Throw_DocxWriterException() throws DocxWriterException {
        // Given
        workingDirectory = null;
        DocxWriter writer = buildDocxWriter();

        // When
        writer.export();

        // Then
    }

    @Test(expected = DocxWriterException.class)
    public void export_With_NullExportFile_Should_Throw_DocxWriterException() throws DocxWriterException {
        // Given
        exportFile = null;
        DocxWriter writer = buildDocxWriter();

        // When
        writer.export();

        // Then
    }

    @Test(expected = DocxWriterException.class)
    public void export_With_NullExporter_Should_Throw_DocxWriterException() throws DocxWriterException {
        // Given
        exporter = null;
        DocxWriter writer = buildDocxWriter();

        // When
        writer.export();

        // Then
    }

    @Test(expected = DocxWriterException.class)
    public void export_With_NonExisting_WorkingDirectory_Should_Throw_DocxWriterException() throws DocxWriterException {
        // Given
        workingDirectory = Mockito.mock(File.class);
        Mockito.when(workingDirectory.exists()).thenReturn(false);
        DocxWriter writer = buildDocxWriter();

        // When
        writer.export();

        // Then
    }

    @Test(expected = DocxWriterException.class)
    public void export_With_Non_Writeable_WorkingDirectory_Should_Throw_DocxWriterException() throws DocxWriterException {
        // Given
        workingDirectory = Mockito.mock(File.class);
        Mockito.when(workingDirectory.exists()).thenReturn(true);
        Mockito.when(workingDirectory.canWrite()).thenReturn(false);
        DocxWriter writer = buildDocxWriter();

        // When
        writer.export();

        // Then
    }

    @Test(expected = DocxWriterException.class)
    public void export_With_NonExisting_ExportFile_Should_Throw_DocxWriterException() throws DocxWriterException {
        // Given
        exportFile = Mockito.mock(File.class);
        Mockito.when(exportFile.exists()).thenReturn(false);
        DocxWriter writer = buildDocxWriter();

        // When
        writer.export();

        // Then
    }

    @Test(expected = DocxWriterException.class)
    public void export_With_Non_Writeable_ExportFile_Should_Throw_DocxWriterException() throws DocxWriterException {
        // Given
        exportFile = Mockito.mock(File.class);
        Mockito.when(exportFile.exists()).thenReturn(true);
        Mockito.when(exportFile.canWrite()).thenReturn(false);
        DocxWriter writer = buildDocxWriter();

        // When
        writer.export();

        // Then
    }

    @Test(expected = DocxWriterException.class)
    public void export_With_Export_Compile_Failed_Should_Throw_DocxWriterException() throws DocxWriterException, DocxExporterException {
        // Given
        Mockito.doThrow(DocxExporterException.class).when(exporter).compile(Matchers.any(File.class));
        DocxWriter writer = buildDocxWriter();

        // When
        writer.export();

        // Then
    }

    private DocxWriter buildDocxWriter() {
        return DocxWriter.builder().workingDirectory(workingDirectory).exportFile(exportFile).exporter(exporter).build();
    }
}


