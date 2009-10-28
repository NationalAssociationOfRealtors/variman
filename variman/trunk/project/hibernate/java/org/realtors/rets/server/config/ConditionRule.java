package org.realtors.rets.server.config;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConditionRule
{
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

    public String getSqlConstraint()
    {
        return mSqlConstraint;
    }

    public void setSqlConstraint(String sqlConstraint)
    {
        mSqlConstraint = sqlConstraint;
    }

    public Integer getId() {
        return this.mId;
    }

    public void setId(Integer id) {
        this.mId = id;
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

    private String mResource;
    private String mRetsClass;
    private String mSqlConstraint;
    private Integer mId;
    private Map mExtendableProperties;
}
