/*
 */
package org.realtors.rets.server.webapp;

import org.realtors.rets.server.dmql.DmqlCompiler;

import antlr.ANTLRException;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SearchAction
{
    public String getResourceId()
    {
        return mResourceId;
    }

    public void setResourceId(String resourceId)
    {
        mResourceId = resourceId;
    }

    public String getClassName()
    {
        return mClassName;
    }

    public void setClassName(String className)
    {
        mClassName = className;
    }

    public String getQueryType()
    {
        return mQueryType;
    }

    public void setQueryType(String queryType) throws RetsReplyException
    {
        if (!queryType.equals("DMQL2"))
        {
            throw new RetsReplyException(20203, "Miscellaneous Search Error");
        }
        mQueryType = queryType;
    }

    public String getQuery()
    {
        return mQuery;
    }

    public void setQuery(String query)
    {
        mQuery = query;
    }

    public String getFormat()
    {
        return mFormat;
    }

    public void setFormat(String format)
    {
        mFormat = format;
    }

    public String getSql() throws ANTLRException
    {
        return DmqlCompiler.dmqlToSql(mQuery);
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("resourceId", mResourceId)
            .append("className", mClassName)
            .append("queryType", mQueryType)
            .append("query", mQuery)
            .append("format", mFormat)
            .toString();
    }

    private String mResourceId;
    private String mClassName;
    private String mQueryType;
    private String mQuery;
    private String mFormat;
}
