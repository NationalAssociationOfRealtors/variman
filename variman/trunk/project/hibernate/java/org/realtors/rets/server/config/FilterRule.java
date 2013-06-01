package org.realtors.rets.server.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;
import org.apache.commons.lang.StringUtils;

/**
 * Represents a field filtering rule (either include or exclude)
 */
public class FilterRule
{
    public FilterRule()
    {
        this(INCLUDE);
    }

    public FilterRule(Type type)
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

    /**
     * Determines if the specified system name is a member of this
     * rule.
     *
     * @param systemName    the system name of interest
     * @return true if this rule contains an entry for systemName, false otherwise
     */
    public boolean containsSystemName(String systemName) {
        return this.mSystemNames.contains(systemName);
    }

    /**
     * Determines whether the specified system name should be included
     * based on this filter rule.
     * 
     * @param systemName    the system name to check
     * @return true if the system name should be included, false otherwise.
     * @todo Fix this method.  It is misleading with multiple groups.
     */
    public boolean includeSystemName(String systemName)
    {
        boolean includeSystemName = mSystemNames.contains(systemName);
        
        if (mType == EXCLUDE)
        {
            includeSystemName = !includeSystemName;
        }
        return includeSystemName;
    }
    
    
    /**
     * Determine whether the specified system name should be excluded
     * based on this filter rule
     * 
     * @param systemName    the system to check
     * @return true if the system name should be excluded, false otherwise.
     */
    public boolean excludeSystemName(String systemName) {
        if (mType == EXCLUDE && this.mSystemNames.contains(systemName)) {
            return true;
        } else {
            return false;
        }
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

    public int getId()
    {
        return mId;
    }

    public void setId(int id)
    {
        mId = id;
    }

    public Map getExtendableProperties()
    {
        if (mExtendableProperties == null) {
            mExtendableProperties = new LinkedHashMap();
        }
       return mExtendableProperties;
    }

    public void setExtendableProperties(Map extendableProperties) {
           this.mExtendableProperties = extendableProperties;
    }

    public Object getExtendableProperty(String name) {
        return getExtendableProperties().get(name);
    }

    public void setExtendableProperty(String name, Object value) {
        getExtendableProperties().put(name, value);
    }

    public static final Type INCLUDE = new Type("include");
    public static final Type EXCLUDE = new Type("exclude");
    private Type mType;
    private String mResource;
    private String mRetsClass;
    private List mSystemNames;
    private int mId;
    private Map mExtendableProperties;
}
