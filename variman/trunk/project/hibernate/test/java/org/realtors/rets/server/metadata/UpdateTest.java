/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class UpdateTest extends TestCase
{
    public void testLevel()
    {
        Update update = ObjectMother.createUpdate();
        assertEquals("Property:RES", update.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createUpdate().getChildren();
        assertEquals(1, children.size());
        assertTrue(children.get(0) instanceof UpdateType[]);
    }

    public void testTableName()
    {
        assertEquals("UPDATE", Update.TABLE);
        ServerMetadata update = ObjectMother.createUpdate();
        assertEquals(Update.TABLE, update.getTableName());
    }
}
