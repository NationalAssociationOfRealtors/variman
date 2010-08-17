/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.realtors.rets.server.Util;

public class RetsConfig
{
    public static RetsConfig getInstance() {
        return sRetsConfig;
    }

    public RetsConfig()
    {
        mPort = 6103;
        mNonceInitialTimeout = -1;
        mNonceSuccessTimeout = -1;
        mSecurityConstraints = new SecurityConstraints();
        mStrictParsing = true;
        
        /*
         * Store an instance so that static methods can get to this. This is
         * a temporary hack.
         * TODO: remove this hack.
         */
        sRetsConfig = this;
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

    public DatabaseConfig getDatabase()
    {
        return mDatabase;
    }

    public void setDatabase(DatabaseConfig database)
    {
        mDatabase = database;
    }
    
    public boolean getStrictParsing()
    {
        return mStrictParsing;
    }
    
    public void setStrictParsing(boolean strict)
    {
        mStrictParsing = strict;
    }
    
    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("Address", (this.mAddress == null) ? "all addresses" : this.mAddress)
            .append("Port", this.mPort)
            .append("GetObject root", mGetObjectRoot)
            .append("GetObject photo pattern", mPhotoPattern)
            .append("nonce initial timeout", mNonceInitialTimeout)
            .append("nonce success timeout", mNonceSuccessTimeout)
            .append("strict parsing", mStrictParsing)
            .append(mDatabase)
            .toString();
    }

    public String getPhotoPattern(String defaultValue)
    {
        return getDefault(mPhotoPattern, defaultValue);
    }

    public String  getGetObjectRoot(String defaultValue)
    {
        return getDefault(mGetObjectRoot, defaultValue);
    }

    public String getObjectSetPattern(String defaultValue)
    {
        return getDefault(mObjectSetPattern, defaultValue);
    }

    public int getNonceInitialTimeout(int defaultValue)
    {
        return getDefault(mNonceInitialTimeout, defaultValue);
    }

    public int getNonceSuccessTimeout(int defaultValue)
    {
        return getDefault(mNonceSuccessTimeout, defaultValue);
    }

    private String getDefault(String string, String defaultValue)
    {
        if (string == null)
        {
            return defaultValue;
        }
        else
        {
            return string;
        }
    }

    private int getDefault(int number, int defaultValue)
    {
        if (number == -1)
        {
            return defaultValue;
        }
        else
        {
            return number;
        }
    }

    public void setMetadataDir(String metadataDir)
    {
        mMetadataDir = metadataDir;
    }

    public String getMetadataDir()
    {
        return mMetadataDir;
    }

    public List getAllGroupRules()
    {
        return mSecurityConstraints.getAllGroupRules();
    }

    public void setAllGroupRules(List allGroupRules) {
        mSecurityConstraints.setAllConstraints(allGroupRules);
    }

    public SecurityConstraints getSecurityConstraints()
    {
        return mSecurityConstraints;
    }

    public void setSecurityConstraints(List securityConstraints)
    {
        mSecurityConstraints.setAllConstraints(securityConstraints);
    }

    public Integer getId()
    {
        return this.mId;
    }

    public void setId(Integer id)
    {
        this.mId = id;
    }

    public Map getExtendableProperties()
    {
        if (mExtendableProperties == null)
        {
            mExtendableProperties = new LinkedHashMap();
        }
       return mExtendableProperties;
    }

    public void setExtendableProperties(Map extendableProperties)
    {
        mExtendableProperties = extendableProperties;
    }

    public Object getExtendableProperty(String name)
    {
        return getExtendableProperties().get(name);
    }

    public void setExtendableProperty(String name, Object value)
    {
        getExtendableProperties().put(name, value);
    }

    private static final Logger LOG = Logger.getLogger(RetsConfig.class);

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
    private boolean mStrictParsing;
    private Integer mId;
    private Map mExtendableProperties;

    private static RetsConfig sRetsConfig;
}
