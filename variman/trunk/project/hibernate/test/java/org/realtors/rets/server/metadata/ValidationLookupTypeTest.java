/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class ValidationLookupTypeTest extends TestCase
{
    public void testLevel()
    {
        ValidationLookupType validationLookupType =
            ObjectMother.createValidationLookupType();
        assertEquals("Property:School", validationLookupType.getLevel());
    }
}
