/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class ValidationExpressionTest extends TestCase
{
    public void testLevel()
    {
        ValidationExpression validationExpression =
            ObjectMother.createValidationExpression();
        assertEquals("Property", validationExpression.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createValidationExpression().getChildren();
        assertEquals(0, children.size());
    }
}
