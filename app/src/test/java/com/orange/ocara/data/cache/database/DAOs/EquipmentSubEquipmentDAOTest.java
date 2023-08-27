package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.data.cache.database.NonTables.EquipmentWithSubEquipment;
import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.crossRef.EquipmentSubEquipmentCrossRef;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EquipmentSubEquipmentDAOTest extends DAOTest {

    @Before
    public void setup() {
        super.setup();
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
        equipmentSubEquipmentDAO.insert(new EquipmentSubEquipmentCrossRef("e2", "e4", "r1", 2)).blockingAwait();
        equipmentSubEquipmentDAO.insert(new EquipmentSubEquipmentCrossRef("e2", "e5", "r2", 1)).blockingAwait();

    }

    @Test
    public void loadSubEquipmentsForListOfParents() {
        List<String> parents = new ArrayList<>();
        parents.add("e1");
        parents.add("e2");
        List<EquipmentWithSubEquipment> equipmentWithSubEquipments = equipmentSubEquipmentDAO.loadSubEquipments(parents, "r1", 1).blockingGet();
        Assert.assertEquals(equipmentWithSubEquipments.size(), 3);
        int e1e3 = 0, e1e4 = 0, e2e3 = 0;
        for (EquipmentWithSubEquipment equipmentWithSubEquipment : equipmentWithSubEquipments) {
            if (equipmentWithSubEquipment.getParentRef().equals("e1") &&
                    equipmentWithSubEquipment.getSubequipment().getReference().equals("e3")) {
                e1e3++;
            } else if (equipmentWithSubEquipment.getParentRef().equals("e1") &&
                    equipmentWithSubEquipment.getSubequipment().getReference().equals("e4")) {
                e1e4++;
            } else if (equipmentWithSubEquipment.getParentRef().equals("e2") &&
                    equipmentWithSubEquipment.getSubequipment().getReference().equals("e3")) {
                e2e3++;
            }
        }
        Assert.assertEquals(e1e3, 1);
        Assert.assertEquals(e1e4, 1);
        Assert.assertEquals(e2e3, 1);
    }

    @Test
    public void loadSubEquipmentsForOneParent() {
        List<EquipmentWithSubEquipment> equipment1Children = equipmentSubEquipmentDAO.loadSubEquipments("e1", "r1", 1).blockingGet();
        List<EquipmentWithSubEquipment> equipment2Children = equipmentSubEquipmentDAO.loadSubEquipments("e2", "r1", 1).blockingGet();
        Assert.assertEquals(equipment1Children.size(), 2);
        Assert.assertEquals(equipment2Children.size(), 1);

        int e1e3 = 0, e1e4 = 0, e2e3 = 0;
        for (EquipmentWithSubEquipment equipmentWithSubEquipment : equipment1Children) {
            if (equipmentWithSubEquipment.getParentRef().equals("e1") &&
                    equipmentWithSubEquipment.getSubequipment().getReference().equals("e3")) {
                e1e3++;
            } else if (equipmentWithSubEquipment.getParentRef().equals("e1") &&
                    equipmentWithSubEquipment.getSubequipment().getReference().equals("e4")) {
                e1e4++;
            }
        }
        for (EquipmentWithSubEquipment equipmentWithSubEquipment : equipment2Children) {
            if (equipmentWithSubEquipment.getParentRef().equals("e2") &&
                    equipmentWithSubEquipment.getSubequipment().getReference().equals("e3")) {
                e2e3++;
            }
        }
        Assert.assertEquals(e1e3, 1);
        Assert.assertEquals(e1e4, 1);
        Assert.assertEquals(e2e3, 1);
    }
}