package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.data.cache.database.crossRef.AuditEquipmentSubEquipment;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AuditEquipmentSubEquipmentDAOTest extends DAOTest {
    @Test
    public void deleteSubEquipmentsAnd_getSubEquipmentsOfAudit() {
        List<AuditEquipmentSubEquipment> auditEquipmentSubEquipments = new ArrayList<>();
        auditEquipmentSubEquipments.add(new AuditEquipmentSubEquipment.AuditEquipmentSubEquipmentBuilder()
                .setChildRef("r1")
                .setAuditEquipmentId(1)
                .setAudit_id(1)
                .createAuditEquipmentSubEquipment());
        auditEquipmentSubEquipments.add(new AuditEquipmentSubEquipment.AuditEquipmentSubEquipmentBuilder()
                .setChildRef("r2")
                .setAuditEquipmentId(2)
                .setAudit_id(1)
                .createAuditEquipmentSubEquipment());
        auditEquipmentSubEquipments.add(new AuditEquipmentSubEquipment.AuditEquipmentSubEquipmentBuilder()
                .setChildRef("r3")
                .setAuditEquipmentId(3)
                .setAudit_id(2)
                .createAuditEquipmentSubEquipment());

        auditEquipmentSubEquipmentDAO.insertAndReturnSingle(auditEquipmentSubEquipments).blockingGet();
        auditEquipmentSubEquipmentDAO.deleteSubEquipments(1).blockingAwait();
        List<AuditEquipmentSubEquipment> lst = auditEquipmentSubEquipmentDAO.getSubEquipmentsOfAudit(1).blockingGet();
        List<AuditEquipmentSubEquipment> lst2 = auditEquipmentSubEquipmentDAO.getSubEquipmentsOfAudit(2).blockingGet();
        Assert.assertEquals(1, lst.size());
        Assert.assertEquals(1, lst2.size());
    }

    @Test
    public void deleteChildren_getSubEquipmentsOfAudit() {
        List<AuditEquipmentSubEquipment> auditEquipmentSubEquipments = new ArrayList<>();
        auditEquipmentSubEquipments.add(new AuditEquipmentSubEquipment.AuditEquipmentSubEquipmentBuilder()
                .setChildRef("r1")
                .setAuditEquipmentId(1)
                .setAudit_id(1)
                .createAuditEquipmentSubEquipment());
        auditEquipmentSubEquipments.add(new AuditEquipmentSubEquipment.AuditEquipmentSubEquipmentBuilder()
                .setChildRef("r2")
                .setAuditEquipmentId(1)
                .setAudit_id(1)
                .createAuditEquipmentSubEquipment());
        auditEquipmentSubEquipments.add(new AuditEquipmentSubEquipment.AuditEquipmentSubEquipmentBuilder()
                .setChildRef("r3")
                .setAuditEquipmentId(1)
                .setAudit_id(1)
                .createAuditEquipmentSubEquipment());
        auditEquipmentSubEquipments.add(new AuditEquipmentSubEquipment.AuditEquipmentSubEquipmentBuilder()
                .setChildRef("r1")
                .setAuditEquipmentId(2)
                .setAudit_id(1)
                .createAuditEquipmentSubEquipment());
        auditEquipmentSubEquipmentDAO.insert(auditEquipmentSubEquipments).blockingAwait();
        List<String> children = new ArrayList<>();
        children.add("r1");
        children.add("r2");
        auditEquipmentSubEquipmentDAO.deleteChildren(1, 1, children).blockingAwait();
        List<AuditEquipmentSubEquipment> auditEquipmentSubEquipmentList = auditEquipmentSubEquipmentDAO.getSubEquipmentsOfAudit(1).blockingGet();
        Assert.assertEquals(2, auditEquipmentSubEquipmentList.size());

        Assert.assertEquals("r3", auditEquipmentSubEquipmentList.get(0).getChildRef());
        Assert.assertEquals(1, auditEquipmentSubEquipmentList.get(0).getAuditEquipmentId());

        Assert.assertEquals("r1", auditEquipmentSubEquipmentList.get(1).getChildRef());
        Assert.assertEquals(2, auditEquipmentSubEquipmentList.get(1).getAuditEquipmentId());
    }
}
