/*
 */
package org.realtors.rets.server.metadata;

import java.util.List;

import junit.framework.TestCase;

public class SearchHelpTest extends TestCase
{
    public void testLevel()
    {
        SearchHelp searchHelp = ObjectMother.createSearchHelp();
        assertEquals("Property", searchHelp.getLevel());
    }

    public void testChildren()
    {
        List children = ObjectMother.createSearchHelp().getChildren();
        assertEquals(0, children.size());
    }

    public void testTableName()
    {
        assertEquals("SEARCH_HELP", SearchHelp.TABLE);
        ServerMetadata searchHelp = ObjectMother.createSearchHelp();
        assertEquals(SearchHelp.TABLE, searchHelp.getTableName());
    }
}
