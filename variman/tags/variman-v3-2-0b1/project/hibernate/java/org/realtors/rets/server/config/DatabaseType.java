/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

import java.util.Map;
import java.util.HashMap;

public abstract class DatabaseType
{
    public abstract String getName();

    public abstract String getLongName();

    public abstract String getDriverClass();

    public abstract String getDialectClass();

    public abstract String getUrl(String hostName, String databaseName);

    public static DatabaseType getType(String name)
    {
        return (DatabaseType) sKnownTypes.get(name);
    }

    public static final DatabaseType MYSQL = new MySQLType();
    public static final DatabaseType POSTGRESQL = new PostgreSQLType();
    public static final DatabaseType SQLSERVER_JSQL = new SQLServerJSQLType();
    public static final DatabaseType SQLSERVER_JTDS = new SQLServerjTDSType();

    private static Map sKnownTypes;

    static
    {
        sKnownTypes = new HashMap();
        registerType(MYSQL);
        registerType(POSTGRESQL);
        registerType(SQLSERVER_JSQL);
        registerType(SQLSERVER_JTDS);
    }

    public static void registerType(DatabaseType databaseType)
    {
        sKnownTypes.put(databaseType.getName(), databaseType);
    }
}
