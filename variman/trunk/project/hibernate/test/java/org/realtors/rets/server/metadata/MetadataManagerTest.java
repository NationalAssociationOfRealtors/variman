/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class MetadataManagerTest extends TestCase
{
    public void testFindUnknownTable()
    {
        MetadataManager manager = new MetadataManager();
        List found = manager.find("unknown_table", "");
        assertEquals(0, found.size());
    }

    public void testFindUnknownLevel()
    {
        MetadataManager manager = new MetadataManager();
        manager.add(ObjectMother.createSystem());
        List found = manager.find(MSystem.TABLE, "unknown_level");
        assertEquals(0, found.size());
    }

    public void testAdd()
    {
        Table table = ObjectMother.createTable();
        MetadataManager manager = new MetadataManager();
        manager.add(table);
        List found = manager.find(Table.TABLE, "Property:RES");
        assertEquals(1, found.size());
        assertSame(table, found.get(0));
    }

    public void testAddRecursive()
    {
        Table table = ObjectMother.createTable();
        MSystem system = table.getMClass().getResource().getSystem();
        MetadataManager manager = new MetadataManager();
        manager.addRecursive(system);
        List found = manager.find(Table.TABLE, "Property:RES");
        assertEquals(1, found.size());
        assertSame(table, found.get(0));
    }
}
