package com.orange.ocara.business.NewInteractors;

import android.content.Context;
import android.os.Build;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.orange.ocara.domain.interactors.InsertAuditTask;
import com.orange.ocara.domain.models.AuditModel;
import com.orange.ocara.domain.models.RulesetModel;
import com.orange.ocara.domain.models.SiteModel;
import com.orange.ocara.domain.repositories.AuditRepository;
import com.orange.ocara.data.cache.database.OcaraDB;
import com.orange.ocara.data.cache.database.Tables.Audit;
import com.orange.ocara.utils.enums.AuditLevel;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

@Config(sdk = {Build.VERSION_CODES.O_MR1})
@RunWith(AndroidJUnit4.class)
public class InsertAuditTaskTest {

    InsertAuditTask insertAuditTask;

    @Before
    public void setUp() throws Exception {

        Context context = ApplicationProvider.getApplicationContext();
        OcaraDB ocaraDB = Room.inMemoryDatabaseBuilder(context, OcaraDB.class)
                .allowMainThreadQueries()
                .build();

        insertAuditTask = new InsertAuditTask(new AuditRepository(context, ocaraDB));

    }

    @Test
    public void executeInsertAuditTask() {


        insertAuditTask.execute(createTempAudit())
                .test()
                .assertValue(1L);
    }

    private AuditModel createTempAudit() {


        return AuditModel.builder()
                .name("Fake test AUDIT")
                .id(1)
                .status(false)
                .level(AuditLevel.BEGINNER)
                .site(SiteModel.builder().name("Test site") .build())
                .ruleset(RulesetModel.builder() .version("42").reference("test ref").build())
                .version(11)
                .date(119191L)
                .userName("sherif")
                .build();
    }
}