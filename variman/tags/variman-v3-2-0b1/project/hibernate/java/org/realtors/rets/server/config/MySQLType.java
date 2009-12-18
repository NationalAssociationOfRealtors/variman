/*
 * Variman RETS Server
 */

package org.realtors.rets.server.config;

public class MySQLType extends DatabaseType
{
    public static final String NAME = "mysql";
    public static final String LONG_NAME = "MySQL";
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String DIALECT =
        "org.hibernate.dialect.MySQLDialect";

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
     * Returns a MySQL JDBC URL.  MySQL URLs take the form:
     *
     *   jdbc:mysql://[host]/[database]
     *
     * @param hostName Host name of database server
     * @param databaseName Name of database instance
     * @return a MySQL JDBC URL
     */
    public String getUrl(String hostName, String databaseName)
    {
        StringBuffer url = new StringBuffer();
        url.append("jdbc:mysql://");
        url.append(hostName).append("/").append(databaseName);
        return url.toString();
    }
}
