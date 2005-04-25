/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class LessThanClauseTest extends TestCase
{
    public void testToSql()
    {
        LessThanClause clause =
            new LessThanClause("LP", new StringSqlConverter("100000"));
        assertEquals("LP <= 100000", TestUtil.toSql(clause));
    }

    public void testEquals()
    {
        LessThanClause clause1 =
            new LessThanClause("LP", new StringSqlConverter("100000"));
        LessThanClause clause2 =
            new LessThanClause("LP", new StringSqlConverter("100000"));
        assertEquals(clause1, clause2);
    }
}
