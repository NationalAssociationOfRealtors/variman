/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class BetweenClauseTest extends TestCase
{
    public void testToSql()
    {
        BetweenClause range =
            new BetweenClause("LP", new StringSqlConverter("5"),
                              new StringSqlConverter("10"));
        assertEquals("LP BETWEEN 5 AND 10", TestUtil.toSql(range));
    }
}
