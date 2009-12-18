/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

import java.util.Properties;

import org.hibernate.cfg.Environment;
import org.realtors.rets.server.Util;

/**
 * A set of Hibernate dependent utility methods.
 */
public class HibernateUtils {

    /**
     * Returns a set of Hibernate properties, suitable for configuring
     * Hibernate.
     * 
     * @param databaseConfig The database configuration to extract Hibernate
     *         configuration properties from. Must not be {@code null}.
     * @return Hibernate configuration properties.
     * @see org.hibernate.cfg.Configuration#setProperties
     */
    public static Properties createHibernateProperties(DatabaseConfig databaseConfig)
    {
        Properties properties = new Properties();
        
        String username = databaseConfig.getUsername();
        String password = databaseConfig.getPassword();
        String hostName = databaseConfig.getHostName();
        String databaseName = databaseConfig.getDatabaseName();
        DatabaseType databaseType = databaseConfig.getDatabaseType();
        String driverClass = databaseType.getDriverClass();
        String url = databaseType.getUrl(hostName, databaseName);
        String dialectClass = databaseType.getDialectClass();
        String showSql = Util.toString(databaseConfig.getShowSql());
        int maxActive = databaseConfig.getMaxActive();
        int maxWait = databaseConfig.getMaxWait();
        int maxPsActive = databaseConfig.getMaxPsActive();
        
        properties.setProperty(Environment.DRIVER, driverClass);
        properties.setProperty(Environment.URL, url);
        properties.setProperty(Environment.USER, username);
        properties.setProperty(Environment.PASS, password);
        properties.setProperty(Environment.DIALECT, dialectClass);
        properties.setProperty(Environment.SHOW_SQL, showSql);
        
        properties.setProperty(Environment.C3P0_ACQUIRE_INCREMENT, "0");
        properties.setProperty(Environment.C3P0_MAX_SIZE, "" + maxActive);
        properties.setProperty(Environment.C3P0_TIMEOUT, "" + maxWait);
        properties.setProperty(Environment.C3P0_MAX_STATEMENTS, "" + maxPsActive);
        return properties;
    }

}
