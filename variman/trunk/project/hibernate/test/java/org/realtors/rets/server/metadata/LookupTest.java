/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class LookupTest extends TestCase
{
    public void testLevel()
    {
        Lookup lookup = ObjectMother.createLookup();
        assertEquals("Property", lookup.getLevel());
    }

    public void testChilren()
    {
        List children = ObjectMother.createLookup().getChildren();
        assertEquals(1, children.size());
        assertTrue(children.get(0) instanceof LookupType[]);
    }
}
