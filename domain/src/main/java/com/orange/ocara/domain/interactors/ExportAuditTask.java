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


package com.orange.ocara.domain.interactors;

import android.content.Context;
import android.content.res.AssetManager;

import com.orange.ocara.domain.docexport.models.AssetsHelper;
import com.orange.ocara.domain.docexport.models.AuditDocxExporter;
import com.orange.ocara.domain.docexport.models.DocxExporter;
import com.orange.ocara.domain.docexport.models.DocxWriter;
import com.orange.ocara.domain.models.AuditModel;
import com.orange.ocara.domain.models.ProfileTypeModel;
import com.orange.ocara.domain.models.RulesetModel;
import com.orange.ocara.utils.FileUtils;





import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.SingleEmitter;
import io.reactivex.annotations.NonNull;
import timber.log.Timber;

public class ExportAuditTask {
    //    AuditRepository auditRepository;
    @Inject
    LoadAuditForExportTask loadAuditForExportTask;
    @Inject
    GetProfileTypeFromRuleSetAsMap getProfileTypeFormRuleSet;
    @Inject
    ComputeAuditStatsByHandicapForExportTask computeAuditStatsByHandicapTask;
    Context context;

    @Inject
    public ExportAuditTask(@ApplicationContext Context context, LoadAuditForExportTask loadAuditForExportTask,
                           GetProfileTypeFromRuleSetAsMap getProfileTypeFormRuleSet,
                           ComputeAuditStatsByHandicapForExportTask computeAuditStatsByHandicapTask) {
        this.loadAuditForExportTask = loadAuditForExportTask;
        this.getProfileTypeFormRuleSet = getProfileTypeFormRuleSet;
        this.computeAuditStatsByHandicapTask = computeAuditStatsByHandicapTask;
        this.context = context;
    }

    public Observable<String> executeTask(Long auditId) {
        return loadAuditForExportTask.execute(auditId).concatMap(auditModel -> {
            return execute(auditModel);
//            return sing;
        });


//        loadAuditForExportTask.execute(auditId).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribeWith(new DisposableObserver<AuditModel>() {
//            @Override
//            public void onNext(@NonNull AuditModel auditModel) {
//                System.err.println("Aud Model Returned ");
//                execute(auditModel);
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                System.err.println("err ");
//
//            }
//
//            @Override
//            public void onComplete() {
//                System.err.println("comp ");
//
//
//            }
//        });

    }

    private Observable<String> execute(AuditModel audit) {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
//            AuditModel audit = null;//loadAuditForExportTask.execute(auditId).blockingGet();
                final AssetManager assetManager = context.getAssets();
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
                final File exportDir = new File(context.getExternalCacheDir(), folder);

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

                        final RulesetModel entity = audit.getRuleset();
//                final Map<String, ProfileTypeEntity> profileTypeFormRuleSet = mRuleSetService.getProfilTypeFormRuleSet(entity);
                        final Map<String, ProfileTypeModel> profileTypeFormRuleSet = getProfileTypeFormRuleSet.execute(entity.getReference(), Integer.parseInt(entity.getVersion())).blockingGet();
                        DocxExporter auditExporter = new AuditDocxExporter(context, audit, templateDir, profileTypeFormRuleSet, computeAuditStatsByHandicapTask);
                        DocxWriter writer = DocxWriter
                                .builder()
                                .workingDirectory(workingDir)
                                .exportFile(exportFile)
                                .exporter(auditExporter)
                                .build();

                        writer.export();

                        emitter.onNext(exportFile.getPath());
                        emitter.onComplete();
//                Intent i = new Intent(EXPORT_SUCCESS);
//                i.putExtra("path", exportFile.getPath());
//                LocalBroadcastManager
//                        .getInstance(this)
//                        .sendBroadcast(i);
//                sendBroadcast(i);
                    } else {
                        Timber.e("Message=Could not create file;Path=%s", exportFile.getAbsolutePath());

//                Intent i = new Intent(EXPORT_FAILED);
//                LocalBroadcastManager
//                        .getInstance(this)
//                        .sendBroadcast(i);
//                sendBroadcast(i);

                        emitter.onError(null);
                    }

                } catch (Exception e) {
                    Timber.e(e, "Failed to copy assets");

//            Intent i = new Intent(EXPORT_FAILED);
//            LocalBroadcastManager
//                    .getInstance(this)
//                    .sendBroadcast(i);
//            sendBroadcast(i);
                    emitter.onError(e);
                } finally {
                    FileUtils.deleteQuietly(templateDir);
                    FileUtils.deleteQuietly(workingDir);
                }


            }
        });

    }

    private void export(Long auditId, SingleEmitter emitter) {
//
//
//        AuditModel audit = loadAuditForExportTask.execute(auditId).blockingGet();
//        final AssetManager assetManager = context.getAssets();
//        String locale = Locale.getDefault().getLanguage();
//        String folder;
//        if (locale.equals("en")) {
//            folder = "export/";
//        } else {
//            folder = "export-" + locale + "/";
//        }
//        Timber.v("locale " + locale + " folder " + folder);
//
//        /*
//         * The file that is saved in exportDir shall be accessible for external app, such as mailers,
//         * so as to be shared with other people.
//         * That's why we use we shall use {@link ContextWrapper#getExternalCacheDir()}, rather than
//         * {@link ContextWrapper#getCacheDir()}
//         */
//        final File exportDir = new File(context.getExternalCacheDir(), folder);
//
//        final File templateDir = new File(exportDir, "docx/template");
//        final File workingDir = new File(exportDir, "docx/working");
//
//        final File exportFile = new File(exportDir, String.format("docx/audit_%d.docx", audit.getId()));
//
//        try {
//            // Create and cleanup Working and Template directories
//            createAndCleanup(workingDir);
//            createAndCleanup(templateDir);
//
//            FileUtils.deleteQuietly(exportFile);
//            boolean isCreated = exportFile.createNewFile();
//
//            if (isCreated) {
//                AssetsHelper.copyAsset(assetManager, folder + "docx", templateDir);
//                FileUtils.copyDirectory(templateDir, workingDir);
//
//                final RulesetModel entity = audit.getRuleset();
////                final Map<String, ProfileTypeEntity> profileTypeFormRuleSet = mRuleSetService.getProfilTypeFormRuleSet(entity);
//                final Map<String, ProfileTypeModel> profileTypeFormRuleSet = getProfileTypeFormRuleSet.execute(entity.getReference(), Integer.parseInt(entity.getVersion())).blockingGet();
//                DocxExporter auditExporter = new AuditDocxExporter(context, audit, templateDir, profileTypeFormRuleSet, computeAuditStatsByHandicapTask);
//                DocxWriter writer = DocxWriter
//                        .builder()
//                        .workingDirectory(workingDir)
//                        .exportFile(exportFile)
//                        .exporter(auditExporter)
//                        .build();
//
//                writer.export();
//
//                emitter.onSuccess();
////                Intent i = new Intent(EXPORT_SUCCESS);
////                i.putExtra("path", exportFile.getPath());
////                LocalBroadcastManager
////                        .getInstance(this)
////                        .sendBroadcast(i);
////                sendBroadcast(i);
//            } else {
//                Timber.e("Message=Could not create file;Path=%s", exportFile.getAbsolutePath());
//
////                Intent i = new Intent(EXPORT_FAILED);
////                LocalBroadcastManager
////                        .getInstance(this)
////                        .sendBroadcast(i);
////                sendBroadcast(i);
//
//                emitter.onError();
//            }
//
//        } catch (Exception e) {
//            Timber.e(e, "Failed to copy assets");
//
////            Intent i = new Intent(EXPORT_FAILED);
////            LocalBroadcastManager
////                    .getInstance(this)
////                    .sendBroadcast(i);
////            sendBroadcast(i);
//            emitter.onError();
//        } finally {
//            FileUtils.deleteQuietly(templateDir);
//            FileUtils.deleteQuietly(workingDir);
//        }

    }

    private void createAndCleanup(File workingDir) throws IOException {
        workingDir.mkdirs();
        FileUtils.cleanDirectory(workingDir);
    }

}
