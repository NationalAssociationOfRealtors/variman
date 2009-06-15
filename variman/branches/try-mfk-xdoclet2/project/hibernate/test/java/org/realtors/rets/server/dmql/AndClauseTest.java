/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class AndClauseTest extends TestCase
{
    public void testOneElement()
    {
        AndClause andClause = new AndClause();
        andClause.add(new StringSqlConverter("one"));
        assertEquals("(one)", TestUtil.toSql(andClause));
    }

    public void testMultipleElements()
    {
        AndClause andClause = new AndClause();
        andClause.add(new StringSqlConverter("one"));
        andClause.add(new StringSqlConverter("two"));
        andClause.add(new StringSqlConverter("three"));
        assertEquals("(one) AND (two) AND (three)", TestUtil.toSql(andClause));
    }

    public void testEquals()
    {
        AndClause andClause1 = new AndClause();
        andClause1.add(new StringSqlConverter("one"));
        andClause1.add(new StringSqlConverter("two"));

        AndClause andClause2 = new AndClause();
        andClause2.add(new StringSqlConverter("one"));
        andClause2.add(new StringSqlConverter("two"));

        assertEquals(andClause1,  andClause2);
    }
}
