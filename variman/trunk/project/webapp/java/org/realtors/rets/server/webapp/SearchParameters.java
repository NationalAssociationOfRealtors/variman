/*
 */
package org.realtors.rets.server.webapp;

import java.util.Map;

import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.Util;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SearchParameters
{
    public SearchParameters(Map parameterMap) throws RetsReplyException
    {
        mResourceId = getParameter(parameterMap, "SearchType");
        mClassName = getParameter(parameterMap, "Class");
        mQueryType = getParameter(parameterMap, "QueryType");
        if (!mQueryType.equals("DMQL2"))
        {
            throw new RetsReplyException(20203, "Miscellaneous Search Error");
        }
        mQuery = getParameter(parameterMap, "Query");
        mFormat = getParameter(parameterMap, "Format");
    }

    private String getParameter(Map parameterMap, String name)
    {
        String[] values = (String[]) parameterMap.get(name);
        if (values != null)
        {
            return values[0];
        }
        else
        {
            return null;
        }
    }

    public String getClassName()
    {
        return mClassName;
    }

    public String getFormat()
    {
        return mFormat;
    }

    public String getQuery()
    {
        return mQuery;
    }

    public String getQueryType()
    {
        return mQueryType;
    }

    public String getResourceId()
    {
        return mResourceId;
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
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
