package com.orange.ocara.data.cache.database.DAOs;
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
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.orange.ocara.data.cache.database.crossRef.QuestionsEquipmentsCrossRef;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface QuestionEquipmentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(QuestionsEquipmentsCrossRef questionsEquipmentsCrossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<QuestionsEquipmentsCrossRef> questionsEquipmentsCrossRef);

}
