/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

public class PostgreSQLType extends DatabaseType
{
    public static final String NAME = "postgresql";
    public static final String LONG_NAME = "PostgreSQL";
    public static final String DRIVER = "org.postgresql.Driver";
    public static final String DIALECT =
        "org.hibernate.dialect.PostgreSQLDialect";

    public String getName()
    {
        return NAME;
    }

    public String getLongName()
    {
        return LONG_NAME;
    }

    public String getDriverClass()
    {
        return DRIVER;
    }

    public String getDialectClass()
    {
        return DIALECT;
    }

    /**
     * Returns a Postgres JDBC URL.  Postgres URLs take the form:
     *
     *   jdbc:postgresql://[host]/[database]
     *
     * @param hostName Host name of database server
     * @param databaseName Name of database instance
     * @return a Postgres JDBC URL
     */
    public String getUrl(String hostName, String databaseName)
    {
        StringBuffer url = new StringBuffer();
        url.append("jdbc:postgresql://");
        url.append(hostName).append("/").append(databaseName);
        return url.toString();
    }
}