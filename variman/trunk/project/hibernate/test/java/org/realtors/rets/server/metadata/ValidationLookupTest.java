/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class ValidationLookupTest extends TestCase
{
    public void testLevel()
    {
        ValidationLookup validationLookup =
            ObjectMother.createValidationLookup();
        assertEquals("Property", validationLookup.getLevel());
    }
}
