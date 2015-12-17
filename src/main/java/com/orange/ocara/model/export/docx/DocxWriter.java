/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import lombok.Builder;

@Builder
public class DocxWriter {
    private File workingDirectory;
    private File exportFile;
    private DocxExporter exporter;

    /**
     * Transforms the unzipped directory (templateDirectory) to exportFile docx file (exportFile) using exporter.
     *
     * @throws DocxWriterException If failed exportFile export
     */
    public void export() throws DocxWriterException {

        if (!checkDirectory(workingDirectory)) {
            throw new DocxWriterException("Working directory could not be checked");
        }

        if (!checkFile(exportFile)) {
            throw new DocxWriterException("Export File could not be checked");
        }

        if (exporter == null) {
            throw new DocxWriterException("Exporter could not be null");
        }

        // compile
        try {
            exporter.compile(workingDirectory);
        } catch (DocxExporterException e) {
            throw new DocxWriterException("Failed to compile exporter", e);
        }

        // zip templateDirectory to exportFile
        final String rootPath = FilenameUtils.normalize(workingDirectory.getPath() + "/");
        ZipOutputStream zos = null;

        try {
            zos = new ZipOutputStream(new FileOutputStream(exportFile));
            zipDirectory(rootPath, workingDirectory, zos);
        } catch (IOException e) {
            throw new DocxWriterException("Failed to zip template directory", e);
        } finally {
            IOUtils.closeQuietly(zos);
        }

    }

    /**
     * To check a file. File must exists, be writeable
     *
     * @param file file to check
     * @return true if ok, false otherwise
     */
    private boolean checkFile(File file) {
        return file != null && file.exists() && file.canWrite() && file.isFile();
    }

    /**
     * To check a directory. Directory must exists, be writeable.
     *
     * @param directory Directory to check
     * @return true if ok, false otherwise
     */
    private boolean checkDirectory(File directory) {
        return directory != null && directory.exists() && directory.canWrite() && directory.isDirectory();
    }

    /**
     * To zip a directory.
     *
     * @param rootPath  root path
     * @param directory directory
     * @param zos       ZipOutputStream
     * @throws IOException
     */
    private void zipDirectory(String rootPath, File directory, ZipOutputStream zos) throws IOException {
        //get a listing of the directory content
        File[] files = directory.listFiles();

        //loop through dirList, and zip the files
        for (File file : files) {
            if (file.isDirectory()) {
                zipDirectory(rootPath, file, zos);
                continue;
            }

            String filePath = FilenameUtils.normalize(file.getPath(), true);
            String fileName = StringUtils.difference(rootPath, filePath);

            fileName = fileName.replaceAll("\\[_\\]", "_").replaceAll("\\[.\\]", ".");

            //create a FileInputStream on top of file
            ZipEntry anEntry = new ZipEntry(fileName);
            zos.putNextEntry(anEntry);

            FileUtils.copyFile(file, zos);
        }
    }
}
