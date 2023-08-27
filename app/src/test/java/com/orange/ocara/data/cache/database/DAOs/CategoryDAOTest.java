package com.orange.ocara.data.cache.database.DAOs;

import com.orange.ocara.data.cache.database.NonTables.CategoryWithEquipments;
import com.orange.ocara.data.cache.database.Tables.Equipment;
import com.orange.ocara.data.cache.database.Tables.EquipmentCategory;
import com.orange.ocara.data.cache.database.crossRef.EquipmentAndCategoryCrossRef;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategoryDAOTest extends DAOTest {

    @Test
    public void getCategoriesWithIds() {
        List<EquipmentCategory> equipmentCategories = new ArrayList<>();
        equipmentCategories.add(EquipmentCategory.EquipmentCategoryBuilder.anEquipmentCategory()
                .withName("cat1")
                .build());
        equipmentCategories.add(EquipmentCategory.EquipmentCategoryBuilder.anEquipmentCategory()
                .withName("cat2")
                .build());
        equipmentCategories.add(EquipmentCategory.EquipmentCategoryBuilder.anEquipmentCategory()
                .withName("cat3")
                .build());
        List<Long> ids = categoryDAO.insert(equipmentCategories).blockingGet();
        List<EquipmentCategory> equipmentCategoryList = categoryDAO.getCategories(ids).blockingGet();
        HashMap<String, Integer> freq = new HashMap<>();
        freq.put("cat1", 0);
        freq.put("cat2", 0);
        freq.put("cat3", 0);
        for (EquipmentCategory equipmentCategory : equipmentCategoryList) {
            freq.put(equipmentCategory.getName(), freq.get(equipmentCategory.getName()) + 1);
        }
        Assert.assertEquals(equipmentCategoryList.size(), 3);
        Assert.assertEquals(freq.get("cat1").intValue(), 1);
        Assert.assertEquals(freq.get("cat2").intValue(), 1);
        Assert.assertEquals(freq.get("cat3").intValue(), 1);
    }

    @Test
    public void testGetCategories() {
        categoryDAO.insert(EquipmentCategory.EquipmentCategoryBuilder.anEquipmentCategory()
                .withName("cat1")
                .withRulesetRef("r1")
                .withRulesetVer(1)
                .build()).blockingAwait();
        categoryDAO.insert(EquipmentCategory.EquipmentCategoryBuilder.anEquipmentCategory()
                .withName("cat2")
                .withRulesetRef("r1")
                .withRulesetVer(1)
                .build()).blockingAwait();
        categoryDAO.insert(EquipmentCategory.EquipmentCategoryBuilder.anEquipmentCategory()
                .withName("cat3")
                .withRulesetRef("r2")
                .withRulesetVer(1)
                .build()).blockingAwait();

        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e1")
                .build()).blockingAwait();
        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e2")
                .build()).blockingAwait();
        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e3")
                .build()).blockingAwait();
        equipmentDAO.insert(Equipment.EquipmentBuilder.anEquipment()
                .withReference("e4")
                .build()).blockingAwait();

        List<EquipmentAndCategoryCrossRef> equipmentAndCategoryList = new ArrayList<>();
        equipmentAndCategoryList.add(new EquipmentAndCategoryCrossRef("e1", 1));
        equipmentAndCategoryList.add(new EquipmentAndCategoryCrossRef("e2", 1));
        equipmentAndCategoryList.add(new EquipmentAndCategoryCrossRef("e3", 2));
        equipmentAndCategoryList.add(new EquipmentAndCategoryCrossRef("e4", 3));
        equipmentAndCategoryRelationDAO.insert(equipmentAndCategoryList).blockingAwait();

        List<CategoryWithEquipments> categoryWithEquipments = categoryDAO.getCategories("r1", 1).blockingGet();
        Assert.assertEquals(categoryWithEquipments.size(), 3);
        HashMap<String, Integer> expected = new HashMap<>();
        expected.put("e1", 1);
        expected.put("e2", 1);
        expected.put("e3", 2);
        for (CategoryWithEquipments categoryWithEquipment : categoryWithEquipments) {
            Assert.assertEquals(categoryWithEquipment.getEquipmentCategory().getId(), expected.get(categoryWithEquipment.getEquipment().getReference()).intValue());
        }
    }
}