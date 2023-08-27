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

public class NoAnsWithProfileAndImpact extends CountImpactName {

//    int count;
    String profileName;
//    String impactName;
    String profileRef;
    Answer answer;

    public NoAnsWithProfileAndImpact(int count, String profileName, String impactName, String profileRef, Answer answer) {
        super(count, impactName);
//        this.count = count;
        this.profileName = profileName;
//        this.impactName = impactName;
        this.profileRef = profileRef;
        this.answer = answer;
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

//    public String getImpactName() {
//        return impactName;
//    }
//
//    public void setImpactName(String impactName) {
//        this.impactName = impactName;
//    }

    public String getProfileRef() {
        return profileRef;
    }

    public void setProfileRef(String profileRef) {
        this.profileRef = profileRef;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
