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
            ObjectMother.createValidationExternalType();
        assertEquals("Property:VET1", validationExternlType.getLevel());
    }

    public void testChildren()
    {
        List children =
            ObjectMother.createValidationExternalType().getChildren();
        assertEquals(0, children.size());
    }

    public void testTableName()
    {
        assertEquals("VALIDATION_EXTERNAL_TYPE", ValidationExternalType.TABLE);
        ServerMetadata validationExternalType =
            ObjectMother.createValidationExternalType();
        assertEquals(ValidationExternalType.TABLE,
                     validationExternalType.getTableName());
    }
}
