/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class LookupTest extends TestCase
{
    public void testLevel()
    {
        Lookup lookup = ObjectMother.createLookup();
        assertEquals("Property", lookup.getLevel());
    }
}
