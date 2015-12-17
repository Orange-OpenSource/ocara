/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export.docx;

import com.orange.ocara.model.Audit;
import com.orange.ocara.model.AuditObject;
import com.orange.ocara.model.Comment;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.poifs.filesystem.Ole10Native;
import org.apache.poi.poifs.filesystem.Ole10NativeException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;

public class AuditDocxExporter implements DocxExporter {
    private final Audit audit;
    private final File templateDirectory;

    private final Mustache.Compiler engine;

    public AuditDocxExporter(Audit audit, final File templateDirectory) {
        this.audit = audit;
        this.templateDirectory = templateDirectory;

        // Create engine
        engine = Mustache.compiler().withLoader(new Mustache.TemplateLoader() {
            @Override
            public Reader getTemplate(String name) throws Exception {
                return buildTemplateReader(name);
            }
        });
    }

    @Override
    public void compile(File workingDirectory) throws DocxExporterException {

        AuditPresenter presenter = new AuditPresenter(audit);

        try {

            setUpMedia(workingDirectory);


            // Chart
            render("word/charts/chart1.xml", workingDirectory, presenter);

            // Document.xml.rels
            render("word/[_]rels/document.xml.rels", workingDirectory, presenter);

            // Document
            render("word/document.xml", workingDirectory, presenter);

        } catch (Exception e) {
            throw new DocxExporterException("", e);
        }
    }

    private void setUpMedia(File workingDirectory) throws IOException, Ole10NativeException {
        File mediaDir = new File(workingDirectory.getPath(), "word/media");

        // Copy audit object icons
        File iconDir = new File(mediaDir, "icon");
        File commentDir = new File(mediaDir, "comment");

        for (AuditObject auditObject : audit.getObjects()) {

            copyAuditObjectIcon(auditObject, iconDir);

            for (Comment comment : auditObject.getComments()) {
                copyAuditObjectComment(auditObject, comment, commentDir);
            }

        }

        for (Comment comment : audit.getComments()){
            copyAuditComment(comment,commentDir);
        }


    }


    private void copyAuditObjectIcon(AuditObject auditObject, File iconDir) throws IOException {
        File from = new File(auditObject.getObjectDescription().getIcon());

        String extension = FilenameUtils.getExtension(from.getPath());
        File to = new File(iconDir, String.format("%s.%s", auditObject.getObjectDescriptionId(), extension));

        FileUtils.copyFile(from, to);
    }

    private void copyAuditObjectComment(AuditObject auditObject, Comment comment, File commentDir) throws IOException, Ole10NativeException {
        switch (comment.getType()) {
            case PHOTO:
                FileUtils.copyFileToDirectory(FileUtils.toFile(new URL(comment.getAttachment())), commentDir);
                break;

            case AUDIO: {


                String baseName = FilenameUtils.getBaseName(comment.getAttachment());

                File from = FileUtils.toFile(new URL(comment.getAttachment()));
                File to = new File(commentDir, String.format("%s.bin", baseName));

                createOleObject(from, to);
                break;
            }

            default:
                break;
        }
    }

    private void copyAuditComment(Comment comment, File commentDir) throws IOException, Ole10NativeException {
        switch (comment.getType()) {
            case PHOTO:
                FileUtils.copyFileToDirectory(FileUtils.toFile(new URL(comment.getAttachment())), commentDir);
                break;

            case AUDIO: {


                String baseName = FilenameUtils.getBaseName(comment.getAttachment());

                File from = FileUtils.toFile(new URL(comment.getAttachment()));
                File to = new File(commentDir, String.format("%s.bin", baseName));

                createOleObject(from, to);
                break;
            }

            default:
                break;
        }
    }


    /**
     * Create OleObject using a sample.
     *
     * @param from File to embed
     * @param to   Destination file
     */
    private void createOleObject(File from, File to) throws IOException, Ole10NativeException {
        File existingOleObject = new File(templateDirectory, "word/embeddings/oleObject.bin");

        OutputStream os = null;
        try {
            // When
            POIFSFileSystem fs = new POIFSFileSystem(FileUtils.openInputStream(existingOleObject));

            fs.getRoot().getEntry(Ole10Native.OLE10_NATIVE).delete();

            Ole10Native ole = new Ole10Native(from.getName(), from.getName(), from.getName(), IOUtils.toByteArray(FileUtils.openInputStream(from)));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ole.writeOut(stream);

            fs.getRoot().createDocument(Ole10Native.OLE10_NATIVE, new ByteArrayInputStream(stream.toByteArray()));

            os = FileUtils.openOutputStream(to);
            fs.writeFilesystem(os);

        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    private void render(String templatePath, File workingDirectory, Object toRender) throws IOException {
        FileWriter writer = null;

        try {
            File template = new File(workingDirectory.getPath(), templatePath);
            if (template.getParentFile() != null) {
                template.getParentFile().mkdirs();
            }

            writer = new FileWriter(template);

            Template mustacheTemplate = engine.compile(buildTemplateReader(templatePath));

            mustacheTemplate.execute(toRender, writer);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    private Reader buildTemplateReader(String name) throws IOException {
        return new FileReader(new File(templateDirectory, name));
    }


}
