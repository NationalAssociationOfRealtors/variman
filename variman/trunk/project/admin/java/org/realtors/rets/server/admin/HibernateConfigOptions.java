/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Aug 14, 2003
 *
 */
package org.realtors.rets.server.admin;

/**
 * @author kgarner
 */
public class HibernateConfigOptions
{
    /**
     * @return
     */
    public int getC3P0MaxSize()
    {
        return mC3P0MaxSize;
    }

    /**
     * @return
     */
    public int getC3P0MaxStatements()
    {
        return mC3P0MaxStatements;
    }

    /**
     * @return
     */
    public int getC3P0MinSize()
    {
        return mC3P0MinSize;
    }

    /**
     * @return
     */
    public int getC3P0Timeout()
    {
        return mC3P0Timeout;
    }

    /**
     * @return
     */
    public String getConnectionDriverClass()
    {
        return mConnectionDriverClass;
    }

    /**
     * @return
     */
    public String getConnectionPassword()
    {
        return mConnectionPassword;
    }

    /**
     * @return
     */
    public String getConnectionURL()
    {
        return mConnectionURL;
    }

    /**
     * @return
     */
    public String getConnectionUsername()
    {
        return mConnectionUsername;
    }

    /**
     * @return
     */
    public String getDebug()
    {
        return mDebug;
    }

    /**
     * @return
     */
    public String getDialect()
    {
        return mDialect;
    }

    /**
     * @return
     */
    public int getStatementCacheSize()
    {
        return mStatementCacheSize;
    }

    /**
     * @param i
     */
    public void setC3P0MaxSize(int i)
    {
        mC3P0MaxSize = i;
    }

    /**
     * @param i
     */
    public void setC3P0MaxStatements(int i)
    {
        mC3P0MaxStatements = i;
    }

    /**
     * @param i
     */
    public void setC3P0MinSize(int i)
    {
        mC3P0MinSize = i;
    }

    /**
     * @param i
     */
    public void setC3P0Timeout(int i)
    {
        mC3P0Timeout = i;
    }

    /**
     * @param string
     */
    public void setConnectionDriverClass(String string)
    {
        mConnectionDriverClass = string;
    }

    /**
     * @param string
     */
    public void setConnectionPassword(String string)
    {
        mConnectionPassword = string;
    }

    /**
     * @param string
     */
    public void setConnectionURL(String string)
    {
        mConnectionURL = string;
    }

    /**
     * @param string
     */
    public void setConnectionUsername(String string)
    {
        mConnectionUsername = string;
    }

    /**
     * @param string
     */
    public void setDebug(String string)
    {
        mDebug = string;
    }

    /**
     * @param string
     */
    public void setDialect(String string)
    {
        mDialect = string;
    }

    /**
     * @param i
     */
    public void setStatementCacheSize(int i)
    {
        mStatementCacheSize = i;
    }

    private int mC3P0MaxSize;
    private int mC3P0MaxStatements;
    private int mC3P0MinSize;
    private int mC3P0Timeout;
    private String mConnectionDriverClass;
    private String mConnectionPassword;
    private String mConnectionURL;
    private String mConnectionUsername;
    private String mDebug;
    private String mDialect;
    private int mStatementCacheSize;
}
