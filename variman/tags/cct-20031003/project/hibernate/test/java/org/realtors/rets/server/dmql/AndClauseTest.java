/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class AndClauseTest extends TestCase
{
    public void testAndClause()
    {
        AndClause andClause = new AndClause();
        andClause.setLeft(new DmqlString("left"));
        andClause.setRight(new DmqlString("right"));
        assertEquals("'left' AND 'right'", TestUtil.toSql(andClause));
    }
}
