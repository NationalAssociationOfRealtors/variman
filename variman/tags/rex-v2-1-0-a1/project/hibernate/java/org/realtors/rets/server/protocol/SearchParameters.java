/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.enum.Enum;
import org.apache.commons.lang.math.NumberUtils;
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsVersion;
import org.realtors.rets.server.Util;
import org.realtors.rets.server.User;

public class SearchParameters
{
    public SearchParameters(Map parameterMap, RetsVersion version)
        throws RetsReplyException
    {
        this(parameterMap, version, null);
    }

    public SearchParameters(Map parameterMap, RetsVersion version, User user)
        throws RetsReplyException
    {
        mResourceId = getParameter(parameterMap, "SearchType");
        mClassName = getParameter(parameterMap, "Class");
        initQueryType(getParameter(parameterMap, "QueryType"), version);
        initQuery(getParameter(parameterMap, "Query"));
        initFormat(getParameter(parameterMap, "Format"));
        initStandardNames(getParameter(parameterMap, "StandardNames"));
        initSelect(getParameter(parameterMap, "Select"));
        initLimit(getParameter(parameterMap, "Limit"));
        initOffset(getParameter(parameterMap, "Offset"));
        initCount(getParameter(parameterMap, "Count"));
        mUser = user;
    }

    private void initCount(String count)
    {
        mCount = Count.getEnum(count);
        if (mCount == null)
        {
            mCount = NO_COUNT;
        }
    }

    private void initOffset(String offset)
    {
        mOffset = NumberUtils.stringToInt(offset, 1);
    }

    private void initLimit(String limit)
    {
        // "NONE" will not parse, and will correctly be set to MAX_VALUE. So
        // will any other non-integer string, but we'll let it slide, rather
        // than throwing an error.
        mLimit = NumberUtils.stringToInt(limit, Integer.MAX_VALUE);
    }

    private void initSelect(String select)
    {
        if (select != null)
        {
            mSelect = StringUtils.split(select, ",");
        }
    }

    private void initQuery(String query) throws RetsReplyException
    {
        if (query == null)
        {
            throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR,
                                         "query is required");
        }
        mQuery = query;
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
            throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR,
                                         "Invalid standard names: " +
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
            throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR,
                                         "Invalid query type: " +
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

    public String[] getSelect()
    {
        return mSelect;
    }

    public int getLimit()
    {
        return mLimit;
    }

    public int getOffset()
    {
        return mOffset;
    }

    public Count getCount()
    {
        return mCount;
    }

    public boolean countRequested()
    {
        return ((mCount == COUNT_AND_DATA) || (mCount == COUNT_ONLY));
    }

    public boolean dataRequested()
    {
        return ((mCount == NO_COUNT) || (mCount == COUNT_AND_DATA));
    }

    public User getUser()
    {
        return mUser;
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("resourceId", mResourceId)
            .append("className", mClassName)
            .append("queryType", mQueryType)
            .append("query", mQuery)
            .append("format", mFormat)
            .append("user", mUser)
            .toString();
    }

    public static class Count extends Enum
    {
        private Count(String name)
        {
            super(name);
        }

        public static Count getEnum(String name)
        {
            return (Count) getEnum(Count.class, name);
        }
    }

    public static final String DMQL = "DMQL";
    public static final String DMQL2 = "DMQL2";
    public static final Count NO_COUNT = new Count("0");
    public static final Count COUNT_AND_DATA = new Count("1");
    public static final Count COUNT_ONLY = new Count("2");

    private String mResourceId;
    private String mClassName;
    private String mQueryType;
    private String mQuery;
    private String mFormat;
    private boolean mStandardNames;
    private String[] mSelect;
    private int mLimit;
    private int mOffset;
    private Count mCount;
    private User mUser;
}
