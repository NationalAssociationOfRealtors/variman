/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class ValidationExternalTypeTest extends TestCase
{
    public void testLevel()
    {
        ValidationExternalType validationExternlType =
            ObjectMother.createValidationExternlType();
        assertEquals("Property:VET1", validationExternlType.getLevel());
    }
}
