/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class TableTest extends TestCase
{
    public void testLevel()
    {
        Table table = ObjectMother.createTable();
        assertEquals("Property:RES", table.getLevel());
    }
}
