/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class OrClauseTest extends TestCase
{
    public void testOrClause()
    {
        OrClause orClause = new OrClause();
        orClause.setLeft(new TestSqlConverter("left"));
        orClause.setRight(new TestSqlConverter("right"));
        assertEquals("left OR right", TestUtil.toSql(orClause));
    }
}
