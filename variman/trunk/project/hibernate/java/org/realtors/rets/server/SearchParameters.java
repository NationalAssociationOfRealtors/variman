/*
 */
package org.realtors.rets.server;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SearchParameters
{
    public SearchParameters(Map parameterMap, RetsVersion version)
        throws RetsReplyException
    {
        mResourceId = getParameter(parameterMap, "SearchType");
        mClassName = getParameter(parameterMap, "Class");
        initQueryType(parameterMap, version);
        mQuery = getParameter(parameterMap, "Query");
        initFormat(parameterMap);
    }

    private void initQueryType(Map parameterMap, RetsVersion version)
        throws RetsReplyException
    {
        mQueryType = getParameter(parameterMap, "QueryType");
        boolean validQueryType = false;
        validQueryType |=
            ((version == RetsVersion.RETS_1_0) && mQueryType.equals(DMQL));
        validQueryType |=
            ((version == RetsVersion.RETS_1_5) && mQueryType.equals(DMQL2));
        if (!validQueryType)
        {
            throw new RetsReplyException(20203, "Invalid query type: " +
                                                mQueryType);
        }
    }

    private void initFormat(Map parameterMap)
    {
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

    public static final String DMQL = "DMQL";
    public static final String DMQL2 = "DMQL2";

    private String mResourceId;
    private String mClassName;
    private String mQueryType;
    private String mQuery;
    private String mFormat;
}
