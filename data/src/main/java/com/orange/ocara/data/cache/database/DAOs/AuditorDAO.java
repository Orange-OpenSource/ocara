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
import androidx.room.Query;
import androidx.room.Update;

import com.orange.ocara.data.cache.database.Tables.Auditor;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface AuditorDAO {

    @Query("select * From auditor")
    Single<List<Auditor>> getAllAuditors();


    @Query("select * From auditor order by id DESC")
    Single<List<Auditor>> getAllAuditorsSortedDesc();

    @Query("select firstName From auditor group by firstName")
    Single<List<String>> getAllAuditorsNames();

    @Query("INSERT OR IGNORE  INTO auditor (firstName) VALUES (:auditorName)")
    void insertAuditor(String auditorName);

    @Query("delete from auditor where id=:id")
    Completable delete(int id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updateAuditor(Auditor auditor);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<Long> insertAuditor(Auditor auditor);


    @Query("select count(audit_id) from audit left join auditor on auditorId = id where id=:id")
    Single<Integer> getNumberOfRelatedAudits(int id);
}
