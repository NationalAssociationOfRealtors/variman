/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class MClassTest extends TestCase
{
    public void testLevel()
    {
        MClass clazz = ObjectMother.createClass();
        assertEquals("Property", clazz.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createClass().getChildren();
        assertEquals(2, children.size());
        assertTrue(children.get(0) instanceof Table[]);
        assertTrue(children.get(1) instanceof Update[]);
    }

    public void testTableName()
    {
        assertEquals("CLASS", MClass.TABLE);
        ServerMetadata clazz = ObjectMother.createClass();
        assertEquals(MClass.TABLE, clazz.getTableName());
    }
}
