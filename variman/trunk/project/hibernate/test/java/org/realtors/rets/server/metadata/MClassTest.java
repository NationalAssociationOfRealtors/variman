/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class MClassTest extends TestCase
{
    public void testLevel()
    {
        MClass clazz = ObjectMother.createClass();
        assertEquals("Property", clazz.getLevel());
    }
}
