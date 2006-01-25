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

    public void testTableName()
    {
        assertEquals("LOOKUP_TYPE", LookupType.TABLE);
        ServerMetadata lookupType = ObjectMother.createLookupType();
        assertEquals(LookupType.TABLE, lookupType.getTableName());
    }
}
