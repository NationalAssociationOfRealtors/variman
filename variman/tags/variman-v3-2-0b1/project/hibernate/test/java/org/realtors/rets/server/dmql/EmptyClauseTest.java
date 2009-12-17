/*
 * Variman RETS Server
 *
 * Author: RealGo
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class EmptyClauseTest extends TestCase
{
    public void testToSql() 
    {
        EmptyClause emptyClause = new EmptyClause("field");
        assertEquals("field is null ", TestUtil.toSql(emptyClause));

        emptyClause = new EmptyClause("field", DmqlFieldType.BOOLEAN);
        assertEquals("field is null ", TestUtil.toSql(emptyClause));

        emptyClause = new EmptyClause("field", DmqlFieldType.LOOKUP);
        assertEquals("field is null ", TestUtil.toSql(emptyClause));

        emptyClause = new EmptyClause("field", DmqlFieldType.LOOKUP_MULTI);
        assertEquals("field is null ", TestUtil.toSql(emptyClause));

        emptyClause = new EmptyClause("field", DmqlFieldType.NUMERIC);
        assertEquals("field is null ", TestUtil.toSql(emptyClause));

        emptyClause = new EmptyClause("field", DmqlFieldType.TEMPORAL);
        assertEquals("field is null ", TestUtil.toSql(emptyClause));

        emptyClause = new EmptyClause("field", DmqlFieldType.CHARACTER);
        assertEquals("(field is null or field = '') ", TestUtil.toSql(emptyClause));
    }

    public void testEquals()
    {
        EmptyClause emptyClause1 = new EmptyClause("field");
        EmptyClause emptyClause2 = new EmptyClause("field");
        assertEquals(emptyClause1, emptyClause2);
        
        emptyClause1 = new EmptyClause("field", DmqlFieldType.CHARACTER);
        emptyClause2 = new EmptyClause("field", DmqlFieldType.CHARACTER);
        assertEquals(emptyClause1, emptyClause2);
    }
}
