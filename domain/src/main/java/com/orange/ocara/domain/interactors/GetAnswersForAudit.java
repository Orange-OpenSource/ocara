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

import com.orange.ocara.domain.models.ProfileAnswersUIModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;


public class GetAnswersForAudit {

    public GetAnswersForAudit() {
    }

    public Single<List<ProfileAnswersUIModel>> getProfilesAnswers(Long auditId) {

        List<ProfileAnswersUIModel> ret = new ArrayList<>();
//        Map<String, Integer> noMap = new HashedMap<>();
//        noMap.put("Gênant", 3);
//        noMap.put("Bloquant", 4);
////        ProfileAnswersUIModel model = new ProfileAnswersUIModel("P1" , 3 ,2 , noMap);
//        ret.add(new ProfileAnswersUIModel("P1", 3, 2, noMap));
//        ret.add(new ProfileAnswersUIModel("P2", 4, 1, noMap));
//        ret.add(new ProfileAnswersUIModel("P3", 5, 3, noMap));
//        ret.add(new ProfileAnswersUIModel("P4", 6, 4, noMap));
//        ret.add(new ProfileAnswersUIModel("P5", 7, 1, noMap));
//        noMap.put("Gênant", 2);
//        noMap.put("Bloquant", 1);
//        ret.add(new ProfileAnswersUIModel("P6", 8, 9, noMap));
//        ret.add(new ProfileAnswersUIModel("P7", 9, 4, noMap));
//        ret.add(new ProfileAnswersUIModel("P8", 10, 5, noMap));
//        ret.add(new ProfileAnswersUIModel("P9", 1, 7, noMap));
//        noMap.put("Gênant", 3);
//        noMap.put("Bloquant", 3);
//        ret.add(new ProfileAnswersUIModel("All disabilities", 3, 4, noMap));
        return Single.just(ret);
    }
}
