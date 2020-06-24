/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.ui.intent.export;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.AssetManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.business.service.impl.RuleSetServiceImpl;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.db.ModelManagerImpl;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.net.model.ProfileTypeEntity;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.tools.FileUtils;
import com.orange.ocara.ui.intent.export.docx.AssetsHelper;
import com.orange.ocara.ui.intent.export.docx.AuditDocxExporter;
import com.orange.ocara.ui.intent.export.docx.DocxExporter;
import com.orange.ocara.ui.intent.export.docx.DocxWriter;
import com.orange.ocara.ui.tools.RefreshStrategy;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

/**
 * Implementation of {@link IntentService}
 *
 * Service dedicated to exporting an {@link AuditEntity} into a DOCX file
 */
@EIntentService
public class AuditExportService extends IntentService {

    public static final String EXPORT_SUCCESS = "com.orange.ocara.model.export.EXPORT_SUCCESS";
    public static final String EXPORT_FAILED = "com.orange.ocara.model.export.EXPORT_FAILED";
    private static final RefreshStrategy RULE_REFRESH_STRATEGY = RefreshStrategy.ruleAnswerRefreshStrategy();

    @Bean(ModelManagerImpl.class)
    ModelManager modelManager;

    @Bean(RuleSetServiceImpl.class)
    RuleSetService mRuleSetService;

    /**
     * Default constructor
     */
    public AuditExportService() {
        super(AuditExportService.class.getSimpleName());
    }

    @ServiceAction
    void toDocx(long auditId) {
        AuditEntity audit = modelManager.getAudit(auditId);
        modelManager.refresh(audit, RULE_REFRESH_STRATEGY);

        exportToDocx(audit);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        // Do nothing here
    }

    /**
     * Produces a {@link File} and write some content in it
     *
     * @param audit an {@link AuditEntity}
     */
    private void exportToDocx(AuditEntity audit) {
        final AssetManager assetManager = getAssets();
        String locale = Locale.getDefault().getLanguage();
        String folder;
        if (locale.equals("en")) {
            folder = "export/";
        } else {
            folder = "export-" + locale + "/";
        }
        Timber.v("locale " + locale + " folder " + folder);

        /*
         * The file that is saved in exportDir shall be accessible for external app, such as mailers,
         * so as to be shared with other people.
         * That's why we use we shall use {@link ContextWrapper#getExternalCacheDir()}, rather than
         * {@link ContextWrapper#getCacheDir()}
         */
        final File exportDir = new File(getExternalCacheDir(), folder);

        final File templateDir = new File(exportDir, "docx/template");
        final File workingDir = new File(exportDir, "docx/working");

        final File exportFile = new File(exportDir, String.format("docx/audit_%d.docx", audit.getId()));

        try {
            // Create and cleanup Working and Template directories
            createAndCleanup(workingDir);
            createAndCleanup(templateDir);

            FileUtils.deleteQuietly(exportFile);
            boolean isCreated = exportFile.createNewFile();

            if (isCreated) {
                AssetsHelper.copyAsset(assetManager, folder + "docx", templateDir);
                FileUtils.copyDirectory(templateDir, workingDir);

                final RulesetEntity entity = audit.getRuleSet();
                final Map<String, ProfileTypeEntity> profileTypeFormRuleSet = mRuleSetService.getProfilTypeFormRuleSet(entity);
                DocxExporter auditExporter = new AuditDocxExporter(getApplicationContext(), audit, templateDir, profileTypeFormRuleSet);
                DocxWriter writer = DocxWriter
                        .builder()
                        .workingDirectory(workingDir)
                        .exportFile(exportFile)
                        .exporter(auditExporter)
                        .build();

                writer.export();

                Intent i = new Intent(EXPORT_SUCCESS);
                i.putExtra("path", exportFile.getPath());
                LocalBroadcastManager
                        .getInstance(this)
                        .sendBroadcast(i);
            } else {
                Timber.e("Message=Could not create file;Path=%s", exportFile.getAbsolutePath());

                Intent i = new Intent(EXPORT_FAILED);
                LocalBroadcastManager
                        .getInstance(this)
                        .sendBroadcast(i);
            }

        } catch (Exception e) {
            Timber.e(e, "Failed to copy assets");

            Intent i = new Intent(EXPORT_FAILED);
            LocalBroadcastManager
                    .getInstance(this)
                    .sendBroadcast(i);
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
