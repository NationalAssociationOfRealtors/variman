package org.realtors.rets.server;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.realtors.rets.server.config.GroupRules;

public class QueryCountTableTest extends TestCase
{
    public void testNoGroupRules()
    {
        QueryCountTable table = new QueryCountTable();
        QueryCount queryCount = table.getQueryCountForUser("joe");
        assertNotNull(queryCount);
        assertTrue(queryCount.isNoQueryLimit());
        assertEquals(0, table.getCacheHit());

        queryCount = table.getQueryCountForUser("joe");
        assertNotNull(queryCount);
        assertTrue(queryCount.isNoQueryLimit());
        assertEquals(1, table.getCacheHit());
    }

    public void testFred()
    {
        List allRules = new ArrayList();
        GroupRules rules;

        rules = new GroupRules("threePerDay");
        rules.setQueryCountLimit(3, QueryCount.PER_DAY);
        allRules.add(rules);

        rules = new GroupRules("fourPerHour");
        rules.setQueryCountLimit(4, QueryCount.PER_HOUR);
        allRules.add(rules);

        rules = new GroupRules("noLimit");
        allRules.add(rules);

        // Most restrictive rules in list is fourPerHour
        QueryCountTable table = new QueryCountTable();
        QueryCount queryCount = table.getQueryCountForUser("fred", allRules);
        assertNotNull(queryCount);
        assertFalse(queryCount.isNoQueryLimit());
        assertEquals(4, queryCount.getLimit());
        assertEquals(QueryCount.PER_HOUR, queryCount.getLimitPeriod());
        assertEquals(0, table.getCacheHit());

        // This should hit the cache
        queryCount = table.getQueryCountForUser("fred", null);
        assertNotNull(queryCount);
        assertFalse(queryCount.isNoQueryLimit());
        assertEquals(4, queryCount.getLimit());
        assertEquals(QueryCount.PER_HOUR, queryCount.getLimitPeriod());
        assertEquals(1, table.getCacheHit());
    }
}
