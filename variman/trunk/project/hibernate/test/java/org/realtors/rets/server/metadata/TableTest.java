/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

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
}
