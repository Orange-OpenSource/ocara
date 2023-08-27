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


package com.orange.ocara.domain.repositories;

import com.orange.ocara.data.cache.database.DAOs.DocReportProfilesDAO;
import com.orange.ocara.data.cache.database.NonTables.DocReportProfileQuestionsRulesAnswers;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.domain.models.DocReportProfileQuestionsRulesAnswersModel;
import com.orange.ocara.utils.enums.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;

public class AuditDocReportRepository {
    DocReportProfilesDAO reportDAO;

    @Inject
    public AuditDocReportRepository(OcaraDB ocaraDB) {
        reportDAO = ocaraDB.docReportProfilesDAO();
    }

    public Single<ArrayList<DocReportProfileQuestionsRulesAnswersModel>> getProfilesQuestionsRulesAnswers(Long auditId, String ruleSetRef, int ruleSetVer) {
        return reportDAO.getProfilesQuestionsRulesAnswers(auditId, ruleSetRef, ruleSetVer).map(
                profiles -> {

                    ArrayList<DocReportProfileQuestionsRulesAnswersModel> list = new ArrayList<>();
                    for (DocReportProfileQuestionsRulesAnswers profil : profiles) {
                        int idx = getProfileIdx(profil.getProfileRef(), list);
                        if (idx != -1) {
                            if (list.get(idx).getQuestionRulesMap().get(profil.getQuestionRef()) != null) {
                                DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName ruleAns = new DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName
                                        (profil.getRuleRef(), profil.getRuleLable(), profil.getRuleAnswer(), profil.getRuleImpactRef(), profil.getImpactName(), profil.getQuestionName());
                                list.get(idx).getQuestionRulesMap().get(profil.getQuestionRef()).add(ruleAns);
                            } else {
                                list.get(idx).getQuestionRulesMap().put(profil.getQuestionRef(), new ArrayList<>());
                                DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName ruleAns = new DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName
                                        (profil.getRuleRef(), profil.getRuleLable(), profil.getRuleAnswer(), profil.getRuleImpactRef(), profil.getImpactName(), profil.getQuestionName());
                                list.get(idx).getQuestionRulesMap().get(profil.getQuestionRef()).add(ruleAns);
                            }
                        } else {
                            DocReportProfileQuestionsRulesAnswersModel obj =
                                    new DocReportProfileQuestionsRulesAnswersModel(profil.getProfileRef(), profil.getProfileName());
                            obj.getQuestionRulesMap().put(profil.getQuestionRef(), new ArrayList<>());
                            DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName ruleAns = new DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName
                                    (profil.getRuleRef(), profil.getRuleLable(), profil.getRuleAnswer(), profil.getRuleImpactRef(), profil.getImpactName(), profil.getQuestionName());
                            obj.getQuestionRulesMap().get(profil.getQuestionRef()).add(ruleAns);
                            list.add(obj);
                        }
                    }

//                    clearYesProfiles(list)
                    list = clearNAProfiles(list);
                    return clearYesProfiles(list);
                }

        );
    }

    private int getProfileIdx(String profileRef, List<DocReportProfileQuestionsRulesAnswersModel> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getProfileRef().equals(profileRef)) return i;
        }
        return -1;
    }


    private ArrayList<DocReportProfileQuestionsRulesAnswersModel> clearYesProfiles(ArrayList<DocReportProfileQuestionsRulesAnswersModel> list) {
        for (int i = 0; i < list.size(); i++) {
            if (isAllQuestionsAnsYes(list.get(i).getQuestionRulesMap())) {
                list.get(i).getQuestionRulesMap().clear();
            }
        }
        return list;
    }

    private boolean isAllQuestionsAnsYes(Map<String, ArrayList<DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName>> questions) {
        for (Map.Entry<String, ArrayList<DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName>> entry : questions.entrySet()) {
            if (!isQuestionAnsYes(entry.getValue())) return false;
        }
        return true;
    }

    private boolean isQuestionAnsYes(ArrayList<DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName> answers) {
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).getRuleAnswer() != Answer.OK) {
                return false;
            }
        }
        return true;
    }


    private ArrayList<DocReportProfileQuestionsRulesAnswersModel> clearNAProfiles(ArrayList<DocReportProfileQuestionsRulesAnswersModel> list) {
        for (int i = 0; i < list.size(); i++) {
            if (isAllQuestionsAnsNA(list.get(i).getQuestionRulesMap())) {
                list.get(i).getQuestionRulesMap().clear();
            }
        }
        return list;
    }

    private boolean isAllQuestionsAnsNA(Map<String, ArrayList<DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName>> questions) {
        for (Map.Entry<String, ArrayList<DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName>> entry : questions.entrySet()) {
            if (!isQuestionAnsNA(entry.getValue())) return false;
        }
        return true;
    }

    private boolean isQuestionAnsNA(ArrayList<DocReportProfileQuestionsRulesAnswersModel.UnVerifiedRuleAnswerWithQuestionName> answers) {
        for (int i = 0; i < answers.size(); i++) {
            if (! (answers.get(i).getRuleAnswer() == Answer.NO_ANSWER||answers.get(i).getRuleAnswer() == Answer.NOT_APPLICABLE) ) {
                return false;
            }
        }
        return true;
    }
}
