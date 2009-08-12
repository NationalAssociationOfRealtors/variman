/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

public class SQLServerjTDSType extends DatabaseType
{
    public static final String NAME = "sqlserver-jtds";
    public static final String LONG_NAME = "SQLServer (jTDS)";
    public static final String DIALECT =
        "org.hibernate.dialect.SQLServerDialect";
    public static final String DRIVER = "net.sourceforge.jtds.jdbc.Driver";

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
     * Returns a jTDS JDBC URL.  jTDS URLs take the form:
     *
     *   jdbc:jtds:sqlserver://[hostName]/[databaseName]
     *
     * @param hostName Host name of database server
     * @param databaseName Name of database instance
     * @return a SQL Server JDBC URL
     */
    public String getUrl(String hostName, String databaseName)
    {
        StringBuffer url = new StringBuffer();
        url.append("jdbc:jtds:sqlserver://");
        url.append(hostName);
        url.append("/").append(databaseName);
        return url.toString();
    }
}
