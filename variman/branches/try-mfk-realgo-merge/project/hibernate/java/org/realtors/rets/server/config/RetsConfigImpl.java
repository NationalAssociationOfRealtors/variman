/*
 * Variman RETS Server
 *
 * Author: Dave Dribin, Danny Hurlburt
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.config;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.realtors.rets.server.Util;

public class RetsConfigImpl implements RetsConfig
{
    public RetsConfigImpl()
    {
        mPort = 6103;
        mNonceInitialTimeout = -1;
        mNonceSuccessTimeout = -1;
        mSecurityConstraints = new SecurityConstraints();
    }

    public String getAddress()
    {
        return mAddress;
    }

    public void setAddress(String address)
    {
        if (StringUtils.isBlank(address))
            mAddress = null;
        else
            mAddress = address;
    }

    public int getPort()
    {
        return mPort;
    }

    public void setPort(int port)
    {
        mPort = port;
    }

    public String getGetObjectRoot()
    {
        return mGetObjectRoot;
    }

    public void setGetObjectRoot(String getObjectRoot)
    {
        mGetObjectRoot = getObjectRoot;
    }

    public String getPhotoPattern()
    {
        return mPhotoPattern;
    }

    public void setPhotoPattern(String photoPattern)
    {
        mPhotoPattern = photoPattern;
    }

    public String getObjectSetPattern()
    {
        return mObjectSetPattern;
    }

    public void setObjectSetPattern(String objectSetPattern)
    {
        mObjectSetPattern = objectSetPattern;
    }

    public int getNonceInitialTimeout()
    {
        return mNonceInitialTimeout;
    }

    public void setNonceInitialTimeout(int nonceInitialTimeout)
    {
        mNonceInitialTimeout = nonceInitialTimeout;
    }

    public int getNonceSuccessTimeout()
    {
        return mNonceSuccessTimeout;
    }

    public void setNonceSuccessTimeout(int nonceSuccessTimeout)
    {
        mNonceSuccessTimeout = nonceSuccessTimeout;
    }

    public String getMetadataDir()
    {
        return mMetadataDir;
    }

    public void setMetadataDir(String metadataDir)
    {
        mMetadataDir = metadataDir;
    }

    public DatabaseConfig getDatabase()
    {
        return mDatabase;
    }

    public void setDatabase(DatabaseConfig database)
    {
        mDatabase = database;
    }

    public SecurityConstraints getSecurityConstraints()
    {
        return mSecurityConstraints;
    }

    public void setSecurityConstraints(SecurityConstraints securityConstraints)
    {
        mSecurityConstraints = securityConstraints;
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("GetObject root", mGetObjectRoot)
            .append("GetObject photo pattern", mPhotoPattern)
            .append("nonce initial timeout", mNonceInitialTimeout)
            .append("nonce success timeout", mNonceSuccessTimeout)
            .append(mDatabase)
            .toString();
    }

    private String mAddress;
    private int mPort;
    private String mGetObjectRoot;
    private String mPhotoPattern;
    private String mObjectSetPattern;
    private int mNonceInitialTimeout;
    private int mNonceSuccessTimeout;
    private DatabaseConfig mDatabase;
    private String mMetadataDir;
    private SecurityConstraints mSecurityConstraints;

}
