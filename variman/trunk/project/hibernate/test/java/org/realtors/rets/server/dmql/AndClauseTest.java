/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class AndClauseTest extends TestCase
{
    public void testAndClause()
    {
        AndClause andClause = new AndClause();
        andClause.setLeft(new TestSqlConverter("left"));
        andClause.setRight(new TestSqlConverter("right"));
        assertEquals("left AND right", TestUtil.toSql(andClause));
    }
}
