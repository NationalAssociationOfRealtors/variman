/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class ValidationExternalTest extends TestCase
{
    public void testLevel()
    {
        ValidationExternal validationExternal =
            ObjectMother.createValidationExternal();
        assertEquals("Property", validationExternal.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createValidationExternal().getChildren();
        assertEquals(1, children.size());
        assertTrue(children.get(0) instanceof ValidationExternalType[]);
    }
}
