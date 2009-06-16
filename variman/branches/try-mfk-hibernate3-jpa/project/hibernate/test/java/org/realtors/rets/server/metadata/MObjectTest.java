/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class MObjectTest extends TestCase
{
    public void testLevel()
    {
        MObject object = ObjectMother.createMObject();
        assertEquals("Property", object.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createMObject().getChildren();
        assertEquals(0, children.size());
    }

    public void testTableName()
    {
        assertEquals("OBJECT", MObject.TABLE);
        ServerMetadata object = ObjectMother.createMObject();
        assertEquals(MObject.TABLE, object.getTableName());
    }
}
