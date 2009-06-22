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
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class FilterRuleImpl implements FilterRule
{
    public FilterRuleImpl()
    {
        this(Type.INCLUDE);
    }

    public FilterRuleImpl(Type type)
    {
        mType = type;
        mSystemNames = new ArrayList();
    }

    public Type getType()
    {
        return mType;
    }

    public void setType(Type type)
    {
        mType = type;
    }

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

    public List/*String*/ getSystemNames()
    {
        return mSystemNames;
    }

    public void setSystemNames(List/*String*/ systemNames)
    {
        mSystemNames = systemNames;
    }

    public void addSystemName(String systemName)
    {
        mSystemNames.add(systemName);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("For ").append(mResourceId).append(":").append(mRetsClassName);
        buffer.append(", ").append(mType).append(" fields: ");
        buffer.append(StringUtils.join(mSystemNames.iterator(), " "));
        return buffer.toString();
    }

    private Type mType;
    private String mResourceId;
    private String mRetsClassName;
    private List/*String*/ mSystemNames;
    
}
