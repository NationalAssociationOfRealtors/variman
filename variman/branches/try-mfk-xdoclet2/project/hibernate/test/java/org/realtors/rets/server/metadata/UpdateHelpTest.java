/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class UpdateHelpTest extends TestCase
{
    public void testLevel()
    {
        UpdateHelp updateHelp = ObjectMother.createUpdateHelp();
        assertEquals("Property", updateHelp.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createUpdateHelp().getChildren();
        assertEquals(0, children.size());
    }

    public void testTableName()
    {
        assertEquals("UPDATE_HELP", UpdateHelp.TABLE);
        ServerMetadata updateHelp = ObjectMother.createUpdateHelp();
        assertEquals(UpdateHelp.TABLE, updateHelp.getTableName());
    }
}
