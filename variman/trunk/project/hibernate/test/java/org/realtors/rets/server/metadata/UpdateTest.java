/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class UpdateTest extends TestCase
{
    public void testLevel()
    {
        Update update = ObjectMother.createUpdate();
        assertEquals("Property:RES", update.getLevel());
    }
}
