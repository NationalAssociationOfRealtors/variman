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
        initQueryType(getParameter(parameterMap, "QueryType"), version);
        mQuery = getParameter(parameterMap, "Query");
        initFormat(getParameter(parameterMap, "Format"));
        initStandardNames(getParameter(parameterMap, "StandardNames"));
    }

    private void initStandardNames(String standardNames)
        throws RetsReplyException
    {
        if ((standardNames  == null) || standardNames.equals("0"))
        {
            mStandardNames = false;
        }
        else if (standardNames.equals("1"))
        {
            mStandardNames = true;
        }
        else
        {
            throw new RetsReplyException(20203, "Invalid standard names: " +
                                                standardNames);
        }
    }

    private void initQueryType(String queryType, RetsVersion version)
        throws RetsReplyException
    {
        mQueryType = queryType;
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

    private void initFormat(String format)
    {
        mFormat = format;
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

    public boolean isStandardNames()
    {
        return mStandardNames;
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
    private boolean mStandardNames;
}
