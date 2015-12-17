/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import org.apache.commons.io.FileUtils;
import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", resourceDir = "../../build/intermediates/res/debug", emulateSdk = 18)
public class DocxWriterTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private File workingDirectory;
    private File exportFile;
    private DocxExporter exporter;


    @Before
    public void setUp() throws IOException {
        workingDirectory = testFolder.newFolder("working");
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


    @Test
    public void export_Should_Succeed() throws DocxWriterException, IOException {

        // Given
        DocxWriter writer = buildDocxWriter();
        FileUtils.copyDirectory(new File("src/main/assets/export/docx"), workingDirectory);

        // When
        writer.export();

        // Then
        Assertions.assertThat(isZipFile(exportFile)).isTrue();
    }

    @Test
    public void isZipFile_With_Docx_Should_Succeed() throws DocxWriterException, IOException {

        // Given
        File file = new File("src/test/resources/templates/expected.docx");

        // When
        boolean result = isZipFile(file);

        // Then
        Assertions.assertThat(result).isTrue();
    }


    private DocxWriter buildDocxWriter() {
        return DocxWriter.builder().workingDirectory(workingDirectory).exportFile(exportFile).exporter(exporter).build();
    }

    /**
     * Determine whether a file is a ZIP File.
     */
    private static boolean isZipFile(File file) throws IOException {
        if (file.isDirectory()) {
            return false;
        }
        if (!file.canRead()) {
            throw new IOException("Cannot read file " + file.getAbsolutePath());
        }
        if (file.length() < 4) {
            return false;
        }
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        int test = in.readInt();
        in.close();
        return test == 0x504b0304;
    }
}


