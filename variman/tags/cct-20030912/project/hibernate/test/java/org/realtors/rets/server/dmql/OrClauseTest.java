/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class OrClauseTest extends TestCase
{
    public void testOrClause()
    {
        OrClause orClause = new OrClause();
        orClause.setLeft(new DmqlString("left"));
        orClause.setRight(new DmqlString("right"));
        assertEquals("'left' OR 'right'", TestUtil.toSql(orClause));
    }
}
