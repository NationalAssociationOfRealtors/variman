/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class HibernateRetsConfig implements RetsConfig
{
    public HibernateRetsConfig()
    {
        super();
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public String getAddress()
    {
        return mAddress;
    }

    public void setAddress(String address)
    {
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
        if (mSecurityConstraints == null) {
            mSecurityConstraints = new SecurityConstraints();
        }
        return mSecurityConstraints;
    }

    public void setSecurityConstraints(SecurityConstraints securityConstraints)
    {
        mSecurityConstraints = securityConstraints;
        updateAllGroupRules();
    }
    
    private void updateAllGroupRules()
    {
        Set allGroupRules = getAllGroupRules();
        SecurityConstraints securityConstraints = getSecurityConstraints();
        List groupRulesList = new ArrayList(allGroupRules);
        securityConstraints.setAllGroupRules(groupRulesList);
    }
    
    /*
     * Used by Hibernate.
     */
    public Set getAllGroupRules() {
        if (mAllGroupRules == null) {
            mAllGroupRules = Collections.EMPTY_SET;
        }
        return mAllGroupRules;
    }

    /*
     * Used by Hibernate.
     */
    public void setAllGroupRules(Set allGroupRules) {
        mAllGroupRules = allGroupRules;
        updateAllGroupRules();
    }
    
    private Integer mId;
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
    private Set/*GroupRules*/ mAllGroupRules;

}
