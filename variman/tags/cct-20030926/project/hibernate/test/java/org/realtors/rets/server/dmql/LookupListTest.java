/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class LookupListTest extends TestCase
{

    public void testLookupOr()
    {
        LookupList lookupList = new LookupList(LookupListType.OR,
                                               "FieldName");
        lookupList.setSqlColumn("field");
        lookupList.addLookup("V1");
        lookupList.addLookup("V2");
        assertEquals("field = 'V1' OR field = 'V2'",
                     TestUtil.toSql(lookupList));
    }

    public void testLookupMultiOr()
    {
        LookupList lookupList = new LookupList(LookupListType.OR,
                                               "FieldName");
        lookupList.setSqlColumn("field");
        lookupList.addLookup("V1");
        lookupList.addLookup("V2");
        lookupList.setLookupMultiTable("property_lkup");
        assertEquals("id IN (SELECT data_id FROM property_lkup " +
                     "WHERE name = 'field' AND " +
                     "(value = 'V1' OR value = 'V2'))",
                     TestUtil.toSql(lookupList));
    }

    public void testLookupMultiAnd()
    {
        LookupList lookupList = new LookupList(LookupListType.AND,
                                               "FieldName");
        lookupList.setSqlColumn("field");
        lookupList.addLookup("V1");
        lookupList.addLookup("V2");
        lookupList.setLookupMultiTable("property_lkup");
        assertEquals("id IN (SELECT data_id FROM property_lkup " +
                     "WHERE name = 'field' AND " +
                     "(value = 'V1' AND value = 'V2'))",
                     TestUtil.toSql(lookupList));
    }

    public void testLookupNot()
    {
        LookupList lookupList = new LookupList(LookupListType.NOT,
                                               "FieldName");
        lookupList.setSqlColumn("field");
        lookupList.addLookup("V1");
        lookupList.addLookup("V2");
        assertEquals("NOT (field = 'V1' OR field = 'V2')",
                     TestUtil.toSql(lookupList));
    }

    public void testLookupMultiNot()
    {
        LookupList lookupList = new LookupList(LookupListType.NOT,
                                               "FieldName");
        lookupList.setSqlColumn("field");
        lookupList.addLookup("V1");
        lookupList.addLookup("V2");
        lookupList.setLookupMultiTable("property_lkup");
        assertEquals("id NOT IN (SELECT data_id FROM property_lkup " +
                     "WHERE name = 'field' AND " +
                     "(value = 'V1' OR value = 'V2'))",
                     TestUtil.toSql(lookupList));
    }
}
