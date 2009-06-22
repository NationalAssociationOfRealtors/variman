package org.realtors.rets.server;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.GroupRulesImpl;

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

        rules = new GroupRulesImpl(new Group("threePerDay"));
        rules.setQueryLimit(QueryLimit.valueOf(3, QueryLimit.Period.PER_DAY));
        allRules.add(rules);

        rules = new GroupRulesImpl(new Group("fourPerHour"));
        rules.setQueryLimit(QueryLimit.valueOf(4, QueryLimit.Period.PER_HOUR));
        allRules.add(rules);

        rules = new GroupRulesImpl(new Group("noLimit"));
        allRules.add(rules);

        // Most restrictive rules in list is fourPerHour
        QueryCountTable table = new QueryCountTable();
        QueryCount queryCount = table.getQueryCountForUser("fred", allRules);
        assertNotNull(queryCount);
        assertFalse(queryCount.isNoQueryLimit());
        assertEquals(4, queryCount.getLimit());
        assertEquals(QueryLimit.Period.PER_HOUR, queryCount.getLimitPeriod());
        assertEquals(0, table.getCacheHit());

        // This should hit the cache
        queryCount = table.getQueryCountForUser("fred", null);
        assertNotNull(queryCount);
        assertFalse(queryCount.isNoQueryLimit());
        assertEquals(4, queryCount.getLimit());
        assertEquals(QueryLimit.Period.PER_HOUR, queryCount.getLimitPeriod());
        assertEquals(1, table.getCacheHit());
    }
}
