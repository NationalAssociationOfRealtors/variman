package org.realtors.rets.server.config;

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

    private String mResource;
    private String mRetsClass;
    private String mSqlConstraint;
}
