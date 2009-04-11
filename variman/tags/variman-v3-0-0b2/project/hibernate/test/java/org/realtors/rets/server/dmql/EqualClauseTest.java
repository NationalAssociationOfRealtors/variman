package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class EqualClauseTest extends TestCase
{
    public void testToSql()
    {
        EqualClause equalClause =
            new EqualClause("field", new StringSqlConverter("value"));
        assertEquals("field = value", TestUtil.toSql(equalClause));
    }

    public void testEquals()
    {
        EqualClause equalClause1 =
            new EqualClause("field", new StringSqlConverter("value"));
        EqualClause equalClause2 =
            new EqualClause("field", new StringSqlConverter("value"));
        assertEquals(equalClause1, equalClause2);
    }
}
