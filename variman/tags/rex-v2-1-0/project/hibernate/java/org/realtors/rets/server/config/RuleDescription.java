package org.realtors.rets.server.config;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang.enum.Enum;
import org.apache.commons.lang.StringUtils;

public class RuleDescription
{
    public RuleDescription()
    {
        this(INCLUDE);
    }

    public RuleDescription(Type type)
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

    public String getResource()
    {
        return mResource;
    }

    public void setResource(String resource)
    {
        mResource = resource;
    }

    public String getRetsClass()
    {
        return mRetsClass;
    }

    public void setRetsClass(String retsClass)
    {
        mRetsClass = retsClass;
    }

    public List getSystemNames()
    {
        return mSystemNames;
    }

    public void setSystemNames(List systemNames)
    {
        mSystemNames = systemNames;
    }

    public void addSystemName(String systemName)
    {
        mSystemNames.add(systemName);
    }

    public boolean includeSystemName(String systemName)
    {
        boolean includeSystemName = mSystemNames.contains(systemName);
        if (mType == EXCLUDE)
        {
            includeSystemName = !includeSystemName;
        }
        return includeSystemName;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("For ").append(mResource).append(":").append(mRetsClass);
        buffer.append(", ").append(mType).append(" fields: ");
        buffer.append(StringUtils.join(mSystemNames.iterator(), " "));
        return buffer.toString();
    }

    public static class Type extends Enum
    {
        private Type(String type)
        {
            super(type);
        }
    }

    public static final Type INCLUDE = new Type("include");
    public static final Type EXCLUDE = new Type("exclude");
    private Type mType;
    private String mResource;
    private String mRetsClass;
    private List mSystemNames;
}
