/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import junit.framework.TestCase;
import org.realtors.rets.server.Group;

public class TableTest extends TestCase
{
    public void testLevel()
    {
        Table table = ObjectMother.createTable();
        assertEquals("Property:RES", table.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createTable().getChildren();
        assertEquals(0, children.size());
    }

    public void testTableName()
    {
        assertEquals("TABLE", Table.TABLE);
        ServerMetadata table = ObjectMother.createTable();
        assertEquals(Table.TABLE, table.getTableName());
    }

    public void testGroups()
    {
        Table table = ObjectMother.createTable();
        Group agents = new Group("agents");
        Group newspapers = new Group("newspapers");

        // This table starts with open permission
        assertTrue(table.isGroupReadable(agents));
        assertTrue(table.isGroupReadable(newspapers));

        Set excludes = new HashSet();
        excludes.add(newspapers);
        table.setExcludeGroups(excludes);
        assertTrue(table.isGroupReadable(agents));
        assertFalse(table.isGroupReadable(newspapers));

        // Check that includes overrides excludes
        Set includes = new HashSet();
        includes.add(newspapers);
        table.setIncludeGroups(includes);
        assertFalse(table.isGroupReadable(agents));
        assertTrue(table.isGroupReadable(newspapers));

        includes = new HashSet();
        includes.add(agents);
        table.setIncludeGroups(includes);
        assertTrue(table.isGroupReadable(agents));
        assertFalse(table.isGroupReadable(newspapers));

        table.setExcludeGroups(null);
        assertTrue(table.isGroupReadable(agents));
        assertFalse(table.isGroupReadable(newspapers));
    }
}
