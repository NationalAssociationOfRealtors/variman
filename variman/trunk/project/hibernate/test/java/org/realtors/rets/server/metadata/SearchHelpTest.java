/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.TestCase;

public class SearchHelpTest extends TestCase
{
    public void testLevel()
    {
        SearchHelp searchHelp = ObjectMother.createSearchHelp();
        assertEquals("Property", searchHelp.getLevel());
    }
}
