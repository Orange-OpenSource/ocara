package com.orange.ocara.data.cache.database.NonTables.scoreCalc;
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

import com.orange.ocara.utils.enums.Answer;

public class YesDoubtAnsWithprofileAndAns extends AnswerCount{

//    int count;
    String profileName;
    String profileRef;
//    QuestionAnswerModel.Answer answer;

    public YesDoubtAnsWithprofileAndAns(int count, String profileName, String profileRef, Answer answer) {
        super(count, answer);
//        this.count = count;
        this.profileName = profileName;
        this.profileRef = profileRef;
//        this.answer = answer;
    }

//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileRef() {
        return profileRef;
    }

    public void setProfileRef(String profileRef) {
        this.profileRef = profileRef;
    }

//    public QuestionAnswerModel.Answer getAnswer() {
//        return answer;
//    }
//
//    public void setAnswer(QuestionAnswerModel.Answer answer) {
//        this.answer = answer;
//    }
}
