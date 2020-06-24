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

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orange.ocara.business.model.RuleSetStat;
import com.orange.ocara.tools.ListUtils;

import java.io.Serializable;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * an entity for {@link Ruleset} that is also the DTO for webservices
 */
@NoArgsConstructor
@Table(name = RulesetEntity.TABLE_NAME, id = "_id")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class RulesetEntity extends Model implements Serializable {

    public static final String TABLE_NAME = "RulsetDetails";
    public static final String ID = "reference";
    public static final String VERSION = "version";
    public static final String COMMENT = "comment";
    public static final String LANGUAGE = "language";
    public static final String REFERENCE = "reference";
    public static final String TYPE = "type";
    public static final String USER_CREDENTIALS = "userCredentials";
    public static final String QUESTIONNAIRES = "questionnaires";
    public static final String DATE = "date";
    public static final String ILLUSTRATIONS = "illustrations";
    public static final String RULES = "rules";
    public static final String QUESTIONS = "questions";
    public static final String OBJECT_DESCRIPTIONS = "objectDescriptions";
    public static final String QUESTIONNAIRE_GROUPS = "questionnaireGroups";
    public static final String IMPACT_VALUES = "impactValues";
    public static final String PROFILE_TYPES = "profileTypes";

    @Column(name = VERSION)
    private String version;

    @Column(name = LANGUAGE)
    private String language;

    @Column(name = REFERENCE)
    private String reference;

    @Column(name = COMMENT)
    private String comment;

    @Column(name = TYPE)
    private String type;

    @Column(name = "authorName")
    private String authorName;

    @Column(name = DATE)
    private String date;

    @Column(name = "ruleCategoryName")
    private String ruleCategoryName;

    @SerializedName(OBJECT_DESCRIPTIONS)
    @Expose
    private List<EquipmentEntity> objectDescriptions = null;

    @SerializedName(QUESTIONNAIRE_GROUPS)
    @Expose
    public List<QuestionnaireGroupEntity> questionnaireGroups = null;

    @SerializedName(QUESTIONS)
    @Expose
    private List<QuestionEntity> questions = null;

    @SerializedName(QUESTIONNAIRES)
    @Expose
    private List<QuestionnaireEntity> questionnaires = null;

    @SerializedName(RULES)
    @Expose
    private List<RuleEntity> rules = null;

    @SerializedName(ILLUSTRATIONS)
    @Expose
    private List<IllustrationEntity> illustrations = null;

    @SerializedName(IMPACT_VALUES)
    @Expose
    private List<ImpactValueEntity> impactValues = null;

    @SerializedName(PROFILE_TYPES)
    @Expose
    private List<ProfileTypeEntity> profileTypes = null;

    private RuleSetStat stat = RuleSetStat.OFFLINE;

    /**
     * Retrieves a Questionnaire based on an object description
     *
     * @param objectRef a reference for a {@link EquipmentEntity}
     * @return a {@link QuestionnaireEntity}, or null if it does not exist
     */
    public QuestionnaireEntity getQuestionnaire(String objectRef) {
        String tableName = Cache.getTableName(QuestionnaireEntity.class);
        return new Select()
                .from(QuestionnaireEntity.class)
                .where(tableName + "." + QuestionnaireEntity.RULSETDETAILS + "=?", getId())
                .and(tableName + "." + QuestionnaireEntity.OBJECT_DESCRIPTION + "=?", objectRef)
                .executeSingle();
    }

    /**
     * Checks if a ruleset has some questionnaires, based on an object description
     *
     * @param objectRef a reference for a {@link EquipmentEntity}
     * @return true if at least one item does exist
     */
    public boolean containsQuestionnaires(String objectRef) {
        return new Select()
                .from(QuestionnaireEntity.class)
                .where(Cache.getTableName(QuestionnaireEntity.class) + "." + QuestionnaireEntity.RULSETDETAILS + "=?", getId())
                .and(Cache.getTableName(QuestionnaireEntity.class) + "." + QuestionnaireEntity.OBJECT_DESCRIPTION + "=?", objectRef)
                .exists();
    }

    /**
     * Retrieves a rule based on its reference
     *
     * @param ruleRef a reference for a {@link RuleEntity}
     * @return a {@link RuleEntity}, or null if it does not exist
     */
    public RuleEntity getRule(String ruleRef) {
        return new Select()
                .from(RuleEntity.class)
                .where(RuleEntity.REFERENCE + " = ?", ruleRef)
                .and(RuleEntity.RULESET_DETAILS + " = ?", getId())
                .executeSingle();
    }

    public List<QuestionnaireGroupEntity> getQuestionnaireGroupDb() {
        return getMany(QuestionnaireGroupEntity.class, QuestionnaireGroupEntity.RULESSET_DETAILS);
    }

    public List<EquipmentEntity> getObjectDescriptionsDb() {
        return getMany(EquipmentEntity.class, EquipmentEntity.RULESSET_DETAILS);
    }

    public List<QuestionEntity> getQuestionsDb() {
        return getMany(QuestionEntity.class, QuestionEntity.RULESET_DETAILS);
    }

    public List<ProfileTypeEntity> getProfilTypesDb() {
        return getMany(ProfileTypeEntity.class, ProfileTypeEntity.DETAILS);
    }

    public List<ImpactValueEntity> getImpactValuesDb() {
        return getMany(ImpactValueEntity.class, ImpactValueEntity.RULE_DETAIL);
    }


    /**
     * Converts a {@link RulesetWs} into a {@link RulesetEntity}
     *
     * @param input a {@link RulesetWs}
     * @return an instance of {@link RulesetEntity}
     */
    public static RulesetEntity toEntity(RulesetWs input) {

        RulesetEntity output = new RulesetEntity();

        output.setComment(input.getComment());
        output.setDate(input.getDate());
        output.setLanguage(input.getLanguage());
        output.setReference(input.getReference());
        output.setVersion(input.getVersion().toString());
        output.setType(input.getType());
        output.setStat(RuleSetStat.ONLINE);

        List<IllustrationWs> inputIllustrations = input.getIllustrations();
        List<IllustrationEntity> outputIllustrations = ListUtils.newArrayList();
        for (IllustrationWs item : inputIllustrations) {
            outputIllustrations.add(IllustrationEntity.toEntity(item, output));
        }
        output.setIllustrations(outputIllustrations);

        List<ImpactValueWs> inputImpactValues = input.getImpactValues();
        List<ImpactValueEntity> outputImpactValues = ListUtils.newArrayList();
        for (ImpactValueWs item : inputImpactValues) {
            outputImpactValues.add(ImpactValueEntity.toEntity(item, output));
        }
        output.setImpactValues(outputImpactValues);

        List<EquipmentWs> inputEquipments = input.getEquipments();
        List<EquipmentEntity> outputEquipments = ListUtils.newArrayList();
        for (EquipmentWs item : inputEquipments) {
            outputEquipments.add(EquipmentEntity.toEntity(item, output));
        }
        output.setObjectDescriptions(outputEquipments);

        List<ProfileTypeWs> inputProfileTypes = input.getProfileTypes();
        List<ProfileTypeEntity> outputProfileTypes = ListUtils.newArrayList();
        for (ProfileTypeWs item : inputProfileTypes) {
            ProfileTypeEntity profileTypeEntity = ProfileTypeEntity.toEntity(item, output);
            outputProfileTypes.add(profileTypeEntity);
        }
        output.setProfileTypes(outputProfileTypes);
        output.setRuleCategoryName(outputProfileTypes.get(0).getRulesCategories().get(0).getName());

        List<QuestionnaireGroupWs> inputQuestionnaireGroups = input.getQuestionnaireGroups();
        List<QuestionnaireGroupEntity> outputQuestionnaireGroups = ListUtils.newArrayList();
        for (QuestionnaireGroupWs item : inputQuestionnaireGroups) {
            outputQuestionnaireGroups.add(QuestionnaireGroupEntity.toEntity(item, output));
        }
        output.setQuestionnaireGroups(outputQuestionnaireGroups);

        List<QuestionnaireWs> inputQuestionnaires = input.getQuestionnaires();
        List<QuestionnaireEntity> outputQuestionnaires = ListUtils.newArrayList();
        for (QuestionnaireWs item : inputQuestionnaires) {
            outputQuestionnaires.add(QuestionnaireEntity.toEntity(item, output));
        }
        output.setQuestionnaires(outputQuestionnaires);

        List<QuestionWs> inputQuestions = input.getQuestions();
        List<QuestionEntity> outputQuestions = ListUtils.newArrayList();
        for (QuestionWs item : inputQuestions) {
            outputQuestions.add(QuestionEntity.toEntity(item, output));
        }
        output.setQuestions(outputQuestions);

        List<RuleWs> inputRules = input.getRules();
        List<RuleEntity> outputRules = ListUtils.newArrayList();
        for (RuleWs item : inputRules) {
            outputRules.add(RuleEntity.toEntity(item, output));
        }
        output.setRules(outputRules);

        output.setAuthorName(input.getUserCredentials() == null ? null : input.getUserCredentials().getUsername());

        return output;
    }
}
