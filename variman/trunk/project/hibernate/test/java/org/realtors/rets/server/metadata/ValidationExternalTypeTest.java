/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class ValidationExternalTypeTest extends TestCase
{
    public void testLevel()
    {
        ValidationExternalType validationExternlType =
            ObjectMother.createValidationExternlType();
        assertEquals("Property:VET1", validationExternlType.getLevel());
    }

    public void testChildren()
    {
        List children =
            ObjectMother.createValidationExternlType().getChildren();
        assertEquals(0, children.size());
    }
}
