/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class ValidationExpressionTest extends TestCase
{
    public void testLevel()
    {
        ValidationExpression validationExpression =
            ObjectMother.createValidationExpression();
        assertEquals("Property", validationExpression.getLevel());
    }
}
