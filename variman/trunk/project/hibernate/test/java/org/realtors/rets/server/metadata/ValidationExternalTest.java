/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class ValidationExternalTest extends TestCase
{
    public void testLevel()
    {
        ValidationExternal validationExternal =
            ObjectMother.createValidationExternal();
        assertEquals("Property", validationExternal.getLevel());
    }
}
