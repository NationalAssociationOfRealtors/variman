/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class EditMaskTest extends TestCase
{
    public void testLevel()
    {
        EditMask editMask = ObjectMother.createEditMask();
        assertEquals("Property", editMask.getLevel());
    }
}
