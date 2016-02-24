/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.export;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v4.content.LocalBroadcastManager;

import com.orange.ocara.model.Audit;
import com.orange.ocara.model.ModelManager;
import com.orange.ocara.model.RefreshStrategy;
import com.orange.ocara.model.export.docx.AuditDocxExporter;
import com.orange.ocara.model.export.docx.DocxExporter;
import com.orange.ocara.model.export.docx.DocxWriter;
import com.orange.ocara.tools.AssetsHelper;
import com.orange.ocara.tools.injection.DaggerIntentService;

import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;

import timber.log.Timber;

@EIntentService
public class AuditExportService extends DaggerIntentService {

    public static final String EXPORT_SUCCESS = "com.orange.ocara.model.export.EXPORT_SUCCESS";
    public static final String EXPORT_FAILED = "com.orange.ocara.model.export.EXPORT_FAILED";
    private static final RefreshStrategy RULE_REFRESH_STRATEGY = RefreshStrategy.builder().depth(RefreshStrategy.DependencyDepth.RULE_ANSWER).build();

    @Inject
    ModelManager modelManager;

    public AuditExportService() {
        super(AuditExportService.class.getSimpleName());
    }

    @ServiceAction
    void toDocx(long auditId) {
        Audit audit = modelManager.getAudit(auditId);
        modelManager.refresh(audit,  RULE_REFRESH_STRATEGY);

        exportToDocx(audit);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        // Do nothing here
    }

    private void exportToDocx(Audit audit) {
        final AssetManager assetManager = getAssets();
        String locale = Locale.getDefault().getLanguage();
        String folder;
        if (locale.equals("en")) {
            folder = "export/";
        } else {
            folder = "export-"+locale+"/";
        }
        Timber.v("locale "+locale+ " folder "+folder);
        final File exportDir = new File(getCacheDir(), folder);

        final File templateDir = new File(exportDir, "docx/template");
        final File workingDir = new File(exportDir, "docx/working");

        final File exportFile = new File(exportDir, String.format("docx/audit_%d.docx", audit.getId()));

        try {
            // Create and cleanup Working and Template directories
            createAndCleanup(workingDir);
            createAndCleanup(templateDir);

            FileUtils.deleteQuietly(exportFile);
            exportFile.createNewFile();

            AssetsHelper.copyAsset(assetManager, folder+"docx", templateDir);
            FileUtils.copyDirectory(templateDir, workingDir);

            DocxExporter auditExporter = new AuditDocxExporter(audit, templateDir);
            DocxWriter writer = DocxWriter.builder().workingDirectory(workingDir).exportFile(exportFile).exporter(auditExporter).build();
            writer.export();

            Intent i = new Intent(EXPORT_SUCCESS);
            i.putExtra("path", exportFile.getPath());
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);

        } catch (Exception e) {
            Timber.e(e, "Failed to copy assets");

            Intent i = new Intent(EXPORT_FAILED);
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);

        } finally {
            FileUtils.deleteQuietly(templateDir);
            FileUtils.deleteQuietly(workingDir);
        }
    }

    private void createAndCleanup(File workingDir) throws IOException {
        workingDir.mkdirs();
        FileUtils.cleanDirectory(workingDir);
    }

}
