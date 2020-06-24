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

package com.orange.ocara.data.net.model;

import android.content.Context;

import com.activeandroid.query.Select;
import com.orange.ocara.BuildConfig;
import com.orange.ocara.data.net.client.ApiClient;
import com.orange.ocara.tools.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * toolbox for saving entities into the local db
 */
public final class DatabaseHelper {

    private DatabaseHelper() {
    }

    public static void addQuestionaireToBase(RulesetEntity rulsetDetails) {
        List<QuestionnaireEntity> questionnaires = rulsetDetails.getQuestionnaires();
        try {
            if (questionnaires != null) {
                for (QuestionnaireEntity entity : questionnaires) {
                    Timber.d("DatabaseHelper addQuestionaireToBase find Ref %s ", entity.getReference());

                    boolean entityExists = new Select()
                            .from(QuestionnaireEntity.class)
                            .where(QuestionnaireEntity.REFERENCE + " = ?", entity.getReference())
                            .and(QuestionnaireEntity.RULSETDETAILS + " = ?", rulsetDetails.getId())
                            .exists();
                    if (!entityExists) {
                        Timber.d("DatabaseHelper addQuestionaireToBase create ");
                        entity.setRuleSetDetail(rulsetDetails);
                        entity.save();
                        addChainToBase(entity, rulsetDetails);
                    }
                }
            } else {
                Timber.e("DatabaseHelper addQuestionaireToBase NULL");
            }
        } catch (Exception e) {
            Timber.e(e, "DatabaseHelper CREATE QUESTIONNAIRE");
        }
    }

    private static void addChainToBase(QuestionnaireEntity questionnaire, RulesetEntity rulsetDetails) {
        List<ChainEntity> chains = questionnaire.getChains();
        try {
            if (chains != null) {
                for (ChainEntity entity : chains) {
                    Timber.d("DatabaseHelper addChainToBase find Ref %s ", entity.getReference());
                    boolean entityExists = new Select()
                            .from(ChainEntity.class)
                            .where(ChainEntity.REFERENCE + " = ?", entity.getReference())
                            .and(ChainEntity.RULSETDETAILS + " = ?", rulsetDetails.getId())
                            .and(ChainEntity.QUESTIONNAIRE + " = ?", questionnaire.getId())
                            .exists();

                    if (!entityExists) {
                        Timber.d("DatabaseHelper addChainToBase create ");
                        entity.setQuestionnaire(questionnaire);
                        entity.setRuleset(rulsetDetails);
                        entity.save();
                    }
                }
            } else {
                Timber.e("DatabaseHelper addChainToBase NULL");
            }
        } catch (Exception e) {
            Timber.e(e, "CREATE CHAIN");
        }
    }

    public static void addQuestionToBase(RulesetEntity rulsetDetails) {
        final List<QuestionEntity> questions = rulsetDetails.getQuestions();
        try {
            if (questions != null) {
                for (QuestionEntity entity : questions) {
                    boolean entityExists = new Select().from(QuestionEntity.class)
                            .where(QuestionEntity.REFERENCE + " = ?", entity.getReference())
                            .and(QuestionEntity.RULESET_DETAILS + " =? ", rulsetDetails.getId())
                            .exists();
                    if (!entityExists) {
                        entity.setRuleSetDetail(rulsetDetails);
                        entity.save();
                        final SubjectEntity subject = entity.getSubject();
                        subject.setQuestion(entity);
                        subject.save();
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, "CREATE QUESTION");
        }
    }

    public static void addRuleToBase(RulesetEntity ruleset) {
        try {
            if (ruleset.getRules() != null) {
                for (RuleEntity entity : ruleset.getRules()) {

                    boolean entityExists = new Select()
                            .from(RuleEntity.class)
                            .where(RuleEntity.REFERENCE + " = ?", entity.getReference())
                            .and(RuleEntity.RULESET_DETAILS + " = ?", ruleset.getId())
                            .exists();

                    if (!entityExists) {
                        entity.setRuleSeDetail(ruleset);
                        entity.save();
                        addRuleImpactToBase(entity);
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, "CREATE RULE");
        }
    }

    private static void addRuleImpactToBase(final RuleEntity rule) {
        try {
            if (rule.getRuleImpacts() != null) {
                for (RuleImpactEntity entity : rule.getRuleImpacts()) {

                    boolean entityExists = new Select()
                            .from(RuleImpactEntity.class)
                            .where(RuleImpactEntity.REFERENCE + " = ?", entity.getReference())
                            .and(RuleImpactEntity.RULE + " = ?", rule.getId())
                            .exists();

                    if (!entityExists) {
                        entity.setRule(rule);
                        entity.save();
                    } else {
                        Timber.i("Message=ruleImpactDb already exists;RuleImpactId=%d", entity.getId());
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, "CREATE RULE IMPACT");
        }
    }

    public static void addIllustrationToBase(RulesetEntity rulsetDetails, final Context context) {
        try {
            if (rulsetDetails.getIllustrations() != null) {
                for (IllustrationEntity entity : rulsetDetails.getIllustrations()) {
                    boolean entityExists = new Select().from(IllustrationEntity.class)
                            .where(IllustrationEntity.REFERENCE + " = ?", entity.getReference())
                            .and(IllustrationEntity.DETAIL + " = ?", rulsetDetails.getId())
                            .exists();

                    if (!entityExists) {
                        entity.setRulesetDetail(rulsetDetails);
                        if (!entity.getImage().isEmpty()) {
                            copyUrlTofile(context, entity.getImage());
                        }
                        entity.save();
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, "CREATE ILLUSTRATIONS");
        }
    }

    public static void addIllustration(RulesetEntity rulsetDetails) {
        try {
            List<IllustrationEntity> illustrations = rulsetDetails.getIllustrations();
            if (illustrations != null) {
                for (IllustrationEntity entity : illustrations) {
                    boolean entityExists = new Select()
                            .from(IllustrationEntity.class)
                            .where(IllustrationEntity.REFERENCE + " = ?", entity.getReference())
                            .and(IllustrationEntity.DETAIL + " = ?", rulsetDetails.getId())
                            .exists();

                    if (!entityExists) {
                        entity.setRulesetDetail(rulsetDetails);
                        entity.save();
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, "CREATE ILLUSTRATIONS");
        }
    }

    private static void copyUrlTofile(final Context context, final String filename) throws IOException {
        try {
            File img = new File(context.getExternalCacheDir() + File.separator + filename);
            URL url = new URL(BuildConfig.OCARA_SERVEUR + "images/" + filename);

            Request request = new Request.Builder().url(url).build();
            Response response = ApiClient.getOkHttpClient().newCall(request).execute();

            InputStream is = response.body().byteStream();

            FileUtils.copyInputStreamToFile(is, img);
        } catch (FileNotFoundException | NullPointerException e) {
            Timber.e(e, "Picture Save Error : SAVE picture error - Do nothing");
        }
    }

    public static void addQuestionnaireGroups(final RulesetEntity body) {
        final List<QuestionnaireGroupEntity> questionnaireGroups = body.getQuestionnaireGroups();
        try {
            if (questionnaireGroups != null) {
                for (QuestionnaireGroupEntity entity : questionnaireGroups) {
                    boolean entityExists = new Select().from(QuestionnaireGroupEntity.class)
                            .where(QuestionnaireGroupEntity.NAME + " = ?", entity.getName())
                            .and(QuestionnaireGroupEntity.RULESSET_DETAILS + " = ?", body.getId())
                            .exists();
                    if (!entityExists) {
                        entity.setRuleSetDetail(body);
                        entity.save();
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, "CREATE QUESTIONNAIREGROUPS");
        }
    }

    public static void addObjectDescription(final RulesetEntity body, final Context context) {
        final List<EquipmentEntity> objectDescriptions = body.getObjectDescriptions();
        try {
            if (objectDescriptions != null) {
                for (EquipmentEntity entity : objectDescriptions) {
                    boolean entityExists = new Select()
                            .from(EquipmentEntity.class)
                            .where(EquipmentEntity.REFERENCE + " = ?", entity.getReference())
                            .and(EquipmentEntity.RULESSET_DETAILS + " =? ", body.getId())
                            .exists();

                    if (!entityExists) {
                        Timber.d(
                                "Message=Saving new ObjectDescription;ObjectDescriptionRef=%s;ObjectDescriptionName=%s;QuestionnaireRef=%s",
                                entity.getReference(), entity.getName(), entity.getQuestionaireRef());

                        entity.setRuleSetDetail(body);
                        copyUrlTofile(context, entity.getIcon());
                        entity.save();
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, "CREATE ObjDesc");
        }
    }

    public static void addObjectDescription(final RulesetEntity body) {
        final List<EquipmentEntity> objectDescriptions = body.getObjectDescriptions();
        try {
            if (objectDescriptions != null) {
                for (EquipmentEntity entity : objectDescriptions) {
                    boolean entityExists = new Select()
                            .from(EquipmentEntity.class)
                            .where(EquipmentEntity.REFERENCE + " = ?", entity.getReference())
                            .and(EquipmentEntity.RULESSET_DETAILS + " =? ", body.getId())
                            .exists();

                    if (!entityExists) {
                        Timber.d(
                                "Message=Saving new ObjectDescription;ObjectDescriptionRef=%s;ObjectDescriptionName=%s;QuestionnaireRef=%s",
                                entity.getReference(), entity.getName(), entity.getQuestionaireRef());

                        entity.setRuleSetDetail(body);
                        entity.save();
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, "CREATE ObjDesc");
        }
    }

    public static void addImpactToBase(final RulesetEntity body) {
        final List<ImpactValueEntity> impactValues = body.getImpactValues();
        try {
            if (impactValues != null) {
                for (ImpactValueEntity entity : impactValues) {
                    boolean entityExists = new Select().from(ImpactValueEntity.class)
                            .where(ImpactValueEntity.REFERENCE + " = ?", entity.getReference())
                            .and(ImpactValueEntity.RULE_DETAIL + " = ?", body.getId())
                            .exists();
                    if (!entityExists) {
                        entity.setDetails(body);
                        entity.save();
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, "CREATE ObjDesc");
        }
    }

    public static void addProfileType(final RulesetEntity body, final Context context) {
        final List<ProfileTypeEntity> profileTypes = body.getProfileTypes();
        try {
            if (profileTypes != null) {
                for (ProfileTypeEntity entity : profileTypes) {

                    boolean entityExists = new Select().from(ProfileTypeEntity.class)
                            .where(ProfileTypeEntity.REFERENCE + " = ?", entity.getReference())
                            .and(ProfileTypeEntity.DETAILS + " = ?", body.getId())
                            .exists();

                    if (!entityExists) {
                        entity.setMRulsetDetails(body);
                        copyUrlTofile(context, entity.getIcon());
                        entity.save();

                        final List<RulesetCategoryEntity> rulesCategories = entity.getRulesCategories();
                        if (rulesCategories != null) {
                            for (RulesetCategoryEntity rulesCategory : rulesCategories) {
                                rulesCategory.setProfilType(entity);
                                rulesCategory.save();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, "CREATE ObjDesc");
        }
    }

    public static void addProfileType(final RulesetEntity body) {
        final List<ProfileTypeEntity> profileTypes = body.getProfileTypes();
        try {
            if (profileTypes != null) {
                for (ProfileTypeEntity entity : profileTypes) {

                    boolean entityExists = new Select().from(ProfileTypeEntity.class)
                            .where(ProfileTypeEntity.REFERENCE + " = ?", entity.getReference())
                            .and(ProfileTypeEntity.DETAILS + " = ?", body.getId())
                            .exists();

                    if (!entityExists) {
                        entity.setMRulsetDetails(body);
                        entity.save();

                        final List<RulesetCategoryEntity> rulesCategories = entity.getRulesCategories();
                        if (rulesCategories != null) {
                            for (RulesetCategoryEntity rulesCategory : rulesCategories) {
                                rulesCategory.setProfilType(entity);
                                rulesCategory.save();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, "CREATE ObjDesc");
        }
    }
}
