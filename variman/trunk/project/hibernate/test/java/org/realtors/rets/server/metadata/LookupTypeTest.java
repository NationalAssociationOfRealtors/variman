/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class LookupTypeTest extends TestCase
{
    public void testLevel()
    {
        LookupType lookupType = ObjectMother.createLookupType();
        assertEquals("Property:E_SCHOOL", lookupType.getLevel());
    }
}
