/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class UpdateTypeTest extends TestCase
{
    public void testLevel()
    {
        UpdateType updateType = ObjectMother.createUpdateType();
        assertEquals("Property:RES:Change", updateType.getLevel());
    }
}
