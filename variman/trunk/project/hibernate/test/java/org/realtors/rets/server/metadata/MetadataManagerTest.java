/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;
import java.util.Map;

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

        MClass aClass = table.getMClass();
        ServerMetadata metadata = manager.findByPath(MClass.TABLE,
                                                     "Property:RES");
        assertNotNull(metadata);
        assertSame(aClass, metadata);

        metadata = manager.findByPath(MSystem.TABLE, "");
        assertNull(metadata);
    }

    public void testLevelMatch()
    {
        assertTrue(MetadataManager.levelsMatch("Property", ""));
        assertTrue(MetadataManager.levelsMatch("Property", "*"));
        assertTrue(MetadataManager.levelsMatch("Property", "Property"));
        assertFalse(MetadataManager.levelsMatch("Property", "foo"));

        assertTrue(MetadataManager.levelsMatch("Property:RES", ""));
        assertTrue(MetadataManager.levelsMatch("Property:RES", "*"));
        assertTrue(MetadataManager.levelsMatch("Property:RES", "Property:*"));
        assertTrue(MetadataManager.levelsMatch("Property:RES", "Property:RES"));
        assertTrue(MetadataManager.levelsMatch("Property:RES", "*:RES"));
        assertFalse(MetadataManager.levelsMatch("Property:RES", "foo"));
        assertFalse(MetadataManager.levelsMatch("Property:RES", "foo:*"));
        assertFalse(MetadataManager.levelsMatch("Property:RES", "*:foo"));
    }

    public void testFindByPattern()
    {
        Table table = ObjectMother.createTable();
        MSystem system = table.getMClass().getResource().getSystem();
        MetadataManager manager = new MetadataManager();
        manager.addRecursive(system);

        Map found = manager.findByPattern(Table.TABLE, "*");
        assertEquals(1, found.size());
        List atLevel = (List) found.get("Property:RES");
        assertNotNull(atLevel);
        assertEquals(1, atLevel.size());
        assertSame(table, atLevel.get(0));

        found = manager.findByPattern(Table.TABLE, "Property:RES");
        assertEquals(1, found.size());
        atLevel = (List) found.get("Property:RES");
        assertNotNull(atLevel);
        assertEquals(1, atLevel.size());
        assertSame(table, atLevel.get(0));

        found = manager.findByPattern(Table.TABLE, "foo");
        assertEquals(0, found.size());
    }
}
