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


package com.orange.ocara.domain.models;

import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

public class ProfileAnswersUIModel {

//    String profileRef;
//    String profileName;
    ProfileTypeModel profileTypeModel;
    int numberOfOk;
    int numberOfDoubt;
    int numberOfNoAns;
    int totalNumberOfNo;
    int numberOfNA;
    Map<String,Integer> numberOfNo; // impact value name : #no

    public ProfileAnswersUIModel() {
        this.profileTypeModel = new ProfileTypeModel();
        this.numberOfOk = 0;
        this.numberOfDoubt = 0;
        this.numberOfNoAns = 0;
        this.numberOfNA = 0;
        this.numberOfNo = new HashedMap<>();
        this.totalNumberOfNo = 0;
    }

    public ProfileAnswersUIModel(ProfileTypeModel profileTypeModel, int numberOfOk, int numberOfDoubt, int numberOfNoAns, int numberOfNA, Map<String, Integer> numberOfNo) {
        this.profileTypeModel = profileTypeModel;
        this.numberOfOk = numberOfOk;
        this.numberOfDoubt = numberOfDoubt;
        this.numberOfNoAns = numberOfNoAns;
        this.numberOfNA = numberOfNA;
        this.numberOfNo = numberOfNo;
        this.totalNumberOfNo = 0;
        for (int no :numberOfNo.values()) {
            totalNumberOfNo += no;
        }
    }

    public ProfileTypeModel getProfileTypeModel() {
        return profileTypeModel;
    }

    public void setProfileTypeModel(ProfileTypeModel profileTypeModel) {
        this.profileTypeModel = profileTypeModel;
    }

    public int getNumberOfOk() {
        return numberOfOk;
    }

    public void setNumberOfOk(int numberOfOk) {
        this.numberOfOk = numberOfOk;
    }

    public int getNumberOfDoubt() {
        return numberOfDoubt;
    }

    public void setNumberOfDoubt(int numberOfDoubt) {
        this.numberOfDoubt = numberOfDoubt;
    }

    public Map<String, Integer> getNumberOfNo() {
        return numberOfNo;
    }

    public void setNumberOfNo(Map<String, Integer> numberOfNo) {
        this.numberOfNo = numberOfNo;
        this.totalNumberOfNo = 0;
        for (int no :numberOfNo.values()) {
            totalNumberOfNo += no;
        }
    }

    public int getTotalNumberOfNo() {
        this.totalNumberOfNo = 0;
        for (int no :numberOfNo.values()) {
            totalNumberOfNo += no;
        }
        return totalNumberOfNo;
    }

    public void setTotalNumberOfNo(int totalNumberOfNo) {
        this.totalNumberOfNo = totalNumberOfNo;
    }

    public int getNumberOfNoAns() {
        return numberOfNoAns;
    }

    public void setNumberOfNoAns(int numberOfNoAns) {
        this.numberOfNoAns = numberOfNoAns;
    }

    public int getNumberOfNA() {
        return numberOfNA;
    }

    public void setNumberOfNA(int numberOfNA) {
        this.numberOfNA = numberOfNA;
    }
//    public String getProfileName() {
//        return profileName;
//    }
//
//    public void setProfileName(String profileName) {
//        this.profileName = profileName;
//    }
}
