/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class ResourceTest extends TestCase
{
    public void testLevel()
    {
        Resource resource = ObjectMother.createResource();
        assertEquals("", resource.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createResource().getChildren();
        assertEquals(8, children.size());
        int i = 0;
        assertTrue(children.get(i++) instanceof MClass[]);
        assertTrue(children.get(i++) instanceof MObject[]);
        assertTrue(children.get(i++) instanceof SearchHelp[]);
        assertTrue(children.get(i++) instanceof EditMask[]);
        assertTrue(children.get(i++) instanceof Lookup[]);
        assertTrue(children.get(i++) instanceof ValidationLookup[]);
        assertTrue(children.get(i++) instanceof ValidationExternal[]);
        assertTrue(children.get(i++) instanceof ValidationExpression[]);
        // This ensures we've checked all elements
        assertEquals(8, i);
    }

    public void testTableName()
    {
        assertEquals("RESOURCE", Resource.TABLE);
        ServerMetadata resource = ObjectMother.createResource();
        assertEquals(Resource.TABLE, resource.getTableName());
    }
}
