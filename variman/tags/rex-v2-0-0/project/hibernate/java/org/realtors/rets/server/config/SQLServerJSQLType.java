/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

public class SQLServerJSQLType extends DatabaseType
{
    public static final String NAME = "sqlserver-jsql";
    public static final String LONG_NAME = "SQLServer (JSQL)";
    public static final String DIALECT =
        "net.sf.hibernate.dialect.SQLServerDialect";
    public static final String DRIVER = "com.jnetdirect.jsql.JSQLDriver";

    public String getName()
    {
        return NAME;
    }

    public String getLongName()
    {
        return LONG_NAME;
    }

    public String getDialectClass()
    {
        return DIALECT;
    }

    public String getDriverClass()
    {
        return DRIVER;
    }

    /**
     * Returns a Postgres JDBC URL.  Postgres URLs take the form:
     *
     *   jdbc:JSQLConnect://[hostName]/database=[databaseName]
     *
     * @param hostName Host name of database server
     * @param databaseName Name of database instance
     * @return a SQL Server JDBC URL
     */
    public String getUrl(String hostName, String databaseName)
    {
        StringBuffer url = new StringBuffer();
        url.append("jdbc:JSQLConnect://");
        url.append(hostName);
        url.append("/database=").append(databaseName);
        return url.toString();
    }
}
