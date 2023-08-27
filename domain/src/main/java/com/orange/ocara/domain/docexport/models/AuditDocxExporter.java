/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.domain.docexport.models;

import android.content.Context;

import com.orange.ocara.domain.interactors.ComputeAuditStatsByHandicapForExportTask;
import com.orange.ocara.domain.interactors.ComputeAuditStatsByHandicapTask;
import com.orange.ocara.domain.models.AuditEquipmentModel;
import com.orange.ocara.domain.models.AuditModel;
import com.orange.ocara.domain.models.CommentModel;
import com.orange.ocara.domain.models.DocReportProfileQuestionsRulesAnswersModel;
import com.orange.ocara.domain.models.ProfileTypeModel;
import com.orange.ocara.utils.FileUtils;
import com.orange.ocara.utils.FilenameUtils;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import org.apache.poi.poifs.filesystem.Ole10Native;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class AuditDocxExporter implements DocxExporter {

    private final AuditModel audit;
    private final File templateDirectory;
    private final Mustache.Compiler engine;
    private final Context context;
    private final Map<String, ProfileTypeModel> mProfilTypeFormRuleSet;
    ComputeAuditStatsByHandicapForExportTask computeAuditStatsByHandicapTask;
    ArrayList<DocReportProfileQuestionsRulesAnswersModel> profilesTableData;

    public AuditDocxExporter(Context context, AuditModel audit, final File templateDirectory,
                             final Map<String, ProfileTypeModel> profilTypeFormRuleSet,
                             ComputeAuditStatsByHandicapForExportTask computeAuditStatsByHandicapTask) {
        this.context = context;
        this.audit = audit;
        this.templateDirectory = templateDirectory;
        mProfilTypeFormRuleSet = profilTypeFormRuleSet;

        // Create engine
        engine = Mustache.compiler().withLoader(this::buildTemplateReader);
        this.computeAuditStatsByHandicapTask = computeAuditStatsByHandicapTask;
        profilesTableData = new ArrayList<>();
    }

    public AuditDocxExporter(Context context, AuditModel audit, final File templateDirectory,
                             final Map<String, ProfileTypeModel> profilTypeFormRuleSet,
                             ComputeAuditStatsByHandicapForExportTask computeAuditStatsByHandicapTask,
                             ArrayList<DocReportProfileQuestionsRulesAnswersModel> profiles) {
        this.context = context;
        this.audit = audit;
        this.templateDirectory = templateDirectory;
        mProfilTypeFormRuleSet = profilTypeFormRuleSet;

        // Create engine
        engine = Mustache.compiler().withLoader(this::buildTemplateReader);
        this.computeAuditStatsByHandicapTask = computeAuditStatsByHandicapTask;
        profilesTableData = profiles;
    }

    @Override
    public void compile(File workingDirectory) throws DocxExporterException {
        AuditPresenter data = new AuditPresenter(computeAuditStatsByHandicapTask, audit, mProfilTypeFormRuleSet, context, profilesTableData);

        try {
            setUpMedia(workingDirectory);

            // Chart
            render("word/charts/chart1.xml", workingDirectory, data);

            // Document.xml.rels
            render("word/[_]rels/document.xml.rels", workingDirectory, data);

            // Document
            render("word/document.xml", workingDirectory, data);

        } catch (Exception e) {
            Timber.e("Message=Could not render document", e);
            throw new DocxExporterException(e.getLocalizedMessage(), e);
        }
    }

    private void setUpMedia(File workingDirectory) throws IOException {
        File mediaDir = new File(workingDirectory.getPath(), "word/media");

        // Copy audit object icons
        File iconDir = new File(mediaDir, "icon");
        File commentDir = new File(mediaDir, "comment");
//        List<AuditEquipmentModel> auditEquipmentModels = loadAuditEquipments.execute(Long.valueOf(audit.getId())).blockingGet();
        for (AuditEquipmentModel auditObject : audit.getObjects()) {
            copyAuditObjectIcon(auditObject, iconDir);

            for (CommentModel comment : auditObject.getComments()) {
                copyAuditComment(comment, commentDir);
            }
        }

        for (CommentModel comment : audit.getComments()) {
            copyAuditComment(comment, commentDir);
        }
    }

    private void copyAuditObjectIcon(AuditEquipmentModel auditObject, File iconDir) throws IOException {

        final File fileFrom = new File(context.getExternalCacheDir() + File.separator + auditObject.getEquipment().getIcon());

        File to = new File(iconDir, auditObject.getEquipment().getIcon());
        FileUtils.copyFile(fileFrom, to);
    }

    private void copyAuditComment(CommentModel comment, File commentDir) throws IOException {
        switch (comment.getType()) {
            case FILE:
            case PHOTO:
                FileUtils.copyFileToDirectory(new File(comment.getAttachment()), commentDir);
                break;
            case AUDIO:
                String baseName = FilenameUtils.getBaseName(comment.getAttachment());

                File from = new File(FileUtils.getAppPath(context) + "/" + comment.getAttachment());
                File to = new File(commentDir, String.format("%s.bin", baseName));

                createOleObject(from, to);
                break;
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
    private void createOleObject(File from, File to) throws IOException {
        File existingOleObject = new File(templateDirectory, "word/embeddings/oleObject.bin");

        OutputStream os = null;

        try (POIFSFileSystem fs = new POIFSFileSystem(FileUtils.openInputStream(existingOleObject))) {

            fs.getRoot().getEntry(Ole10Native.OLE10_NATIVE).delete();

            Ole10Native ole = new Ole10Native(
                    from.getName(),
                    from.getName(),
                    from.getName(),
                    IOUtils.toByteArray(FileUtils.openInputStream(from)));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ole.writeOut(stream);

            fs.getRoot().createDocument(Ole10Native.OLE10_NATIVE, new ByteArrayInputStream(stream.toByteArray()));

            os = FileUtils.openOutputStream(to);
            fs.writeFilesystem(os);

        } finally {
            FileUtils.closeQuietly(os);
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
            FileUtils.closeQuietly(writer);
        }
    }

    private Reader buildTemplateReader(String name) throws IOException {
        return new FileReader(new File(templateDirectory, name));
    }
}
