/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

import java.util.Properties;

import net.sf.hibernate.cfg.Environment;

import junit.framework.TestCase;

public class DatabaseConfigTest extends TestCase
{
    public void testTypeInformation()
    {
        DatabaseConfig config = new DatabaseConfig();
        config.setDatabaseType(DatabaseType.POSTGRESQL);
        config.setHostName("localhost");
        config.setDatabaseName("db_test");
        assertEquals("org.postgresql.Driver", config.getDriver());
        assertEquals("net.sf.hibernate.dialect.PostgreSQLDialect",
                     config.getDialect());
        assertEquals("jdbc:postgresql://localhost/db_test",
                     config.getUrl());
    }

    public void testHibernateProperties()
    {
        DatabaseConfig config = new DatabaseConfig();
        config.setDatabaseType(DatabaseType.POSTGRESQL);
        config.setHostName("localhost");
        config.setDatabaseName("db_test");
        config.setUsername("dave");
        config.setPassword("");
        config.setMaxActive(100);
        config.setMaxWait(120000);
        config.setMaxIdle(10);
        config.setMaxPsActive(50);
        config.setMaxPsWait(60000);
        config.setMaxPsIdle(5);
        config.setShowSql(true);

        Properties properties = config.createHibernateProperties();
        assertEquals("org.postgresql.Driver",
                     properties.getProperty(Environment.DRIVER));
        assertEquals("jdbc:postgresql://localhost/db_test",
                     properties.getProperty(Environment.URL));
        assertEquals("dave", properties.getProperty(Environment.USER));
        assertEquals("", properties.getProperty(Environment.PASS));
        assertEquals("net.sf.hibernate.dialect.PostgreSQLDialect",
                     properties.getProperty(Environment.DIALECT));
        assertEquals("true", properties.getProperty(Environment.SHOW_SQL));

        assertEquals("100", properties.getProperty(Environment.DBCP_MAXACTIVE));
        assertEquals(
            "1", properties.getProperty(Environment.DBCP_WHENEXHAUSTED));
        assertEquals(
            "120000", properties.getProperty(Environment.DBCP_MAXWAIT));
        assertEquals("10", properties.getProperty(Environment.DBCP_MAXIDLE));

        assertEquals(
            "50", properties.getProperty(Environment.DBCP_PS_MAXACTIVE));
        assertEquals(
            "1", properties.getProperty(Environment.DBCP_PS_WHENEXHAUSTED));
        assertEquals(
            "60000", properties.getProperty(Environment.DBCP_PS_MAXWAIT));
        assertEquals("5", properties.getProperty(Environment.DBCP_PS_MAXIDLE));
    }
}
