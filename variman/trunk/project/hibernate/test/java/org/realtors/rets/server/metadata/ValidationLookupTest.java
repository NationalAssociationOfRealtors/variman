/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class ValidationLookupTest extends TestCase
{
    public void testLevel()
    {
        ValidationLookup validationLookup =
            ObjectMother.createValidationLookup();
        assertEquals("Property", validationLookup.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createValidationLookup().getChildren();
        assertEquals(1, children.size());
        assertTrue(children.get(0) instanceof ValidationLookupType[]);
    }

    public void testTableName()
    {
        assertEquals("VALIDATION_LOOKUP", ValidationLookup.TABLE);
        ServerMetadata validationLookup = ObjectMother.createValidationLookup();
        assertEquals(ValidationLookup.TABLE,
                     validationLookup.getTableName());
    }
}
