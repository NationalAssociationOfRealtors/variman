/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class UpdateTypeTest extends TestCase
{
    public void testLevel()
    {
        UpdateType updateType = ObjectMother.createUpdateType();
        assertEquals("Property:RES:Change", updateType.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createUpdateType().getChildren();
        assertEquals(0, children.size());
    }
}
