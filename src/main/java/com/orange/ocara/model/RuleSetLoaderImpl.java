/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model;

import android.content.Context;
import android.content.res.AssetManager;

import com.orange.ocara.model.loader.RuleSetJsonParser;
import com.orange.ocara.modelStatic.RuleSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
/* package. */ class RuleSetLoaderImpl implements RuleSetLoader {

    public static final String RULESETS_FOLDER = "rulesets";
    private final Context context;

    /**
     * Constructor.
     *
     * @param context
     */
    @Inject
    public RuleSetLoaderImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void initialize() {

        final AssetManager assetManager = context.getAssets();

        try {
            String[] assetsList = assetManager.list(RULESETS_FOLDER);

            for (String asset : assetsList) {
                if (isJsonFile(asset)) {
                    Timber.i("ruleset to install : %s", asset);

                    File rulesetDir = installRuleset(asset);
                    File target = new File(rulesetDir, asset);

                    FileUtils.copyInputStreamToFile(assetManager.open(RULESETS_FOLDER + "/" + asset), target);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getInstalledRuleSetIds() {
        Set<String> result = new HashSet<String>();

        String[] ruleSetIds = getAllRuleSetsDir().list();

        for (String ruleSetId : ruleSetIds) {
            Timber.i("installed ruleset : %s", ruleSetId);
            result.add(ruleSetId);
        }
        return result;
    }


    @Override
    public RuleSet loadInstalledRuleSet(String ruleSetId) {
        try {
            return loadRuleSet(ruleSetId);
        } catch (FileNotFoundException e) {
            Timber.e(e, "Cannot load ruleSet %s", ruleSetId);
            throw new RuntimeException(e);
        }
    }

    private RuleSet loadRuleSet(String ruleSetId) throws FileNotFoundException {
        RuleSetJsonParser loader = new RuleSetJsonParser();

        File rulesetDir = getRuleSetDir(ruleSetId);
        File pictos = new File(rulesetDir, "pictos");
        File illustrations = new File(rulesetDir, "illustrations");

        loader.setPictosPath(pictos);
        loader.setImagesPath(illustrations);

        File rulesetFile = new File(rulesetDir, ruleSetId + ".json");

        InputStream jsonStream = new FileInputStream(rulesetFile);
        loader.load(jsonStream);

        Timber.i("ruleset loaded : %s", rulesetFile.getName());
        RuleSet ruleSet = loader.getRuleSet();
        ruleSet.setId(ruleSetId);
        return ruleSet;
    }

    private File installRuleset(String rulesetFile) {
        String ruleSetId = getRuleSetId(rulesetFile);

        File ruleSetDir = getRuleSetDir(ruleSetId);
        createFolder(ruleSetDir);

        return ruleSetDir;
    }

    private String getRuleSetId(String rulesetFile) {
        int i = rulesetFile.indexOf(".json");
        if (i < 0) {
            return rulesetFile;
        }
        return rulesetFile.substring(0, i);
    }

    private File getAllRuleSetsDir() {
        return context.getDir(RULESETS_FOLDER, Context.MODE_PRIVATE);
    }

    private File getRuleSetDir(String ruleSetId) {
        return new File(getAllRuleSetsDir(), ruleSetId);
    }

    @Override
    public void terminate() {
    }

    private boolean isJsonFile(String asset) {
        return FilenameUtils.isExtension(asset, "json");
    }

    private void createFolder(File folder) {
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
