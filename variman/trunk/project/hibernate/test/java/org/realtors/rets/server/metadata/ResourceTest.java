/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class ResourceTest extends TestCase
{
    public void testLevel()
    {
        Resource resource = ObjectMother.createResource();
        assertEquals("", resource.getLevel());
    }
}
