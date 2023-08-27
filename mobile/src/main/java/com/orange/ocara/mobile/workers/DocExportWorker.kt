/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara.mobile.workers

import android.content.Context
import android.content.res.AssetManager
import androidx.hilt.work.HiltWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.orange.ocara.domain.docexport.models.AssetsHelper
import com.orange.ocara.domain.docexport.models.AuditDocxExporter
import com.orange.ocara.domain.docexport.models.DocxExporter
import com.orange.ocara.domain.docexport.models.DocxWriter
import com.orange.ocara.domain.interactors.ComputeAuditStatsByHandicapForExportTask
import com.orange.ocara.domain.interactors.ComputeAuditStatsByHandicapTask
import com.orange.ocara.domain.models.ProfileTypeModel
import com.orange.ocara.domain.models.RulesetModel
import com.orange.ocara.utils.FileUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.*

@HiltWorker
class DocExportWorker @AssistedInject constructor(
        @Assisted private val context: Context,
        @Assisted parameters: WorkerParameters,
        private val mediator: Mediator,
        private val computeAuditStatsByHandicapTask: ComputeAuditStatsByHandicapForExportTask
) : Worker(context, parameters) {

    companion object {
        const val EXPORT_FILE_PATH = "EXPORT_FILE_PATH"
    }

    override fun doWork(): Result {

        val dataBuilder = Data.Builder()

        val assetManager: AssetManager = context.assets
        val locale = Locale.getDefault().language
        val folder: String =
                if (locale == "en") {
                    "export/"
                } else {
                    "export-$locale/"
                }

        Timber.v("locale $locale folder $folder")

        /*
                 * The file that is saved in exportDir shall be accessible for external app, such as mailers,
                 * so as to be shared with other people.
                 * That's why we use we shall use {@link ContextWrapper#getExternalCacheDir()}, rather than
                 * {@link ContextWrapper#getCacheDir()}
                 */

        /*
                 * The file that is saved in exportDir shall be accessible for external app, such as mailers,
                 * so as to be shared with other people.
                 * That's why we use we shall use {@link ContextWrapper#getExternalCacheDir()}, rather than
                 * {@link ContextWrapper#getCacheDir()}
                 */
        val exportDir: File = File(context.externalCacheDir, folder)

        val templateDir = File(exportDir, "docx/template")
        val workingDir = File(exportDir, "docx/working")

        val exportFile = File(exportDir, String.format("docx/%s_V%d.docx",mediator.provideAuditModel()?.name, mediator.provideAuditModel()?.version))

        try {
            // Create and cleanup Working and Template directories
            createAndCleanup(workingDir)
            createAndCleanup(templateDir)
            FileUtils.deleteQuietly(exportFile)
            val isCreated = exportFile.createNewFile()

            if (isCreated) {
                AssetsHelper.copyAsset(assetManager, folder + "docx", templateDir)
                FileUtils.copyDirectory(templateDir, workingDir)


                var auditExporter: DocxExporter = AuditDocxExporter(context, mediator.provideAuditModel(),
                        templateDir, mediator.provideProfileMap(), computeAuditStatsByHandicapTask , mediator.provideAuditProfiles())

                val writer: DocxWriter = DocxWriter
                        .builder()
                        .workingDirectory(workingDir)
                        .exportFile(exportFile)
                        .exporter(auditExporter)
                        .build()
                writer.export()

                dataBuilder.putString(EXPORT_FILE_PATH, exportFile.path)
                Timber.d("Export is done , File Path : ${exportFile.path}")

                val data = dataBuilder.build()
                return Result.success(data)

            } else {
                Timber.e("Message=Could not create file;Path=%s", exportFile.absolutePath)
                return Result.failure()

            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to copy assets")

            return Result.failure()

        } finally {
            FileUtils.deleteQuietly(templateDir)
            FileUtils.deleteQuietly(workingDir)
        }


    }

    @Throws(IOException::class)
    private fun createAndCleanup(workingDir: File) {
        workingDir.mkdirs()
        FileUtils.cleanDirectory(workingDir)
    }
}