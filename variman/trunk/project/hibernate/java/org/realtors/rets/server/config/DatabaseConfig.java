/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

import java.io.Serializable;
import java.util.Properties;

import net.sf.hibernate.cfg.Environment;

import org.realtors.rets.server.Util;

import org.apache.commons.lang.builder.ToStringBuilder;

public class DatabaseConfig implements Serializable
{
    public String getDriver()
    {
        return mDriver;
    }

    public void setDriver(String driver)
    {
        mDriver = driver;
    }

    public int getMaxActive()
    {
        return mMaxActive;
    }

    public void setMaxActive(int maxActive)
    {
        mMaxActive = maxActive;
    }

    public int getMaxIdle()
    {
        return mMaxIdle;
    }

    public void setMaxIdle(int maxIdle)
    {
        mMaxIdle = maxIdle;
    }

    public int getMaxPsActive()
    {
        return mMaxPsActive;
    }

    public void setMaxPsActive(int maxPsActive)
    {
        mMaxPsActive = maxPsActive;
    }

    public int getMaxPsIdle()
    {
        return mMaxPsIdle;
    }

    public void setMaxPsIdle(int maxPsIdle)
    {
        mMaxPsIdle = maxPsIdle;
    }

    public int getMaxPsWait()
    {
        return mMaxPsWait;
    }

    public void setMaxPsWait(int maxPsWait)
    {
        mMaxPsWait = maxPsWait;
    }

    public int getMaxWait()
    {
        return mMaxWait;
    }

    public void setMaxWait(int maxWait)
    {
        mMaxWait = maxWait;
    }

    public String getPassword()
    {
        return mPassword;
    }

    public void setPassword(String password)
    {
        mPassword = password;
    }

    public String getUrl()
    {
        return mUrl;
    }

    public void setUrl(String url)
    {
        mUrl = url;
    }

    public String getUsername()
    {
        return mUsername;
    }

    public void setUsername(String username)
    {
        mUsername = username;
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("driver", mDriver)
            .append("url", mUrl)
            .append("username", mUsername)
            .append("password", mPassword)
            .append("max active", mMaxActive)
            .append("max wait", mMaxWait)
            .append("max idle", mMaxIdle)
            .append("max ps active", mMaxPsActive)
            .append("max ps wait", mMaxPsWait)
            .append("max ps idle", mMaxPsIdle)
            .toString();
    }

    /**
     * Returns a set of Hibernate properties, suitable for configuring
     * Hibernate.
     *
     * @return Hibernate configuration properties.
     * @see net.sf.hibernate.cfg.Configuration#setProperties
     */
    public Properties createHibernateProperties()
    {
        Properties properties = new Properties();
        properties.setProperty(Environment.DRIVER, mDriver);
        properties.setProperty(Environment.URL, mUrl);
        properties.setProperty(Environment.USER, mUsername);
        properties.setProperty(Environment.PASS, mPassword);
        properties.setProperty(Environment.DIALECT,
                               "net.sf.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty(Environment.SHOW_SQL, "false");

        properties.setProperty(Environment.DBCP_MAXACTIVE,
                               Integer.toString(mMaxActive));
        properties.setProperty(Environment.DBCP_WHENEXHAUSTED, "1");
        properties.setProperty(Environment.DBCP_MAXWAIT,
                               Integer.toString(mMaxWait));
        properties.setProperty(Environment.DBCP_MAXIDLE,
                               Integer.toString(mMaxIdle));

        properties.setProperty(Environment.DBCP_PS_MAXACTIVE,
                               Integer.toString(mMaxPsActive));
        properties.setProperty(Environment.DBCP_PS_WHENEXHAUSTED,  "1");
        properties.setProperty(Environment.DBCP_PS_MAXWAIT,
                               Integer.toString(mMaxPsWait));
        properties.setProperty(Environment.DBCP_PS_MAXIDLE,
                               Integer.toString(mMaxPsIdle));
        return properties;
    }

    private String mDriver;
    private String mUrl;
    private String mUsername;
    private String mPassword;
    private int mMaxActive;
    private int mMaxWait;
    private int mMaxIdle;
    private int mMaxPsActive;
    private int mMaxPsWait;
    private int mMaxPsIdle;
}
