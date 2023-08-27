package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.crossRef.EquipmentRulesetVersion;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EquipmentRulesetVersionDAOTest extends DAOTest {

    @Test
    public void getEquipmentsByVersion() {
        List<EquipmentRulesetVersion> equipmentRulesetVersionList = new ArrayList<>();
        equipmentRulesetVersionList.add(new EquipmentRulesetVersion("e1", "r1", 1));
        equipmentRulesetVersionList.add(new EquipmentRulesetVersion("e2", "r1", 1));
        equipmentRulesetVersionList.add(new EquipmentRulesetVersion("e3", "r1", 2));
        equipmentRulesetVersionList.add(new EquipmentRulesetVersion("e4", "r2", 1));
        equipmentRulesetVersionDAO.insert(equipmentRulesetVersionList).blockingAwait();


        Equipment equipment1 = Equipment.EquipmentBuilder.anEquipment()
                .withReference("e1")
                .build();
        Equipment equipment2 = Equipment.EquipmentBuilder.anEquipment()
                .withReference("e2")
                .build();
        Equipment equipment3 = Equipment.EquipmentBuilder.anEquipment()
                .withReference("e3")
                .build();
        Equipment equipment4 = Equipment.EquipmentBuilder.anEquipment()
                .withReference("e4")
                .build();

        equipmentDAO.insert(equipment1).blockingAwait();
        equipmentDAO.insert(equipment2).blockingAwait();
        equipmentDAO.insert(equipment3).blockingAwait();
        equipmentDAO.insert(equipment4).blockingAwait();

        List<Equipment> equipmentList = equipmentRulesetVersionDAO.getEquipmentsByVersion("r1", 1).blockingGet();
        Assert.assertEquals(equipmentList.size(), 2);
        int e1 = 0, e2 = 0;
        for (Equipment equipment : equipmentList) {
            if (equipment.getReference().equals("e1")) {
                e1++;
            } else if (equipment.getReference().equals("e2")) {
                e2++;
            }
        }
        Assert.assertEquals(e1, 1);
        Assert.assertEquals(e2, 1);
    }
}