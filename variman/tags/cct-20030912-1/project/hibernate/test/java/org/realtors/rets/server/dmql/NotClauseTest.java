/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class NotClauseTest extends TestCase
{
    public void testNotClause()
    {
        NotClause notClause = new NotClause();
        notClause.setNegation(new DmqlString("expr"));
        assertEquals("NOT 'expr'", TestUtil.toSql(notClause));
    }
}
