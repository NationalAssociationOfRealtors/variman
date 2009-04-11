/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class ValidationLookupTypeTest extends TestCase
{
    public void testLevel()
    {
        ValidationLookupType validationLookupType =
            ObjectMother.createValidationLookupType();
        assertEquals("Property:School", validationLookupType.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createValidationLookupType().getChildren();
        assertEquals(0, children.size());
    }

    public void testTableName()
    {
        assertEquals("VALIDATION_LOOKUP_TYPE", ValidationLookupType.TABLE);
        ServerMetadata validationLookupType =
            ObjectMother.createValidationLookupType();
        assertEquals(ValidationLookupType.TABLE,
                     validationLookupType.getTableName());
    }
}
