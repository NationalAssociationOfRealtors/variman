/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Aug 5, 2003
 *
 */
package org.realtors.rets.server.admin;

/**
 * @author kgarner
 */
public class ConfigOptions
{
    /**
     * @return
     */
    public String getHostname()
    {
        return mHostname;
    }

    /**
     * @return
     */
    public int getPort()
    {
        return mPort;
    }

    /**
     * @return
     */
    public int getSessionTimeout()
    {
        return mSessionTimeout;
    }

    /**
     * @param string
     */
    public void setHostname(String string)
    {
        mHostname = string;
    }

    /**
     * @param i
     */
    public void setPort(int i)
    {
        mPort = i;
    }

    /**
     * @param i
     */
    public void setSessionTimeout(int i)
    {
        mSessionTimeout = i;
    }

    private String mHostname;
    private int mPort;
    private int mSessionTimeout;
}
