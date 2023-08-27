package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.data.cache.database.Tables.Audit;
import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.crossRef.EquipmentSubEquipmentCrossRef;
import com.orange.ocara.utils.enums.AuditLevel;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class EquipmentDAOTest extends DAOTest {
    @Test
    public void getAllChildrenOfEquipment() {
        Audit audit = new Audit.Builder()
                .setRulesetVer(1)
                .setRulesetRef("r1")
                .setLevel(AuditLevel.BEGINNER)
                .build();
        int auditId = auditDAO.insert(audit).blockingGet().intValue();

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
        Equipment equipment5 = Equipment.EquipmentBuilder.anEquipment()
                .withReference("e5")
                .build();
        equipmentDAO.insert(equipment1).blockingAwait();
        equipmentDAO.insert(equipment2).blockingAwait();
        equipmentDAO.insert(equipment3).blockingAwait();
        equipmentDAO.insert(equipment4).blockingAwait();
        equipmentDAO.insert(equipment5).blockingAwait();
        equipmentSubEquipmentDAO.insert(new EquipmentSubEquipmentCrossRef("e1", "e3", "r1", 1)).blockingAwait();
        equipmentSubEquipmentDAO.insert(new EquipmentSubEquipmentCrossRef("e1", "e4", "r1", 1)).blockingAwait();
        equipmentSubEquipmentDAO.insert(new EquipmentSubEquipmentCrossRef("e1", "e5", "r1", 2)).blockingAwait();
        equipmentSubEquipmentDAO.insert(new EquipmentSubEquipmentCrossRef("e2", "e3", "r1", 1)).blockingAwait();
        equipmentSubEquipmentDAO.insert(new EquipmentSubEquipmentCrossRef("e2", "e4", "r2", 1)).blockingAwait();
        equipmentSubEquipmentDAO.insert(new EquipmentSubEquipmentCrossRef("e2", "e5", "r1", 2)).blockingAwait();

        List<Equipment> equipment1Children = equipmentDAO.getAllChildrenOfEquipment(auditId, "e1").blockingGet();
        List<Equipment> equipment2Children = equipmentDAO.getAllChildrenOfEquipment(auditId, "e2").blockingGet();

        Assert.assertEquals(equipment1Children.size(), 2);
        int e3 = 0, e4 = 0;
        for (Equipment equipment : equipment1Children) {
            if (equipment.getReference().equals("e3")) {
                e3++;
            }
            if (equipment.getReference().equals("e4")) {
                e4++;
            }
        }
        Assert.assertEquals(e3, 1);
        Assert.assertEquals(e4, 1);
        Assert.assertEquals(equipment2Children.size(), 1);
        Assert.assertEquals(equipment2Children.get(0).getReference(), "e3");
    }
}