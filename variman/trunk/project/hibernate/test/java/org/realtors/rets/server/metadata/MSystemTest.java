/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class MSystemTest extends TestCase
{
    public void testChildren()
    {
        List children = ObjectMother.createSystem().getChildren();
        assertEquals(2, children.size());
        assertTrue(children.get(0) instanceof Resource[]);
        assertTrue(children.get(1) instanceof ForeignKey[]);
    }
}
