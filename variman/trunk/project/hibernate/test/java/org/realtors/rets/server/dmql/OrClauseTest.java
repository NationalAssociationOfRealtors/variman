/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class OrClauseTest extends TestCase
{
    public void testOneElement()
    {
        OrClause orClause = new OrClause();
        orClause.add(new StringSqlConverter("one"));
        assertEquals("(one)", TestUtil.toSql(orClause));
    }

    public void testMultipleElements()
    {
        OrClause orClause = new OrClause();
        orClause.add(new StringSqlConverter("one"));
        orClause.add(new StringSqlConverter("two"));
        orClause.add(new StringSqlConverter("three"));
        assertEquals("(one) OR (two) OR (three)", TestUtil.toSql(orClause));
    }

    public void testEquals()
    {
        OrClause orClause1 = new OrClause();
        orClause1.add(new StringSqlConverter("one"));
        orClause1.add(new StringSqlConverter("two"));

        OrClause orClause2 = new OrClause();
        orClause2.add(new StringSqlConverter("one"));
        orClause2.add(new StringSqlConverter("two"));

        assertEquals(orClause1, orClause2);
    }
}
