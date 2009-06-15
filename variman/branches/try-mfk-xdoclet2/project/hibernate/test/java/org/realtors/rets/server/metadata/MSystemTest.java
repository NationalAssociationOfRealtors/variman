/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class MSystemTest extends TestCase
{
    public void testLevel()
    {
        MSystem system = ObjectMother.createSystem();
        assertEquals("", system.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createSystem().getChildren();
        assertEquals(2, children.size());
        assertTrue(children.get(0) instanceof Resource[]);
        assertTrue(children.get(1) instanceof ForeignKey[]);
    }

    public void testTableName()
    {
        assertEquals("SYSTEM", MSystem.TABLE);
        ServerMetadata system = ObjectMother.createSystem();
        assertEquals(MSystem.TABLE, system.getTableName());
    }
}
