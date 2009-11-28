/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;
import java.util.Map;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MTable;

import junit.framework.TestCase;

public class MetadataManagerTest extends TestCase
{
    public void testFindUnknownTable()
    {
        MetadataManager manager = new MetadataManager();
        List found = manager.findByLevel("unknown_table", "");
        assertEquals(0, found.size());
    }

    public void testFindUnknownLevel()
    {
        MetadataManager manager = new MetadataManager();
        manager.add(ObjectMother.createSystem());
        List found = manager.findByLevel(MetadataType.SYSTEM.name(), "unknown_level");
        assertEquals(0, found.size());
    }

    public void testAdd()
    {
        MTable table = ObjectMother.createTable();
        MetadataManager manager = new MetadataManager();
        manager.add(table);
        List found = manager.findByLevel(MetadataType.TABLE.name(), "Property:RES");
        assertEquals(1, found.size());
        assertSame(table, found.get(0));
    }

    public void testAddRecursive()
    {
        MTable table = ObjectMother.createTable();
        MSystem system = table.getMClass().getMResource().getMSystem();
        MetadataManager manager = new MetadataManager();
        manager.addRecursive(system);
        List<MetaObject> found = manager.findByLevel(MetadataType.TABLE.name(), "Property:RES");
        assertEquals(1, found.size());
        assertSame(table, found.get(0));

        MClass aClass = table.getMClass();
        MetaObject metadata = manager.findByPath(MetadataType.CLASS.name(),
                                                     "Property:RES");
        assertNotNull(metadata);
        assertSame(aClass, metadata);

        metadata = manager.findByPath(MetadataType.SYSTEM.name(), "");
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
        MTable table = ObjectMother.createTable();
        MSystem system = table.getMClass().getMResource().getMSystem();
        MetadataManager manager = new MetadataManager();
        manager.addRecursive(system);

        Map<String, List<MetaObject>> found = manager.findByPattern(MetadataType.TABLE.name(), "*");
        assertEquals(1, found.size());
        List<MetaObject> atLevel = found.get("Property:RES");
        assertNotNull(atLevel);
        assertEquals(1, atLevel.size());
        assertSame(table, atLevel.get(0));

        found = manager.findByPattern(MetadataType.TABLE.name(), "Property:RES");
        assertEquals(1, found.size());
        atLevel = found.get("Property:RES");
        assertNotNull(atLevel);
        assertEquals(1, atLevel.size());
        assertSame(table, atLevel.get(0));

        found = manager.findByPattern(MetadataType.TABLE.name(), "foo");
        assertEquals(0, found.size());
    }
}
