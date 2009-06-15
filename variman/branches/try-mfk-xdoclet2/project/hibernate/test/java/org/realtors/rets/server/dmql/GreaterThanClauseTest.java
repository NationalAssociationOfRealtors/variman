/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class GreaterThanClauseTest extends TestCase
{
    public void testToSql()
    {
        GreaterThanClause clause =
            new GreaterThanClause("LP", new StringSqlConverter("100000"));
        assertEquals("LP >= 100000", TestUtil.toSql(clause));
    }

    public void testEquals()
    {
        GreaterThanClause clause1 =
            new GreaterThanClause("LP", new StringSqlConverter("100000"));
        GreaterThanClause clause2 =
            new GreaterThanClause("LP", new StringSqlConverter("100000"));
        assertEquals(clause1, clause2);
    }
}
