package com.orange.ocara.data.cache.database.DAOs;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class AuditorDAOTest extends DAOTest {

    @Test
    public void getAllAuditorsNames() {
        auditorDAO.insertAuditor("abdo");
        auditorDAO.insertAuditor("abdo");
        auditorDAO.insertAuditor("sherif");
        auditorDAO.insertAuditor("amira");
        List<String> lst = auditorDAO.getAllAuditorsNames().blockingGet();
        Assert.assertEquals(3, lst.size());
        HashMap<String, Integer> frequency = new HashMap<>();
        for (String name : lst) {
            if (frequency.containsKey(name)) {
                frequency.put(name, frequency.get(name) + 1);
            } else {
                frequency.put(name, 1);
            }
        }
        Assert.assertEquals(1, frequency.get("abdo").intValue());
        Assert.assertEquals(1, frequency.get("sherif").intValue());
        Assert.assertEquals(1, frequency.get("amira").intValue());
    }
}