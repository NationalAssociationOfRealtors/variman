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

    /*
     * Lookup multis are comma separated text fields.  Say the interior
     * features column, if, can be "COOL,HEAT,FP,BASEMENT"
     */

    public void testLookupMultiOr()
    {
        LookupList lookupList = new LookupList(LookupListType.OR,
                                               "InteriorFeatures");
        lookupList.setSqlColumn("if");
        lookupList.addLookup("FP");
        lookupList.addLookup("HEAT");
        lookupList.setLookupMulti(true);
        assertEquals(
            "(if = 'FP' OR if LIKE 'FP,%' OR if LIKE '%,FP' OR " +
            "if LIKE '%,FP,%') " +
            "OR (if = 'HEAT' OR if LIKE 'HEAT,%' OR if LIKE '%,HEAT' OR " +
            "if LIKE '%,HEAT,%')",
            TestUtil.toSql(lookupList));
    }

    public void testLookupMultiAnd()
    {
        LookupList lookupList = new LookupList(LookupListType.AND,
                                               "InteriorFeatures");
        lookupList.setSqlColumn("if");
        lookupList.addLookup("FP");
        lookupList.addLookup("HEAT");
        lookupList.setLookupMulti(true);
        assertEquals(
            "(if = 'FP' OR if LIKE 'FP,%' OR if LIKE '%,FP' OR " +
            "if LIKE '%,FP,%') " +
            "AND (if = 'HEAT' OR if LIKE 'HEAT,%' OR if LIKE '%,HEAT' OR " +
            "if LIKE '%,HEAT,%')",
            TestUtil.toSql(lookupList));
    }

    public void testLookupMultiNot()
    {
        LookupList lookupList = new LookupList(LookupListType.NOT,
                                               "InteriorFeatures");
        lookupList.setSqlColumn("if");
        lookupList.addLookup("FP");
        lookupList.addLookup("HEAT");
        lookupList.setLookupMulti(true);
        assertEquals(
            "NOT (" +
            "(if = 'FP' OR if LIKE 'FP,%' OR if LIKE '%,FP' OR " +
            "if LIKE '%,FP,%') " +
            "OR (if = 'HEAT' OR if LIKE 'HEAT,%' OR if LIKE '%,HEAT' OR " +
            "if LIKE '%,HEAT,%')" +
            ")",
            TestUtil.toSql(lookupList));
    }

    public void testLookupMultiTableOr()
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

    public void testLookupMultiTableAnd()
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

    public void testLookupMultiTableNot()
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
