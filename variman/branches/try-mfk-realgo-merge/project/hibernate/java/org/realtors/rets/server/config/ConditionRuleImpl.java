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

public class ConditionRuleImpl implements ConditionRule
{
    public String getResourceID()
    {
        return mResourceId;
    }

    public void setResourceID(String resourceId)
    {
        mResourceId = resourceId;
    }

    public String getRetsClassName()
    {
        return mRetsClassName;
    }

    public void setRetsClassName(String retsClassName)
    {
        mRetsClassName = retsClassName;
    }

    public String getSqlConstraint()
    {
        return mSqlConstraint;
    }

    public void setSqlConstraint(String sqlConstraint)
    {
        mSqlConstraint = sqlConstraint;
    }

    public String getDmqlConstraint()
    {
        return mDmqlConstraint;
    }

    public void setDmqlConstraint(String dmqlConstraint)
    {
        mDmqlConstraint = dmqlConstraint;
    }

    private String mResourceId;
    private String mRetsClassName;
    private String mSqlConstraint;
    private String mDmqlConstraint;
    
}
