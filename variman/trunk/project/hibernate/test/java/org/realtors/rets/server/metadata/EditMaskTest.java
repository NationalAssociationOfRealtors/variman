/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class EditMaskTest extends TestCase
{
    public void testLevel()
    {
        EditMask editMask = ObjectMother.createEditMask();
        assertEquals("Property", editMask.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createEditMask().getChildren();
        assertEquals(0, children.size());
    }
}
