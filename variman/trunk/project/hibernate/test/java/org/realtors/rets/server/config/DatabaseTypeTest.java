/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

import junit.framework.TestCase;

public class DatabaseTypeTest extends TestCase
{
    public void testPostgresType()
    {
        DatabaseType type = DatabaseType.getType("postgresql");
        assertNotNull(type);
        assertEquals("org.postgresql.Driver", type.getDriverClass());
        assertEquals("net.sf.hibernate.dialect.PostgreSQLDialect",
                     type.getDialectClass());
        assertEquals("jdbc:postgresql://localhost/rex",
                     type.getUrl("localhost", "rex"));
    }

    public void testSQLServerJSQLType()
    {
        DatabaseType type = DatabaseType.getType("sqlserver-jsql");
        assertNotNull(type);
        assertEquals("com.jnetdirect.jsql.JSQLDriver", type.getDriverClass());
        assertEquals("net.sf.hibernate.dialect.SQLServerDialect",
                     type.getDialectClass());
        assertEquals("jdbc:JSQLConnect://localhost" +
                     "/selectMethod=cursor" +
                     "/database=rex",
                     type.getUrl("localhost", "rex"));
    }

    public void testSQLServerjTDSType()
    {
        DatabaseType type = DatabaseType.getType("sqlserver-jtds");
        assertNotNull(type);
        assertEquals("net.sourceforge.jtds.jdbc.Driver", type.getDriverClass());
        assertEquals("net.sf.hibernate.dialect.SQLServerDialect",
                     type.getDialectClass());
        assertEquals("jdbc:jtds:sqlserver://localhost/rex",
                     type.getUrl("localhost", "rex"));
    }

    public void testUnknowType()
    {
        assertNull(DatabaseType.getType("foo"));
    }
}
