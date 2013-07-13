/*
 * Variman RETS Server
 *
 * Author: RealGo
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.dmql;

import junit.framework.TestCase;

public class AnyClauseTest extends TestCase
{
    public void testToSql() 
    {
        AnyClause anyClause = new AnyClause("field");
        assertEquals("(field is null or field is not null) ", TestUtil.toSql(anyClause));
    }

    public void testEquals()
    {
        AnyClause anyClause1 = new AnyClause("field");
        AnyClause anyClause2 = new AnyClause("field");
        assertEquals(anyClause1, anyClause2);
    }
}
