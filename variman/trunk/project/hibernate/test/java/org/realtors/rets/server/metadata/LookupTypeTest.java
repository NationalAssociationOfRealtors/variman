/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class LookupTypeTest extends TestCase
{
    public void testLevel()
    {
        LookupType lookupType = ObjectMother.createLookupType();
        assertEquals("Property:E_SCHOOL", lookupType.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createLookupType().getChildren();
        assertEquals(0, children.size());
    }
}
